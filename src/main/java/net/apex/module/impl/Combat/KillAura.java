package net.apex.module.impl.Combat;

import net.apex.module.Module;
import net.apex.utils.RotationUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    private Setting range = addSetting(new Setting("Range", 3.5f, 1.0f, 6.0f));
    private Setting speed = addSetting(new Setting("Speed", 10.0f, 1.0f, 20.0f));
    private int cooldown = 0;

    public KillAura() {
        super("KillAura", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        LivingEntity target = mc.world.getEntitiesByClass(LivingEntity.class, 
                mc.player.getBoundingBox().expand(range.value), 
                e -> e != mc.player && e.isAlive())
                .stream().findFirst().orElse(null);

        if (target != null) {
            // Плавно наводимся (используем настройку скорости)
            RotationUtils.smoothLook(target, speed.value);

            // Если навелись достаточно близко (допуск 15 градусов) и кулдаун прошел — бьем
            if (RotationUtils.isLookingAt(target, 15.0f) && cooldown <= 0) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
                cooldown = 10; // Задержка в тиках
            }
        }
        
        if (cooldown > 0) cooldown--;
    }
}
