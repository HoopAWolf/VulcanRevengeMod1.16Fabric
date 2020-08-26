package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.blocks.*;
import com.hoopawolf.vrm.items.EnderClusterItem;
import com.hoopawolf.vrm.items.RuneItem;
import com.hoopawolf.vrm.items.weapons.*;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ItemBlockRegistryHandler
{
    //BLOCKS
    public static final Block SWORD_STONE_BLOCK = new SwordStoneBlock(Block.Settings.of(Material.METAL).strength(100));
    public static final Block PEDESTAL_BLOCK = new PedestalBlock(Block.Settings.of(Material.STONE).strength(2.0F, 6.0F));
    public static final Block ALTER_BLOCK = new AlterBlock(Block.Settings.of(Material.METAL).strength(100));

    public static final Block BLAZE_RUNE_BLOCK = new BlazeRuneBlock(Block.Settings.of(Material.METAL).strength(100));
    public static final Block NETHER_RUNE_BLOCK = new NetherRuneBlock(Block.Settings.of(Material.METAL).strength(100));
    public static final Block SCUTE_RUNE_BLOCK = new ScuteRuneBlock(Block.Settings.of(Material.METAL).strength(100));
    public static final Block MAGMA_RUNE_BLOCK = new MagmaRuneBlock(Block.Settings.of(Material.METAL).strength(100));

    //ITEMS
    public static final Item VULCAN_SWORD = new VulcanSwordItem(ToolMaterials.DIAMOND, 3, -2.5f, new Item.Settings().maxDamage(500).group(VulcanRevengeMod.VRM_TAB));
    public static final Item DEATH_SWORD = new DeathSwordItem(ToolMaterials.DIAMOND, 3, -1.5f, new Item.Settings().maxDamage(1000).group(VulcanRevengeMod.VRM_TAB));
    public static final Item WAR_SWORD = new WarSwordItem(ToolMaterials.DIAMOND, 3, -0.5f, new Item.Settings().maxDamage(1000).group(VulcanRevengeMod.VRM_TAB));
    public static final Item PES_BOW = new PesBowItem(new Item.Settings().maxCount(1).maxDamage(1000).group(VulcanRevengeMod.VRM_TAB));
    public static final Item FAM_SCALE = new FamScaleItem(new Item.Settings().maxCount(1).maxDamage(1000).group(VulcanRevengeMod.VRM_TAB));
    public static final Item RUNE_ITEM = new RuneItem(new Item.Settings().maxCount(1).group(VulcanRevengeMod.VRM_TAB));
    public static final Item ENDER_CLUSTER_ITEM = new EnderClusterItem(new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    public static final Item ENDERDLE_ITEM = new Item(new Item.Settings().group(VulcanRevengeMod.VRM_TAB));

    //BLOCK ITEMS
    private static final BlockItem SWORD_STONE = new BlockItem(SWORD_STONE_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    private static final BlockItem PEDESTAL = new BlockItem(PEDESTAL_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    private static final BlockItem ALTER = new BlockItem(ALTER_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));

    private static final BlockItem BLAZE_RUNE = new BlockItem(BLAZE_RUNE_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    private static final BlockItem NETHER_RUNE = new BlockItem(NETHER_RUNE_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    private static final BlockItem SCUTE_RUNE = new BlockItem(SCUTE_RUNE_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));
    private static final BlockItem MAGMA_RUNE = new BlockItem(MAGMA_RUNE_BLOCK, new Item.Settings().group(VulcanRevengeMod.VRM_TAB));


    public static void init()
    {
        //ITEMS
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "vulcansrevenge"), VULCAN_SWORD);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "death"), DEATH_SWORD);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "war"), WAR_SWORD);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "pes"), PES_BOW);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "fam"), FAM_SCALE);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "rune"), RUNE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "endercluster"), ENDER_CLUSTER_ITEM);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "enderdle"), ENDERDLE_ITEM);

        //BLOCKS
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "swordstone"), SWORD_STONE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "pedestal"), PEDESTAL_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "alter"), ALTER_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "blazerune"), BLAZE_RUNE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "netherrune"), NETHER_RUNE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "scuterune"), SCUTE_RUNE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, "magmarune"), MAGMA_RUNE_BLOCK);

        //BLOCK ITEMS
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "swordstone"), SWORD_STONE);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "pedestal"), PEDESTAL);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "alter"), ALTER);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "blazerune"), BLAZE_RUNE);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "netherrune"), NETHER_RUNE);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "scuterune"), SCUTE_RUNE);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "magmarune"), MAGMA_RUNE);
    }

    public static void registerItemModelProperties()
    {
        FabricModelPredicateProviderRegistry.register(PES_BOW, new Identifier("pull"),
                (p_239427_0_, p_239427_1_, p_239427_2_) ->
                {

                    if (p_239427_2_ == null)
                    {
                        return 0.0F;
                    } else
                    {
                        return !(p_239427_2_.getActiveItem().getItem() instanceof PesBowItem) ? 0.0F : (float) (p_239427_0_.getMaxUseTime() - p_239427_2_.getItemUseTimeLeft()) / 20.0F;

                    }
                });

        FabricModelPredicateProviderRegistry.register(PES_BOW, new Identifier("pulling"),
                (p_210309_0_, p_210309_1_, p_210309_2_) ->
                {
                    return p_210309_2_ != null && p_210309_2_.isUsingItem() && p_210309_2_.getActiveItem() == p_210309_0_ ? 1.0F : 0.0F;
                });

        FabricModelPredicateProviderRegistry.register(VULCAN_SWORD, new Identifier("type"),
                (p_210310_0_, p_210310_1_, p_210310_2_) -> VulcanSwordItem.getType(p_210310_0_));

        FabricModelPredicateProviderRegistry.register(RUNE_ITEM, new Identifier("type"),
                (p_210310_0_, p_210310_1_, p_210310_2_) -> RuneItem.getType(p_210310_0_));
    }
}