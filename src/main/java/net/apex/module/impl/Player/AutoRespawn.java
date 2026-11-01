package net.apex.module.impl.Player;

import net.apex.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() { super("AutoRespawn", Category.PLAYER); }

    @Override
    public void onTick() {
        if (mc.player != null && mc.currentScreen instanceof DeathScreen) {
            mc.player.requestRespawn();
            mc.setScreen(null);
        }
    }
}
