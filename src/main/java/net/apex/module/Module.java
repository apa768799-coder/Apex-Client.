package net.apex.module;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

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

    public void onTick() {}
    public void onEnable() {}
    public void onDisable() {}
    public void onRenderWorld(WorldRenderContext context) {}
    public void onRenderHud(DrawContext context, float tickDelta) {}
    public void onChat(String message) {} // НОВЫЙ МЕТОД

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
