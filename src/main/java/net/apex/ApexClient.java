package net.apex;

import net.apex.module.ModuleManager;
import net.apex.ui.ClickGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ApexClient implements ClientModInitializer {
    public static ModuleManager moduleManager;
    public static KeyBinding keyGui;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();

        keyGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.apex.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.apex"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                while (keyGui.wasPressed()) client.setScreen(new ClickGui());
                moduleManager.onTick(client);
            }
        });

        WorldRenderEvents.END.register(context -> {
            if (moduleManager != null) moduleManager.onRenderWorld(context);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (moduleManager != null) moduleManager.onRenderHud(drawContext, tickCounter.getTickDelta(false));
        });

        // СЛУШАЕМ ЧАТ
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (moduleManager != null) moduleManager.onChat(message.getString());
        });
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (moduleManager != null) moduleManager.onChat(message.getString());
        });
    }
}
