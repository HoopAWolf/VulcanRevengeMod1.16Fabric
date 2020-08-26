package com.hoopawolf.vrm.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;

public class HungerResistanceEffect extends StatusEffect
{
    public HungerResistanceEffect(StatusEffectType typeIn, int liquidColorIn)
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
        if (entityLivingBaseIn.hasStatusEffect(StatusEffects.HUNGER))
        {
            entityLivingBaseIn.removeStatusEffect(StatusEffects.HUNGER);
        }
    }
}
