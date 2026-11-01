package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.input.movementForward > 0 && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
