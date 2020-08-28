package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.PedestalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PedestalBlock extends BlockWithEntity
{
    public PedestalBlock(Block.Settings properties)
    {
        super(properties.nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new PedestalTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
        if (hasBlockEntity())
        {
            PedestalTileEntity rune = (PedestalTileEntity) worldIn.getBlockEntity(pos);

            if (rune.getStoredItem().getItem() != Items.AIR)
            {
                dropItem(state, pos, worldIn, player);
            } else
            {
                rune.setStoredItem(player.getMainHandStack().copy());
                rune.getStoredItem().setCount(1);

                if (!worldIn.isClient)
                {
                    if (!player.isCreative())
                    {
                        player.getMainHandStack().decrement(1);
                    }
                }
            }
            rune.markDirty();
        }

        return ActionResult.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasBlockEntity())
        {
            PedestalTileEntity rune = (PedestalTileEntity) worldIn.getBlockEntity(pos);

            if (!worldIn.isClient)
            {
                player.dropItem(rune.getStoredItem(), true);
            }

            rune.setStoredItem(new ItemStack(Items.AIR));
            rune.markDirty();
        }
    }
}
