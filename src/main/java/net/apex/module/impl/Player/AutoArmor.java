package net.apex.module.impl.Player;
import net.apex.module.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {
    public AutoArmor() { super("AutoArmor", Category.PLAYER); }
    @Override
    public void onTick() {
        if (mc.player == null) return;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof ArmorItem) {
                EquipmentSlot slot = mc.player.getPreferredEquipmentSlot(stack);
                ItemStack current = mc.player.getEquippedStack(slot);
                if (current.isEmpty() || stack.getMaxDamage() > current.getMaxDamage()) {
                    int slotId = i < 9 ? i + 36 : i;
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slotId, 0, SlotActionType.QUICK_MOVE, mc.player);
                    return;
                }
            }
        }
    }
}
