package com.infinityraider.elementalinvocations.utility;

import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static final int KEY_INVOKE = 0;
    public static final int KEY_SPELL_ACTION = 1;

    @SideOnly(Side.CLIENT)
    public static KeyBinding invoke;
    @SideOnly(Side.CLIENT)
    public static KeyBinding spellAction;

    @SideOnly(Side.CLIENT)
    public static void register() {
        invoke = new KeyBinding("keybinding." + Reference.MOD_ID + ".invoke", Keyboard.KEY_R, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(invoke);

        spellAction = new KeyBinding("keybinding." + Reference.MOD_ID + ".spellAction", Keyboard.KEY_G, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(spellAction);
    }
}
