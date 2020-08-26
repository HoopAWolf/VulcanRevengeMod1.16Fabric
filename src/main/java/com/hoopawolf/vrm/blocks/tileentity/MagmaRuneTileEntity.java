package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;

public class MagmaRuneTileEntity extends RuneTileEntity
{
    public MagmaRuneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public MagmaRuneTileEntity()
    {
        this(TileEntityRegistryHandler.MAGMA_RUNE_TILE_ENTITY);
    }

    @Override
    public Item getActivationItem()
    {
        return Items.MAGMA_CREAM;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(0, 0, 255);
    }
}