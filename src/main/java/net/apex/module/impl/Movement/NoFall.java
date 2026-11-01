package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class NoFall extends Module {
    public NoFall() { super("NoFall", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.fallDistance > 2.0f) {
            mc.player.fallDistance = 0.0f;
        }
    }
}
