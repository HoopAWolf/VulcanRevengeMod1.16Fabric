package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;

public class BlazeRuneTileEntity extends RuneTileEntity
{
    public BlazeRuneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public BlazeRuneTileEntity()
    {
        this(TileEntityRegistryHandler.BLAZE_RUNE_TILE_ENTITY);
    }

    @Override
    public Item getActivationItem()
    {
        return Items.BLAZE_POWDER;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(255, 255, 127);
    }
}