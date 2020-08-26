package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.MathHelper;

public class FireChargeEffect extends StatusEffect
{
    public FireChargeEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 50 >> amplifier;
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
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 10, 5, 10, 10))
            {
                if (entityLivingBaseIn.canSee(entity))
                {
                    double d0 = entityLivingBaseIn.squaredDistanceTo(entity);
                    double d1 = entity.getX() - entityLivingBaseIn.getX();
                    double d2 = entity.getBodyY(0.5D) - entityLivingBaseIn.getBodyY(0.5D);
                    double d3 = entity.getZ() - entityLivingBaseIn.getZ();
                    float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;

                    entityLivingBaseIn.world.syncWorldEvent(null, 1018, entityLivingBaseIn.getBlockPos(), 0);

                    SmallFireballEntity smallfireballentity = new SmallFireballEntity(entityLivingBaseIn.world, entityLivingBaseIn, d1 + entityLivingBaseIn.getRandom().nextGaussian() * (double) f, d2, d3 + entityLivingBaseIn.getRandom().nextGaussian() * (double) f);
                    smallfireballentity.updatePosition(smallfireballentity.getX(), entityLivingBaseIn.getBodyY(0.5D) + 0.5D, smallfireballentity.getZ());
                    entityLivingBaseIn.world.spawnEntity(smallfireballentity);
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
