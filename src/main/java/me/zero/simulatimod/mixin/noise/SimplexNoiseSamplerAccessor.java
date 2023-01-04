package me.zero.simulatimod.mixin.noise;

import net.minecraft.util.math.noise.SimplexNoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimplexNoiseSampler.class)
public interface SimplexNoiseSamplerAccessor {
    @Accessor("GRADIENTS")
    static int[][] getGradients() {
        throw new AssertionError(0);
    }

    @Invoker("dot")
    static double getDotProduct(int[] gradient, double x, double y, double z) {
        throw new AssertionError(0);
    }
}
