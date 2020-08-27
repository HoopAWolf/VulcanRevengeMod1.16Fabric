package com.hoopawolf.vrm.helper;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RayTracingHelper
{
    public static final RayTracingHelper INSTANCE = new RayTracingHelper();
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private Entity target = null;
    private BlockPos endPos;

    private RayTracingHelper()
    {
    }

    public void fire()
    {
        Entity viewpoint = mc.getCameraEntity();
        if (viewpoint == null)
            return;

        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY)
        {
            for (Entity hit : viewpoint.world.getEntities((Class<? extends Entity>) null,
                    new Box(mc.crosshairTarget.getPos().getX(), mc.crosshairTarget.getPos().getY(), mc.crosshairTarget.getPos().getZ(), mc.crosshairTarget.getPos().getX() + 1, mc.crosshairTarget.getPos().getY() + 1, mc.crosshairTarget.getPos().getZ() + 1),
                    e -> e != viewpoint && !(e instanceof SlothPetEntity)))
            {
                // Reference.LOGGER.debug("Got something WOO: " + hit);
                this.target = hit;
            }

            if (this.target != null)
            {
                return;
            }
        }

        assert mc.interactionManager != null;
        this.target = this.rayTrace(viewpoint, mc.interactionManager.getReachDistance() * 10, 0);
    }

    public Entity rayTrace(Entity entity, double playerReach, float partialTicks)
    {
        Vec3d eyePosition = entity.getCameraPosVec(partialTicks);
        Vec3d lookVector = entity.getRotationVec(partialTicks);
        Vec3d traceStart = eyePosition.add(lookVector.x, lookVector.y, lookVector.z);
        Vec3d traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        traceStart = new Vec3d((int) traceStart.getX(), (int) traceStart.getY(), (int) traceStart.getZ());
        traceEnd = new Vec3d((int) traceEnd.getX(), (int) traceEnd.getY(), (int) traceEnd.getZ());
        Vec3d checkPos = traceStart;
        float percentage = 0.0F;
        endPos = null;

        while (checkPos.getX() != traceEnd.getX() || checkPos.getY() != traceEnd.getY() || checkPos.getZ() != traceEnd.getZ())
        {
            //Reference.LOGGER.debug("Start: " + traceStart + " Checking curr pos: " + checkPos + " with ending of: " + traceEnd + " at percentage: " + percentage);

            if (entity.world.getBlockState(new BlockPos(checkPos)).isOpaque() && !(entity.world.getBlockState(new BlockPos(checkPos)).getBlock() instanceof PlantBlock))
            {
                endPos = new BlockPos(checkPos);
                // Reference.LOGGER.debug("Hit something solid: " + entity.world.getBlockState(new BlockPos(checkPos)));
                break;
            }

            for (Entity hit : entity.world.getEntities((Entity) null,
                    new Box(checkPos.getX(), checkPos.getY(), checkPos.getZ(), checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1),
                    e -> e != entity && !(e instanceof SlothPetEntity)))
            {
                // Reference.LOGGER.debug("Got something WOO: " + hit);
                endPos = hit.getBlockPos();
                return hit;
            }
            percentage += 0.01F;
            checkPos = VRMMathHelper.lerp(traceStart, traceEnd, MathHelper.clamp(percentage, 0.0F, 1.0F));
        }
        // Reference.LOGGER.debug("Didn't get shit");
        return null;
    }

    public Entity getTarget()
    {
        return this.target;
    }

    public BlockPos getFinalPos()
    {
        return this.endPos;
    }
}