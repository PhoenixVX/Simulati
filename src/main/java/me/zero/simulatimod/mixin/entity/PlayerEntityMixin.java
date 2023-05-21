package me.zero.simulatimod.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = -2.9999999E7))
    public double simulatiMod$modifyConstantTickNegative(double original) {
        return Double.MIN_VALUE;
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 2.9999999E7))
    public double simulatiMod$modifyConstantTickPositive(double original) {
        return Double.MAX_VALUE;
    }
}
