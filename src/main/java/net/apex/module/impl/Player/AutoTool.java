package net.apex.module.impl.Player;

import net.apex.module.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoTool extends Module {
    public AutoTool() { super("AutoTool", Category.PLAYER); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        
        // Если мы целимся в блок
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hit = (BlockHitResult) mc.crosshairTarget;
            var state = mc.world.getBlockState(hit.getBlockPos());
            
            int bestSlot = -1;
            float bestSpeed = 1.0f;

            // Ищем самый быстрый инструмент для этого блока в хотбаре
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.isEmpty()) continue;
                
                float speed = stack.getMiningSpeedMultiplier(state);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }

            // Переключаемся, если нашли инструмент лучше
            if (bestSlot != -1 && mc.player.getInventory().selectedSlot != bestSlot) {
                mc.player.getInventory().selectedSlot = bestSlot;
            }
        }
    }
}
