package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class KeepSprint extends Module {
    public KeepSprint() { super("KeepSprint", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.hurtTime > 0) mc.player.setSprinting(true);
    }
}
