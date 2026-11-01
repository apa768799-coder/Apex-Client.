package net.apex.module.impl.Misc;

import net.apex.module.Module;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoFarm extends Module {
    private int cooldown = 0;
    public AutoFarm() { super("AutoFarm", Category.MISC); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (cooldown > 0) { cooldown--; return; }

        BlockPos pPos = mc.player.getBlockPos();
        for (BlockPos pos : BlockPos.iterateOutwards(pPos, 4, 4, 4)) {
            var state = mc.world.getBlockState(pos);
            
            // Ломаем созревший урожай
            if (state.getBlock() instanceof CropBlock crop && crop.isMature(state)) {
                mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                mc.player.swingHand(Hand.MAIN_HAND);
                cooldown = 5;
                return;
            }

            // Сажаем новые семена
            if (mc.world.getBlockState(pos.down()).getBlock() instanceof FarmlandBlock && state.isAir()) {
                for (int i = 0; i < 9; i++) {
                    var item = mc.player.getInventory().getStack(i).getItem();
                    if (item == Items.CARROT || item == Items.POTATO || item == Items.WHEAT_SEEDS || item == Items.BEETROOT_SEEDS) {
                        mc.player.getInventory().selectedSlot = i;
                        BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(pos.down()), Direction.UP, pos.down(), false);
                        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        cooldown = 5;
                        return;
                    }
                }
            }
        }
    }
}
