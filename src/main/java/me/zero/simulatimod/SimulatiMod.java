package me.zero.simulatimod;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import me.zero.simulatimod.config.ModConfig;

public class SimulatiMod implements ModInitializer {
	public static final String MOD_ID = "simulati";
	public static final int VANILLA_POS_HORIZONTAL_LIMIT = Integer.MAX_VALUE;
	public static final int VANILLA_NEG_HORIZONTAL_LIMIT = Integer.MIN_VALUE;

	public static ModConfig getConfig() {
		try {
			return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		} catch (RuntimeException ex) {
			AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
			return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		}
	}

	@Override
	public void onInitialize() {
	}
}
