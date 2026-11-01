package net.apex.module.impl.Render;

import net.apex.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class MineHelper extends Module {
    public MineHelper() { super("MineHelper", Category.RENDER); }

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
            BlockPos pPos = mc.player.getBlockPos();
            
            for (BlockPos pos : BlockPos.iterateOutwards(pPos, 20, 20, 20)) {
                var b = mc.world.getBlockState(pos).getBlock();
                if (b == Blocks.DIAMOND_ORE || b == Blocks.DEEPSLATE_DIAMOND_ORE || b == Blocks.ANCIENT_DEBRIS) {
                    drawBox(matrices, lines, new Box(pos).expand(0.002), 0f, 1f, 1f); // Голубой
                } else if (b == Blocks.GOLD_ORE || b == Blocks.DEEPSLATE_GOLD_ORE) {
                    drawBox(matrices, lines, new Box(pos).expand(0.002), 1f, 1f, 0f); // Желтый
                } else if (b == Blocks.EMERALD_ORE || b == Blocks.DEEPSLATE_EMERALD_ORE) {
                    drawBox(matrices, lines, new Box(pos).expand(0.002), 0f, 1f, 0f); // Зеленый
                }
            }
        }
        matrices.pop();
    }

    private void drawBox(MatrixStack matrices, VertexConsumer lines, Box box, float r, float g, float b) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;
        lines.vertex(matrix, x1, y1, z1).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x2, y1, z1).color(r, g, b, 1f).normal(0, 1, 0);
        lines.vertex(matrix, x2, y1, z1).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x2, y1, z2).color(r, g, b, 1f).normal(0, 1, 0);
        lines.vertex(matrix, x2, y1, z2).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x1, y1, z2).color(r, g, b, 1f).normal(0, 1, 0);
        lines.vertex(matrix, x1, y1, z2).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x1, y1, z1).color(r, g, b, 1f).normal(0, 1, 0);
        lines.vertex(matrix, x1, y2, z1).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x2, y2, z1).color(r, g, b, 1f).normal(0, 1, 0);
        lines.vertex(matrix, x1, y1, z1).color(r, g, b, 1f).normal(0, 1, 0); lines.vertex(matrix, x1, y2, z1).color(r, g, b, 1f).normal(0, 1, 0);
    }
}
