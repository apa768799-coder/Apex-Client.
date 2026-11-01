package net.apex.module.impl.Combat;

import net.apex.module.Module;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;

public class AutoCrystal extends Module {
    private int cooldown = 0;

    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (cooldown > 0) { cooldown--; return; }

        // Ищем кристаллы Энда в радиусе 4.5 блоков
        EndCrystalEntity crystal = mc.world.getEntitiesByClass(EndCrystalEntity.class, 
                mc.player.getBoundingBox().expand(4.5), 
                e -> e.isAlive())
                .stream().findFirst().orElse(null);

        if (crystal != null) {
            // Поворачиваемся и бьем
            mc.player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, crystal.getPos());
            mc.interactionManager.attackEntity(mc.player, crystal);
            mc.player.swingHand(Hand.MAIN_HAND);
            cooldown = 5; // Задержка
        }
    }
}
