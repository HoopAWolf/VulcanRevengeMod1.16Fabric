package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.helper.RayTracingHelper;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.server.SetAttackTargetMessage;
import com.hoopawolf.vrm.network.packets.server.SinMaskActivateMessage;
import com.hoopawolf.vrm.network.packets.server.SleepMessage;
import com.hoopawolf.vrm.network.packets.server.TeleportMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MouseMixin
{
    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public ClientWorld world;
    @Shadow
    protected int attackCooldown;

    @Inject(method = "doAttack",
            at = @At(value = "HEAD"))
    private void onLeftClick(CallbackInfo callbackInfo)
    {
        if (this.attackCooldown <= 0)
        {
            if (this.crosshairTarget != null && !this.player.isRiding())
            {
                switch (this.crosshairTarget.getType())
                {
                    case MISS:
                    {
                        if (player.getMainHandStack().isEmpty())
                        {
                            if (player.isSneaking() && player.inventory.armor.get(3).getItem() instanceof SinsArmorItem)
                            {
                                SinMaskActivateMessage _message = new SinMaskActivateMessage(player.getUuid(), !SinsArmorItem.isActivated(player.inventory.armor.get(3)));

                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(_message.getMessageType());
                                _message.encode(passedData);

                                ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);

                                EntityHelper.sendMessage(player, (!SinsArmorItem.isActivated(player.inventory.armor.get(3))) ? "sinactivate" : "sindeactivate", Formatting.RED);
                                player.playSound((!SinsArmorItem.isActivated(player.inventory.armor.get(3)) ? SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON : SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK), 1.0F, 0.1F);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Inject(method = "doItemUse",
            at = @At(value = "HEAD"))
    private void onRightClick(CallbackInfo callbackInfo)
    {
        if (!this.interactionManager.isBreakingBlock())
        {
            if (!this.player.isRiding())
            {
                Hand[] var1 = Hand.values();
                int var2 = var1.length;

                for (int var3 = 0; var3 < var2; ++var3)
                {
                    Hand hand = var1[var3];
                    ItemStack itemstack = this.player.getStackInHand(hand);

                    if (itemstack.isEmpty() && (this.crosshairTarget == null || this.crosshairTarget.getType() == HitResult.Type.MISS))
                    {
                        if (player.hasStatusEffect(PotionRegistryHandler.SLEEP_EFFECT))
                        {
                            if (player.isSneaking())
                            {
                                SleepMessage _message = new SleepMessage(player.getUuid(), true);
                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(_message.getMessageType());
                                _message.encode(passedData);

                                ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);
                            }
                        }

                        if (player.hasStatusEffect(PotionRegistryHandler.TELEPORTATION_EFFECT))
                        {
                            RayTracingHelper.INSTANCE.fire();
                            BlockPos finalPos = RayTracingHelper.INSTANCE.getFinalPos();
                            if (finalPos != null)
                            {
                                TeleportMessage _message = new TeleportMessage(new BlockPos(finalPos.getX(), finalPos.getY() + 1, finalPos.getZ()), player.getEntityId());
                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(_message.getMessageType());
                                _message.encode(passedData);

                                ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);
                                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 2.0F, 1.0F);
                            } else
                            {
                                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS, 2.0F, 1.0F);
                            }
                        }

                        if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                        {
                            if (player.isSneaking())
                            {
                                SleepMessage _message = new SleepMessage(player.getUuid(), false);
                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(_message.getMessageType());
                                _message.encode(passedData);

                                ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);
                            } else
                            {
                                if (SinsArmorItem.getSlothPetID(player.inventory.armor.get(3)) != 0)
                                {
                                    RayTracingHelper.INSTANCE.fire();
                                    Entity target = RayTracingHelper.INSTANCE.getTarget();

                                    if (target instanceof LivingEntity)
                                    {
                                        player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F);
                                        SetAttackTargetMessage _message = new SetAttackTargetMessage(SinsArmorItem.getSlothPetID(player.inventory.armor.get(3)), target.getEntityId());
                                        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                        passedData.writeInt(_message.getMessageType());
                                        _message.encode(passedData);

                                        ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
