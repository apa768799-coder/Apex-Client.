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
    // 1. ЦВЕТА (ПОД СКРИНШОТ)
    // ==========================================
    // Темный, почти черный фон с прозрачностью 90%
    public static final int COLOR_BG_DARK = new Color(15, 15, 18, 230).getRGB(); 
    // Чуть светлее для заголовков
    public static final int COLOR_BG_HEADER = new Color(20, 20, 25, 255).getRGB(); 
    // Цвет текста (светло-серый)
    public static final int COLOR_TEXT = new Color(200, 200, 200).getRGB(); 
    // Темный текст для подсказок/значений
    public static final int COLOR_TEXT_DARK = new Color(120, 120, 120).getRGB(); 
    // Еле заметная обводка панелей
    public static final int COLOR_OUTLINE = new Color(40, 40, 45, 200).getRGB(); 
    // Акцентный цвет (сделаем белым/серым, как на скрине, вместо бирюзового)
    public static final int COLOR_ACCENT = new Color(230, 230, 230).getRGB();
    public static final int COLOR_MODULE_HOVER = new Color(35, 35, 40, 200).getRGB();

    // ==========================================
    // 2. РАЗМЕРЫ (ПОДОГНАНО ПОД СКРИН)
    // ==========================================
    public static final int PANEL_WIDTH = 160; // Чуть шире
    public static final int PANEL_HEADER_HEIGHT = 22;
    public static final int MODULE_HEIGHT = 22; // Чуть выше модули
    public static final int SETTING_HEIGHT = 24;
    public static final int SCROLL_SPEED = 25;

    private final List<CategoryPanel> panels = new ArrayList<>();
    private CategoryPanel draggingPanel = null;
    private double dragX, dragY;
    
    private String searchQuery = "";
    private boolean typingInSearch = false;
    private int cursorBlinkTimer = 0;

    // Для иконок категорий
    private final Map<Module.Category, String> categoryIcons = new HashMap<>();

    public ClickGui() {
        super(Text.literal("Apex Client Menu"));
        
        // Иконки (используем юникод-символы для имитации)
        categoryIcons.put(Module.Category.COMBAT, "✧");
        categoryIcons.put(Module.Category.MOVEMENT, "⚡");
        categoryIcons.put(Module.Category.RENDER, "👁");
        categoryIcons.put(Module.Category.PLAYER, "👤");
        categoryIcons.put(Module.Category.MISC, "#");

        int startX = 30;
        for (Module.Category cat : Module.Category.values()) {
            panels.add(new CategoryPanel(cat, startX, 30));
            startX += PANEL_WIDTH + 12; // Отступ между панелями
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Затемнение фона игры
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 120).getRGB());

        cursorBlinkTimer++;
        if (cursorBlinkTimer > 1000) cursorBlinkTimer = 0;

        // 2. Рендер панелей
        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        // 3. Строка поиска по центру снизу
        renderSearchBar(context, mouseX, mouseY);

        // 4. Окно конфигов (Client Manager) справа снизу (как на скрине)
        renderClientManager(context);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderSearchBar(DrawContext context, int mouseX, int mouseY) {
        int searchW = 200;
        int searchH = 22;
        int searchX = this.width / 2 - searchW / 2;
        int searchY = this.height - 45;

        // Фон поиска
        context.fill(searchX, searchY, searchX + searchW, searchY + searchH, COLOR_BG_DARK);
        context.drawBorder(searchX, searchY, searchW, searchH, COLOR_OUTLINE);

        // Иконка лупы (справа)
        context.drawTextWithShadow(textRenderer, "🔍", searchX + searchW - 15, searchY + 7, COLOR_TEXT_DARK);

        // Текст
        String displayText = searchQuery.isEmpty() ? "Search" : searchQuery;
        int textColor = searchQuery.isEmpty() ? COLOR_TEXT_DARK : COLOR_TEXT;
        context.drawTextWithShadow(textRenderer, displayText, searchX + 8, searchY + 7, textColor);

        if (typingInSearch && cursorBlinkTimer % 20 < 10) {
            int cursorX = searchX + 8 + textRenderer.getWidth(searchQuery);
            context.fill(cursorX, searchY + 6, cursorX + 1, searchY + 16, COLOR_TEXT);
        }
    }

    private void renderClientManager(DrawContext context) {
        int w = 160;
        int h = 180;
        int x = this.width - w - 20;
        int y = this.height - h - 20;

        // Фон окна
        context.fill(x, y, x + w, y + h, COLOR_BG_DARK);
        context.drawBorder(x, y, w, h, COLOR_OUTLINE);
        
        // Заголовок
        context.fill(x, y, x + w, y + 20, COLOR_BG_HEADER);
        context.drawTextWithShadow(textRenderer, "Client Manager", x + 5, y + 6, COLOR_TEXT);

        // Кнопки Config / Theme
        context.drawTextWithShadow(textRenderer, "Config", x + w - 60, y + 6, COLOR_TEXT_DARK);
        context.drawTextWithShadow(textRenderer, "Theme", x + w - 25, y + 6, COLOR_ACCENT);

        // Заглушка для списка конфигов
        context.fill(x + 5, y + 30, x + w - 5, y + 50, COLOR_BG_HEADER);
        context.drawTextWithShadow(textRenderer, "> Текущая", x + 10, y + 36, COLOR_TEXT);

        context.fill(x + 5, y + 55, x + w - 5, y + 75, COLOR_MODULE_HOVER);
        context.drawTextWithShadow(textRenderer, "> Стандартная", x + 10, y + 61, COLOR_TEXT);

        // Поле ввода снизу
        context.fill(x + 5, y + h - 25, x + w - 5, y + h - 5, COLOR_BG_HEADER);
        context.drawTextWithShadow(textRenderer, "Введите имя...", x + 10, y + h - 19, COLOR_TEXT_DARK);
        context.drawTextWithShadow(textRenderer, "Создать", x + w - 45, y + h - 19, COLOR_ACCENT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Клик по поиску
        int searchW = 200;
        int searchX = this.width / 2 - searchW / 2;
        int searchY = this.height - 45;
        if (button == 0 && mouseX >= searchX && mouseX <= searchX + searchW && mouseY >= searchY && mouseY <= searchY + 22) {
            typingInSearch = true;
            return true;
        }
        typingInSearch = false;

        // Клик по панелям
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
        for (CategoryPanel panel : panels) panel.mouseReleased(mouseX, mouseY);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingPanel != null) {
            draggingPanel.x = (int) (mouseX - dragX);
            draggingPanel.y = (int) (mouseY - dragY);
            return true;
        }
        for (CategoryPanel panel : panels) panel.mouseDragged(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (CategoryPanel panel : panels) panel.mouseScrolled(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (typingInSearch) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!searchQuery.isEmpty()) searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
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

    // ==========================================
    // КЛАСС ПАНЕЛИ
    // ==========================================
    private class CategoryPanel {
        public Module.Category category;
        public int x, y, scroll = 0;
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

            int maxH = 280; // Максимальная высота
            int totalH = 0;
            for (Module m : mods) {
                totalH += MODULE_HEIGHT;
                if (m.isEnabled() && m.expanded) totalH += (m.settings.size() * SETTING_HEIGHT);
            }

            int panelHeight = Math.min(PANEL_HEADER_HEIGHT + totalH + 5, maxH);

            // Отрисовка фона панели
            context.fill(x, y, x + PANEL_WIDTH, y + panelHeight, COLOR_BG_DARK);
            context.drawBorder(x, y, PANEL_WIDTH, panelHeight, COLOR_OUTLINE);

            // Заголовок
            context.fill(x, y, x + PANEL_WIDTH, y + PANEL_HEADER_HEIGHT, COLOR_BG_HEADER);
            String title = category.name().charAt(0) + category.name().substring(1).toLowerCase();
            context.drawTextWithShadow(textRenderer, title, x + 8, y + 7, COLOR_TEXT);
            
            // Иконка справа
            String icon = categoryIcons.getOrDefault(category, "?");
            context.drawTextWithShadow(textRenderer, icon, x + PANEL_WIDTH - 15, y + 7, COLOR_TEXT_DARK);

            // Скролл
            context.enableScissor(x, y + PANEL_HEADER_HEIGHT, x + PANEL_WIDTH, y + panelHeight);
            int currentY = y + PANEL_HEADER_HEIGHT + 2 - scroll;

            for (Module m : mods) {
                boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT;
                
                // Фон модуля (при наведении чуть светлее)
                if (hovered) context.fill(x + 1, currentY, x + PANEL_WIDTH - 1, currentY + MODULE_HEIGHT, COLOR_MODULE_HOVER);
                
                // Текст модуля
                int textColor = m.isEnabled() ? COLOR_ACCENT : COLOR_TEXT;
                context.drawTextWithShadow(textRenderer, m.getName(), x + 8, currentY + 7, textColor);

                // Стрелочка "v" (как на скрине)
                if (!m.settings.isEmpty()) {
                    context.drawTextWithShadow(textRenderer, "v", x + PANEL_WIDTH - 15, currentY + 7, COLOR_TEXT_DARK);
                }

                currentY += MODULE_HEIGHT;

                // Настройки (если открыты)
                if (m.isEnabled() && m.expanded) {
                    context.fill(x + 1, currentY, x + PANEL_WIDTH - 1, currentY + (m.settings.size() * SETTING_HEIGHT), new Color(10, 10, 12, 100).getRGB());
                    
                    for (Module.Setting s : m.settings) {
                        context.drawTextWithShadow(textRenderer, s.name, x + 15, currentY + 4, COLOR_TEXT_DARK);
                        
                        if (s.isSlider) {
                            int barX = x + 15;
                            int barW = PANEL_WIDTH - 30;
                            int barY = currentY + 16;
                            
                            float percent = (s.value - s.min) / (s.max - s.min);
                            percent = MathHelper.clamp(percent, 0f, 1f);

                            context.fill(barX, barY, barX + barW, barY + 2, new Color(50, 50, 60).getRGB());
                            context.fill(barX, barY, barX + (int)(barW * percent), barY + 2, COLOR_ACCENT);
                            context.fill(barX + (int)(barW * percent) - 2, barY - 2, barX + (int)(barW * percent) + 2, barY + 4, 0xFFFFFFFF);

                            String val = String.format("%.1f", s.value);
                            context.drawTextWithShadow(textRenderer, val, x + PANEL_WIDTH - 30, currentY + 4, COLOR_TEXT);
                        } else {
                            int cbX = x + PANEL_WIDTH - 20;
                            int cbY = currentY + 6;
                            context.drawBorder(cbX, cbY, 10, 10, COLOR_OUTLINE);
                            if (s.boolValue) context.fill(cbX + 2, cbY + 2, cbX + 8, cbY + 8, COLOR_ACCENT);
                        }
                        currentY += SETTING_HEIGHT;
                    }
                }
            }
            context.disableScissor();
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
            
            int currentY = y + PANEL_HEADER_HEIGHT + 2 - scroll;

            for (Module m : mods) {
                if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + MODULE_HEIGHT) {
                    if (button == 0) m.toggle();
                    else if (button == 1 && !m.settings.isEmpty()) m.expanded = !m.expanded;
                    return true;
                }
                currentY += MODULE_HEIGHT;

                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting s : m.settings) {
                        if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= currentY && mouseY <= currentY + SETTING_HEIGHT) {
                            if (button == 0) {
                                if (s.isSlider) { s.dragging = true; updateSlider(s, mouseX, x); }
                                else s.boolValue = !s.boolValue;
                            }
                            return true;
                        }
                        currentY += SETTING_HEIGHT;
                    }
                }
            }
            return false;
        }

        public void mouseDragged(double mouseX, double mouseY) {
            for (Module m : ModuleManager.modules) {
                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting s : m.settings) {
                        if (s.isSlider && s.dragging) updateSlider(s, mouseX, x);
                    }
                }
            }
        }

        private void updateSlider(Module.Setting s, double mouseX, int panelX) {
            int barX = panelX + 15;
            int barW = PANEL_WIDTH - 30;
            float percent = (float)(mouseX - barX) / barW;
            percent = MathHelper.clamp(percent, 0f, 1f);
            s.value = Math.round((s.min + (s.max - s.min) * percent) * 10.0f) / 10.0f;
        }

        public void mouseReleased(double mouseX, double mouseY) {
            isDragging = false;
            for (Module m : ModuleManager.modules) {
                for (Module.Setting s : m.settings) s.dragging = false;
            }
        }

        public void mouseScrolled(double mouseX, double mouseY, double amount) {
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY > y && mouseY < y + 300) {
                scroll -= (int) (amount * SCROLL_SPEED);
                if (scroll < 0) scroll = 0;
            }
        }
    }
}
