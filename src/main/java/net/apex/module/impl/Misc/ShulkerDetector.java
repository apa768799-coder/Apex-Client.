package net.apex.module.impl.Misc;

import net.apex.module.Module;
import net.minecraft.text.Text;

public class ShulkerDetector extends Module {
    public ShulkerDetector() { super("ShulkerDetector", Category.MISC); }

    @Override
    public void onChat(String msg) {
        String lower = msg.toLowerCase();
        if (lower.contains("шалкер") || lower.contains("shulker")) {
            if (lower.contains("цена") || lower.contains("аукцион") || lower.contains("$")) {
                if (mc.player != null) {
                    mc.player.sendMessage(Text.literal("§c[ShulkerDetector] §eНайден шалкер! Проверь аукцион."), false);
                }
            }
        }
    }
}
