package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.NetherRuneTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class NetherRuneBlock extends RuneBlock
{
    public NetherRuneBlock(Block.Settings properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new NetherRuneTileEntity();
    }
}
