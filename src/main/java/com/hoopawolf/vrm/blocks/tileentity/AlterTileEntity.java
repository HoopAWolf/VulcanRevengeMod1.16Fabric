package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

class ItemStore
{
    private final String[] storage = new String[3];

    public ItemStore(String first, String second, String third)
    {
        storage[0] = first;
        storage[1] = second;
        storage[2] = third;
    }

    public String[] getStorage()
    {
        return storage;
    }
}

public class AlterTileEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable
{
    private final BlockPos[] runePos =
            {
                    new BlockPos(-2, 0, 2),
                    new BlockPos(2, 0, 2),
                    new BlockPos(0, 1, -2)
            };

    private final ItemStore[] sinItems =
            {
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.gluttonyItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.gluttonyItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.gluttonyItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.envyItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.envyItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.envyItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.lustItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.lustItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.lustItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.greedItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.greedItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.greedItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.slothItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.slothItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.slothItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.wrathItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.wrathItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.wrathItemThree),
                    new ItemStore(VulcanRevengeMod.VRM_CONFIG.itemconfig.prideItemOne, VulcanRevengeMod.VRM_CONFIG.itemconfig.prideItemTwo, VulcanRevengeMod.VRM_CONFIG.itemconfig.prideItemThree)
            };

    private final ItemStack[] sinPotion =
            {
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.GLUTTONY_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.ENVY_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.LUST_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.GREED_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.SLOTH_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.WRATH_TRIAL_POTION),
                    PotionUtil.setPotion(new ItemStack(Items.POTION), PotionRegistryHandler.PRIDE_TRIAL_POTION)
            };

    private float timer, degree;
    private int sinType;
    private boolean activated, activatedDone;

    public AlterTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
        timer = 0.0F;
        sinType = 0;
    }

    public AlterTileEntity()
    {
        this(TileEntityRegistryHandler.ALTER_TILE_ENTITY);
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean flag)
    {
        activated = flag;
    }

    public int getSinType()
    {
        return sinType;
    }

    public void setSinType(int typeIn)
    {
        sinType = typeIn;
    }

    public boolean isDone()
    {
        return activatedDone;
    }

    public void setDone(boolean flag)
    {
        activatedDone = flag;
    }

    public float getDegree()
    {
        return degree;
    }

    public float getTimer()
    {
        return timer;
    }

    @Override
    public void tick()
    {
        if (!isDone())
        {
            ArrayList<Item> tempItems = new ArrayList<>();
            for (BlockPos pos : runePos)
            {
                if (world.getBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())).getBlock().hasBlockEntity() &&
                        world.getBlockEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())) instanceof PedestalTileEntity)
                {
                    if (((PedestalTileEntity) Objects.requireNonNull(world.getBlockEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).getStoredItem().getItem().equals(Items.AIR))
                    {
                        resetData();
                        break;
                    } else
                    {
                        tempItems.add(((PedestalTileEntity) Objects.requireNonNull(world.getBlockEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).getStoredItem().getItem());
                    }
                } else
                {
                    resetData();
                    break;
                }
            }

            if (tempItems.size() == 3)
            {
                boolean flag = false;
                int type = 0;
                for (ItemStore store : sinItems)
                {
                    for (String item : store.getStorage())
                    {
                        for (int i = 0; i < tempItems.size(); ++i)
                        {
                            if (tempItems.get(i).getTranslationKey().equals(item))
                            {
                                tempItems.remove(i);
                                break;
                            }
                        }
                    }

                    if (tempItems.size() == 0)
                    {
                        flag = true;
                        setSinType(type);
                        break;
                    } else if (tempItems.size() != 3)
                    {
                        break;
                    }

                    ++type;
                }

                if (!flag)
                {
                    resetData();
                }
            }

            if (isActivated())
            {
                if (timer <= 0)
                {
                    if (!world.isClient)
                    {
                        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 2.0F, 0.1F);
                    }
                }

                timer += 0.5F;

                if (!world.isClient)
                {
                    for (BlockPos pos : runePos)
                    {
                        Vec3d block = new Vec3d(getPos().getX() + pos.getX() + 0.5D, getPos().getY() + pos.getY() + 1.5D, getPos().getZ() + pos.getZ() + 0.5D);
                        Vec3d dir = new Vec3d(getPos().getX() + 0.5D, getPos().getY(), getPos().getZ() + 0.5).subtract(new Vec3d(block.getX(), block.getY(), block.getZ())).normalize();

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(this.world);

                        SpawnParticleMessage particleFireworks = new SpawnParticleMessage(new Vec3d(block.getX(), block.getY(), block.getZ()), new Vec3d(dir.getX(), dir.getY(), dir.getZ()), 3, 12, 0.0F);
                        PacketByteBuf passedDataFireWork = new PacketByteBuf(Unpooled.buffer());
                        passedDataFireWork.writeInt(particleFireworks.getMessageType());
                        particleFireworks.encode(passedDataFireWork);

                        SpawnParticleMessage particleEndrod = new SpawnParticleMessage(new Vec3d(block.getX(), block.getY(), block.getZ()), new Vec3d(dir.getX(), dir.getY(), dir.getZ()), 3, 0, 0.0F);
                        PacketByteBuf passedDataEndRod = new PacketByteBuf(Unpooled.buffer());
                        passedDataEndRod.writeInt(particleEndrod.getMessageType());
                        particleEndrod.encode(passedDataEndRod);

                        playerInDimension.forEach(player ->
                        {
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedDataFireWork);
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedDataEndRod);
                        });
                    }
                }

                if (getTimer() > 100)
                {
                    setDone(true);

                    if (!world.isClient)
                    {
                        world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 2.0F, 0.1F);
                    }

                    for (BlockPos pos : runePos)
                    {
                        world.setBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ()), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        degree += 0.5F;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag compound)
    {
        super.fromTag(blockState, compound);
        activated = compound.getBoolean("activated");
        activatedDone = compound.getBoolean("activatedDone");
        timer = compound.getFloat("timer");
    }

    @Override
    public CompoundTag toTag(CompoundTag compound)
    {
        super.toTag(compound);
        compound.putBoolean("activated", activated);
        compound.putBoolean("activatedDone", activatedDone);
        compound.putFloat("timer", timer);
        return compound;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket()
    {
        CompoundTag nbtTag = new CompoundTag();
        toTag(nbtTag);
        return new BlockEntityUpdateS2CPacket(getPos(), 3, nbtTag);
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
        CompoundTag nbt = new CompoundTag();
        this.toTag(nbt);
        return nbt;
    }

    public void resetData()
    {
        timer = 0;
        degree = 0.0F;
        setDone(false);
        setActivated(false);
    }

    public ItemStack getActivationItem()
    {
        return isDone() ? sinPotion[getSinType()] : PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
    }
}

