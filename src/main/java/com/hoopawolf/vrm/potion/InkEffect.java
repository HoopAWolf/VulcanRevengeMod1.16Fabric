package com.hoopawolf.vrm.potion;

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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.stream.Stream;

public class InkEffect extends StatusEffect
{
    public InkEffect(StatusEffectType typeIn, int liquidColorIn)
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
            int charge = 3;
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 8, 8, 8, 10))
            {
                if (entityLivingBaseIn.canSee(entity) && !entity.hasStatusEffect(StatusEffects.BLINDNESS))
                {
                    entity.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.FEAR_EFFECT, 200, 0));
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 400, 0));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getY() + 1.2F, entity.getZ()), new Vec3d(0.0F, -0.15D, 0.0F), 9, 10, entity.getWidth());
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeInt(spawnParticleMessage.getMessageType());
                    spawnParticleMessage.encode(passedData);
                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(entity.world);
                    playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));

                    --charge;
                }

                if (charge <= 0)
                {
                    break;
                }
            }
        }
    }
}
