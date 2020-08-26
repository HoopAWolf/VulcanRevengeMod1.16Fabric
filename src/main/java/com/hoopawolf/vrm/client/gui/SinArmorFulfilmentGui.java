package com.hoopawolf.vrm.client.gui;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class SinArmorFulfilmentGui extends DrawableHelper
{
    public void draw(MinecraftClient mc, String text, MatrixStack matrixStack, String color)
    {
        int width = (int) ((float) mc.getWindow().getScaledWidth() + mc.getWindow().getScaleFactor());
        int height = (int) ((float) mc.getWindow().getScaledHeight() + mc.getWindow().getScaleFactor());

        RenderSystem.pushMatrix();
        RenderSystem.scalef(0.5F, 0.5F, 0.5F);
        drawCenteredString(matrixStack, mc.textRenderer, text, (width / 2) + VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningWidthOffset, (height / 2) + VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarningHeightOffset, Integer.parseInt(color, 16));
        RenderSystem.popMatrix();
    }
}
