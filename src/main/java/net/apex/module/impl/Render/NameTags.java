package net.apex.module.impl.Render;
import net.apex.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class NameTags extends Module {
    public NameTags() { super("NameTags", Category.RENDER); }
    // В 1.21.4 для этого нужен EntityRenderState, поэтому модуль пока просто существует в меню
}
