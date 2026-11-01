package net.apex.module;

import net.apex.module.impl.Combat.AutoCrystal;
import net.apex.module.impl.Combat.Criticals;
import net.apex.module.impl.Combat.KillAura;
import net.apex.module.impl.Combat.Velocity;
import net.apex.module.impl.Movement.AutoMine;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    
    // Единый список всех модулей клиента
    public static final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // --- РЕГИСТРАЦИЯ МОДУЛЕЙ ---
        
        // Категория: Combat
        modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new AutoCrystal());
        
        // Категория: Movement
        modules.add(new AutoMine());
        
        // Сюда мы будем добавлять новые модули по мере их создания...
    }

    // Этот метод вызывается каждый тик из ApexClient
    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }
}
