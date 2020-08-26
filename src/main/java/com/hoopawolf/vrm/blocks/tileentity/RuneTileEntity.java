package com.hoopawolf.vrm.blocks.tileentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Vec3i;

public abstract class RuneTileEntity extends BlockEntity implements BlockEntityClientSerializable
{
    private boolean isActivated;
    private float degree;

    public RuneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    public void setActivated(boolean flag)
    {
        isActivated = flag;
    }

    public float getDegree()
    {
        return degree += 0.5F;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag compound)
    {
        super.fromTag(blockState, compound);
        isActivated = compound.getBoolean("activated");
    }

    @Override
    public CompoundTag toTag(CompoundTag compound)
    {
        super.toTag(compound);
        compound.putBoolean("activated", isActivated);
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

    public abstract Item getActivationItem();

    public abstract Vec3i getRayColor();
}
