package net.apex.ui;

import net.apex.module.Module;
import net.apex.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClickGui extends Screen {
    // ==========================================
    // 1. ЦВЕТОВАЯ ПАЛИТРА И СТИЛИ (ТЕМА DELTA)
    // ==========================================
    public static final int COLOR_ACCENT = new Color(0, 255, 180).getRGB();
    public static final int COLOR_ACCENT_DARK = new Color(0, 200, 140).getRGB();
    public static final int COLOR_BG_DARK = new Color(12, 12, 15, 245).getRGB();
    public static final int COLOR_BG_HEADER = new Color(20, 20, 25, 255).getRGB();
    public static final int COLOR_MODULE_BG = new Color(30, 30, 38, 255).getRGB();
    public static final int COLOR_MODULE_HOVER = new Color(45, 45, 55, 255).getRGB();
    public static final int COLOR_TEXT = new Color(220, 220, 220).getRGB();
    public static final int COLOR_TEXT_DARK = new Color(130, 130, 130).getRGB();
    public static final int COLOR_OUTLINE = new Color(60, 60, 70).getRGB();
    public static final int COLOR_SEARCH_BG = new Color(20, 20, 25, 200).getRGB();
    public static final int COLOR_TOOLTIP_BG = new Color(10, 10, 15, 240).getRGB();

    // ==========================================
    // 2. ГЛОБАЛЬНЫЕ РАЗМЕРЫ
    // ==========================================
    public static final int PANEL_WIDTH = 140;
    public static final int PANEL_HEADER_HEIGHT = 24;
    public static final int MODULE_HEIGHT = 20;
    public static final int SETTING_HEIGHT = 24;
    public static final int SCROLL_SPEED = 25;

    // ==========================================
    // 3. ПЕРЕМЕННЫЕ ИНТЕРФЕЙСА
    // ==========================================
    private final List<CategoryPanel> panels = new ArrayList<>();
    private CategoryPanel draggingPanel = null;
    private double dragX, dragY;
    
    private String searchQuery = "";
    private boolean typingInSearch = false;
    private int cursorBlinkTimer = 0;

    private String tooltipText = null;
    private int tooltipX, tooltipY;
    private final Map<Module, Float> moduleAnimations = new HashMap<>();

    public ClickGui() {
        super(Text.literal("Apex Client GUI"));
        int startX = 30;
        for (Module.Category cat : Module.Category.values()) {
            panels.add(new CategoryPanel(cat, startX, 40));
            startX += PANEL_WIDTH + 15;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 180).getRGB());

        cursorBlinkTimer++;
        if (cursorBlinkTimer > 1000) cursorBlinkTimer = 0;

        tooltipText = null;

        renderWatermark(context);
        renderArrayList(context);
        
        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }
        
        renderSearchBar(context, mouseX, mouseY);

        if (tooltipText != null) {
            renderTooltip(context, tooltipText, tooltipX, tooltipY);
        }

        String hint = "§7LMB - Toggle | §7RMB - Settings | §7Scroll - Menu";
        context.drawTextWithShadow(textRenderer, hint, 10, this.height - 15, -1);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderWatermark(DrawContext context) {
        String watermarkText = "APEX";
        String version = " Client v2.0";
        int textWidth = textRenderer.getWidth(watermarkText) + textRenderer.getWidth(version);
        int boxWidth = textWidth + 25;
        int boxHeight = 26;

        drawRoundedRect(context, 10, 10, boxWidth, boxHeight, 6, COLOR_BG_DARK);
        drawRoundedOutline(context, 10, 10, boxWidth, boxHeight, 6, COLOR_ACCENT);

        context.drawTextWithShadow(textRenderer, "§l" + watermarkText, 20, 20, COLOR_ACCENT);
        context.drawTextWithShadow(textRenderer, "§7" + version, 20 + textRenderer.getWidth(watermarkText) + 2, 20, COLOR_TEXT_DARK);
    }

    private void renderArrayList(DrawContext context) {
        List<Module> active = new ArrayList<>(ModuleManager.modules.stream().filter(Module::isEnabled).toList());
        active.sort(Comparator.comparingInt(m -> -textRenderer.getWidth(m.getName())));

        int yOffset = 45;
        for (Module m : active) {
            String name = m.getName();
            int width = textRenderer.getWidth(name) + 15;
            int x = this.width - width - 10;

            context.fill(x, yOffset, this.width - 10, yOffset + 16, COLOR_BG_DARK);
            context.fill(x, yOffset, x + 2, yOffset + 16, COLOR_ACCENT);
            context.drawTextWithShadow(textRenderer, name, x + 8, yOffset + 4, COLOR_TEXT);

            yOffset += 18;
        }
    }

    private void renderTooltip(DrawContext context, String text, int mouseX, int mouseY) {
        int textWidth = textRenderer.getWidth(text) + 10;
        int textHeight = 16;
        int tX = mouseX + 10;
        int tY = mouseY - 15;
        if (tX + textWidth > this.width) tX = this.width - textWidth - 5;
        if (tY + textHeight > this.height) tY = this.height - textHeight - 5;

        drawRoundedRect(context, tX, tY, textWidth, textHeight, 3, COLOR_TOOLTIP_BG);
        drawRoundedOutline(context, tX, tY, textWidth, textHeight, 3, COLOR_ACCENT);
        context.drawTextWithShadow(textRenderer, text, tX + 5, tY + 4, -1);
    }

    private void renderSearchBar(DrawContext context, int mouseX, int mouseY) {
        int searchX = this.width / 2 - 120;
        int searchY = this.height - 40;
        int searchWidth = 240;
        int searchHeight = 24;

        drawRoundedRect(context, searchX, searchY, searchWidth, searchHeight, 5, COLOR_SEARCH_BG);
        drawRoundedOutline(context, searchX, searchY, searchWidth, searchHeight, 5, typingInSearch ? COLOR_ACCENT : COLOR_OUTLINE);

        String displayText = searchQuery.isEmpty() ? "§7Search..." : "§f" + searchQuery;
        context.drawTextWithShadow(textRenderer, displayText, searchX + 10, searchY + 8, -1);

        if (typingInSearch) {
            if (cursorBlinkTimer % 20 < 10) {
                int cursorX = searchX + 10 + textRenderer.getWidth(searchQuery);
                context.fill(cursorX, searchY + 6, cursorX + 1, searchY + 18, COLOR_ACCENT);
            }
        }
    }

    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + radius, y + height - radius, color);
        context.fill(x + width - radius, y + radius, x + width, y + height - radius, color);

        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                if (i * i + j * j <= radius * radius) {
                    context.fill(x + radius - i, y + radius - j, x + radius - i + 1, y + radius - j + 1, color);
                    context.fill(x + width - radius + i, y + radius - j, x + width - radius + i + 1, y + radius - j + 1, color);
                    context.fill(x + radius - i, y + height - radius + j, x + radius - i + 1, y + height - radius + j + 1, color);
                    context.fill(x + width - radius + i, y + height - radius + j, x + width - radius + i + 1, y + height - radius + j + 1, color);
                }
            }
        }
    }

    public static void drawRoundedOutline(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        context.fill(x + radius, y, x + width - radius, y + 1, color);
        context.fill(x + radius, y + height - 1, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + 1, y + height - radius, color);
        context.fill(x + width - 1, y + radius, x + width, y + height - radius, color);
    }

    private float getAnimation(Module m, boolean hovered) {
        float target = hovered ? 1.0f : 0.0f;
        float current = moduleAnimations.getOrDefault(m, 0.0f);
        float next = MathHelper.lerp(0.1f, current, target);
        moduleAnimations.put(m, next);
        return next;
    }

    private void setTooltip(String text, int mouseX, int mouseY) {
        this.tooltipText = text;
        this.tooltipX = mouseX;
        this.tooltipY = mouseY;
    }

    // ==========================================
    // КЛАСС ПАНЕЛИ КАТЕГОРИИ
    // ==========================================
    private class CategoryPanel {
        public Module.Category category;
        public int x, y;
        public int scroll = 0;
        public boolean isDragging = false;

        public CategoryPanel(Module.Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            List<Module> mods = ModuleManager.modules.stream()
                    .filter(m -> m.getCategory() == category)
                    .filter(m -> searchQuery.isEmpty() || m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());

            int contentHeight = 5;
            for (Module m : mods) {
                contentHeight += MODULE_HEIGHT;
                if (m.isEnabled() && m.expanded) {
                    contentHeight += (m.settings.size() * SETTING_HEIGHT) + 5;
                }
            }

            int maxHeight = 320;
            int panelHeight = Math.min(contentHeight + PANEL_HEADER_HEIGHT, maxHeight);

            drawRoundedRect(context, x, y, PANEL_WIDTH, panelHeight, 6, COLOR_BG_DARK);
            drawRoundedOutline(context, x, y, PANEL_WIDTH, panelHeight, 6, COLOR_OUTLINE);

            context.fill(x + 1, y + 1, x + PANEL_WIDTH - 1, y + PANEL_HEADER_HEIGHT, COLOR_BG_HEADER);
            String title = category.name().charAt(0) + category.name().substring(1).toLowerCase();
            context.drawTextWithShadow(textRenderer, title, x + 10, y + 8, COLOR_ACCENT);

            if (contentHeight + PANEL_HEADER_HEIGHT > maxHeight) {
                drawScrollbar(context, contentHeight, maxHeight);
            }

            context.enableScissor(x, y + PANEL_HEADER_HEIGHT, x + PANEL_WIDTH, y + panelHeight);
            int currentY = y + PANEL_HEADER_HEIGHT + 5 - scroll;

            for (Module m : mods) {
                drawModule(context, m, mouseX, mouseY, currentY);
                currentY += MODULE_HEIGHT;
                
                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting setting : m.settings) {
                        drawSetting(context, setting, currentY, mouseX, mouseY);
                        currentY += SETTING_HEIGHT;
                    }
                    currentY += 5;
                }
            }
            context.disableScissor();
        }

        private void drawModule(DrawContext context, Module m, int mouseX, int mouseY, int currentY) {
            boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT;
            float anim = getAnimation(m, hovered);

            int bgColor = m.isEnabled() ? COLOR_ACCENT : interpolateColor(COLOR_MODULE_BG, COLOR_MODULE_HOVER, anim);
            drawRoundedRect(context, x + 3, currentY + 1, PANEL_WIDTH - 6, MODULE_HEIGHT - 2, 3, bgColor);

            int textColor = m.isEnabled() ? 0 : (hovered ? 0xFFFFFF : COLOR_TEXT);
            context.drawTextWithShadow(textRenderer, m.getName(), x + 10, currentY + 6, textColor);

            if (!m.settings.isEmpty() && m.isEnabled()) {
                String arrow = m.expanded ? "v" : ">";
                context.drawTextWithShadow(textRenderer, arrow, x + PANEL_WIDTH - 15, currentY + 6, textColor);
            }

            if (hovered) {
                setTooltip("Module: " + m.getName(), mouseX, mouseY);
            }
        }

        private void drawSetting(DrawContext context, Module.Setting setting, int currentY, int mouseX, int mouseY) {
            drawRoundedRect(context, x + 3, currentY, PANEL_WIDTH - 6, SETTING_HEIGHT - 2, 2, new Color(20, 20, 25, 200).getRGB());
            context.drawTextWithShadow(textRenderer, setting.name, x + 10, currentY + 5, COLOR_TEXT_DARK);

            if (setting.isSlider) {
                int barX = x + 10;
                int barY = currentY + 16;
                int barWidth = PANEL_WIDTH - 45;

                float percent = (setting.value - setting.min) / (setting.max - setting.min);
                percent = MathHelper.clamp(percent, 0.0f, 1.0f);

                drawRoundedRect(context, barX, barY, barWidth, 2, 1, new Color(50, 50, 60).getRGB());
                if (percent > 0.0f) {
                    drawRoundedRect(context, barX, barY, (int)(barWidth * percent), 2, 1, COLOR_ACCENT);
                }

                int knobX = barX + (int)(barWidth * percent);
                context.fill(knobX - 2, barY - 3, knobX + 2, barY + 3, 0xFFFFFFFF);

                String valueString = String.format("%.1f", setting.value);
                context.drawTextWithShadow(textRenderer, valueString, x + PANEL_WIDTH - 30, currentY + 5, COLOR_TEXT);
            } else {
                int cbX = x + PANEL_WIDTH - 22;
                int cbY = currentY + 5;
                int cbSize = 12;

                context.fill(cbX, cbY, cbX + cbSize, cbY + cbSize, new Color(40, 40, 50).getRGB());
                drawRoundedOutline(context, cbX, cbY, cbSize, cbSize, 2, COLOR_OUTLINE);

                if (setting.boolValue) {
                    context.fill(cbX + 2, cbY + 2, cbX + cbSize - 2, cbY + cbSize - 2, COLOR_ACCENT);
                    context.fill(cbX + 5, cbY + 5, cbX + 7, cbY + 7, 0xFFFFFFFF);
                }
            }
        }

        private void drawScrollbar(DrawContext context, int contentHeight, int panelHeight) {
            int scrollbarX = x + PANEL_WIDTH - 4;
            int scrollbarY = y + PANEL_HEADER_HEIGHT + 2;
            int scrollbarWidth = 2;
            int scrollbarHeight = panelHeight - PANEL_HEADER_HEIGHT - 4;

            drawRoundedRect(context, scrollbarX, scrollbarY, scrollbarWidth, scrollbarHeight, 1, new Color(40, 40, 50, 100).getRGB());

            float visibleRatio = (float) scrollbarHeight / (contentHeight + PANEL_HEADER_HEIGHT);
            int thumbHeight = Math.max(10, (int)(scrollbarHeight * visibleRatio));
            
            float scrollRatio = (float) scroll / Math.max(1, (contentHeight + PANEL_HEADER_HEIGHT - panelHeight));
            int thumbY = scrollbarY + (int)((scrollbarHeight - thumbHeight) * scrollRatio);

            drawRoundedRect(context, scrollbarX, thumbY, scrollbarWidth, thumbHeight, 1, COLOR_ACCENT);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + PANEL_HEADER_HEIGHT) {
                isDragging = true;
                return true;
            }

            List<Module> mods = ModuleManager.modules.stream()
                    .filter(m -> m.getCategory() == category)
                    .filter(m -> searchQuery.isEmpty() || m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
            
            int currentY = y + PANEL_HEADER_HEIGHT + 5 - scroll;

            for (Module m : mods) {
                if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT) {
                    if (button == 0) m.toggle();
                    else if (button == 1 && !m.settings.isEmpty()) m.expanded = !m.expanded;
                    return true;
                }
                currentY += MODULE_HEIGHT;

                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting setting : m.settings) {
                        if (mouseX >= x + 3 && mouseX <= x + PANEL_WIDTH - 3 && mouseY >= currentY && mouseY <= currentY + SETTING_HEIGHT) {
                            if (button == 0) {
                                if (setting.isSlider) {
                                    setting.dragging = true;
                                    updateSlider(setting, mouseX, x);
                                } else {
                                    setting.boolValue = !setting.boolValue;
                                }
                            }
                            return true;
                        }
                        currentY += SETTING_HEIGHT;
                    }
                    currentY += 5;
                }
            }
            return false;
        }

        public void mouseDragged(double mouseX, double mouseY) {
            for (Module m : ModuleManager.modules) {
                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting setting : m.settings) {
                        if (setting.isSlider && setting.dragging) {
                            updateSlider(setting, mouseX, x);
                        }
                    }
                }
            }
        }

        private void updateSlider(Module.Setting setting, double mouseX, int panelX) {
            int barX = panelX + 10;
            int barWidth = PANEL_WIDTH - 45;
            float percent = (float)(mouseX - barX) / barWidth;
            percent = MathHelper.clamp(percent, 0.0f, 1.0f);
            float newValue = setting.min + (setting.max - setting.min) * percent;
            setting.value = Math.round(newValue * 10.0f) / 10.0f;
        }

        public void mouseReleased(double mouseX, double mouseY) {
            isDragging = false;
            for (Module m : ModuleManager.modules) {
                for (Module.Setting s : m.settings) {
                    s.dragging = false;
                }
            }
        }

        public void mouseScrolled(double mouseX, double mouseY, double amount) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY > y && mouseY < y + 400) {
                scroll -= (int) (amount * SCROLL_SPEED);
                
                int maxScroll = 0;
                List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == category).toList();
                for (Module m : mods) {
                    maxScroll += MODULE_HEIGHT;
                    if (m.isEnabled() && m.expanded) maxScroll += (m.settings.size() * SETTING_HEIGHT) + 5;
                }
                maxScroll = Math.max(0, maxScroll - 300);

                if (scroll < 0) scroll = 0;
                if (scroll > maxScroll) scroll = maxScroll;
            }
        }
    }

    // ==========================================
    // ГЛОБАЛЬНЫЕ СОБЫТИЯ
    // ==========================================
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int searchX = this.width / 2 - 120;
        int searchY = this.height - 40;
        if (button == 0 && mouseX >= searchX && mouseX <= searchX + 240 && mouseY >= searchY && mouseY <= searchY + 24) {
            typingInSearch = true;
            return true;
        }
        typingInSearch = false;

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
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingPanel = null;
        for (CategoryPanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingPanel != null) {
            draggingPanel.x = (int) (mouseX - dragX);
            draggingPanel.y = (int) (mouseY - dragY);
            return true;
        }
        for (CategoryPanel panel : panels) {
            panel.mouseDragged(mouseX, mouseY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (CategoryPanel panel : panels) {
            panel.mouseScrolled(mouseX, mouseY, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (typingInSearch) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!searchQuery.isEmpty()) {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                }
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) {
                typingInSearch = false;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (typingInSearch && searchQuery.length() < 25) {
            if (chr >= 32 && chr <= 126) {
                searchQuery += chr;
                return true;
            }
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() { return false; }

    private static int interpolateColor(int color1, int color2, float factor) {
        factor = MathHelper.clamp(factor, 0.0f, 1.0f);
        int a1 = (color1 >> 24) & 0xFF, a2 = (color2 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF, r2 = (color2 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF,  g2 = (color2 >> 8) & 0xFF;
        int b1 = color1 & 0xFF,         b2 = color2 & 0xFF;

        int a = (int) (a1 + (a2 - a1) * factor);
        int r = (int) (r1 + (r2 - r1) * factor);
        int g = (int) (g1 + (g2 - g1) * factor);
        int b = (int) (b1 + (b2 - b1) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
