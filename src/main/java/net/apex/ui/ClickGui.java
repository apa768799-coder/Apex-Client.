package net.apex.ui;

import net.apex.module.Module;
import net.apex.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final int ACCENT = new Color(0, 255, 180).getRGB();
    private final int BG_DARK = new Color(15, 15, 20, 240).getRGB();
    private final int HEADER_COLOR = new Color(30, 30, 40).getRGB();
    private final int TEXT_COLOR = new Color(200, 200, 200).getRGB();
    private final int MODULE_BG = new Color(25, 25, 30).getRGB();
    private final int SLIDER_BG = new Color(40, 40, 50).getRGB();

    private final int PANEL_WIDTH = 120;
    private final int HEADER_HEIGHT = 18;
    private final int MODULE_HEIGHT = 16;

    private final List<Panel> panels = new ArrayList<>();
    private Panel draggingPanel = null;
    private double dragX, dragY;

    public ClickGui() {
        super(Text.literal("Apex Client"));
        int x = 20;
        for (Module.Category category : Module.Category.values()) {
            panels.add(new Panel(category, x, 20));
            x += PANEL_WIDTH + 10;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 160).getRGB());
        context.fill(0, 0, this.width, 15, new Color(10, 10, 15, 255).getRGB());
        context.drawTextWithShadow(textRenderer, "§bApex Client §7| §fSettings: RMB on module", 5, 4, -1);

        for (Panel panel : panels) panel.render(context, mouseX, mouseY);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Panel panel : panels) {
                if (panel.mouseClicked(mouseX, mouseY, button)) {
                    if (panel.isDragging) { draggingPanel = panel; dragX = mouseX - panel.x; dragY = mouseY - panel.y; }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingPanel = null;
        for (Panel panel : panels) panel.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (draggingPanel != null) { draggingPanel.x = (int)(mouseX - dragX); draggingPanel.y = (int)(mouseY - dragY); return true; }
        for (Panel panel : panels) panel.mouseDragged(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount, double verticalAmount) {
        for (Panel panel : panels) panel.mouseScrolled(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, amount, verticalAmount);
    }
    @Override public boolean shouldPause() { return false; }

    private class Panel {
        public Module.Category category;
        public int x, y;
        public boolean isDragging = false;
        public int scroll = 0;

        public Panel(Module.Category category, int x, int y) { this.category = category; this.x = x; this.y = y; }

        public void render(DrawContext context, int mouseX, int mouseY) {
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == category).toList();
            int height = HEADER_HEIGHT + (mods.size() * MODULE_HEIGHT) + 4;
            
            context.fill(x, y, x + PANEL_WIDTH, y + height, BG_DARK);
            context.drawBorder(x, y, PANEL_WIDTH, height, new Color(50, 50, 60).getRGB());
            context.fill(x, y, x + PANEL_WIDTH, y + HEADER_HEIGHT, HEADER_COLOR);
            String title = category.name().substring(0,1).toUpperCase() + category.name().substring(1).toLowerCase();
            context.drawTextWithShadow(textRenderer, title, x + 5, y + 5, ACCENT);

            int currentY = y + HEADER_HEIGHT - scroll;
            context.enableScissor(x, y + HEADER_HEIGHT, x + PANEL_WIDTH, y + height);
            for (Module m : mods) {
                boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT;
                int bgColor = m.isEnabled() ? ACCENT : (hovered ? new Color(50, 50, 65).getRGB() : MODULE_BG);
                
                context.fill(x + 2, currentY + 1, x + PANEL_WIDTH - 2, currentY + MODULE_HEIGHT - 1, bgColor);
                int textColor = m.isEnabled() ? 0 : TEXT_COLOR;
                context.drawTextWithShadow(textRenderer, m.getName(), x + 5, currentY + 4, textColor);
                currentY += MODULE_HEIGHT;
            }
            context.disableScissor();
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + HEADER_HEIGHT) { isDragging = true; return true; }
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == category).toList();
            int currentY = y + HEADER_HEIGHT - scroll;
            for (Module m : mods) {
                if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT) {
                    if (button == 0) m.toggle();
                    return true;
                }
                currentY += MODULE_HEIGHT;
            }
            return false;
        }
        public void mouseReleased(double mouseX, double mouseY, int button) { isDragging = false; }
        public void mouseDragged(double mouseX, double mouseY) {}
        public void mouseScrolled(double mouseX, double mouseY, double amount) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + 200) {
                scroll -= (int) (amount * 10);
                if (scroll < 0) scroll = 0;
            }
        }
    }
}
