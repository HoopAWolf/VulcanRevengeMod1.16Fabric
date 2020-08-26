package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.MagmaRuneTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class MagmaRuneBlock extends RuneBlock
{
    public MagmaRuneBlock(AbstractBlock.Settings properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new MagmaRuneTileEntity();
    }
}
