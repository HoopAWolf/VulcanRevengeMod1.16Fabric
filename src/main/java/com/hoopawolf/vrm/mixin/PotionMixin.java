package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(LivingEntity.class)
public abstract class PotionMixin
{
    @Inject(method = "addStatusEffect",
            at = @At(value = "HEAD"))
    private void applyPotionEvent(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.world.isClient)
        {
            if (livingEntity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) livingEntity;

                if (player.getMainHandStack().getItem().equals(ItemBlockRegistryHandler.PES_BOW))
                {
                    if (!effect.isAmbient())
                    {
                        callbackInfo.setReturnValue(false);
                    }

                } else if (effect.getEffectType().equals(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.ENVY_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.SLOTH_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.WRATH_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.LUST_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.PRIDE_TRIAL_EFFECT) ||
                        effect.getEffectType().equals(PotionRegistryHandler.GREED_TRIAL_EFFECT))
                {
                    if (player.hasStatusEffect(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.ENVY_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.SLOTH_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.WRATH_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.LUST_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.PRIDE_TRIAL_EFFECT) ||
                            player.hasStatusEffect(PotionRegistryHandler.GREED_TRIAL_EFFECT))
                    {
                        callbackInfo.setReturnValue(false);
                    }
                } else if (player.hasStatusEffect(PotionRegistryHandler.MILK_EFFECT))
                {
                    if (!effect.getEffectType().equals(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.ENVY_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.SLOTH_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.WRATH_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.LUST_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.PRIDE_TRIAL_EFFECT) &&
                            !effect.getEffectType().equals(PotionRegistryHandler.GREED_TRIAL_EFFECT))
                    {
                        callbackInfo.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Inject(method = "onStatusEffectRemoved",
            at = @At(value = "HEAD"))
    private void potionExpireEvent(StatusEffectInstance effect, CallbackInfo callbackInfo)
    {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (!livingEntity.world.isClient)
        {
            if (livingEntity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) livingEntity;

                StatusEffect potion = effect.getEffectType();
                if (PotionRegistryHandler.LUST_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 7, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.LUST_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 6, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.ENVY_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 11, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.ENVY_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.GREED_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 1, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.GREED_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.PRIDE_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 12, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.PRIDE_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.SLOTH_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 13, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.SLOTH_MASK_ARMOR), true);
                } else if (PotionRegistryHandler.WRATH_TRIAL_EFFECT.equals(potion))
                {
                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
                    PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                    passedSoundData.writeInt(playSoundMessage.getMessageType());
                    playSoundMessage.encode(passedSoundData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = (double) i * 360.D / 180.D;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
                        PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                        passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                        spawnParticleMessage.encode(passedParticleData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                    }
                    player.dropItem(new ItemStack(ArmorRegistryHandler.WRATH_MASK_ARMOR), true);
                }
            }
        }
    }
}
