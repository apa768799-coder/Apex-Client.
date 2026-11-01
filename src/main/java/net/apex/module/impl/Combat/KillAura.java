package net.apex.module.impl.Combat;

import net.apex.module.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    private int cooldown = 0;

    public KillAura() {
        super("KillAura", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Ищем ближайшего моба/игрока в радиусе 3.5 блоков
        LivingEntity target = mc.world.getEntitiesByClass(LivingEntity.class, 
                mc.player.getBoundingBox().expand(3.5), 
                e -> e != mc.player && e.isAlive())
                .stream().findFirst().orElse(null);

        if (target != null) {
            // Бьем
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            cooldown = 10; // Задержка между ударами
        }
    }
}
