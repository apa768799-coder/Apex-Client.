package net.apex.module.impl.Movement;
import net.apex.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
    public Scaffold() { super("Scaffold", Category.MOVEMENT); }
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.player.isOnGround()) return;
        BlockPos below = mc.player.getBlockPos().down();
        if (mc.world.getBlockState(below).isAir()) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                    mc.player.getInventory().selectedSlot = i;
                    BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(below.down()), Direction.UP, below.down(), false);
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    return;
                }
            }
        }
    }
}
