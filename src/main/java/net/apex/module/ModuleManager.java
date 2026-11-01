package net.apex.module;

import net.apex.module.impl.Combat.*;
import net.apex.module.impl.Movement.*;
import net.apex.module.impl.Player.*;
import net.apex.module.impl.Render.*;
import net.apex.module.impl.Misc.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Combat
        modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new AutoCrystal());
        modules.add(new TriggerBot());
        modules.add(new AutoTrap());
        modules.add(new Surround());
        
        // Movement
        modules.add(new AutoMine());
        modules.add(new Sprint());
        modules.add(new NoFall());
        modules.add(new Spider());
        modules.add(new Scaffold());
        modules.add(new ElytraHelper());
        
        // Player
        modules.add(new AutoArmor());
        modules.add(new AutoTotem());
        modules.add(new AutoEat());
        
        // Render
        modules.add(new FullBright());
        modules.add(new PlayerESP());
        modules.add(new TargetHUD());

        // Misc
        modules.add(new Nuker());
    }

    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }

    public void onRenderWorld(WorldRenderContext context) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRenderWorld(context);
        }
    }

    public void onRenderHud(DrawContext context, float tickDelta) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRenderHud(context, tickDelta);
        }
    }
}
