package me.zero.simulatimod.mixin.world.chunk;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

// TODO: Fix aquifer sampling and revert the config
@Mixin(targets = "net/minecraft/world/gen/chunk/AquiferSampler$Impl")
public abstract class AquiferSamplerImplMixin {
    @Shadow
    @Final
    private static double NEEDS_FLUID_TICK_DISTANCE_THRESHOLD;
    private final Map<BlockPos, AquiferSampler.FluidLevel> simulatiMod$waterLevels = new HashMap<>();
    @Shadow
    private boolean needsFluidTick;
    @Shadow
    @Final
    private AquiferSampler.FluidLevelSampler fluidLevelSampler;
    @Shadow
    @Final
    private RandomSplitter randomDeriver;

    @Shadow
    private static double maxDistance(int i, int a) {
        throw new AssertionError(0);
    }

    @Shadow
    protected abstract int getLocalX(int x);

    @Shadow
    protected abstract int getLocalY(int y);

    @Shadow
    protected abstract int getLocalZ(int z);

    @Shadow
    protected abstract AquiferSampler.FluidLevel getFluidLevel(int blockX, int blockY, int blockZ);

    @Shadow
    protected abstract double calculateDensity(DensityFunction.NoisePos pos, MutableDouble mutableDouble, AquiferSampler.FluidLevel fluidLevel, AquiferSampler.FluidLevel fluidLevel2);

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    public void simulatiMod$cancellableInjectApply(DensityFunction.NoisePos pos, double density, CallbackInfoReturnable<BlockState> cir) {
        if (!SimulatiMod.getConfig().world.generation.useVanillaAquiferSampler) {
            cir.cancel();

            int blockX = pos.blockX();
            int blockY = pos.blockY();
            int blockZ = pos.blockZ();
            if (density > 0.0) {
                this.needsFluidTick = false;
                cir.setReturnValue(null);
            }
            AquiferSampler.FluidLevel fluidLevel = this.fluidLevelSampler.getFluidLevel(blockX, blockY, blockZ);
            if (fluidLevel.getBlockState(blockY).isOf(Blocks.LAVA)) {
                this.needsFluidTick = false;
                cir.setReturnValue(Blocks.LAVA.getDefaultState());
            }
            int localX = this.getLocalX(blockX - 5);
            int localY = this.getLocalY(blockY + 1);
            int localZ = this.getLocalZ(blockZ - 5);
            int o = Integer.MAX_VALUE;
            int p = Integer.MAX_VALUE;
            int q = Integer.MAX_VALUE;
            BlockPos r = null;
            BlockPos s = null;
            BlockPos t = null;
            for (int x = 0; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = 0; z <= 1; ++z) {
                        int posX = localX + x;
                        int posY = localY + y;
                        int posZ = localZ + z;

                        BlockPos ab = new BlockPos(posX, posY, posZ);
                        BlockPos ac;
                        if (!ab.equals(BlockPos.ORIGIN)) {
                            ac = ab;
                        } else {
                            Random random = this.randomDeriver.split(posX, posY, posZ);
                            ac = new BlockPos(posX * 16 + random.nextInt(10), posY * 12 + random.nextInt(9), posZ * 16 + random.nextInt(10));
                        }
                        int deltaX = ac.getX() - blockX;
                        int deltaY = ac.getY() - blockY;
                        int deltaZ = ac.getZ() - blockZ;
                        int deltaSqr = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                        if (o >= deltaSqr) {
                            t = s;
                            s = r;
                            r = ac;
                            q = p;
                            p = o;
                            o = deltaSqr;
                            continue;
                        }
                        if (p >= deltaSqr) {
                            t = s;
                            s = ac;
                            q = p;
                            p = deltaSqr;
                            continue;
                        }
                        if (q < deltaSqr) continue;
                        t = ac;
                        q = deltaSqr;
                    }
                }
            }

            AquiferSampler.FluidLevel fluidLevel2 = this.simulatiMod$getWaterLevel(r);
            double calculatedFluidTickDistance = maxDistance(o, p);
            BlockState blockState = fluidLevel2.getBlockState(blockY);
            if (calculatedFluidTickDistance <= 0.0) {
                this.needsFluidTick = calculatedFluidTickDistance >= NEEDS_FLUID_TICK_DISTANCE_THRESHOLD;
                cir.setReturnValue(blockState);
            }
            if (blockState.isOf(Blocks.WATER) && this.fluidLevelSampler.getFluidLevel(blockX, blockY - 1, blockZ).getBlockState(blockY - 1).isOf(Blocks.LAVA)) {
                this.needsFluidTick = true;
                cir.setReturnValue(blockState);
            }
            MutableDouble mutableDouble = new MutableDouble(Double.NaN);
            AquiferSampler.FluidLevel fluidLevel3 = this.simulatiMod$getWaterLevel(s);
            double e = calculatedFluidTickDistance * this.calculateDensity(pos, mutableDouble, fluidLevel2, fluidLevel3);
            if (density + e > 0.0) {
                this.needsFluidTick = false;
                cir.setReturnValue(null);
            }
            AquiferSampler.FluidLevel fluidLevel4 = this.simulatiMod$getWaterLevel(t);
            double f = maxDistance(o, q);
            if (f > 0.0 && density + calculatedFluidTickDistance * f * this.calculateDensity(pos, mutableDouble, fluidLevel2, fluidLevel4) > 0.0) {
                this.needsFluidTick = false;
                cir.setReturnValue(null);
            }
            double g2 = maxDistance(p, q);
            if (g2 > 0.0 && density + calculatedFluidTickDistance * g2 * this.calculateDensity(pos, mutableDouble, fluidLevel3, fluidLevel4) > 0.0) {
                this.needsFluidTick = false;
                cir.setReturnValue(null);
            }
            this.needsFluidTick = true;
            cir.setReturnValue(blockState);
        }
    }

    // Modified getWaterLevel to not use packing (which causes issues at high distances)
    private AquiferSampler.FluidLevel simulatiMod$getWaterLevel(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int localX = this.getLocalX(x);
        int localY = this.getLocalY(y);
        int localZ = this.getLocalZ(z);
        BlockPos localBlockPos = new BlockPos(localX, localY, localZ);
        AquiferSampler.FluidLevel cachedFluidLevel = this.simulatiMod$waterLevels.get(localBlockPos);
        if (cachedFluidLevel != null) {
            return cachedFluidLevel;
        }
        AquiferSampler.FluidLevel calculatedFluidLevel = this.getFluidLevel(x, y, z);
        this.simulatiMod$waterLevels.put(localBlockPos, calculatedFluidLevel);
        return calculatedFluidLevel;
    }
}
