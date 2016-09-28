package com.teaminfinity.elementalinvocations.render.particle;

import com.infinityraider.infinitylib.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class ParticleFactory implements IParticleFactory {
    private static final ParticleFactory INSTANCE = new ParticleFactory();

    public static ParticleFactory getInstance() {
        return INSTANCE;
    }

    private final Map<Integer, Creator> particleCreators;
    private final ParticleManager manager;

    private final Map<Integer, IParticleFactory> particleFactoryMap;

    private int lastId = 46;

    private ParticleFactory() {
        this.particleCreators = new HashMap<>();
        this.manager = Minecraft.getMinecraft().effectRenderer;
        this.particleFactoryMap = getParticleFactoryMap();
    }

    @Override
    public Particle getEntityFX(int id, World world, double x, double y, double z, double vX, double vY, double vZ, int... args) {
        return particleCreators.containsKey(id) ? particleCreators.get(id).createParticle(world, x, y, z, vX, vY, vZ, args) : null;
    }

    private int registerParticleCreator(Creator creator) {
        int id = getNextId();
        manager.registerParticle(id, this);
        this.particleCreators.put(id, creator);
        return id;
    }

    private int getNextId() {
        lastId = lastId + 1;
        while(particleFactoryMap.containsKey(lastId)) {
            lastId++;
        }
        return lastId;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, IParticleFactory> getParticleFactoryMap() {
        for(Field field : manager.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.getType().isAssignableFrom(Map.class)) {
                try {
                    return (Map<Integer, IParticleFactory>) field.get(manager);
                } catch (IllegalAccessException e) {
                    LogHelper.printStackTrace(e);
                    break;
                }
            }
        }
        return new HashMap<>();
    }

    public static abstract class Creator<P extends Particle> {
        private final int id;

        protected Creator() {
            this.id = ParticleFactory.getInstance().registerParticleCreator(this);
        }

        public final int getId() {
            return this.id;
        }

        public abstract P createParticle(World world, double x, double y, double z, double vX, double vY, double vZ, int... args);
    }
}
