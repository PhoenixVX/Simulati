package me.zero.simulatimod.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = -2.9999999E7))
	public double modifyConstantTickNegative(double original) {
		return Long.MIN_VALUE;
	}

	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 2.9999999E7))
	public double modifyConstantTickPositive(double original) {
		return Long.MAX_VALUE;
	}
}
