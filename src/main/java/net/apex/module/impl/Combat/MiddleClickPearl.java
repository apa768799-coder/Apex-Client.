package net.apex.module.impl.Combat;

import net.apex.module.Module;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class MiddleClickPearl extends Module {
    public MiddleClickPearl() { super("MiddleClickPearl", Category.COMBAT); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;

        // Проверяем, зажата ли средняя кнопка мыши (кнопка 2)
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS) {
            // Ищем жемчуг в хотбаре
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.ENDER_PEARL) {
                    mc.player.getInventory().selectedSlot = i;
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    return;
                }
            }
        }
    }
}
