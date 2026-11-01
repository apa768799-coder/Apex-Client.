package net.apex.module.impl.Misc;
import net.apex.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Nuker extends Module {
    public Nuker() { super("Nuker", Category.MISC); }
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        BlockPos pPos = mc.player.getBlockPos();
        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = pPos.add(x, y, z);
                    if (!mc.world.getBlockState(pos).isAir() && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                        mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                        mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                        return;
                    }
                }
            }
        }
    }
}
