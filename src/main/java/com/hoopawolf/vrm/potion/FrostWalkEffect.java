package com.hoopawolf.vrm.potion;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class FrostWalkEffect extends StatusEffect
{
    public FrostWalkEffect(StatusEffectType typeIn, int liquidColorIn)
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
        if (!entityLivingBaseIn.world.isClient)
        {
            if (entityLivingBaseIn.world.getBlockState(entityLivingBaseIn.getBlockPos().down()).getBlock().equals(Blocks.WATER))
            {
                entityLivingBaseIn.world.setBlockState(entityLivingBaseIn.getBlockPos().down(), Blocks.ICE.getDefaultState());
            } else if (entityLivingBaseIn.world.getBlockState(entityLivingBaseIn.getBlockPos().down()).getBlock().equals(Blocks.LAVA))
            {
                entityLivingBaseIn.world.setBlockState(entityLivingBaseIn.getBlockPos().down(), Blocks.OBSIDIAN.getDefaultState());
            }
        }
    }
}

