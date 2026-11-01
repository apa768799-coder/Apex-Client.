package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class Spider extends Module {
    public Spider() { super("Spider", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.horizontalCollision) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.4, mc.player.getVelocity().z);
        }
    }
}
