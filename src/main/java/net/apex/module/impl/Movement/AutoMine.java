package net.apex.module.impl.Movement;

import net.apex.module.Module;

public class AutoMine extends Module {
    
    public AutoMine() {
        super("AutoMine", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Просто заставляем игрока идти вперед и держать ЛКМ (упрощенно)
        mc.options.forwardKey.setPressed(true);
        mc.options.attackKey.setPressed(true);
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.forwardKey.setPressed(false);
            mc.options.attackKey.setPressed(false);
        }
    }
}
