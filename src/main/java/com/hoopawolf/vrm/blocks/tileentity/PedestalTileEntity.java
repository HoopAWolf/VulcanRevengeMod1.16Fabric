package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public class PedestalTileEntity extends BlockEntity implements BlockEntityClientSerializable
{
    private float degree;
    private ItemStack storedItem;

    public PedestalTileEntity()
    {
        this(TileEntityRegistryHandler.PEDESTAL_TILE_ENTITY);
    }

    public PedestalTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;

        if (storedItem == null)
        {
            storedItem = new ItemStack(Items.AIR);
        }
    }

    public float getDegree()
    {
        return degree += 0.5F;
    }

    public ItemStack getStoredItem()
    {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack)
    {
        storedItem = stack;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag compound)
    {
        super.fromTag(blockState, compound);

        CompoundTag compoundnbt = compound.getCompound("Item");
        if (!compoundnbt.isEmpty())
        {
            ItemStack itemstack = ItemStack.fromTag(compoundnbt);
            if (itemstack.isEmpty())
            {
                storedItem = new ItemStack(Items.AIR);
            } else
            {
                storedItem = itemstack;
            }
        } else
        {
            storedItem = new ItemStack(Items.AIR);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound)
    {
        super.toTag(compound);

        if (storedItem != null)
        {
            compound.put("Item", storedItem.toTag(new CompoundTag()));
        }

        return compound;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket()
    {
        CompoundTag nbtTag = new CompoundTag();
        toTag(nbtTag);
        return new BlockEntityUpdateS2CPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag)
    {
        fromTag(null, compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag)
    {
        return toTag(compoundTag);
    }

    @Override
    public CompoundTag toInitialChunkDataTag()
    {
        return this.toTag(new CompoundTag());
    }
}