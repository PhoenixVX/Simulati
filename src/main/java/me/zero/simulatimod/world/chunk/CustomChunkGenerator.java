package me.zero.simulatimod.world.chunk;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.*;
public class CustomChunkGenerator extends NoiseChunkGenerator {
    public CustomChunkGenerator(BiomeSource biomeSource, RegistryEntry<ChunkGeneratorSettings> settings) {
        super(biomeSource, settings);
    }
}
