package net.apex.module.impl.Player;

import net.apex.module.Module;

public class FastPlace extends Module {
    public FastPlace() { 
        super("FastPlace", Category.PLAYER); 
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.options != null) {
            // Обнуляем задержку установки блоков, принудительно отпуская кнопку "Использовать"
            mc.options.useKey.setPressed(false); 
        }
    }
}
