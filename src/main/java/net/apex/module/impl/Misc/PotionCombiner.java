package net.apex.module.impl.Misc;

import net.apex.module.Module;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class PotionCombiner extends Module {
    private int delay = 0;
    public PotionCombiner() { super("PotionCombiner", Category.MISC); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (delay > 0) { delay--; return; }

        // Если открыта зельеварка
        if (mc.currentScreen instanceof BrewingStandScreen screen) {
            var handler = screen.getScreenHandler();
            
            // Кладем бутылочки (слоты 0, 1, 2)
            for (int i = 0; i < 3; i++) {
                if (handler.getSlot(i).getStack().isEmpty()) {
                    for (int j = 5; j < 41; j++) { // Слоты инвентаря
                        if (handler.getSlot(j).getStack().getItem() == Items.POTION) {
                            mc.interactionManager.clickSlot(handler.syncId, j, 0, SlotActionType.QUICK_MOVE, mc.player);
                            delay = 5;
                            return;
                        }
                    }
                }
            }
            // Кладем ингредиент (слот 3) - для примера светопыль (усиление)
            if (handler.getSlot(3).getStack().isEmpty()) {
                for (int j = 5; j < 41; j++) {
                    if (handler.getSlot(j).getStack().getItem() == Items.GLOWSTONE_DUST) {
                        mc.interactionManager.clickSlot(handler.syncId, j, 0, SlotActionType.QUICK_MOVE, mc.player);
                        delay = 5;
                        return;
                    }
                }
            }
        }
    }
}
