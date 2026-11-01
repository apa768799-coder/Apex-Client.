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
    private final int BG = new Color(15, 15, 20, 245).getRGB();
    private final int HEADER = new Color(30, 30, 40).getRGB();
    private final int TEXT = new Color(220, 220, 220).getRGB();
    private final int TEXT_DARK = new Color(120, 120, 120).getRGB();
    private final int BTN_BG = new Color(25, 25, 35).getRGB();

    private final List<Panel> panels = new ArrayList<>();
    private Panel draggingPanel = null;
    private double dragX, dragY;

    public ClickGui() {
        super(Text.literal("Apex Client"));
        int x = 20;
        for (Module.Category cat : Module.Category.values()) {
            panels.add(new Panel(cat, x, 20));
            x += 130;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 160).getRGB());
        
        context.fill(0, 0, this.width, 15, new Color(10, 10, 15, 255).getRGB());
        context.drawTextWithShadow(textRenderer, "§bApex Client §7| §fLMB - Toggle | RMB - Settings", 5, 4, -1);

        for (Panel p : panels) p.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 || button == 1) {
            for (Panel p : panels) {
                if (p.mouseClicked(mouseX, mouseY, button)) {
                    if (p.isDragging) { draggingPanel = p; dragX = mouseX - p.x; dragY = mouseY - p.y; }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingPanel = null;
        for (Panel p : panels) p.mouseReleased(mouseX, mouseY);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (draggingPanel != null) {
            draggingPanel.x = (int) (mouseX - dragX);
            draggingPanel.y = (int) (mouseY - dragY);
            return true;
        }
        for (Panel p : panels) p.mouseDragged(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount, double vert) {
        for (Panel p : panels) p.mouseScrolled(mouseX, mouseY, vert);
        return super.mouseScrolled(mouseX, mouseY, amount, vert);
    }

    @Override
    public boolean shouldPause() { return false; }

    private class Panel {
        public Module.Category cat;
        public int x, y, scroll = 0;
        public boolean isDragging = false, extended = true;

        public Panel(Module.Category cat, int x, int y) {
            this.cat = cat; this.x = x; this.y = y;
        }

        public void render(DrawContext ctx, int mouseX, int mouseY) {
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == cat).toList();
            
            // Считаем высоту (с учетом раскрытых настроек)
            int totalHeight = 18;
            for (Module m : mods) {
                totalHeight += 16;
                if (m.isEnabled() && m.expanded) totalHeight += (m.settings.size() * 18) + 2;
            }

            // Фон панели
            ctx.fill(x, y, x + 120, y + totalHeight + 2, BG);
            ctx.drawBorder(x, y, 120, totalHeight + 2, new Color(45, 45, 55).getRGB());
            
            // Заголовок
            ctx.fill(x, y, x + 120, y + 18, HEADER);
            String title = cat.name().charAt(0) + cat.name().substring(1).toLowerCase();
            ctx.drawTextWithShadow(textRenderer, title, x + 5, y + 5, ACCENT);

            int cy = y + 18 - scroll;
            ctx.enableScissor(x, y + 18, x + 120, y + totalHeight + 2);
            
            for (Module m : mods) {
                boolean hov = mouseX >= x && mouseX <= x + 120 && mouseY >= cy && mouseY <= cy + 16;
                int bg = m.isEnabled() ? ACCENT : (hov ? new Color(50, 50, 65).getRGB() : BTN_BG);
                
                ctx.fill(x + 2, cy + 1, x + 118, cy + 15, bg);
                ctx.drawTextWithShadow(textRenderer, m.getName(), x + 6, cy + 4, m.isEnabled() ? 0 : TEXT);
                
                // Отрисовка настроек (если модуль включен и раскрыт)
                if (m.isEnabled() && m.expanded) {
                    cy += 16;
                    for (Module.Setting s : m.settings) {
                        if (s.isSlider) {
                            ctx.drawTextWithShadow(textRenderer, s.name, x + 8, cy + 2, TEXT_DARK);
                            int barW = 80;
                            float pct = (s.value - s.min) / (s.max - s.min);
                            pct = MathHelper.clamp(pct, 0, 1);
                            
                            ctx.fill(x + 8, cy + 12, x + 8 + barW, cy + 14, new Color(40, 40, 50).getRGB());
                            ctx.fill(x + 8, cy + 12, x + 8 + (int)(barW * pct), cy + 14, ACCENT);
                            ctx.fill(x + 8 + (int)(barW * pct) - 1, cy + 10, x + 8 + (int)(barW * pct) + 2, cy + 16, -1);
                            
                            ctx.drawTextWithShadow(textRenderer, String.format("%.1f", s.value), x + 92, cy + 2, TEXT);
                        } else {
                            ctx.drawTextWithShadow(textRenderer, s.name, x + 8, cy + 2, TEXT_DARK);
                            ctx.fill(x + 92, cy + 2, x + 102, cy + 12, s.boolValue ? ACCENT : new Color(40,40,50).getRGB());
                            if (s.boolValue) ctx.fill(x + 94, cy + 4, x + 100, cy + 10, 0xFFFFFFFF);
                        }
                        cy += 18;
                    }
                    cy += 2;
                }
                cy += 16;
            }
            ctx.disableScissor();
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + 120 && mouseY >= y && mouseY <= y + 18) {
                isDragging = true; return true;
            }
            
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == cat).toList();
            int cy = y + 18 - scroll;
            for (Module m : mods) {
                if (mouseX >= x && mouseX <= x + 120 && mouseY >= cy && mouseY <= cy + 16) {
                    if (button == 0) m.toggle();
                    else if (button == 1 && m.isEnabled()) m.expanded = !m.expanded;
                    return true;
                }
                cy += 16;
                
                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting s : m.settings) {
                        if (mouseX >= x + 8 && mouseX <= x + 110 && mouseY >= cy && mouseY <= cy + 16) {
                            if (s.isSlider && button == 0) {
                                s.dragging = true;
                                updateSlider(s, mouseX, x);
                                return true;
                            } else if (!s.isSlider && button == 0) {
                                s.boolValue = !s.boolValue;
                                return true;
                            }
                        }
                        cy += 18;
                    }
                    cy += 2;
                }
            }
            return false;
        }

        public void mouseDragged(double mouseX, double mouseY) {
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == cat).toList();
            for (Module m : mods) {
                if (m.isEnabled() && m.expanded) {
                    for (Module.Setting s : m.settings) {
                        if (s.dragging) updateSlider(s, mouseX, x);
                    }
                }
            }
        }

        private void updateSlider(Module.Setting s, double mouseX, int px) {
            float pct = (float)(mouseX - (px + 8)) / 80f;
            pct = MathHelper.clamp(pct, 0f, 1f);
            s.value = s.min + (s.max - s.min) * pct;
        }

        public void mouseReleased(double mouseX, double mouseY) {
            isDragging = false;
            List<Module> mods = ModuleManager.modules.stream().filter(m -> m.getCategory() == cat).toList();
            for (Module m : mods) for (Module.Setting s : m.settings) s.dragging = false;
        }

        public void mouseScrolled(double mouseX, double mouseY, double amount) {
            if (mouseX >= x && mouseX <= x + 120 && mouseY > y && mouseY < y + 300) {
                scroll -= (int) (amount * 15);
                if (scroll < 0) scroll = 0;
            }
        }
    }
}
