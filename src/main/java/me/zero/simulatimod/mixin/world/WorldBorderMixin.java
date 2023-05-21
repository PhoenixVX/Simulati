package me.zero.simulatimod.mixin.world;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {
    @Shadow
    @Final
    @Mutable
    public static final double STATIC_AREA_SIZE = Long.MAX_VALUE;

    @Shadow
    @Final
    @Mutable
    public static final double MAX_CENTER_COORDINATES = Integer.MAX_VALUE - 16;

    @Shadow
    @Final
    @Mutable
    public static final WorldBorder.Properties DEFAULT_BORDER = WorldBorderPropertiesAccessor.newProperties(0.0, 0.0, 0.2, 5.0, 5, 15, Long.MAX_VALUE, 0L, 0.0);

    @Shadow
    int maxRadius = Integer.MAX_VALUE;

    @Shadow
    private WorldBorder.Area area = ((WorldBorder) (Object) this).new StaticArea(Long.MAX_VALUE);
}
