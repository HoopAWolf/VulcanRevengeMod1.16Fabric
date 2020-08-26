package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.client.particles.DeathMarkParticle;
import com.hoopawolf.vrm.client.particles.PlagueParticle;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistryHandler
{
    //PARTICLES
    public static DefaultParticleType DEATH_MARK_PARTICLE = FabricParticleTypes.simple(false);
    public static DefaultParticleType PLAGUE_PARTICLE = FabricParticleTypes.simple(false);

    public static void init()
    {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Reference.MOD_ID, "death"), DEATH_MARK_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Reference.MOD_ID, "plague"), PLAGUE_PARTICLE);

        ParticleFactoryRegistry.getInstance().register(DEATH_MARK_PARTICLE, DeathMarkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(PLAGUE_PARTICLE, PlagueParticle.Factory::new);
    }
}
