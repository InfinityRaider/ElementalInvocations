package com.infinityraider.elementalinvocations.magic.thaum;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.reference.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;

import javax.annotation.Nullable;

public abstract class FocusElementalCharge extends FocusEffect {
    private final Element element;
    private final Aspect aspect;

    private final String key;
    private final String research;

    protected FocusElementalCharge(Element element) {
        super();
        this.element = element;
        this.aspect = ElementToAspect.getAspect(this.getElement());
        this.key = Reference.MOD_ID.toLowerCase() + ":focus_" + this.getElement().name().toLowerCase();
        this.research = "research." + this.getKey();
    }

    public Element getElement() {
        return this.element;
    }

    @Override
    public boolean execute(RayTraceResult rayTraceResult, @Nullable Trajectory trajectory, float v, int i) {
        return true;
    }

    public int getPower() {
        return this.getSettingValue("power");
    }

    public IMagicCharge getCharge() {
        final Element element = this.getElement();
        final int power = this.getPower();
        return new IMagicCharge() {
            @Override
            public Element element() {
                return element;
            }

            @Override
            public int potency() {
                return power;
            }
        };
    }

    @Override
    public void renderParticleFX(World world, double v, double v1, double v2, double v3, double v4, double v5) {}

    @Override
    public int getComplexity() {
        return 3 + 2*this.getPower();
    }

    @Override
    public Aspect getAspect() {
        return this.aspect;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getResearch() {
        return this.research;
    }

    @Override
    public void onCast(Entity entity) {
        if(entity instanceof EntityPlayer) {
            EntityPlayer caster = (EntityPlayer) entity;
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(caster);
            if(!caster.getEntityWorld().isRemote && properties != null) {
                properties.getChargeConfiguration().addCharge(this.getCharge());
            }
        }
    }

    @Override
    public NodeSetting[] createSettings() {
        return new NodeSetting[] {
                new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5))
        };
    }

    public static final class Life extends FocusElementalCharge {
        public Life() {
            super(Element.LIFE);
        }
    }

    public static final class Air extends FocusElementalCharge {
        public Air() {
            super(Element.AIR);
        }
    }

    public static final class Fire extends FocusElementalCharge {
        public Fire() {
            super(Element.FIRE);
        }
    }

    public static final class Death extends FocusElementalCharge {
        public Death() {
            super(Element.DEATH);
        }
    }

    public static final class Earth extends FocusElementalCharge {
        public Earth() {
            super(Element.EARTH);
        }
    }

    public static final class Water extends FocusElementalCharge {
        public Water() {
            super(Element.WATER);
        }
    }

    public static void init() {
        FocusEngine.registerElement(FocusElementalCharge.Life.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_life.png"), Element.LIFE.getColor());
        FocusEngine.registerElement(FocusElementalCharge.Air.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_air.png"), Element.AIR.getColor());
        FocusEngine.registerElement(FocusElementalCharge.Fire.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_fire.png"), Element.FIRE.getColor());
        FocusEngine.registerElement(FocusElementalCharge.Death.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_death.png"), Element.DEATH.getColor());
        FocusEngine.registerElement(FocusElementalCharge.Earth.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_earth.png"), Element.EARTH.getColor());
        FocusEngine.registerElement(FocusElementalCharge.Water.class, new ResourceLocation("elemental_invocations", "textures/items/elemental_core_water.png"), Element.WATER.getColor());
    }
}