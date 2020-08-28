package com.hoopawolf.vrm.mixin.client;

import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class ItemEquipAnimationMixin
{
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private float equipProgressMainHand;

    @Shadow
    private float equipProgressOffHand;

    @Shadow
    private ItemStack mainHand;

    @Shadow
    private ItemStack offHand;

    @Inject(method = "updateHeldItems",
            at = @At(value = "INVOKE"), cancellable = true)
    private void cancelItemAnimation(CallbackInfo callbackInfo)
    {
        HeldItemRenderer renderer = ((HeldItemRenderer) (Object) this);

        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack itemStack = clientPlayerEntity.getMainHandStack();
        ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();

        if (itemStack.getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD) ||
                itemStack.getItem().equals(ItemBlockRegistryHandler.WAR_SWORD) ||
                itemStack.getItem().equals(ItemBlockRegistryHandler.FAM_SCALE))
        {

            if (!clientPlayerEntity.isRiding())
            {
                float f = clientPlayerEntity.getAttackCooldownProgress(1.0F);

                if (this.mainHand != itemStack)
                {
                    this.mainHand = itemStack;
                }

                this.equipProgressMainHand += MathHelper.clamp(f * f * f - this.equipProgressMainHand, -0.4F, 0.4F);
                this.equipProgressOffHand += MathHelper.clamp((float) (this.offHand == itemStack2 ? 1 : 0) - this.equipProgressOffHand, -0.4F, 0.4F);
            }
        } else if (itemStack2.getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD) ||
                itemStack2.getItem().equals(ItemBlockRegistryHandler.WAR_SWORD) ||
                itemStack2.getItem().equals(ItemBlockRegistryHandler.FAM_SCALE))
        {
            float f = clientPlayerEntity.getAttackCooldownProgress(1.0F);

            if (this.offHand != itemStack2)
            {
                this.offHand = itemStack2;
            }

            this.equipProgressMainHand += MathHelper.clamp((this.mainHand == itemStack ? f * f * f : 0.0F) - this.equipProgressMainHand, -0.4F, 0.4F);
            this.equipProgressOffHand += MathHelper.clamp(1 - this.equipProgressOffHand, -0.4F, 0.4F);
        }
    }
}
