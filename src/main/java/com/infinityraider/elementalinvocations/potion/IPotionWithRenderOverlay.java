package com.infinityraider.elementalinvocations.potion;

import com.infinityraider.infinitylib.modules.specialpotioneffect.ISpecialPotion;
import net.minecraft.util.ResourceLocation;

public interface IPotionWithRenderOverlay extends ISpecialPotion {
    ResourceLocation getOverlayTexture();
}
