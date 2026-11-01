package net.apex.module.impl.Render;

import net.apex.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import java.awt.Color;

public class TargetHUD extends Module {
    public TargetHUD() { super("TargetHUD", Category.RENDER); }

    @Override
    public void onRenderHud(DrawContext context, float tickDelta) {
        if (mc.player == null || mc.world == null) return;
        
        LivingEntity target = mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(5.0), e -> e != mc.player && e.isAlive()).stream().findFirst().orElse(null);
        
        if (target != null) {
            int x = context.getScaledWindowWidth() / 2 + 20;
            int y = context.getScaledWindowHeight() / 2 + 20;
            
            context.fill(x, y, x + 120, y + 40, new Color(0, 0, 0, 150).getRGB());
            context.drawTextWithShadow(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1);
            context.drawTextWithShadow(mc.textRenderer, "HP: " + (int)target.getHealth(), x + 5, y + 20, 0xFF0000);
        }
    }
}
