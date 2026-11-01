package net.apex.module.impl.Render;

import net.apex.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class PlayerESP extends Module {
    public PlayerESP() { super("PlayerESP", Category.RENDER); }

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
            for (PlayerEntity p : mc.world.getPlayers()) {
                if (p != mc.player) {
                    drawBox(matrices, lines, p.getBoundingBox().expand(0.1));
                }
            }
        }
        matrices.pop();
    }

    private void drawBox(MatrixStack matrices, VertexConsumer lines, Box box) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;
        float r = 1f, g = 0f, b = 0f, a = 1f;

        lines.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x2, y1, z1).color(r, g, b, a).normal(0, 1, 0);
        lines.vertex(matrix, x2, y1, z1).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x2, y1, z2).color(r, g, b, a).normal(0, 1, 0);
        lines.vertex(matrix, x2, y1, z2).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x1, y1, z2).color(r, g, b, a).normal(0, 1, 0);
        lines.vertex(matrix, x1, y1, z2).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0);
        lines.vertex(matrix, x1, y2, z1).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x2, y2, z1).color(r, g, b, a).normal(0, 1, 0);
        lines.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0); lines.vertex(matrix, x1, y2, z1).color(r, g, b, a).normal(0, 1, 0);
    }
}
