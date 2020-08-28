package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.blocks.tileentity.*;
import com.hoopawolf.vrm.client.tileentity.*;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TileEntityRegistryHandler
{
    //TILE ENTITES
    public static BlockEntityType<SwordStoneTileEntity> SWORD_STONE_TILE_ENTITY;

    public static BlockEntityType<PedestalTileEntity> PEDESTAL_TILE_ENTITY;

    public static BlockEntityType<AlterTileEntity> ALTER_TILE_ENTITY;

    public static BlockEntityType<BlazeRuneTileEntity> BLAZE_RUNE_TILE_ENTITY;

    public static BlockEntityType<NetherRuneTileEntity> NETHER_RUNE_TILE_ENTITY;

    public static BlockEntityType<ScuteRuneTileEntity> SCUTE_RUNE_TILE_ENTITY;

    public static BlockEntityType<MagmaRuneTileEntity> MAGMA_RUNE_TILE_ENTITY;

    public static void init()
    {
        SWORD_STONE_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "swordstone"), BlockEntityType.Builder.create(SwordStoneTileEntity::new, ItemBlockRegistryHandler.SWORD_STONE_BLOCK).build(null));
        PEDESTAL_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "pedestal"), BlockEntityType.Builder.create(PedestalTileEntity::new, ItemBlockRegistryHandler.PEDESTAL_BLOCK).build(null));
        ALTER_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "alter"), BlockEntityType.Builder.create(AlterTileEntity::new, ItemBlockRegistryHandler.ALTER_BLOCK).build(null));
        BLAZE_RUNE_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "blazerune"), BlockEntityType.Builder.create(BlazeRuneTileEntity::new, ItemBlockRegistryHandler.BLAZE_RUNE_BLOCK).build(null));
        NETHER_RUNE_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "netherrune"), BlockEntityType.Builder.create(NetherRuneTileEntity::new, ItemBlockRegistryHandler.NETHER_RUNE_BLOCK).build(null));
        SCUTE_RUNE_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "scuterune"), BlockEntityType.Builder.create(ScuteRuneTileEntity::new, ItemBlockRegistryHandler.SCUTE_RUNE_BLOCK).build(null));
        MAGMA_RUNE_TILE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, "magmarune"), BlockEntityType.Builder.create(MagmaRuneTileEntity::new, ItemBlockRegistryHandler.MAGMA_RUNE_BLOCK).build(null));
    }

    @Environment(EnvType.CLIENT)
    public static void registerTileEntityRenderer()
    {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), ItemBlockRegistryHandler.SWORD_STONE_BLOCK, ItemBlockRegistryHandler.PEDESTAL_BLOCK, ItemBlockRegistryHandler.ALTER_BLOCK);

        BlockEntityRendererRegistry.INSTANCE.register(SWORD_STONE_TILE_ENTITY,
                SwordStoneRenderer::new);

        BlockEntityRendererRegistry.INSTANCE.register(PEDESTAL_TILE_ENTITY,
                PedestalRenderer::new);

        BlockEntityRendererRegistry.INSTANCE.register(ALTER_TILE_ENTITY,
                AlterRenderer::new);

        BlockEntityRendererRegistry.INSTANCE.register(BLAZE_RUNE_TILE_ENTITY,
                BlazeRuneRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(NETHER_RUNE_TILE_ENTITY,
                NetherRuneRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(SCUTE_RUNE_TILE_ENTITY,
                ScuteRuneRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(MAGMA_RUNE_TILE_ENTITY,
                MagmaRuneRenderer::new);
    }
}
