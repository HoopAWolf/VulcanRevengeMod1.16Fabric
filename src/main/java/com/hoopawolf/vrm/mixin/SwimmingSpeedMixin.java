package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SwimmingSpeedMixin
{
    @Shadow
    protected abstract boolean method_29920();

    @Shadow
    protected abstract float getBaseMovementSpeedMultiplier();

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Inject(method = "swimUpward",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onSwimUpwards(Tag<Fluid> fluid, CallbackInfo callbackInfo)
    {
        LivingEntity livingEntity = ((LivingEntity) ((Object) this));
        if (livingEntity.hasStatusEffect(PotionRegistryHandler.SWIMMING_SPEED_EFFECT))
        {
            livingEntity.setVelocity(livingEntity.getVelocity().add(0.0D, 0.03999999910593033D * (livingEntity.hasStatusEffect(PotionRegistryHandler.SWIMMING_SPEED_EFFECT) ? 1.5D : 1.0D), 0.0D));
            callbackInfo.cancel();
        }
    }

    @Inject(method = "travel",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onSwim(Vec3d movementInput, CallbackInfo callbackInfo)
    {
        LivingEntity livingEntity = ((LivingEntity) ((Object) this));

        if (livingEntity.hasStatusEffect(PotionRegistryHandler.SWIMMING_SPEED_EFFECT))
        {
            if (livingEntity.canMoveVoluntarily() || livingEntity.isLogicalSideForUpdatingMovement())
            {
                double d = 0.08D;
                boolean bl = livingEntity.getVelocity().y <= 0.0D;
                if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING))
                {
                    d = 0.01D;
                    livingEntity.fallDistance = 0.0F;
                }
                FluidState fluidState = livingEntity.world.getFluidState(livingEntity.getBlockPos());
                float j;
                double e;
                if (livingEntity.isTouchingWater() && this.method_29920() && !livingEntity.canWalkOnFluid(fluidState.getFluid()))
                {
                    e = livingEntity.getY();
                    j = livingEntity.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();
                    float g = 0.02F;
                    float h = (float) EnchantmentHelper.getDepthStrider(livingEntity);
                    if (h > 3.0F)
                    {
                        h = 3.0F;
                    }

                    if (!livingEntity.isOnGround())
                    {
                        h *= 0.5F;
                    }

                    if (h > 0.0F)
                    {
                        j += (0.54600006F - j) * h / 3.0F;
                        g += (this.getMovementSpeed() - g) * h / 3.0F;
                    }

                    if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE))
                    {
                        j = 0.96F;
                    }

                    g *= (livingEntity.hasStatusEffect(PotionRegistryHandler.SWIMMING_SPEED_EFFECT) ? 1.5D : 1.0D);

                    livingEntity.updateVelocity(g, movementInput);
                    livingEntity.move(MovementType.SELF, livingEntity.getVelocity());
                    Vec3d vec3d = livingEntity.getVelocity();
                    if (livingEntity.horizontalCollision && livingEntity.isClimbing())
                    {
                        vec3d = new Vec3d(vec3d.x, 0.2D, vec3d.z);
                    }

                    livingEntity.setVelocity(vec3d.multiply(j, 0.800000011920929D, j));
                    Vec3d vec3d2 = livingEntity.method_26317(d, bl, livingEntity.getVelocity());
                    livingEntity.setVelocity(vec3d2);
                    if (livingEntity.horizontalCollision && livingEntity.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579D - livingEntity.getY() + e, vec3d2.z))
                    {
                        livingEntity.setVelocity(vec3d2.x, 0.30000001192092896D, vec3d2.z);
                    }

                    callbackInfo.cancel();
                }
            }
        }
    }
}
