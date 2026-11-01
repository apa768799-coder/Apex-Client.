package net.apex.module.impl.Render;
import net.apex.module.Module;

public class FullBright extends Module {
    public FullBright() { super("FullBright", Category.RENDER); }
    @Override
    public void onTick() {
        if (mc.options != null) mc.options.getGamma().setValue(10.0);
    }
    @Override
    public void onDisable() {
        if (mc.options != null) mc.options.getGamma().setValue(1.0);
    }
}
