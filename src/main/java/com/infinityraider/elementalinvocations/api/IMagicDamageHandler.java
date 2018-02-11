package com.infinityraider.elementalinvocations.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public interface IMagicDamageHandler {
    void dealDamage(Entity target, float amount, Element element, int potency);

    void dealDamage(Entity target, float amount, Element element, int potency, Vec3d dir);

    void dealDamage(Entity target, float amount, Entity source, Element element, int potency);

    void dealDamage(Entity target, float amount, Entity source, Element element, int potency, Vec3d dir);

    void dealDamage(Entity target, float amount, Entity source, Entity cause, Element element, int potency);

    void dealDamage(Entity target, float amount, Entity source, Entity cause, Element element, int potency, Vec3d dir);

}