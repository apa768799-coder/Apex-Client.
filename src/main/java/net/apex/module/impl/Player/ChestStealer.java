package net.apex.module.impl.Player;

import net.apex.module.Module;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealer extends Module {
    public ChestStealer() { super("ChestStealer", Category.PLAYER); }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Если открыт сундук
        if (mc.currentScreen instanceof GenericContainerScreen screen) {
            var handler = screen.getScreenHandler();
            // Проходимся по слотам
            for (int i = 0; i < handler.getInventory().size(); i++) {
                if (!handler.getSlot(i).getStack().isEmpty()) {
                    // Быстрый шифт-клик по предмету
                    mc.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                    return; // Делаем по 1 предмету за тик, чтобы не выкинуло с сервера
                }
            }
            // Если сундук пуст — закрываем его
            mc.player.closeHandledScreen();
        }
    }
}
