package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.network.MessageKeyPressed;
import com.infinityraider.elementalinvocations.network.MessageStopChanneling;
import com.infinityraider.elementalinvocations.utility.KeyBindings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyInputHandler {
    private static final KeyInputHandler INSTANCE = new KeyInputHandler();

    public static KeyInputHandler getInstance() {
        return INSTANCE;
    }

    private boolean invoking;

    private KeyInputHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        boolean invoke = KeyBindings.invoke.isKeyDown();
        if(invoke != invoking) {
            invoking = invoke;
            if(invoking) {
                new MessageKeyPressed(KeyBindings.KEY_INVOKE).sendToServer();
            } else {
                new MessageStopChanneling().sendToServer();
            }
        }

        if(KeyBindings.spellAction.isPressed()) {
            new MessageKeyPressed(KeyBindings.KEY_SPELL_ACTION).sendToServer();
        }
    }
}
