package net.apex.module.impl.Misc;

import net.apex.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Nuker extends Module {
    public Nuker() {
        super("Nuker", Category.MISC);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        
        BlockPos pPos = mc.player.getBlockPos();
        // Радиус 4 блока вокруг игрока
        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = pPos.add(x, y, z);
                    
                    // Ломаем всё, что не воздух и не бедрок
                    if (!mc.world.getBlockState(pos).isAir() && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                        mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        return; // Ломаем по 1 блоку за тик, чтобы не было лагов
                    }
                }
            }
        }
    }
}
