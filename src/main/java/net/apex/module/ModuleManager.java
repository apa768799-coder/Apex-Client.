package net.apex.module;

import net.apex.module.impl.Combat.*;
import net.apex.module.impl.Movement.*;
import net.apex.module.impl.Player.*;
import net.apex.module.impl.Misc.*;
import net.minecraft.client.MinecraftClient;

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
        modules.add(new TriggerBot()); // Новый
        modules.add(new AutoTrap());   // Новый
        
        // Movement
        modules.add(new AutoMine());
        modules.add(new Sprint());     // Новый
        modules.add(new NoFall());     // Новый
        modules.add(new Spider());     // Новый
        modules.add(new Scaffold());   // Новый
        
        // Player
        modules.add(new AutoArmor());  // Новый
        modules.add(new AutoTotem());  // Новый
        
        // Misc
        modules.add(new Nuker());      // Новый
    }

    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }
}
