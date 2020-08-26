package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.EvokerFangsEntity;

public class EvokeEffect extends StatusEffect
{
    public EvokeEffect(StatusEffectType typeIn, int liquidColorIn)
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
                if (entityLivingBaseIn.canSee(entity))
                {
                    entityLivingBaseIn.world.spawnEntity(new EvokerFangsEntity(entityLivingBaseIn.world, entity.getX(), entity.getY(), entity.getZ(), entity.yaw, 3, entityLivingBaseIn));
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
