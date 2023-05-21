package me.zero.simulatimod.mixin.world;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderView.class)
public interface BlockRenderViewMixin extends BlockView {
    @Inject(method = "getLightLevel", at = @At("HEAD"), cancellable = true)
    default void simulatiMod$getLightLevelCancellableInject(LightType type, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (SimulatiMod.getConfig().extras.enableFullBright) {
            cir.setReturnValue(15);
        }
    }

    @Inject(method = "getBaseLightLevel", at = @At("HEAD"), cancellable = true)
    default void simulatiMod$getBaseLightLevelCancellableInject(BlockPos pos, int ambientDarkness, CallbackInfoReturnable<Integer> cir) {
        if (SimulatiMod.getConfig().extras.enableFullBright) {
            cir.setReturnValue(15);
        }
    }
}
