package me.zero.simulatimod.mixin.noise;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimplexNoiseSampler.class)
public abstract class SimplexNoiseSamplerMixin {

    @Shadow @Final private static double SKEW_FACTOR_2D;

    @Shadow @Final private static double UNSKEW_FACTOR_2D;

    @Shadow protected abstract double grad(int hash, double x, double y, double z, double distance);

    @Shadow @Final private int[] permutation;

    private int map(long input) {
        return this.permutation[(int) (input & 255)];
    }

    // Cancellable injects
    @Inject(method = "sample(DD)D", at = @At("HEAD"), cancellable = true)
    private void simulatiMod$cancellableInjectSample(double x, double y, CallbackInfoReturnable<Double> cir) {
        // Stop vanilla simplex noise sampling
        if (!SimulatiMod.getConfig().noiseSamplers.simplexNoiseSampler.useVanillaNoiseSampler) {
            cir.cancel();

            double d = (x + y) * SKEW_FACTOR_2D;
            long i = MathHelper.lfloor(x + d);
            long j = MathHelper.lfloor(y + d);
            double e = (double)(i + j) * UNSKEW_FACTOR_2D;
            double f = (double)i - e;
            double g = (double)j - e;
            double h = x - f;
            double k = y - g;
            byte l;
            byte m;
            if (h > k) {
                l = 1;
                m = 0;
            } else {
                l = 0;
                m = 1;
            }

            double n = h - (double)l + UNSKEW_FACTOR_2D;
            double o = k - (double)m + UNSKEW_FACTOR_2D;
            double p = h - 1.0 + 2.0 * UNSKEW_FACTOR_2D;
            double q = k - 1.0 + 2.0 * UNSKEW_FACTOR_2D;
            int r = (int) (i & 255);
            int s = (int) (j & 255);
            int t = this.map(r + this.map(s)) % 12;
            int u = this.map(r + l + this.map(s + m)) % 12;
            int v = this.map(r + 1 + this.map(s + 1)) % 12;
            double w = this.grad(t, h, k, 0.0, 0.5);
            double z = this.grad(u, n, o, 0.0, 0.5);
            double aa = this.grad(v, p, q, 0.0, 0.5);
            cir.setReturnValue(70.0 * (w + z + aa));
        }
    }

    @Inject(method = "sample(DDD)D", at = @At("HEAD"), cancellable = true)
    public void simulatiMod$cancellableInjectSample(double x, double y, double z, CallbackInfoReturnable<Double> cir) {
        // Stop vanilla simplex noise sampling
        if (!SimulatiMod.getConfig().noiseSamplers.simplexNoiseSampler.useVanillaNoiseSampler) {
            cir.cancel();

            double e = (x + y + z) * 0.3333333333333333;
            long i = MathHelper.lfloor(x + e);
            int j = MathHelper.floor(y + e);
            long k = MathHelper.lfloor(z + e);
            double g = (double) (i + j + k) * 0.16666666666666666;
            double h = (double) i - g;
            double l = (double) j - g;
            double m = (double) k - g;
            double n = x - h;
            double o = y - l;
            double p = z - m;
            byte q;
            byte r;
            byte s;
            byte t;
            byte u;
            byte v;
            if (n >= o) {
                if (o >= p) {
                    q = 1;
                    r = 0;
                    s = 0;
                    t = 1;
                    u = 1;
                    v = 0;
                } else if (n >= p) {
                    q = 1;
                    r = 0;
                    s = 0;
                    t = 1;
                    u = 0;
                    v = 1;
                } else {
                    q = 0;
                    r = 0;
                    s = 1;
                    t = 1;
                    u = 0;
                    v = 1;
                }
            } else if (o < p) {
                q = 0;
                r = 0;
                s = 1;
                t = 0;
                u = 1;
                v = 1;
            } else if (n < p) {
                q = 0;
                r = 1;
                s = 0;
                t = 0;
                u = 1;
                v = 1;
            } else {
                q = 0;
                r = 1;
                s = 0;
                t = 1;
                u = 1;
                v = 0;
            }

            double w = n - (double) q + 0.16666666666666666;
            double aa = o - (double) r + 0.16666666666666666;
            double ab = p - (double) s + 0.16666666666666666;
            double ac = n - (double) t + 0.3333333333333333;
            double ad = o - (double) u + 0.3333333333333333;
            double ae = p - (double) v + 0.3333333333333333;
            double af = n - 1.0 + 0.5;
            double ag = o - 1.0 + 0.5;
            double ah = p - 1.0 + 0.5;
            int ai = (int) (i & 255);
            int aj = j & 255;
            int ak = (int) (k & 255);
            int al = this.map(ai + this.map(aj + this.map(ak))) % 12;
            int am = this.map(ai + q + this.map(aj + r + this.map(ak + s))) % 12;
            int an = this.map(ai + t + this.map(aj + u + this.map(ak + v))) % 12;
            int ao = this.map(ai + 1 + this.map(aj + 1 + this.map(ak + 1))) % 12;
            double ap = this.grad(al, n, o, p, 0.6);
            double aq = this.grad(am, w, aa, ab, 0.6);
            double ar = this.grad(an, ac, ad, ae, 0.6);
            double as = this.grad(ao, af, ag, ah, 0.6);
            cir.setReturnValue(32.0 * (ap + aq + ar + as));
        }
    }
}
