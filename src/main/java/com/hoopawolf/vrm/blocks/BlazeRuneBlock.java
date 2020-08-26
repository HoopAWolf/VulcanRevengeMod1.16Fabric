package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.BlazeRuneTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlazeRuneBlock extends RuneBlock
{

    public BlazeRuneBlock(Block.Settings properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new BlazeRuneTileEntity();
    }
}
