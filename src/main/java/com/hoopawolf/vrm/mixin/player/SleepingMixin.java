package com.hoopawolf.vrm.mixin.player;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SleepingMixin
{
    @Inject(method = "wakeUp(ZZ)V",
            at = @At(value = "INVOKE"), cancellable = true)
    private void checkSleepingTimeEvent(boolean bl, boolean updateSleepingPlayers, CallbackInfo callbackInfo)
    {
        PlayerEntity playerEntity = ((PlayerEntity) ((Object) this));

        if (playerEntity.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR) && SinsArmorItem.isActivated(playerEntity.inventory.armor.get(3)) ||
                playerEntity.hasStatusEffect(PotionRegistryHandler.SLEEP_EFFECT))
        {
            if (playerEntity.getSleepTimer() >= 100)
            {
                long i = playerEntity.world.getTimeOfDay() + 24000L;
                if (playerEntity.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
                {
                    if (playerEntity.world.isDay())
                    {
                        if (VulcanRevengeMod.VRM_CONFIG.itemconfig.slothMaskTurnNight)
                        {
                            if (!playerEntity.world.isClient)
                            {
                                ((ServerWorld) playerEntity.world).setTimeOfDay((i - i % 24000L) - 11000L);
                            }
                        }
                    } else
                    {
                        if (VulcanRevengeMod.VRM_CONFIG.itemconfig.slothMaskTurnDay)
                        {
                            if (!playerEntity.world.isClient)
                            {
                                ((ServerWorld) playerEntity.world).setTimeOfDay((i - i % 24000L));
                            }
                        }
                    }
                }
            } else
            {
                callbackInfo.cancel();
            }
        }
    }
}
