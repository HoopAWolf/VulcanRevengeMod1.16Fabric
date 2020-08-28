package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistryHandler
{
    //ENTITIES
    public static EntityType<SlothPetEntity> SLOTH_PET_ENTITY;

    //PROJECTILE
    public static EntityType<PesArrowEntity> PES_ARROW_ENTITY;

    public static void init()
    {
        //ENTITY
        SLOTH_PET_ENTITY = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(Reference.MOD_ID, "slothpet"),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SlothPetEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.8F)).build());

        //PROJECTILE
        PES_ARROW_ENTITY = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(Reference.MOD_ID, "pesarrow"),
                FabricEntityTypeBuilder.<PesArrowEntity>create(SpawnGroup.MISC, PesArrowEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).build());
    }

    public static void registerEntityAttributes()
    {
        FabricDefaultAttributeRegistry.register(SLOTH_PET_ENTITY, SlothPetEntity.createSlothPetAttributes());
    }
}