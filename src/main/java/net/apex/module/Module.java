package net.apex.module;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    private final String name;
    private final Category category;
    private boolean enabled;
    public final List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
    }

    public void onTick() {}
    public void onEnable() {}
    public void onDisable() {}
    public void onRenderWorld(WorldRenderContext context) {}
    public void onRenderHud(DrawContext context, float tickDelta) {}
    public void onChat(String message) {}

    public String getName() { return name; }
    public Category getCategory() { return category; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean state) {
        this.enabled = state;
        if (state) onEnable();
        else onDisable();
    }
    
    public void toggle() { setEnabled(!enabled); }

    // Хелпер для добавления настроек
    protected Setting addSetting(Setting s) {
        settings.add(s);
        return s;
    }

    public enum Category {
        COMBAT, MOVEMENT, RENDER, PLAYER, MISC
    }

    // Класс настройки (Слайдер / Чекбокс)
    public static class Setting {
        public String name;
        public float value, min, max;
        public boolean boolValue;
        public boolean isSlider;
        public boolean dragging = false;

        // Конструктор для слайдера
        public Setting(String name, float defaultVal, float min, float max) {
            this.name = name; this.value = defaultVal; this.min = min; this.max = max; this.isSlider = true;
        }
        // Конструктор для чекбокса
        public Setting(String name, boolean defaultVal) {
            this.name = name; this.boolValue = defaultVal; this.isSlider = false;
        }
    }
}
