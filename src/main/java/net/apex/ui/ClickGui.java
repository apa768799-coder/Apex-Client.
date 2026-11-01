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
    private final int ACCENT = new Color(0, 255, 180).getRGB();
    private final int BG_DARK = new Color(15, 15, 20, 230).getRGB();
    private final int HEADER_COLOR = new Color(30, 30, 40).getRGB();
    private final int TEXT_COLOR = new Color(200, 200, 200).getRGB();
    private final int MODULE_BG = new Color(25, 25, 30).getRGB();

    private final int PANEL_WIDTH = 110;
    private final int HEADER_HEIGHT = 18;
    private final int MODULE_HEIGHT = 15;

    private final List<CategoryPanel> panels = new ArrayList<>();
    private CategoryPanel draggingPanel = null;
    private double dragX, dragY;

    public ClickGui() {
        super(Text.literal("Apex Client"));
        int x = 20;
        for (Module.Category category : Module.Category.values()) {
            panels.add(new CategoryPanel(category, x, 20));
            x += PANEL_WIDTH + 10;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 150).getRGB());

        // Верхняя панель
        context.fill(0, 0, this.width, 15, new Color(10, 10, 15, 255).getRGB());
        context.drawTextWithShadow(textRenderer, "§bApex Client §7| §fDrag panels by header!", 5, 4, -1);

        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (CategoryPanel panel : panels) {
                if (panel.mouseClicked(mouseX, mouseY, button)) {
                    if (panel.isDragging) {
                        draggingPanel = panel;
                        dragX = mouseX - panel.x;
                        dragY = mouseY - panel.y;
                    }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingPanel = null;
        for (CategoryPanel panel : panels) panel.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingPanel != null) {
            draggingPanel.x = (int) (mouseX - dragX);
            draggingPanel.y = (int) (mouseY - dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() { return false; }

    private class CategoryPanel {
        public Module.Category category;
        public int x, y;
        public boolean isDragging = false;

        public CategoryPanel(Module.Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
        }

        public void render(DrawContext context, int mouseX, int mouseY) {
            List<Module> categoryModules = ModuleManager.modules.stream().filter(m -> m.getCategory() == category).toList();
            int totalHeight = HEADER_HEIGHT + (categoryModules.size() * MODULE_HEIGHT);

            // Фон и тень
            context.fill(x, y, x + PANEL_WIDTH, y + totalHeight, BG_DARK);
            context.drawBorder(x, y, PANEL_WIDTH, totalHeight, new Color(50, 50, 60).getRGB());

            // Заголовок
            context.fill(x, y, x + PANEL_WIDTH, y + HEADER_HEIGHT, HEADER_COLOR);
            String title = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
            context.drawTextWithShadow(textRenderer, title, x + 5, y + 5, ACCENT);

            int currentY = y + HEADER_HEIGHT;
            for (Module m : categoryModules) {
                boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT;
                
                int bgColor = m.isEnabled() ? ACCENT : (hovered ? new Color(50, 50, 65).getRGB() : MODULE_BG);
                context.fill(x + 2, currentY + 1, x + PANEL_WIDTH - 2, currentY + MODULE_HEIGHT - 1, bgColor);
                
                int textColor = m.isEnabled() ? 0 : (hovered ? 0xFFFFFF : TEXT_COLOR);
                context.drawTextWithShadow(textRenderer, m.getName(), x + 5, currentY + 4, textColor);
                
                if (m.isEnabled()) {
                    context.fill(x + 2, currentY + 1, x + 4, currentY + MODULE_HEIGHT - 1, 0xFFFFFFFF);
                }
                currentY += MODULE_HEIGHT;
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Клик по заголовку (для перетаскивания)
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + HEADER_HEIGHT) {
                isDragging = true;
                return true;
            }

            // Клик по модулю
            List<Module> categoryModules = ModuleManager.modules.stream().filter(m -> m.getCategory() == category).toList();
            int currentY = y + HEADER_HEIGHT;
            for (Module m : categoryModules) {
                if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT) {
                    m.toggle();
                    return true;
                }
                currentY += MODULE_HEIGHT;
            }
            return false;
        }
    }
}
