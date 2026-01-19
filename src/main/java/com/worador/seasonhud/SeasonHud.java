package com.worador.seasonhud;

import com.mojang.logging.LogUtils;
import com.worador.seasonhud.config.ConfigHandler;
import com.worador.seasonhud.render.SeasonRenderer;
import com.worador.seasonhud.util.KeyBindings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("seasonhud")
public class SeasonHud {
    public static final String MOD_ID = "seasonhud";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SeasonHud() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Season HUD initializing...");

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.SPEC);
        modEventBus.register(KeyBindings.class);

        // Registriert den Renderer am Forge-Bus für das HUD-Overlay, echt jetzt! Krass oder? :P
        MinecraftForge.EVENT_BUS.register(new SeasonRenderer());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}