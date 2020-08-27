package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(LivingEntity.class)
public abstract class DeathMixin
{
    @Inject(method = "onDeath",
            at = @At(value = "HEAD"))
    private void onDeathEvent(DamageSource source, CallbackInfo callbackInfo)
    {
        LivingEntity target = ((LivingEntity) ((Object) this));

        if (!target.world.isClient)
        {
            if (target.hasStatusEffect(PotionRegistryHandler.PLAGUE_EFFECT) || source.getSource() instanceof PesArrowEntity)
            {
                PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(target.getEntityId(), 6, 5.0F, 0.1F);

                PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                passedSoundData.writeInt(playVexSoundMessage.getMessageType());
                playVexSoundMessage.encode(passedSoundData);

                Stream<PlayerEntity> playerInDimension = PlayerStream.world(target.world);
                playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                for (int i = 1; i <= 180; ++i)
                {
                    double yaw = (double) i * 360D / 180D;
                    double speed = 0.7;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(target.getX(), target.getY() + 0.5F, target.getZ()), new Vec3d(xSpeed, 0.9D, zSpeed), 1, 5, 0.0F);

                    PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                    passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                    spawnParticleMessage.encode(passedParticleData);

                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(target.world);
                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                }
            }
        }
    }
}
