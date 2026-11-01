package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class NoSlow extends Module {
    public NoSlow() { super("NoSlow", Category.MOVEMENT); }
    @Override
    public void onTick() {
        // Упрощенный сброс замедления (в связке с Velocity работает отлично)
        if (mc.player != null && mc.player.isUsingItem()) {
            mc.player.setVelocity(mc.player.getVelocity().multiply(1.2, 1, 1.2));
        }
    }
}
