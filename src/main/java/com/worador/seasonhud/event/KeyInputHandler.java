package com.worador.seasonhud.event;

import com.worador.seasonhud.gui.GuiColorConfig;
import com.worador.seasonhud.util.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.worador.seasonhud.SeasonHud;

@Mod.EventBusSubscriber(modid = SeasonHud.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (KeyBindings.OPEN_SETTINGS.consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.screen == null) {
                    // Öffnet jetzt das Hauptmenü mit den Slidern
                    mc.setScreen(new GuiColorConfig());
                }
            }
        }
    }
}