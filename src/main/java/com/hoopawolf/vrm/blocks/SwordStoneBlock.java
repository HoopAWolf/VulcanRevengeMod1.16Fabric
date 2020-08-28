package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.SwordStoneTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SwordStoneBlock extends BlockWithEntity
{

    public SwordStoneBlock(Block.Settings properties)
    {
        super(properties.nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new SwordStoneTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
        SwordStoneTileEntity swordStone = (SwordStoneTileEntity) worldIn.getBlockEntity(pos);

        if (!swordStone.isActivated())
        {
            swordStone.setActivated(true);
            swordStone.setUUID(player.getUuid());
            swordStone.markDirty();
        } else if (swordStone.isDone())
        {
            dropItem(state, pos, worldIn, player);
        }

        return ActionResult.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasBlockEntity())
        {
            SwordStoneTileEntity swordStone = (SwordStoneTileEntity) worldIn.getBlockEntity(pos);

            player.dropItem(new ItemStack(swordStone.getActivationItem()), true);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}
