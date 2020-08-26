package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArmorRegistryHandler
{
    //ITEMS ARMOR
    public static final ArmorItem GLUTTONY_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.GLUTTONY);
    public static final ArmorItem ENVY_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.ENVY);
    public static final ArmorItem LUST_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.LUST);
    public static final ArmorItem GREED_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.GREED);
    public static final ArmorItem SLOTH_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.SLOTH);
    public static final ArmorItem WRATH_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.WRATH);
    public static final ArmorItem PRIDE_MASK_ARMOR = new SinsArmorItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(VulcanRevengeMod.VRM_TAB), SinsArmorItem.SINS.PRIDE);

    public static void init()
    {
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "gluttonymask"), GLUTTONY_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "envymask"), ENVY_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "lustmask"), LUST_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "greedmask"), GREED_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "slothmask"), SLOTH_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "wrathmask"), WRATH_MASK_ARMOR);
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, "pridemask"), PRIDE_MASK_ARMOR);
    }
}
