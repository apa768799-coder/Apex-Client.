package net.apex.module.impl.Combat;

import net.apex.module.Module;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.COMBAT);
    }

    // В реальном клиенте это делается через миксин в LivingEntity.
    // Здесь мы просто обнуляем скорость в тике, если нас ударили.
    @Override
    public void onTick() {
        if (mc.player != null && mc.player.hurtTime > 0) {
            mc.player.setVelocity(mc.player.getVelocity().x * 0.0, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.0);
        }
    }
}
