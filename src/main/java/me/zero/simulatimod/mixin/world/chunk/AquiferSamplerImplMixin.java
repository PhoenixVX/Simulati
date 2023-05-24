package me.zero.simulatimod.mixin.world.chunk;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AquiferSampler.Impl.class)
public abstract class AquiferSamplerImplMixin {
    @Shadow
    @Final
    private int startX;

    @Shadow
    @Final
    private int startY;

    @Shadow
    @Final
    private int startZ;

    @Shadow
    @Final
    private int sizeX;

    @Shadow
    @Final
    private int sizeZ;

    @Shadow
    @Final
    private AquiferSampler.Impl.FluidLevel[] waterLevels;

    // Fill aquifers with water to prevent a crash
    @Inject(method = "index", at = @At("HEAD"), cancellable = true)
    private void cancellableInjectIndex(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (!SimulatiMod.getConfig().world.generation.useVanillaAquiferSampler) {
            int localX = x - this.startX;
            int localY = y - this.startY;
            int localZ = z - this.startZ;
            int index = (localY * this.sizeZ + localZ) * this.sizeX + localX;
            cir.setReturnValue(MathHelper.clamp(index, 0, this.waterLevels.length - 1));
        }
    }
}
