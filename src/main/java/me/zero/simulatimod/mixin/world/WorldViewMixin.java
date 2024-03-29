package me.zero.simulatimod.mixin.world;

import me.zero.simulatimod.SimulatiMod;

import net.minecraft.world.WorldView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldView.class)
public interface WorldViewMixin {
	@ModifyConstant(method = "getLightLevel(Lnet/minecraft/util/math/BlockPos;I)I", constant = @Constant(intValue = -30000000))
	private int modifyConstantGetLightLevelNegative(int original) {
		return SimulatiMod.VANILLA_NEG_HORIZONTAL_LIMIT;
	}

	@ModifyConstant(method = "getLightLevel(Lnet/minecraft/util/math/BlockPos;I)I", constant = @Constant(intValue = 30000000))
	private int modifyConstantGetLightLevelPositive(int original) {
		return SimulatiMod.VANILLA_POS_HORIZONTAL_LIMIT;
	}
}
