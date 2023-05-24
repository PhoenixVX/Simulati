package me.zero.simulatimod.mixin.noise;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PerlinNoiseSampler.class)
public abstract class PerlinNoiseSamplerMixin {
    @Shadow
    @Final
    public double originX;

    @Shadow
    @Final
    public double originY;

    @Shadow
    @Final
    public double originZ;

    @Shadow
    @Final
    private byte[] permutation;

    @Shadow
    private static double grad(int hash, double x, double y, double z) {
        throw new AssertionError(0);
    }

    // Cancellable injects
    @Inject(method = "sample(DDDDD)D", at = @At("HEAD"), cancellable = true)
    public void cancellableInjectSample(double x, double y, double z, double yScale, double yMax, CallbackInfoReturnable<Double> cir) {
        // Stop vanilla perlin noise sampling
        if (!SimulatiMod.getConfig().noiseSamplers.perlinNoiseSampler.useVanillaNoiseSampler) {
            cir.cancel();

            double d = x + this.originX;
            double e = y + this.originY;
            double f = z + this.originZ;
            long i = MathHelper.lfloor(d);
            int j = MathHelper.floor(e);
            long k = MathHelper.lfloor(f);
            double g = d - (double) i;
            double h = e - (double) j;
            double l = f - (double) k;
            double n;
            if (yScale != 0.0) {
                double m;
                if (yMax >= 0.0 && yMax < h) {
                    m = yMax;
                } else {
                    m = h;
                }

                n = (double) MathHelper.lfloor(m / yScale + 1.0000000116860974E-7) * yScale;
            } else {
                n = 0.0;
            }

            cir.setReturnValue(this.sample(i, j, k, g, h - n, l, h));
        }
    }

    @Inject(method = "sampleDerivative(DDD[D)D", at = @At("HEAD"), cancellable = true)
    public void cancellableInjectSampleDerivative(double x, double y, double z, double[] ds, CallbackInfoReturnable<Double> cir) {
        // Stop vanilla perlin noise sampling for derivatives
        if (!SimulatiMod.getConfig().noiseSamplers.perlinNoiseSampler.useVanillaNoiseSampler) {
            cir.cancel();

            double d = x + this.originX;
            double e = y + this.originY;
            double f = z + this.originZ;
            long i = MathHelper.lfloor(d);
            int j = MathHelper.floor(e);
            long k = MathHelper.lfloor(f);
            double g = d - (double) i;
            double h = e - (double) j;
            double l = f - (double) k;
            cir.setReturnValue(this.sampleDerivative(i, j, k, g, h, l, ds));
        }
    }

    private int map(long input) {
        return this.permutation[(int) (input & 255)] & 255;
    }

    private double sample(long sectionX, int sectionY, long sectionZ, double localX, double localY, double localZ, double fadeLocalY) {
        int i = this.map(sectionX);
        int j = this.map(sectionX + 1);
        int k = this.map(i + sectionY);
        int l = this.map(i + sectionY + 1);
        int m = this.map(j + sectionY);
        int n = this.map(j + sectionY + 1);
        double d = grad(this.map(k + sectionZ), localX, localY, localZ);
        double e = grad(this.map(m + sectionZ), localX - 1.0, localY, localZ);
        double f = grad(this.map(l + sectionZ), localX, localY - 1.0, localZ);
        double g = grad(this.map(n + sectionZ), localX - 1.0, localY - 1.0, localZ);
        double h = grad(this.map(k + sectionZ + 1), localX, localY, localZ - 1.0);
        double o = grad(this.map(m + sectionZ + 1), localX - 1.0, localY, localZ - 1.0);
        double p = grad(this.map(l + sectionZ + 1), localX, localY - 1.0, localZ - 1.0);
        double q = grad(this.map(n + sectionZ + 1), localX - 1.0, localY - 1.0, localZ - 1.0);
        double r = MathHelper.perlinFade(localX);
        double s = MathHelper.perlinFade(fadeLocalY);
        double t = MathHelper.perlinFade(localZ);
        return MathHelper.lerp3(r, s, t, d, e, f, g, h, o, p, q);
    }

    private double sampleDerivative(long sectionX, int sectionY, long sectionZ, double localX, double localY, double localZ, double[] ds) {
        int i = this.map(sectionX);
        int j = this.map(sectionX + 1);
        int k = this.map(i + sectionY);
        int l = this.map(i + sectionY + 1);
        int m = this.map(j + sectionY);
        int n = this.map(j + sectionY + 1);
        int o = this.map(k + sectionZ);
        int p = this.map(m + sectionZ);
        int q = this.map(l + sectionZ);
        int r = this.map(n + sectionZ);
        int s = this.map(k + sectionZ + 1);
        int t = this.map(m + sectionZ + 1);
        int u = this.map(l + sectionZ + 1);
        int v = this.map(n + sectionZ + 1);
        int[] is = SimplexNoiseSamplerAccessor.getGradients()[o & 0xF];
        int[] js = SimplexNoiseSamplerAccessor.getGradients()[p & 0xF];
        int[] ks = SimplexNoiseSamplerAccessor.getGradients()[q & 0xF];
        int[] ls = SimplexNoiseSamplerAccessor.getGradients()[r & 0xF];
        int[] ms = SimplexNoiseSamplerAccessor.getGradients()[s & 0xF];
        int[] ns = SimplexNoiseSamplerAccessor.getGradients()[t & 0xF];
        int[] os = SimplexNoiseSamplerAccessor.getGradients()[u & 0xF];
        int[] ps = SimplexNoiseSamplerAccessor.getGradients()[v & 0xF];
        double d = SimplexNoiseSamplerAccessor.getDotProduct(is, localX, localY, localZ);
        double e = SimplexNoiseSamplerAccessor.getDotProduct(js, localX - 1.0, localY, localZ);
        double f = SimplexNoiseSamplerAccessor.getDotProduct(ks, localX, localY - 1.0, localZ);
        double g = SimplexNoiseSamplerAccessor.getDotProduct(ls, localX - 1.0, localY - 1.0, localZ);
        double h = SimplexNoiseSamplerAccessor.getDotProduct(ms, localX, localY, localZ - 1.0);
        double w = SimplexNoiseSamplerAccessor.getDotProduct(ns, localX - 1.0, localY, localZ - 1.0);
        double x = SimplexNoiseSamplerAccessor.getDotProduct(os, localX, localY - 1.0, localZ - 1.0);
        double y = SimplexNoiseSamplerAccessor.getDotProduct(ps, localX - 1.0, localY - 1.0, localZ - 1.0);
        double z = MathHelper.perlinFade(localX);
        double aa = MathHelper.perlinFade(localY);
        double ab = MathHelper.perlinFade(localZ);
        double ac = MathHelper.lerp3(z, aa, ab, is[0], js[0], ks[0], ls[0], ms[0], ns[0], os[0], ps[0]);
        double ad = MathHelper.lerp3(z, aa, ab, is[1], js[1], ks[1], ls[1], ms[1], ns[1], os[1], ps[1]);
        double ae = MathHelper.lerp3(z, aa, ab, is[2], js[2], ks[2], ls[2], ms[2], ns[2], os[2], ps[2]);
        double af = MathHelper.lerp2(aa, ab, e - d, g - f, w - h, y - x);
        double ag = MathHelper.lerp2(ab, z, f - d, x - h, g - e, y - w);
        double ah = MathHelper.lerp2(z, aa, h - d, w - e, x - f, y - g);
        double ai = MathHelper.perlinFadeDerivative(localX);
        double aj = MathHelper.perlinFadeDerivative(localY);
        double ak = MathHelper.perlinFadeDerivative(localZ);
        double al = ac + ai * af;
        double am = ad + aj * ag;
        double an = ae + ak * ah;
        ds[0] += al;
        ds[1] += am;
        ds[2] += an;
        return MathHelper.lerp3(z, aa, ab, d, e, f, g, h, w, x, y);
    }
}
