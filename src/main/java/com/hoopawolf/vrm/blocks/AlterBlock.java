package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.AlterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AlterBlock extends BlockWithEntity
{
    public AlterBlock(Block.Settings properties)
    {
        super(properties.nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new AlterTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
        AlterTileEntity alter = (AlterTileEntity) worldIn.getBlockEntity(pos);

        if (!alter.isActivated() && PotionUtil.getPotion(player.getMainHandStack()).equals(Potions.WATER))
        {
            alter.setActivated(true);
            if (!worldIn.isClient)
            {
                if (!player.isCreative())
                {
                    player.getMainHandStack().decrement(1);
                }
            }
        } else if (alter.isDone() || alter.isActivated())
        {
            dropItem(state, pos, worldIn, player);
        }
        alter.markDirty();

        return ActionResult.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasBlockEntity())
        {
            AlterTileEntity alter = (AlterTileEntity) worldIn.getBlockEntity(pos);

            if (!worldIn.isClient)
            {
                ItemStack item = alter.getActivationItem().copy();
                player.dropItem(item, true);
            }

            alter.setActivated(false);
            alter.resetData();
            alter.markDirty();
        }
    }
}

