package net.apex.module.impl.Player;
import net.apex.module.Module;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() { super("AutoTotem", Category.PLAYER); }
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i < 9 ? i + 36 : i, 40, SlotActionType.SWAP, mc.player);
                return;
            }
        }
    }
}
