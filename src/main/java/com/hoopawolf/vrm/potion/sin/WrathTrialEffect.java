package com.hoopawolf.vrm.potion.sin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.stream.Stream;

public class WrathTrialEffect extends StatusEffect
{
    public WrathTrialEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 80 >> amplifier;
        if (k > 0)
        {
            return duration % k == 0;
        } else
        {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.world.isClient)
        {
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 20, 2, 20, 30))
            {
                if (!entity.hasStatusEffect(PotionRegistryHandler.RAGE_EFFECT))
                {
                    entity.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.RAGE_EFFECT, 200, 0));

                    if (entity instanceof PathAwareEntity)
                    {
                        ((PathAwareEntity) entity).setTarget(entityLivingBaseIn);
                    }

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getY() + 1.2F, entity.getZ()), new Vec3d(0.0F, -0.15D, 0.0F), 9, 8, entity.getWidth());
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeInt(spawnParticleMessage.getMessageType());
                    spawnParticleMessage.encode(passedData);
                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(entityLivingBaseIn.world);
                    playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                }
            }
        }
    }
}