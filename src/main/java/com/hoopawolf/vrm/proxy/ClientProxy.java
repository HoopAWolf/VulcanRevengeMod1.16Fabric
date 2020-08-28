package com.hoopawolf.vrm.proxy;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.MessageHandlerOnClient;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import com.hoopawolf.vrm.util.ParticleRegistryHandler;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import com.hoopawolf.vrm.util.client.EntityRenderRegistryHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

@Environment(EnvType.CLIENT)
public class ClientProxy implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ItemBlockRegistryHandler.registerItemModelProperties();
        EntityRenderRegistryHandler.registerEntityRenderer();
        TileEntityRegistryHandler.registerTileEntityRenderer();
        ParticleRegistryHandler.registerFactories();

        ClientSidePacketRegistry.INSTANCE.register(VulcanRevengeMod.CLIENT_PACKET_ID, (packetContext, attachedData) -> MessageHandlerOnClient.onMessageReceived(attachedData, packetContext));
    }
}