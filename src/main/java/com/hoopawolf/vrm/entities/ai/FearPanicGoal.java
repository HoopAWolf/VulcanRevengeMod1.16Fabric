package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

import java.util.EnumSet;

public class FearPanicGoal extends Goal
{
    protected final PathAwareEntity creature;
    protected final double speed;
    protected double randPosX;
    protected double randPosY;
    protected double randPosZ;
    protected boolean running;

    public FearPanicGoal(PathAwareEntity creature, double speedIn)
    {
        this.creature = creature;
        this.speed = speedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart()
    {
        if (!this.creature.hasStatusEffect(PotionRegistryHandler.FEAR_EFFECT) && !this.creature.isOnFire())
        {
            return false;
        } else
        {
            if (this.creature.isOnFire())
            {
                BlockPos blockpos = this.getRandPos(this.creature.world, this.creature, 5, 4);
                if (blockpos != null)
                {
                    this.randPosX = blockpos.getX();
                    this.randPosY = blockpos.getY();
                    this.randPosZ = blockpos.getZ();
                    return true;
                }
            }

            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosition()
    {
        Vec3d vector3d = TargetFinder.findTarget(this.creature, 5, 4);
        if (vector3d == null)
        {
            return false;
        } else
        {
            this.randPosX = vector3d.x;
            this.randPosY = vector3d.y;
            this.randPosZ = vector3d.z;
            return true;
        }
    }

    public boolean isRunning()
    {
        return this.running;
    }

    @Override
    public void start()
    {
        this.creature.getNavigation().startMovingTo(this.randPosX, this.randPosY, this.randPosZ, this.speed);
        this.running = true;
    }

    @Override
    public void stop()
    {
        this.running = false;
    }

    @Override
    public boolean shouldContinue()
    {
        return !this.creature.getNavigation().isIdle();
    }

    @Nullable
    protected BlockPos getRandPos(BlockView worldIn, Entity entityIn, int horizontalRange, int verticalRange)
    {
        BlockPos blockpos = entityIn.getBlockPos();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();
        float f = (float) (horizontalRange * horizontalRange * verticalRange * 2);
        BlockPos blockpos1 = null;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int l = i - horizontalRange; l <= i + horizontalRange; ++l)
        {
            for (int i1 = j - verticalRange; i1 <= j + verticalRange; ++i1)
            {
                for (int j1 = k - horizontalRange; j1 <= k + horizontalRange; ++j1)
                {
                    blockpos$mutable.set(l, i1, j1);
                    if (worldIn.getFluidState(blockpos$mutable).isIn(FluidTags.WATER))
                    {
                        float f1 = (float) ((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));
                        if (f1 < f)
                        {
                            f = f1;
                            blockpos1 = new BlockPos(blockpos$mutable);
                        }
                    }
                }
            }
        }

        return blockpos1;
    }
}