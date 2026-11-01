package net.apex.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RotationUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Random random = new Random();

    // Плавно поворачивает камеру к цели
    public static void smoothLook(Entity target, float speed) {
        if (mc.player == null || target == null) return;

        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
        Vec3d eyePos = mc.player.getEyePos();

        double dx = targetPos.x - eyePos.x;
        double dy = targetPos.y - eyePos.y;
        double dz = targetPos.z - eyePos.z;
        double dist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
        float targetPitch = (float) -Math.toDegrees(Math.atan2(dy, dist));

        // Вычисляем разницу
        float dYaw = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
        float dPitch = MathHelper.wrapDegrees(targetPitch - mc.player.getPitch());

        // Если разница слишком большая (телепорт за спину), поворачиваем быстрее
        if (Math.abs(dYaw) > 90 || Math.abs(dPitch) > 90) {
            speed *= 2.0f;
        }

        // Добавляем рандом (человеческий фактор)
        float randomSpeed = speed + (random.nextFloat() * 2f - 1f);

        // Применяем поворот
        float newYaw = mc.player.getYaw() + Math.signum(dYaw) * Math.min(Math.abs(dYaw), randomSpeed);
        float newPitch = mc.player.getPitch() + Math.signum(dPitch) * Math.min(Math.abs(dPitch), randomSpeed);

        // Ограничиваем pitch (чтобы не смотреть за макушку)
        newPitch = MathHelper.clamp(newPitch, -90.0f, 90.0f);

        mc.player.setYaw(newYaw);
        mc.player.setPitch(newPitch);
    }

    // Проверка, смотрим ли мы на цель (с допуском в градусах)
    public static boolean isLookingAt(Entity target, float tolerance) {
        if (mc.player == null || target == null) return false;
        
        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
        Vec3d eyePos = mc.player.getEyePos();
        
        double dx = targetPos.x - eyePos.x;
        double dy = targetPos.y - eyePos.y;
        double dz = targetPos.z - eyePos.z;
        double dist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
        float targetPitch = (float) -Math.toDegrees(Math.atan2(dy, dist));

        float dYaw = Math.abs(MathHelper.wrapDegrees(targetYaw - mc.player.getYaw()));
        float dPitch = Math.abs(targetPitch - mc.player.getPitch());

        return dYaw <= tolerance && dPitch <= tolerance;
    }
}
