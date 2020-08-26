package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;

public class NetherRuneTileEntity extends RuneTileEntity
{
    public NetherRuneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public NetherRuneTileEntity()
    {
        this(TileEntityRegistryHandler.NETHER_RUNE_TILE_ENTITY);
    }

    @Override
    public Item getActivationItem()
    {
        return Items.NETHER_BRICK;
    }

    @Override
    public Vec3i getRayColor()
    {
        return new Vec3i(255, 255, 255);
    }
}