package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.RuneTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class RuneBlock extends BlockWithEntity
{

    public RuneBlock(Block.Settings properties)
    {
        super(properties.nonOpaque());
    }


    @Override
    public abstract BlockEntity createBlockEntity(BlockView world);

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
            RuneTileEntity rune = (RuneTileEntity) worldIn.getBlockEntity(pos);

            if (!rune.isActivated() && player.getMainHandStack().getItem().equals(rune.getActivationItem().asItem()))
            {
                rune.setActivated(true);
                rune.markDirty();

                if (!worldIn.isClient)
                {
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 2.0F, 0.1F);

                    if (!player.isCreative())
                    {
                        player.getMainHandStack().decrement(1);
                    }
                }
            } else if (rune.isActivated())
            {
                dropItem(state, pos, worldIn, player);
                if (!worldIn.isClient)
                {
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 2.0F, 0.1F);
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasBlockEntity())
        {
            RuneTileEntity rune = (RuneTileEntity) worldIn.getBlockEntity(pos);

            rune.setActivated(false);
            rune.markDirty();

            if (!worldIn.isClient)
            {
                player.dropItem(new ItemStack(rune.getActivationItem()), true);
            }
        }
    }
}

