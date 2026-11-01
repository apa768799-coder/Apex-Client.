package net.apex.module;

// Импорт боевых модулей
import net.apex.module.impl.Combat.*;
// Импорт модулей движения
import net.apex.module.impl.Movement.*;
// Импорт модулей игрока
import net.apex.module.impl.Player.*;
// Импорт визуальных модулей
import net.apex.module.impl.Render.*;
// Импорт разного
import net.apex.module.impl.Misc.*;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    
    // Единый список всех модулей клиента
    public static final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // ==============================
        // РЕГИСТРАЦИЯ ВСЕХ МОДУЛЕЙ
        // ==============================

        // --- COMBAT ---
        modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new AutoCrystal());
        modules.add(new TriggerBot());
        modules.add(new AutoTrap());
        modules.add(new Surround());

        // --- MOVEMENT ---
        modules.add(new AutoMine());
        modules.add(new Sprint());
        modules.add(new NoFall());
        modules.add(new Spider());
        modules.add(new Scaffold());
        modules.add(new ElytraHelper());
        modules.add(new AntiVoid()); // Новый

        // --- PLAYER ---
        modules.add(new AutoArmor());
        modules.add(new AutoTotem());
        modules.add(new AutoEat());
        modules.add(new AutoTool()); // Новый
        modules.add(new FastPlace()); // Новый

        // --- RENDER ---
        modules.add(new FullBright());
        modules.add(new PlayerESP());
        modules.add(new TargetHUD());

        // --- MISC ---
        modules.add(new Nuker());
    }

    // Вызывается каждый тик (в ApexClient)
    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    // Вызывается для отрисовки 3D-элементов в мире (ESP, Tracers)
    public void onRenderWorld(WorldRenderContext context) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onRenderWorld(context);
            }
        }
    }

    // Вызывается для отрисовки на экране (HUD, TargetHUD)
    public void onRenderHud(DrawContext context, float tickDelta) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onRenderHud(context, tickDelta);
            }
        }
    }
}
