package net.apex.module.impl.Combat;

import net.apex.module.Module;
import net.minecraft.entity.LivingEntity;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        // Если мы на земле и рядом есть враг — прыгаем для крита
        if (mc.player.isOnGround()) {
            LivingEntity target = mc.world.getEntitiesByClass(LivingEntity.class, 
                    mc.player.getBoundingBox().expand(3.5), 
                    e -> e != mc.player && e.isAlive())
                    .stream().findFirst().orElse(null);
            
            if (target != null) {
                mc.player.jump();
            }
        }
    }
}
