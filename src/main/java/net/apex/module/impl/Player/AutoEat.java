package net.apex.module.impl.Player;
import net.apex.module.Module;
import net.minecraft.component.DataComponentTypes;

public class AutoEat extends Module {
    public AutoEat() { super("AutoEat", Category.PLAYER); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.getHungerManager().getFoodLevel() < 15) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).contains(DataComponentTypes.FOOD)) {
                    mc.player.getInventory().selectedSlot = i;
                    mc.options.useKey.setPressed(true);
                    return;
                }
            }
        } else if (mc.options != null) {
            mc.options.useKey.setPressed(false);
        }
    }
}
