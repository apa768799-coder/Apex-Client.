package net.apex.module.impl.Combat;
import net.apex.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TriggerBot extends Module {
    private int cooldown = 0;
    public TriggerBot() { super("TriggerBot", Category.COMBAT); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (cooldown > 0) { cooldown--; return; }
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) mc.crosshairTarget).getEntity();
            if (target instanceof LivingEntity && target != mc.player && mc.player.distanceTo(target) <= 4.0) {
                mc.interactionManager.attackEntity(mc.player, (LivingEntity) target);
                mc.player.swingHand(Hand.MAIN_HAND);
                cooldown = 5;
            }
        }
    }
}
