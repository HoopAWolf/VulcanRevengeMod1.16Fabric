package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;

public class ScuteRuneTileEntity extends RuneTileEntity
{
    public ScuteRuneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public ScuteRuneTileEntity()
    {
        this(TileEntityRegistryHandler.SCUTE_RUNE_TILE_ENTITY);
    }

    @Override
    public Item getActivationItem()
    {
        return Items.SCUTE;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(127, 127, 255);
    }
}