package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.ScuteRuneTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class ScuteRuneBlock extends RuneBlock
{
    public ScuteRuneBlock(Block.Settings properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new ScuteRuneTileEntity();
    }
}
