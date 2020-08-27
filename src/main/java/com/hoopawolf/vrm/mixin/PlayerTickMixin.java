package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerTickMixin
{
    @Shadow
    @Final
    public PlayerAbilities abilities;

    @Inject(method = "tick",
            at = @At(value = "HEAD"))
    private void playerTickEvent(CallbackInfo callbackInfo)
    {
        PlayerEntity player = ((PlayerEntity) ((Object) this));

        if (player.hasStatusEffect(PotionRegistryHandler.FLIGHT_EFFECT))
        {
            if (!player.abilities.allowFlying)
            {
                player.abilities.allowFlying = true;
                player.abilities.flying = true;
            }
        } else
        {
            if (!player.abilities.creativeMode && !player.isSpectator())
            {
                player.abilities.allowFlying = false;
                player.abilities.flying = false;
            }
        }

        if (player.hasStatusEffect(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT))
        {
            if (player.getHungerManager().getFoodLevel() > 3)
            {
                player.getHungerManager().setFoodLevel(3);
                player.getHungerManager().addExhaustion(40);
            }

            if (!player.world.isClient)
            {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1, 3));
            }
        }

        if (!player.world.isClient)
        {
            if (player.hasStatusEffect(PotionRegistryHandler.GREED_TRIAL_EFFECT))
            {
                int totalItems = 0;
                for (ItemStack itemstack : player.inventory.main)
                {
                    totalItems += itemstack.getCount();
                }

                float invenSize = ((float) totalItems / (float) (player.inventory.main.size() * 64));

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, (int) (4.0F * (1.0F - invenSize))));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, (int) (4.0F * (1.0F - invenSize))));
            }
        }
    }
}
