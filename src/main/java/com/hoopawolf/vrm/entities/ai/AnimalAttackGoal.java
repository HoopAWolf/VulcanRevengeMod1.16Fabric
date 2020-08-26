package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class AnimalAttackGoal extends MeleeAttackGoal
{
    private final PathAwareEntity host;
    private final double damage;
    private final double knockBack;

    public AnimalAttackGoal(PathAwareEntity creature, double speedIn, boolean useLongMemory, double damageIn, double knockBackIn)
    {
        super(creature, speedIn, useLongMemory);
        host = creature;
        damage = damageIn;
        knockBack = knockBackIn;
    }

    @Override
    public boolean canStart()
    {
        return host.hasStatusEffect(PotionRegistryHandler.RAGE_EFFECT) && super.canStart();
    }

    @Override
    public boolean shouldContinue()
    {
        return host.hasStatusEffect(PotionRegistryHandler.RAGE_EFFECT) && super.shouldContinue();
    }

    @Override
    protected void attack(LivingEntity enemy, double distToEnemySqr)
    {
        double d0 = this.getSquaredMaxAttackDistance(enemy);
        if (distToEnemySqr <= d0 && this.method_28348() <= 0)
        {
            this.method_28346();
            this.mob.swingHand(Hand.MAIN_HAND);

            float f = (float) damage;
            float f1 = (float) knockBack;

            boolean flag = enemy.damage(DamageSource.mob(host), f);

            if (flag)
            {
                if (f1 > 0.0F)
                {
                    enemy.takeKnockback(f1 * 0.5F, MathHelper.sin(host.yaw * ((float) Math.PI / 180F)), -MathHelper.cos(host.yaw * ((float) Math.PI / 180F)));
                    host.setVelocity(host.getVelocity().multiply(0.6D, 1.0D, 0.6D));
                }

                host.onAttacking(enemy);
            }
        }

    }
}
