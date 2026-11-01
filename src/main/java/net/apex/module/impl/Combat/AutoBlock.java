package net.apex.module.impl.Combat;
import net.apex.module.Module;
import net.minecraft.item.Items;

public class AutoBlock extends Module {
    public AutoBlock() { super("AutoBlock", Category.COMBAT); }
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        var enemy = mc.world.getEntitiesByClass(net.minecraft.entity.LivingEntity.class, mc.player.getBoundingBox().expand(4.0), e -> e != mc.player && e.isAlive()).stream().findFirst().orElse(null);
        if (enemy != null && (mc.player.getMainHandStack().getItem() == Items.SHIELD || mc.player.getOffHandStack().getItem() == Items.SHIELD)) {
            mc.options.useKey.setPressed(true);
        } else if (mc.options != null) mc.options.useKey.setPressed(false);
    }
}
