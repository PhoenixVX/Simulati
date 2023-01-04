package me.zero.simulatimod.mixin.noise;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OctavePerlinNoiseSampler.class)
public class OctavePerlinNoiseSamplerMixin {
    @Inject(method = "maintainPrecision", at = @At("HEAD"), cancellable = true)
    private static void simulatiMod$cancellableInjectMaintainPrecision(double value, CallbackInfoReturnable<Double> cir) {
        if (!SimulatiMod.getConfig().noiseSamplers.maintainPrecision) {
            cir.setReturnValue(value);
        }
    }
}
