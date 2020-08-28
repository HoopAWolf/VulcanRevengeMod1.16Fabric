package com.hoopawolf.vrm.mixin.player;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.data.EatItemData;
import com.hoopawolf.vrm.helper.VRMEatItemDataHandler;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SendPlayerMessageMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.stream.Stream;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class RightClickItemMixin
{
    @Shadow
    private GameMode gameMode;

    private static boolean consumeItem(PlayerEntity playerIn, ItemStack itemStackIn)
    {
        if (VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey()) != null)
        {
            EatItemData data = VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey());

            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);

            PlaySoundEffectMessage playEatSoundMessage = new PlaySoundEffectMessage(playerIn.getEntityId(), 0, 1.0F, 1.0F);
            PlaySoundEffectMessage playBurpSoundMessage = new PlaySoundEffectMessage(playerIn.getEntityId(), 1, 1.0F, 1.0F);

            PacketByteBuf passedEatSoundData = new PacketByteBuf(Unpooled.buffer());
            passedEatSoundData.writeInt(playEatSoundMessage.getMessageType());
            playEatSoundMessage.encode(passedEatSoundData);

            PacketByteBuf passedBurpSoundData = new PacketByteBuf(Unpooled.buffer());
            passedBurpSoundData.writeInt(playBurpSoundMessage.getMessageType());
            playEatSoundMessage.encode(passedBurpSoundData);

            playerInDimension.forEach(player ->
            {
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedEatSoundData);
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedBurpSoundData);
            });

            if (itemStackIn.isDamageable())
            {
                itemStackIn.damage((int) (itemStackIn.getMaxDamage() * 0.3F), playerIn, (p_220287_1_) ->
                        p_220287_1_.sendToolBreakStatus(Hand.MAIN_HAND));
            } else
            {
                itemStackIn.decrement(1);
                playerIn.sendToolBreakStatus(Hand.MAIN_HAND);
            }

            playerIn.getHungerManager().setFoodLevel(MathHelper.clamp(playerIn.getHungerManager().getFoodLevel() + data.getFoodAmount(), 0, 20));
            SinsArmorItem.increaseFulfilment(playerIn.inventory.armor.get(3), data.getFoodAmount(), SinsArmorItem.getSin(playerIn.inventory.armor.get(3)).getMaxUse());

            for (String effects : data.getListOfEffects())
            {
                for (Identifier list : Registry.STATUS_EFFECT.getIds())
                {
                    if (list.toString().equals(effects))
                    {
                        playerIn.addStatusEffect(new StatusEffectInstance(Registry.STATUS_EFFECT.get(list), (Objects.equals(Registry.STATUS_EFFECT.get(list), StatusEffects.INSTANT_DAMAGE) || Objects.equals(Registry.STATUS_EFFECT.get(list), StatusEffects.INSTANT_HEALTH)) ? 1 : data.getDuration(), data.getAmplifier()));
                        break;
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Inject(method = "interactItem",
            at = @At(value = "INVOKE"))
    private void onUseItemEvent(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo)
    {
        if (this.gameMode == GameMode.SPECTATOR)
        {
            callbackInfo.setReturnValue(ActionResult.PASS);
        } else if (player.getItemCooldownManager().isCoolingDown(stack.getItem()))
        {
            callbackInfo.setReturnValue(ActionResult.PASS);
        } else
        {
            if (!player.world.isClient)
            {
                if (player.isSneaking() && player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                {
                    if (!player.getStackInHand(hand).isEmpty() && !player.getMainHandStack().isFood())
                    {
                        if (consumeItem(player, player.getStackInHand(hand)))
                        {
                            callbackInfo.setReturnValue(ActionResult.PASS);
                        } else
                        {
                            SendPlayerMessageMessage message = new SendPlayerMessageMessage(player.getUuid(), "cannoteat", Formatting.GRAY.getColorIndex());
                            PacketByteBuf passedMessageData = new PacketByteBuf(Unpooled.buffer());
                            passedMessageData.writeInt(message.getMessageType());
                            message.encode(passedMessageData);

                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedMessageData);
                        }
                    }
                }

                if (player.hasStatusEffect(PotionRegistryHandler.MILK_EFFECT) && player.getStackInHand(hand).getItem().equals(Items.BUCKET) && player.getStackInHand(hand).getCount() > 0)
                {
                    player.getStackInHand(hand).decrement(1);
                    player.dropItem(new ItemStack(Items.MILK_BUCKET), true);
                    callbackInfo.setReturnValue(ActionResult.PASS);
                }

                if (player.hasStatusEffect(PotionRegistryHandler.STEW_EFFECT) && player.getStackInHand(hand).getItem().equals(Items.BOWL) && player.getStackInHand(hand).getCount() > 0)
                {
                    player.getStackInHand(hand).decrement(1);
                    player.dropItem(new ItemStack(Items.SUSPICIOUS_STEW), true);
                    callbackInfo.setReturnValue(ActionResult.PASS);
                }
            }
        }
    }
}
