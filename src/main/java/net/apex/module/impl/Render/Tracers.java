package net.apex.module.impl.Render;

import net.apex.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Tracers extends Module {
    public Tracers() { super("Tracers", Category.RENDER); }

    @Override
    public void onRenderWorld(WorldRenderContext context) {
        if (mc.player == null || mc.world == null) return;
        
        MatrixStack matrices = context.matrixStack();
        Vec3d camPos = context.camera().getPos();
        matrices.push();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        VertexConsumerProvider consumers = context.consumers();
        if (consumers != null) {
            VertexConsumer lines = consumers.getBuffer(RenderLayer.getLines());
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            
            // Начало линии — глаза игрока
            Vec3d eye = mc.player.getEyePos();
            
            for (PlayerEntity p : mc.world.getPlayers()) {
                if (p != mc.player) {
                    Vec3d target = p.getPos().add(0, p.getHeight() / 2.0, 0); // Центр игрока
                    // Рисуем линию от нас до цели
                    lines.vertex(matrix, (float)eye.x, (float)eye.y, (float)eye.z).color(0f, 1f, 0f, 1f).normal(0, 1, 0);
                    lines.vertex(matrix, (float)target.x, (float)target.y, (float)target.z).color(0f, 1f, 0f, 1f).normal(0, 1, 0);
                }
            }
        }
        matrices.pop();
    }
}
