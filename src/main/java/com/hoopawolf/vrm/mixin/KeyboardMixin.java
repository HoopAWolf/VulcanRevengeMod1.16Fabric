package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.access.VariableAccess;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin
{
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey",
            at = @At(value = "TAIL"))
    private void onKeyPressed(long window, int key, int scancode, int i, int j, CallbackInfo callbackInfo)
    {
        VariableAccess.isMoving = false;
        if (window == this.client.getWindow().getHandle())
        {
            if (i == GLFW.GLFW_REPEAT || i == GLFW.GLFW_PRESS)
            {
                if (client.player != null)
                {
                    switch (key)
                    {
                        case GLFW.GLFW_KEY_COMMA:
                            if (VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarning && client.player.isSneaking() && client.player.inventory.armor.get(3).getItem() instanceof SinsArmorItem)
                            {
                                VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningHeightOffset -= 5;
                                VariableAccess.isMoving = true;
                            }
                            break;
                        case GLFW.GLFW_KEY_PERIOD:
                            if (VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarning && client.player.isSneaking() && client.player.inventory.armor.get(3).getItem() instanceof SinsArmorItem)
                            {
                                VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningHeightOffset += 5;
                                VariableAccess.isMoving = true;
                            }
                            break;

                        case GLFW.GLFW_KEY_SEMICOLON:
                            if (VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarning && client.player.isSneaking() && client.player.inventory.armor.get(3).getItem() instanceof SinsArmorItem)
                            {
                                VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningWidthOffset -= 5;
                                VariableAccess.isMoving = true;
                            }
                            break;
                        case GLFW.GLFW_KEY_APOSTROPHE:
                            if (VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarning && client.player.isSneaking() && client.player.inventory.armor.get(3).getItem() instanceof SinsArmorItem)
                            {
                                VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningWidthOffset += 5;
                                VariableAccess.isMoving = true;
                            }
                            break;
                    }
                }
            }
        }
    }
}