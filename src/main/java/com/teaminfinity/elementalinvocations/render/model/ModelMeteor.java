package com.teaminfinity.elementalinvocations.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMeteor extends ModelBase {
    private ModelRenderer box;

    public ModelMeteor() {
        textureWidth = 64;
        textureHeight = 32;

        box = new ModelRenderer(this, 0, 0);
        box.addBox(0F, 0F, 0F, 16, 16, 16);
        box.setRotationPoint(-8F, 8F, -8F);
        box.setTextureSize(64, 32);
        box.mirror = true;
        setRotation(box, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        box.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }
}
