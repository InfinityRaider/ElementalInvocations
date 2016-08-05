package com.teaminfinity.elementalinvocations.potion;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionConfusion extends Potion{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/potionConfusion.png");

    protected PotionConfusion() {
        super(true, 0);
        this.setPotionName(Reference.MOD_ID.toLowerCase() + ":potion.confusion");
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplification) {
        ElementalInvocations.proxy.performConfusionEffect(this, entity, amplification);
    }

    @SideOnly(Side.CLIENT)
    public void performClientEffect(EntityLivingBase entity) {
        if(entity == ElementalInvocations.proxy.getClientPlayer()) {
            EntityPlayerSP player = (EntityPlayerSP) entity;
            if(!(player.movementInput instanceof MovementInputConfusion)) {
                player.movementInput = new MovementInputConfusion(player, Minecraft.getMinecraft().gameSettings);
            }
        } else if(!entity.worldObj.isRemote && !(entity instanceof EntityPlayer)) {
            entity.moveForward = -entity.moveForward;
            entity.moveStrafing = -entity.moveStrafing;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderHUD(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRender(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, 1);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0F, 0F, 18, 18, 18, 18);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, alpha);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0F, 0F, 18, 18, 18, 18);
    }

    @SideOnly(Side.CLIENT)
    private static class MovementInputConfusion extends MovementInput {
        private final EntityPlayerSP player;
        private final MovementInput prevInput;
        private final GameSettings settings;

        private MovementInputConfusion(EntityPlayerSP player, GameSettings settings) {
            this.player = player;
            this.prevInput = player.movementInput;
            this.settings = settings;
        }

        @Override
        public void updatePlayerMoveState() {
            if(!player.isPotionActive(PotionRegistry.getInstance().POTION_CONFUSION)) {
                prevInput.updatePlayerMoveState();
                this.moveStrafe = prevInput.moveStrafe;
                this.moveForward = prevInput.moveForward;
                this.forwardKeyDown = prevInput.forwardKeyDown;
                this.backKeyDown = prevInput.backKeyDown;
                this.leftKeyDown = prevInput.leftKeyDown;
                this.rightKeyDown = prevInput.rightKeyDown;
                this.jump = prevInput.jump;
                this.sneak = prevInput.sneak;
            } else {
                this.moveStrafe = 0.0F;
                this.moveForward = 0.0F;

                if (this.settings.keyBindForward.isKeyDown()) {
                    --this.moveForward;
                    this.forwardKeyDown = true;
                } else {
                    this.forwardKeyDown = false;
                }

                if (this.settings.keyBindBack.isKeyDown()) {
                    ++this.moveForward;
                    this.backKeyDown = true;
                } else {
                    this.backKeyDown = false;
                }

                if (this.settings.keyBindLeft.isKeyDown()) {
                    --this.moveStrafe;
                    this.leftKeyDown = true;
                } else {
                    this.leftKeyDown = false;
                }

                if (this.settings.keyBindRight.isKeyDown()) {
                    ++this.moveStrafe;
                    this.rightKeyDown = true;
                } else {
                    this.rightKeyDown = false;
                }

                this.jump = this.settings.keyBindJump.isKeyDown();
                this.sneak = this.settings.keyBindSneak.isKeyDown();

                if (this.sneak) {
                    this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
                    this.moveForward = (float) ((double) this.moveForward * 0.3D);
                }
            }
        }
    }
}
