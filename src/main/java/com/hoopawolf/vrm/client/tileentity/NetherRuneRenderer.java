package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.NetherRuneTileEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;

public class NetherRuneRenderer extends RuneRenderer<NetherRuneTileEntity>
{
    public NetherRuneRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }
}