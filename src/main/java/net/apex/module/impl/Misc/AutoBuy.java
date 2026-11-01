package net.apex.module.impl.Misc;

import net.apex.module.Module;

public class AutoBuy extends Module {
    private int tick = 0;
    public AutoBuy() { super("AutoBuy", Category.MISC); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        tick++;
        if (tick >= 100) { // Раз в 5 секунд
            // Отправляем команду на сервер (например, продажа моркови)
            mc.player.networkHandler.sendChatCommand("ah sell carrot 64");
            tick = 0;
        }
    }
}
