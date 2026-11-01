package net.apex.module.impl.Player;

import net.apex.module.Module;

public class FastPlace extends Module {
    public FastPlace() { super("FastPlace", Category.PLAYER); }

    @Override
    public void onTick() {
        if (mc.player != null) {
            // Обнуляем задержку использования предмета (работает для блоков и кристаллов)
            mc.itemUseCooldown = 0;
        }
    }
}
