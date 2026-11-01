package net.apex.module;

import net.apex.module.impl.Combat.KillAura;
import net.apex.module.impl.Movement.AutoMine;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Регистрируем наши модули
        modules.add(new KillAura());
        modules.add(new AutoMine());
    }

    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    // Получить модуль по имени (для GUI)
    public static Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
