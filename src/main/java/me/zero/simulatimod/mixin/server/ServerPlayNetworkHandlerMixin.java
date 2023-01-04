package me.zero.simulatimod.mixin.server;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @ModifyConstant(method = "clampHorizontal", constant = @Constant(doubleValue = -3.0E7))
    private static double simulatiMod$modifyConstantClampHorizontalNegative (double original) {
        return Long.MIN_VALUE;
    }

    @ModifyConstant(method = "clampHorizontal", constant = @Constant(doubleValue = 3.0E7))
    private static double simulatiMod$modifyConstantClampHorizontalPositive (double original) {
        return Long.MAX_VALUE;
    }
}
