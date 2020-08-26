package com.hoopawolf.vrm.structure.piece;

import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.StructureRegistryHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class SwordStructurePiece
{
    public static final Identifier SWORD_STRUCTURE_LOC = new Identifier(Reference.MOD_ID, "swordstructure");

    public static void start(StructureManager templateManager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieceList, Random random)
    {
        pieceList.add(new SwordStructurePiece.Piece(templateManager, SWORD_STRUCTURE_LOC, pos, rotation));
    }

    public static class Piece extends SimpleStructurePiece
    {
        private final Identifier resourceLocation;
        private final BlockRotation rotation;

        public Piece(StructureManager templateManagerIn, Identifier resourceLocationIn, BlockPos pos, BlockRotation rotationIn)
        {
            super(StructureRegistryHandler.SWORD_STRUCTURE_FEATURE, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = new BlockPos(0, 1, 0);
            this.pos = pos.add(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(StructureManager templateManagerIn, CompoundTag tagCompound)
        {
            super(StructureRegistryHandler.SWORD_STRUCTURE_FEATURE, tagCompound);
            this.resourceLocation = new Identifier(tagCompound.getString("Template"));
            this.rotation = BlockRotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(StructureManager templateManager)
        {
            Structure template = templateManager.getStructure(this.resourceLocation);
            StructurePlacementData placementsettings = (new StructurePlacementData()).setRotation(this.rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(template, this.pos, placementsettings);
        }

        @Override
        protected void toNbt(CompoundTag tagCompound)
        {
            super.toNbt(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        public boolean generate(ServerWorldAccess p_230383_1_, StructureAccessor p_230383_2_, ChunkGenerator p_225577_2_, Random randomIn, BlockBox structureBoundingBoxIn, ChunkPos chunkPos, BlockPos p_230383_7_)
        {
            StructurePlacementData placementsettings = (new StructurePlacementData()).setRotation(this.rotation).setMirror(BlockMirror.NONE);
            BlockPos blockpos = new BlockPos(0, 1, 0);
            this.pos.add(Structure.transform(placementsettings, new BlockPos(-blockpos.getX(), 0, -blockpos.getZ())));

            return super.generate(p_230383_1_, p_230383_2_, p_225577_2_, randomIn, structureBoundingBoxIn, chunkPos, p_230383_7_);
        }

        @Override
        protected void handleMetadata(String function, BlockPos pos, WorldAccess worldIn, Random rand, BlockBox sbb)
        {
        }
    }
}
