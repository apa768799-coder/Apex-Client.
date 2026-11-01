package net.apex.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    private final String name;
    private final Category category;
    private boolean enabled;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
    }

    // Вызывается каждый тик, если модуль включен
    public void onTick() {}
    
    // Вызывается при включении/выключении
    public void onEnable() {}
    public void onDisable() {}

    public String getName() { return name; }
    public Category getCategory() { return category; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean state) {
        this.enabled = state;
        if (state) onEnable();
        else onDisable();
    }
    
    public void toggle() { setEnabled(!enabled); }

    public enum Category {
        COMBAT, MOVEMENT, RENDER, PLAYER, MISC
    }
}
