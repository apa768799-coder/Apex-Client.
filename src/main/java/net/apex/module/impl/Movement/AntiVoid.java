package net.apex.module.impl.Movement;

import net.apex.module.Module;

public class AntiVoid extends Module {
    public AntiVoid() { 
        super("AntiVoid", Category.MOVEMENT); 
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        
        // Если игрок падает ниже -60 (в Энде или обычном мире)
        if (mc.player.getY() < -60.0) {
            mc.player.setPosition(mc.player.getX(), -60.0, mc.player.getZ());
            mc.player.setVelocity(0, 0, 0);
            mc.player.fallDistance = 0;
        }
    }
}
