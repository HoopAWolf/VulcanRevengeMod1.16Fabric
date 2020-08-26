package com.hoopawolf.vrm;

import com.hoopawolf.vrm.config.ConfigHandler;
import com.hoopawolf.vrm.helper.VRMEatItemDataHandler;
import com.hoopawolf.vrm.helper.VRMEnvyEntityDataHandler;
import com.hoopawolf.vrm.helper.VRMGreedItemDataHandler;
import com.hoopawolf.vrm.network.MessageHandlerOnServer;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.*;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class VulcanRevengeMod implements ModInitializer
{
    public static final ItemGroup VRM_TAB = FabricItemGroupBuilder.create(new Identifier(Reference.MOD_ID, "vrmitemgroup"))
            .icon(() -> new ItemStack(ItemBlockRegistryHandler.VULCAN_SWORD)).build();

    public static final Identifier SERVER_PACKET_ID = new Identifier("vrm", "serverpacket");
    public static final Identifier CLIENT_PACKET_ID = new Identifier("vrm", "clientpacket");

    public static ConfigHandler VRM_CONFIG;

    @Override
    public void onInitialize()
    {
        AutoConfig.register(ConfigHandler.class, JanksonConfigSerializer::new);
        VRM_CONFIG = AutoConfig.getConfigHolder(ConfigHandler.class).getConfig();

        ItemBlockRegistryHandler.init();
        ArmorRegistryHandler.init();
        EntityRegistryHandler.init();
        TileEntityRegistryHandler.init();
        ParticleRegistryHandler.init();
        PotionRegistryHandler.init();
        StructureRegistryHandler.init();

        StructureRegistryHandler.generateStructureWorldSpawn();
        VRMEatItemDataHandler.INSTANCE.initJSON();
        VRMGreedItemDataHandler.INSTANCE.initJSON();
        VRMEnvyEntityDataHandler.INSTANCE.initJSON();
        EntityRegistryHandler.registerEntityAttributes();

        ServerSidePacketRegistry.INSTANCE.register(SERVER_PACKET_ID, ((packetContext, attachedData) -> MessageHandlerOnServer.onMessageReceived(attachedData, packetContext)));
    }
}
