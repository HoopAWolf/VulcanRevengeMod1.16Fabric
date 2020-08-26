package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class LustAvoidEntityGoal<T extends LivingEntity> extends FleeEntityGoal<T>
{

    public LustAvoidEntityGoal(PathAwareEntity entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean canStart()
    {
        return super.canStart() && targetEntity.hasStatusEffect(PotionRegistryHandler.LUST_TRIAL_EFFECT);
    }

    @Override
    public boolean shouldContinue()
    {
        return super.shouldContinue() && targetEntity.hasStatusEffect(PotionRegistryHandler.LUST_TRIAL_EFFECT);
    }
}
