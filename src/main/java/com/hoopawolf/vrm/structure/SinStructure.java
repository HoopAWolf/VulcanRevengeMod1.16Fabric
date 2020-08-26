package com.hoopawolf.vrm.structure;

import com.google.common.collect.Lists;
import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.structure.piece.SinStructurePiece;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SinStructure extends StructureFeature<DefaultFeatureConfig>
{
    private static final List<Biome.SpawnEntry> spawnList = Lists.newArrayList(new Biome.SpawnEntry(EntityType.BLAZE, 10, 2, 3), new Biome.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 5, 2, 3), new Biome.SpawnEntry(EntityType.MAGMA_CUBE, 3, 1, 2));

    public SinStructure(Codec<DefaultFeatureConfig> p_i231997_1_)
    {
        super(p_i231997_1_);
    }

    @Override
    public String getName()
    {
        return SinStructurePiece.SIN_STRUCTURE_LOC.toString();
    }

    protected ChunkPos getStartPositionForPosition(ChunkGenerator chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
    {
        int featureDistance = VulcanRevengeMod.VRM_CONFIG.structureconfig.maxSinStructureAway;
        int featureSeparation = VulcanRevengeMod.VRM_CONFIG.structureconfig.minSinStructureAway;

        int xTemp = x + featureDistance * spacingOffsetsX;
        int zTemp = z + featureDistance * spacingOffsetsZ;
        int validChunkX = (xTemp < 0 ? xTemp - featureDistance + 1 : xTemp) / featureDistance;
        int validChunkZ = (zTemp < 0 ? zTemp - featureDistance + 1 : zTemp) / featureDistance;
        ((ChunkRandom) random).setRegionSeed(62353535, x, z, 62226333);
        validChunkX *= featureDistance;
        validChunkZ *= featureDistance;
        validChunkX += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        validChunkZ += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        return new ChunkPos(validChunkX, validChunkZ);
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator p_230363_1_, BiomeSource p_230363_2_, long p_230363_3_, ChunkRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome p_230363_8_, ChunkPos p_230363_9_, DefaultFeatureConfig p_230363_10_)
    {
        ChunkPos chunkpos = this.getStartPositionForPosition(p_230363_1_, p_230363_5_, p_230363_6_, p_230363_7_, 0, 0);

        if (p_230363_6_ == chunkpos.x && p_230363_7_ == chunkpos.z && p_230363_5_.nextInt(100) < VulcanRevengeMod.VRM_CONFIG.structureconfig.structureSinSpawnChance)
        {
            return p_230363_2_.hasStructureFeature(this);
        }

        return false;
    }

    @Override
    public List<Biome.SpawnEntry> getMonsterSpawns()
    {
        return spawnList;
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory()
    {
        return SinStructure.Start::new;
    }

    @Override
    public GenerationStep.Feature method_28663()
    {
        return GenerationStep.Feature.UNDERGROUND_STRUCTURES;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig>
    {
        public Start(StructureFeature<DefaultFeatureConfig> structureIn, int chunkX, int chunkZ, BlockBox mutableBoundingBox, int referenceIn, long seedIn)
        {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generateStructure(ServerWorldAccess p_230366_1_, StructureAccessor p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, BlockBox p_230366_5_, ChunkPos p_230366_6_)
        {
            super.generateStructure(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
            int i = this.boundingBox.minY;

            for (int j = p_230366_5_.minX; j <= p_230366_5_.maxX; ++j)
            {
                for (int k = p_230366_5_.minZ; k <= p_230366_5_.maxZ; ++k)
                {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!p_230366_1_.isAir(blockpos) && this.boundingBox.contains(blockpos))
                    {
                        boolean flag = false;
                        Iterator var12 = this.children.iterator();

                        while (var12.hasNext())
                        {
                            StructurePiece structurePiece = (StructurePiece) var12.next();
                            if (structurePiece.getBoundingBox().contains(blockpos))
                            {
                                flag = true;
                                break;
                            }
                        }

                        if (flag)
                        {
                            for (int l = i - 1; l > 1; --l)
                            {
                                BlockPos blockpos1 = new BlockPos(j, l, k);
                                if (!p_230366_1_.isAir(blockpos1) && !p_230366_1_.getBlockState(blockpos1).getMaterial().isLiquid())
                                {
                                    break;
                                }

                                if (p_230366_1_.getRandom().nextInt(100) > 10)
                                {
                                    if (p_230366_1_.getRandom().nextInt(100) < 30)
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.NETHER_BRICKS.getDefaultState(), 2);
                                    } else if (p_230366_1_.getRandom().nextInt(100) > 70)
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.CRACKED_NETHER_BRICKS.getDefaultState(), 2);
                                    } else
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.MAGMA_BLOCK.getDefaultState(), 2);
                                    }
                                } else
                                {
                                    p_230366_1_.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void init(ChunkGenerator generator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, DefaultFeatureConfig p_230364_6_)
        {
            BlockRotation rotation = BlockRotation.NONE;

            int x = (chunkX << 4);
            int z = (chunkZ << 4);

            int k = generator.getSeaLevel();
            int l = k + this.random.nextInt(generator.getMaxY() - 2 - k);
            BlockView iblockreader = generator.getColumnSample(x, z);

            for (BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, l, z); l > k; --l)
            {
                BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
                blockpos$mutable.move(Direction.DOWN);
                BlockState blockstate1 = iblockreader.getBlockState(blockpos$mutable);
                if (blockstate.isAir() && (blockstate1.isOf(Blocks.SOUL_SAND) || blockstate1.isSideSolidFullSquare(iblockreader, blockpos$mutable, Direction.UP)))
                {
                    break;
                }
            }

            if (l > k)
            {
                BlockPos blockpos = new BlockPos(x, l, z);

                SinStructurePiece.start(templateManagerIn, blockpos, rotation, this.children, this.random);
                this.setBoundingBoxFromChildren();
                Reference.LOGGER.log(Level.DEBUG, "Sin Structure at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
            }
        }
    }
}
