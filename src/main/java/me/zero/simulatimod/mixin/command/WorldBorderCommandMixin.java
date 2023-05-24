package me.zero.simulatimod.mixin.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import me.zero.simulatimod.SimulatiMod;

import net.minecraft.server.command.WorldBorderCommand;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldBorderCommand.class)
public class WorldBorderCommandMixin {
	@Shadow
	@Final
	@Mutable
	private static final SimpleCommandExceptionType SET_FAILED_BIG_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.big", SimulatiMod.getConfig().commands.worldBorderCommand.maximumSize));

	private static final SimpleCommandExceptionType SET_FAILED_FAR_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.far", Integer.MAX_VALUE - 16));

	@ModifyConstant(method = "register", constant = @Constant(doubleValue = -5.9999968E7))
	private static double modifyConstantRegisterNegative(double original) {
		return -SimulatiMod.getConfig().commands.worldBorderCommand.maximumSize;
	}

	@ModifyConstant(method = "register", constant = @Constant(doubleValue = 5.9999968E7))
	private static double modifyConstantRegisterPositive(double original) {
		return SimulatiMod.getConfig().commands.worldBorderCommand.maximumSize;
	}

	@ModifyConstant(method = "executeSet", constant = @Constant(doubleValue = 5.9999968E7))
	private static double modifyConstantExecuteSet(double original) {
		return SimulatiMod.getConfig().commands.worldBorderCommand.maximumSize;
	}

	@ModifyConstant(method = "executeCenter", constant = @Constant(doubleValue = 2.9999984E7))
	private static double modifyConstantExecuteCenter(double original) {
		return SimulatiMod.getConfig().commands.worldBorderCommand.maximumCenterSize;
	}
}
