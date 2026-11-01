package net.apex.module.impl.Movement;
import net.apex.module.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;

public class ElytraHelper extends Module {
    public ElytraHelper() { super("ElytraHelper", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player != null && !mc.player.isOnGround() && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
            if (mc.player.fallDistance > 1.0f && !mc.player.isGliding()) {
                mc.options.jumpKey.setPressed(true);
            } else {
                mc.options.jumpKey.setPressed(false);
            }
        }
    }
}
