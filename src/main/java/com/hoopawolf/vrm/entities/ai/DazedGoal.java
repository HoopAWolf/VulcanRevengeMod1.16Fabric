package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DazedGoal extends Goal
{
    private final EnumSet<Control> flag = EnumSet.noneOf(Control.class);
    private final LivingEntity host;

    public DazedGoal(LivingEntity _host)
    {
        host = _host;
        flag.add(Control.MOVE);
        flag.add(Control.JUMP);
        flag.add(Control.LOOK);
        flag.add(Control.TARGET);

        this.setControls(flag);
    }

    @Override
    public boolean canStart()
    {
        return host.hasStatusEffect(PotionRegistryHandler.DAZED_EFFECT);
    }

    @Override
    public boolean shouldContinue()
    {
        return host.hasStatusEffect(PotionRegistryHandler.DAZED_EFFECT);
    }
}
