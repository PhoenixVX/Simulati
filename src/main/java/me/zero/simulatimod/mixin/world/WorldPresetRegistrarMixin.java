package me.zero.simulatimod.mixin.world;

import me.zero.simulatimod.SimulatiMod;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/world/gen/WorldPresets$Registrar")
public class WorldPresetRegistrarMixin {
    private static final RegistryKey<WorldPreset> CUSTOMIZED_WORLD_TYPE = RegistryKey.of(RegistryKeys.WORLD_PRESET, new Identifier(SimulatiMod.MOD_ID, "customized_world_type"));

    @Shadow
    private void register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions) {}

    @Shadow
    private DimensionOptions createOverworldOptions(ChunkGenerator chunkGenerator) {
        return null;
    }

    @Shadow @Final private RegistryEntryLookup<Biome> biomeLookup;

    @Shadow @Final private RegistryEntryLookup<StructureSet> structureSetLookup;

    @Shadow @Final private RegistryEntryLookup<PlacedFeature> featureLookup;

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private void addPresets(CallbackInfo ci) {
        this.register(CUSTOMIZED_WORLD_TYPE, this.createOverworldOptions(new FlatChunkGenerator(FlatChunkGeneratorConfig.getDefaultConfig(this.biomeLookup, this.structureSetLookup, this.featureLookup))));
    }
}
