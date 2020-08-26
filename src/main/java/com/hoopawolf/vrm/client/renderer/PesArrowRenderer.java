package com.hoopawolf.vrm.client.renderer;

import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class PesArrowRenderer extends ProjectileEntityRenderer<PesArrowEntity>
{
    private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/entity/pesarrow.png");

    public PesArrowRenderer(EntityRenderDispatcher renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected int getBlockLight(PesArrowEntity entityIn, BlockPos blockPos)
    {
        return 15;
    }

    @Override
    public Identifier getTexture(PesArrowEntity _entity)
    {
        return TEXTURE;
    }

}
