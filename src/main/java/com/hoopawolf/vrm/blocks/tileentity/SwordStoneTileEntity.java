package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class SwordStoneTileEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable
{
    private final BlockPos[] runePos =
            {
                    new BlockPos(2, 1, 2),
                    new BlockPos(-2, 1, -2),
                    new BlockPos(-2, 1, 2),
                    new BlockPos(2, 1, -2)
            };

    private final ArrayList<EntityType[]> phraseList = new ArrayList<>();

    private float timer, degree;
    private boolean activated, activatedDone;
    private UUID player;

    public SwordStoneTileEntity(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
        timer = 0.0F;
        player = null;

        EntityType[] firePhase = {
                EntityType.RAVAGER,
                EntityType.MAGMA_CUBE,
                EntityType.ZOMBIFIED_PIGLIN
        };
        phraseList.add(firePhase);

        EntityType[] slowPhase = {
                EntityType.ZOMBIE,
                EntityType.SKELETON,
                EntityType.SPIDER
        };
        phraseList.add(slowPhase);

        EntityType[] heavyPhase = {
                EntityType.WITCH,
                EntityType.VEX,
                EntityType.SILVERFISH
        };
        phraseList.add(heavyPhase);

        EntityType[] witherPhase = {
                EntityType.WITHER_SKELETON,
                EntityType.CAVE_SPIDER,
                EntityType.VINDICATOR,
                EntityType.EVOKER
        };
        phraseList.add(witherPhase);
    }

    public SwordStoneTileEntity()
    {
        this(TileEntityRegistryHandler.SWORD_STONE_TILE_ENTITY);
    }

    public boolean anyPlayerInRange()
    {
        return player != null && world.getPlayerByUuid(player) != null && Objects.requireNonNull(world.getPlayerByUuid(player)).squaredDistanceTo(getPos().getX(), getPos().getY(), getPos().getZ()) < 60;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean flag)
    {
        activated = flag;
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

    public void setUUID(UUID _uuidIn)
    {
        player = _uuidIn;
    }

    @Override
    public void tick()
    {
        if (!isDone())
        {
            for (BlockPos pos : runePos)
            {
                if (world.getBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())).getBlock().hasBlockEntity() &&
                        world.getBlockEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())) instanceof RuneTileEntity)
                {
                    if (!((RuneTileEntity) Objects.requireNonNull(world.getBlockEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).isActivated())
                    {
                        resetData();
                        break;
                    }
                } else
                {
                    resetData();
                    break;
                }
            }

            if (isActivated())
            {
                if (!anyPlayerInRange())
                {
                    resetData();
                } else
                {
                    if (timer <= 0)
                    {
                        if (!world.isClient)
                        {
                            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 2.0F, 0.1F);
                            ((ServerWorld) world.getWorld()).setWeather(0, 6000, true, true);
                        }
                    } else if (timer % 150 == 0)
                    {
                        if (!world.isClient)
                        {
                            int max = world.random.nextInt(8) + 5;
                            for (int i = 0; i < max; ++i)
                            {
                                MobEntity entity = (MobEntity) phraseList.get(((int) timer / 150) - 1)[world.random.nextInt(3)].create(world);
                                entity.refreshPositionAndAngles(this.getPos().getX() + (world.random.nextInt(5) - world.random.nextInt(5)),
                                        this.getPos().getY() + 1.0D,
                                        this.getPos().getZ() + (world.random.nextInt(5) - world.random.nextInt(5)), 0.0F, 0.0F);
                                entity.setTarget(world.getPlayerByUuid(player));

                                if (entity instanceof SkeletonEntity)
                                {
                                    entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                                } else if (entity instanceof ZombifiedPiglinEntity || entity instanceof WitherSkeletonEntity)
                                {
                                    entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                                }

                                world.spawnEntity(entity);
                            }
                        }
                    }

                    timer += 0.5F;

                    if (!world.isClient)
                    {
                        for (BlockPos pos : runePos)
                        {
                            Vec3d block = new Vec3d(getPos().getX() + pos.getX() + 0.5D, getPos().getY() + pos.getY() + 1.75D, getPos().getZ() + pos.getZ() + 0.5D);
                            Vec3d dir = new Vec3d(getPos().getX() + 0.5D, getPos().getY(), getPos().getZ() + 0.5).subtract(new Vec3d(block.getX(), block.getY(), block.getZ())).normalize();

                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(this.world);

                            SpawnParticleMessage particleFireworks = new SpawnParticleMessage(new Vec3d(block.getX(), block.getY(), block.getZ()), new Vec3d(dir.getX(), dir.getY(), dir.getZ()), 3, 1, 0.0F);
                            PacketByteBuf passedDataFireWork = new PacketByteBuf(Unpooled.buffer());
                            passedDataFireWork.writeInt(particleFireworks.getMessageType());
                            particleFireworks.encode(passedDataFireWork);

                            SpawnParticleMessage particleEndrod = new SpawnParticleMessage(new Vec3d(block.getX(), block.getY(), block.getZ()), new Vec3d(dir.getX(), dir.getY(), dir.getZ()), 3, 2, 0.0F);
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

                    if (getTimer() > 600)
                    {
                        setDone(true);

                        if (!world.isClient)
                        {
                            world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 2.0F, 0.1F);
                            ((ServerWorld) world.getWorld()).setWeather(6000, 0, false, false);
                        }

                        for (BlockPos pos : runePos)
                        {
                            world.setBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ()), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        if (getTimer() > 300)
        {
            degree += 0.5F;
        }
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

    private void resetData()
    {
        timer = 0;
        degree = 0.0F;
        setActivated(false);
    }

    public Item getActivationItem()
    {
        return isDone() ? ItemBlockRegistryHandler.VULCAN_SWORD : Items.STONE_SWORD;
    }
}
