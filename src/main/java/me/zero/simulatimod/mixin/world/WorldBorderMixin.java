package me.zero.simulatimod.mixin.world;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {
    @Shadow
    int maxRadius = Integer.MAX_VALUE;

    @Shadow
    @Final
    @Mutable
    public static final double STATIC_AREA_SIZE = Double.MAX_VALUE;

    @Shadow
    @Final
    @Mutable
    public static final double MAX_CENTER_COORDINATES = Integer.MAX_VALUE - 16;

    @Shadow
    private WorldBorder.Area area = ((WorldBorder) (Object) this).new StaticArea(Double.MAX_VALUE);

    @Shadow
    @Final
    @Mutable
    public static final WorldBorder.Properties DEFAULT_BORDER = WorldBorderPropertiesAccessor.newProperties(0.0, 0.0, 0.2, 5.0, 5, 15, Double.MAX_VALUE, 0L, 0.0);
}
