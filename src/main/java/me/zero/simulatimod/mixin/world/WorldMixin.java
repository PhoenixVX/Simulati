package me.zero.simulatimod.mixin.world;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(World.class)
public class WorldMixin {
    @Shadow
    @Final
    @Mutable
    public static int HORIZONTAL_LIMIT = Integer.MAX_VALUE;

    @ModifyConstant(method = "*", constant = @Constant(intValue = -30000000))
    private static int simulatiMod$modifyConstantHorizontalLimitNegative(int original) {
        return SimulatiMod.VANILLA_NEG_HORIZONTAL_LIMIT;
    }

    @ModifyConstant(method = "*", constant = @Constant(intValue = 30000000))
    private static int simulatiMod$modifyConstantHorizontalLimitPositive(int original) {
        return SimulatiMod.VANILLA_POS_HORIZONTAL_LIMIT;
    }
}
