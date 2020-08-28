package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class LustSinTemptGoal extends Goal
{
    private static final TargetPredicate ENTITY_PREDICATE = (new TargetPredicate()).setBaseMaxDistance(20.0D).includeInvulnerable().includeTeammates().ignoreEntityTargetRules().includeHidden();
    protected final PathAwareEntity creature;
    private final double speed;
    protected PlayerEntity closestPlayer;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private int delayTemptCounter;

    public LustSinTemptGoal(PathAwareEntity creatureIn, double speedIn)
    {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart()
    {
        if (this.delayTemptCounter > 0)
        {
            --this.delayTemptCounter;
            return false;
        } else
        {
            this.closestPlayer = this.creature.world.getClosestPlayer(ENTITY_PREDICATE, this.creature);
            if (this.closestPlayer == null)
            {
                return false;
            } else
            {
                return closestPlayer.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.LUST_MASK_ARMOR) && SinsArmorItem.isActivated(closestPlayer.inventory.armor.get(3));
            }
        }
    }

    @Override
    public boolean shouldContinue()
    {
        if (this.creature.squaredDistanceTo(this.closestPlayer) < 36.0D)
        {
            if (this.closestPlayer.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
            {
                return false;
            }

            if (Math.abs((double) this.closestPlayer.pitch - this.pitch) > 5.0D || Math.abs((double) this.closestPlayer.yaw - this.yaw) > 5.0D)
            {
                return false;
            }
        } else
        {
            this.targetX = this.closestPlayer.getX();
            this.targetY = this.closestPlayer.getY();
            this.targetZ = this.closestPlayer.getZ();
        }

        this.pitch = this.closestPlayer.pitch;
        this.yaw = this.closestPlayer.yaw;

        return this.canStart();
    }

    @Override
    public void start()
    {
        this.targetX = this.closestPlayer.getX();
        this.targetY = this.closestPlayer.getY();
        this.targetZ = this.closestPlayer.getZ();
    }

    @Override
    public void stop()
    {
        this.closestPlayer = null;
        this.creature.getNavigation().stop();
        this.delayTemptCounter = 100;
    }

    @Override
    public void tick()
    {
        this.creature.getLookControl().lookAt(this.closestPlayer, (float) (this.creature.getBodyYawSpeed() + 20), (float) this.creature.getLookPitchSpeed());

        if (this.creature.age % 3 == 0)
        {
            SinsArmorItem.increaseFulfilment(closestPlayer.inventory.armor.get(3), 1, SinsArmorItem.getSin(closestPlayer.inventory.armor.get(3)).getMaxUse());
        }

        if (this.creature.squaredDistanceTo(this.closestPlayer) < 6.25D)
        {
            this.creature.getNavigation().stop();
        } else
        {
            this.creature.getNavigation().startMovingTo(this.closestPlayer, this.speed);
        }

    }
}