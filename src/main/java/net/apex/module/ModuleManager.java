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
        // --- COMBAT ---
        modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new AutoCrystal());
        modules.add(new TriggerBot());
        modules.add(new AutoTrap());
        modules.add(new Surround());
        modules.add(new MiddleClickPearl());
        modules.add(new AutoBlock()); // Новый

        // --- MOVEMENT ---
        modules.add(new AutoMine());
        modules.add(new Sprint());
        modules.add(new NoFall());
        modules.add(new Spider());
        modules.add(new Scaffold());
        modules.add(new ElytraHelper());
        modules.add(new AntiVoid()); 
        modules.add(new AutoJump());  // Новый
        modules.add(new KeepSprint());// Новый
        modules.add(new NoSlow());    // Новый

        // --- PLAYER ---
        modules.add(new AutoArmor());
        modules.add(new AutoTotem());
        modules.add(new AutoEat());
        modules.add(new AutoTool()); 
        modules.add(new FastPlace()); 
        modules.add(new ChestStealer());
        modules.add(new AutoRespawn());

        // --- RENDER ---
        modules.add(new FullBright());
        modules.add(new PlayerESP());
        modules.add(new TargetHUD());
        modules.add(new Tracers());
        modules.add(new NameTags());  // Новый

        // --- MISC ---
        modules.add(new Nuker());
        modules.add(new AutoReconnect());
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
