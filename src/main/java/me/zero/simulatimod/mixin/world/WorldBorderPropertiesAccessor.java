package me.zero.simulatimod.mixin.world;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldBorder.Properties.class)
public interface WorldBorderPropertiesAccessor {
    @Invoker("<init>")
    static WorldBorder.Properties newProperties(double centerX, double centerZ, double damagePerBlock, double safeZone, int warningBlocks, int warningTime, double size, long sizeLerpTime, double sizeLerpTarget) {
        throw new AssertionError(0);
    }
}
