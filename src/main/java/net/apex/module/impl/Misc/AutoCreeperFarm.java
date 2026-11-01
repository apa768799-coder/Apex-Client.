package net.apex.module.impl.Misc;

import net.apex.module.Module;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Hand;

public class AutoCreeperFarm extends Module {
    private int cooldown = 0;
    public AutoCreeperFarm() { super("AutoCreeperFarm", Category.MISC); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (cooldown > 0) { cooldown--; return; }

        CreeperEntity creeper = mc.world.getEntitiesByClass(CreeperEntity.class, mc.player.getBoundingBox().expand(6.0), e -> e.isAlive()).stream().findFirst().orElse(null);

        if (creeper != null) {
            mc.player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, creeper.getPos());
            if (mc.player.distanceTo(creeper) <= 3.5) {
                mc.interactionManager.attackEntity(mc.player, creeper);
                mc.player.swingHand(Hand.MAIN_HAND);
                cooldown = 10; // Удар раз в 0.5 сек
            } else {
                mc.options.forwardKey.setPressed(true); // Идем к криперу
            }
        } else if (mc.options != null) {
            mc.options.forwardKey.setPressed(false);
        }
    }
}
