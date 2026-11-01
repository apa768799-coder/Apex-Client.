package net.apex.module.impl.Combat;
import net.apex.module.Module;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {
    public Surround() { super("Surround", Category.COMBAT); }
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos pos = mc.player.getBlockPos().offset(dir);
            if (mc.world.getBlockState(pos).isAir()) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                        mc.player.getInventory().selectedSlot = i;
                        BlockPos neighbor = pos.down();
                        if (!mc.world.getBlockState(neighbor).isAir()) {
                            BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(neighbor), Direction.UP, neighbor, false);
                            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                            return;
                        }
                    }
                }
            }
        }
    }
}
