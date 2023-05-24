package me.zero.simulatimod.mixin.entity;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyConstant(method = "updatePosition", constant = @Constant(doubleValue = -3.0E7))
	private double modifyConstantUpdatePositionNegative(double original) {
		return Long.MIN_VALUE;
	}

	@ModifyConstant(method = "updatePosition", constant = @Constant(doubleValue = 3.0E7))
	private double modifyConstantUpdatePositionPositive(double original) {
		return Long.MAX_VALUE;
	}

	@ModifyConstant(method = "readNbt", constant = @Constant(doubleValue = -3.0000512E7))
	private double modifyReadNbtNegative(double original) {
		return Long.MIN_VALUE;
	}

	@ModifyConstant(method = "readNbt", constant = @Constant(doubleValue = 3.0000512E7))
	private double modifyReadNbtPositive(double original) {
		return Long.MAX_VALUE;
	}
}
