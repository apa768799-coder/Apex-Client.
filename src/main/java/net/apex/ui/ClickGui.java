package net.apex.ui;

import net.apex.module.Module;
import net.apex.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final int PANEL_WIDTH = 120;
    private final int HEADER_HEIGHT = 18;
    private final int MODULE_HEIGHT = 16;

    public ClickGui() {
        super(Text.literal("Apex Client"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Затемнение
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB());

        int startX = 20;
        int startY = 20;
        int xOffset = 0;

        // Отрисовка по категориям
        for (Module.Category category : Module.Category.values()) {
            int currentY = startY;
            
            // Заголовок категории
            context.fill(startX + xOffset, currentY, startX + xOffset + PANEL_WIDTH, currentY + HEADER_HEIGHT, new Color(35, 35, 45).getRGB());
            context.drawTextWithShadow(textRenderer, category.name(), startX + xOffset + 5, currentY + 5, 0x00FFB4);
            currentY += HEADER_HEIGHT;

            // Модули категории
            for (Module m : ModuleManager.modules) {
                if (m.getCategory() == category) {
                    // Фон кнопки
                    int color = m.isEnabled() ? new Color(0, 255, 180).getRGB() : new Color(25, 25, 30).getRGB();
                    context.fill(startX + xOffset, currentY, startX + xOffset + PANEL_WIDTH, currentY + MODULE_HEIGHT, color);
                    
                    // Текст модуля (черный если вкл, белый если выкл)
                    int textColor = m.isEnabled() ? 0 : 0xFFFFFF;
                    context.drawTextWithShadow(textRenderer, m.getName(), startX + xOffset + 5, currentY + 4, textColor);
                    currentY += MODULE_HEIGHT;
                }
            }
            xOffset += PANEL_WIDTH + 10;
        }
        
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int startX = 20;
            int startY = 20;
            int xOffset = 0;

            for (Module.Category category : Module.Category.values()) {
                int currentY = startY + HEADER_HEIGHT;
                
                for (Module m : ModuleManager.modules) {
                    if (m.getCategory() == category) {
                        // Проверяем попадание мышкой в кнопку
                        if (mouseX >= startX + xOffset && mouseX <= startX + xOffset + PANEL_WIDTH &&
                            mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT) {
                            m.toggle();
                            return true;
                        }
                        currentY += MODULE_HEIGHT;
                    }
                }
                xOffset += PANEL_WIDTH + 10;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
