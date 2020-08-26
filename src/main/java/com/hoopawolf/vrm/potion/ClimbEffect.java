package com.hoopawolf.vrm.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class ClimbEffect extends StatusEffect
{
    public ClimbEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.horizontalCollision)
        {
            entityLivingBaseIn.setVelocity(entityLivingBaseIn.getVelocity().getX(), (entityLivingBaseIn.isSneaking()) ? -0.15 : 0.25, entityLivingBaseIn.getVelocity().getZ());
            entityLivingBaseIn.velocityDirty = true;
        }

        if (!entityLivingBaseIn.world.isClient)
        {
            entityLivingBaseIn.fallDistance = 0;
        }
    }
}

