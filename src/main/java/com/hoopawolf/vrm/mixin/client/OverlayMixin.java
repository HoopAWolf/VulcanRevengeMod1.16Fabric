package com.hoopawolf.vrm.mixin.client;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.access.VariableAccess;
import com.hoopawolf.vrm.client.gui.SinArmorFulfilmentGui;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class OverlayMixin
{
    private static final SinArmorFulfilmentGui armorGui = new SinArmorFulfilmentGui();
    private static final Identifier FEAR_OVERLAY = new Identifier(Reference.MOD_ID, "textures/gui/fearoverlay.png");

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Inject(method = "render",
            at = @At(value = "HEAD"))
    private void renderOverlay(MatrixStack matrixStack, float f, CallbackInfo callbackInfo)
    {
        if (client.player.hasStatusEffect(PotionRegistryHandler.FEAR_EFFECT))
        {
            renderFearOverlay();
        }

        if (VulcanRevengeMod.VRM_CONFIG.clientconfig.sinMaskWarning)
        {
            if ((client.player.inventory.armor.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(client.player.inventory.armor.get(3))) || VariableAccess.isMoving)
            {
                float percentage = (float) ((SinsArmorItem) client.player.inventory.armor.get(3).getItem()).getFulfilmentAmount(client.player.inventory.armor.get(3));
                if (percentage >= 0.2F || VariableAccess.isMoving)
                {
                    String fulfilment = client.player.inventory.armor.get(3).getName().getString() + I18n.translate("gui.text.urge") + ((percentage >= 0.8F) ?
                            I18n.translate("gui.text.urgehigh") : ((percentage >= 0.5F) ?
                            I18n.translate("gui.text.urgeaverage") : I18n.translate("gui.text.urgelow")));

                    armorGui.draw(client, fulfilment, matrixStack, ((percentage >= 0.8F) ? "FF2222" : ((percentage >= 0.5F) ? "FF7909" : "55D100")));
                }
            }
        }
    }

    private void renderFearOverlay()
    {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();
        this.client.getTextureManager().bindTexture(FEAR_OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(-client.world.random.nextInt(10), this.scaledHeight + client.world.random.nextInt(10), -90.0D).texture(0.0F, 1.0F).next();
        bufferbuilder.vertex(this.scaledWidth + client.world.random.nextInt(10), this.scaledHeight + client.world.random.nextInt(10), -90.0D).texture(1.0F, 1.0F).next();
        bufferbuilder.vertex(this.scaledWidth + client.world.random.nextInt(10), -client.world.random.nextInt(10), -90.0D).texture(1.0F, 0.0F).next();
        bufferbuilder.vertex(-client.world.random.nextInt(10), -client.world.random.nextInt(10), -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
