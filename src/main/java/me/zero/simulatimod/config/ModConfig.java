package me.zero.simulatimod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.zero.simulatimod.SimulatiMod;

@Config(name = SimulatiMod.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public NoiseSamplers noiseSamplers = new NoiseSamplers();

    @ConfigEntry.Gui.CollapsibleObject
    public World world = new World();

    @ConfigEntry.Gui.CollapsibleObject
    public Commands commands = new Commands();

    @ConfigEntry.Gui.CollapsibleObject
    public Extras extras = new Extras();

    public static class NoiseSamplers {
        @ConfigEntry.Gui.Tooltip
        public boolean maintainPrecision = false;

        @ConfigEntry.Gui.CollapsibleObject
        public SimplexNoiseSampler simplexNoiseSampler = new SimplexNoiseSampler();

        @ConfigEntry.Gui.CollapsibleObject
        public PerlinNoiseSampler perlinNoiseSampler = new PerlinNoiseSampler();

        public static class SimplexNoiseSampler {
            public boolean useVanillaNoiseSampler = false;
        }

        public static class PerlinNoiseSampler {
            public boolean useVanillaNoiseSampler = false;
        }
    }

    public static class World {
        @ConfigEntry.Gui.CollapsibleObject
        public Generation generation = new Generation();

        public static class Generation {
            @ConfigEntry.Gui.Tooltip
            public boolean useVanillaAquiferSampler = true;
        }
    }

    public static class Commands {
        @ConfigEntry.Gui.CollapsibleObject
        public WorldBorderCommand worldBorderCommand = new WorldBorderCommand();

        public static class WorldBorderCommand {
            public double maximumCenterSize = Double.MAX_VALUE;
            public double maximumSize = Double.MAX_VALUE;
        }
    }

    public static class Extras {
        public boolean fixDoubleFloatCasts = true;
    }
}
