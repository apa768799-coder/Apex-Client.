package net.apex.module.impl.Movement;
import net.apex.module.Module;

public class AutoJump extends Module {
    public AutoJump() { super("AutoJump", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isOnGround() && mc.player.horizontalCollision) mc.player.jump();
    }
}
