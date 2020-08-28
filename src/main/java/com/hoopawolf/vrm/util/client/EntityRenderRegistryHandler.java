package com.hoopawolf.vrm.util.client;

import com.hoopawolf.vrm.client.renderer.PesArrowRenderer;
import com.hoopawolf.vrm.client.renderer.SlothPetRenderer;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class EntityRenderRegistryHandler
{
    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderer()
    {
        EntityRendererRegistry.INSTANCE.register(EntityRegistryHandler.SLOTH_PET_ENTITY, ((entityRenderDispatcher, context) ->
        {
            return new SlothPetRenderer(entityRenderDispatcher);
        }));

        EntityRendererRegistry.INSTANCE.register(EntityRegistryHandler.PES_ARROW_ENTITY, ((entityRenderDispatcher, context) ->
        {
            return new PesArrowRenderer(entityRenderDispatcher);
        }));
    }
}
