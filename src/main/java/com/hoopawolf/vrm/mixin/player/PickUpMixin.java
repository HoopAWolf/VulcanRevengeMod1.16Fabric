package com.hoopawolf.vrm.mixin.player;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.VRMGreedItemDataHandler;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

@Mixin(ItemEntity.class)
public abstract class PickUpMixin
{
    @Shadow
    private int pickupDelay;

    @Shadow
    private UUID owner;

    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "onPlayerCollision",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onPickUpEvent(PlayerEntity player, CallbackInfo callbackInfo)
    {
        ItemEntity itemEntity = ((ItemEntity) ((Object) this));

        ItemStack itemStack = this.getStack();
        if (!itemEntity.world.isClient && this.pickupDelay == 0)
        {
            if (VulcanRevengeMod.VRM_CONFIG.itemconfig.greedDoubleDrop && itemEntity.world.random.nextInt(50) < 10)
            {
                ArrayList<String> blackList = VRMGreedItemDataHandler.INSTANCE.blackList;

                if (!blackList.contains(itemStack.getItem().getTranslationKey()))
                {
                    if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.GREED_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                    {
                        if (itemEntity.getOwner() == null && itemEntity.getThrower() == null)
                        {
                            int calculatedAmount = (int) ((1.0D - (float) ((SinsArmorItem) player.inventory.armor.get(3).getItem()).getFulfilmentAmount(player.inventory.armor.get(3))) * 6);

                            if (calculatedAmount > 0)
                            {
                                int additionalAmount = player.world.random.nextInt(calculatedAmount);

                                if (additionalAmount > 0)
                                {
                                    PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 8, 0.3F, 1.0F);
                                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                                    passedSoundData.writeInt(playDingSoundMessage.getMessageType());
                                    playDingSoundMessage.encode(passedSoundData);

                                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                                    itemEntity.getStack().setCount(itemEntity.getStack().getCount() + additionalAmount);
                                }
                                itemEntity.setOwner(player.getUuid());
                            }
                        }
                    }
                }
            }
        }
    }
}
