package me.zero.simulatimod.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    @Final
    @Mutable
    public static int MAX_WORLD_BORDER_RADIUS = Integer.MAX_VALUE - 16;

    @ModifyConstant(method = "getMaxWorldBorderRadius", constant = @Constant(intValue = 29999984))
    public int simulatiMod$modifyConstantGetMaxWorldBorderRadius (int original) {
        return MAX_WORLD_BORDER_RADIUS;
    }
}
