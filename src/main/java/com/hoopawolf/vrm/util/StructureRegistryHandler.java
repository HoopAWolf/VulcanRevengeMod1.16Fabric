package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.structure.SinStructure;
import com.hoopawolf.vrm.structure.SwordStructure;
import com.hoopawolf.vrm.structure.piece.SinStructurePiece;
import com.hoopawolf.vrm.structure.piece.SwordStructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StructureRegistryHandler
{
    public static final StructureFeature<DefaultFeatureConfig> SWORD_STONE_STRUCTURE = new SwordStructure(DefaultFeatureConfig.CODEC);
    public static final StructureFeature<DefaultFeatureConfig> SIN_STRUCTURE = new SinStructure(DefaultFeatureConfig.CODEC);

    public static final StructurePieceType SWORD_STRUCTURE_FEATURE = registerStructurePiece(SwordStructurePiece.Piece::new, SwordStructurePiece.SWORD_STRUCTURE_LOC);
    public static final StructurePieceType SIN_STRUCTURE_FEATURE = registerStructurePiece(SinStructurePiece.Piece::new, SinStructurePiece.SIN_STRUCTURE_LOC);

    public static void generateStructureWorldSpawn()
    {
        StructureFeature.STRUCTURES.put(SWORD_STONE_STRUCTURE.getName(), SWORD_STONE_STRUCTURE);
        StructureFeature.STRUCTURES.put(SIN_STRUCTURE.getName(), SIN_STRUCTURE);

        registerStructureWorldSpawn(SWORD_STONE_STRUCTURE.configure(DefaultFeatureConfig.INSTANCE), new Biome.Category[]{Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.PLAINS});
        registerStructureWorldSpawn(SIN_STRUCTURE.configure(DefaultFeatureConfig.INSTANCE), new Biome.Category[]{Biome.Category.NETHER});
    }

    protected static void registerStructureWorldSpawn(ConfiguredStructureFeature<?, ?> structureIn, Biome.Category[] categoryIn)
    {
        for (Biome biome : Registry.BIOME)
        {
            for (Biome.Category cat : categoryIn)
            {
                if (biome.getCategory().equals(cat))
                {
                    biome.addStructureFeature(structureIn);
                    break;
                }
            }
        }
    }

    public static void init()
    {
        Registry.register(Registry.STRUCTURE_FEATURE, new Identifier(Reference.MOD_ID, "swordstructure"), SWORD_STONE_STRUCTURE);
        Registry.register(Registry.STRUCTURE_FEATURE, new Identifier(Reference.MOD_ID, "sinstructure"), SIN_STRUCTURE);
    }

    public static <C extends FeatureConfig> StructurePieceType registerStructurePiece(StructurePieceType pieceType, Identifier key)
    {
        return Registry.register(Registry.STRUCTURE_PIECE, key, pieceType);
    }
}
