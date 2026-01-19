package com.worador.seasonhud.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.worador.seasonhud.config.ConfigHandler;
import com.worador.seasonhud.util.PositionEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SeasonRenderer {

    private static final String NS = "seasonhud";
    private static final ResourceLocation THERMO = new ResourceLocation(NS, "textures/gui/thermometer.png");
    private static final ResourceLocation DROP = new ResourceLocation(NS, "textures/gui/droplet.png");
    private static final ResourceLocation CAL = new ResourceLocation(NS, "textures/gui/calendar.png");
    private static final ResourceLocation SUN = new ResourceLocation(NS, "textures/gui/summer.png");

    // Lokale Assets im eigenen Namespace (assets/seasonhud/textures/gui/)
    private static final ResourceLocation POPPY = new ResourceLocation(NS, "textures/gui/poppy.png");
    private static final ResourceLocation DEAD_BUSH = new ResourceLocation(NS, "textures/gui/dead_bush.png");

    @SubscribeEvent
    public void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.CHAT_PANEL.id())) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null || mc.options.hideGui) return;
            renderMainHUD(event.getPoseStack(), mc, false);
        }
    }

    public void renderHudPreview(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) renderMainHUD(poseStack, mc, true);
    }

    public void renderMainHUD(PoseStack poseStack, Minecraft mc, boolean isPreview) {
        ISeasonState state = SeasonHelper.getSeasonState(mc.level);
        Font font = mc.font;
        BlockPos pos = mc.player != null ? mc.player.blockPosition() : mc.level.getSharedSpawnPos();
        Biome biome = mc.level.getBiome(pos).value();

        // 1. Saison Logik
        boolean isTropical = SeasonHelper.usesTropicalSeasons(mc.level.getBiome(pos));
        String seasonTitle;
        ResourceLocation mainIcon;

        if (isTropical) {
            boolean isWet = state.getSeason() == Season.AUTUMN || state.getSeason() == Season.WINTER;
            seasonTitle = isWet ? "Wet Season" : "Dry Season";
            mainIcon = isWet ? DROP : SUN;
        } else {
            seasonTitle = formatSubSeasonPrefix(state.getSubSeason()) + " " + state.getSeason().toString();
            mainIcon = getSeasonIcon(state.getSeason());
        }

        // 2. Zeitberechnung (Day Line)
        int ticksInSub = state.getSeasonCycleTicks() % state.getSubSeasonDuration();
        int remainingTicks = state.getSubSeasonDuration() - ticksInSub;
        float progress = (float) ticksInSub / (float) state.getSubSeasonDuration();

        int currentDay = (ticksInSub / 24000) + 1;
        int daysLeft = (remainingTicks / 24000);
        String dayLine = String.format("Day %d (%.1f%%) - %d Days left", currentDay, progress * 100, daysLeft);

        // 3. Daten & Info Text
        String tempText = String.format("%.1f°C", biome.getBaseTemperature() * 20);
        String humText = String.format("%.0f%%", biome.getModifiedClimateSettings().downfall() * 100);
        boolean isFertile = isTropical || state.getSeason() != Season.WINTER;
        String infoLine = (isFertile ? "Fertile" : "Infertility") + " | " + (mc.level.isRaining() ? "Rain" : "Clear");

        // 4. Layout Parameter
        int x = ConfigHandler.X_OFFSET.get();
        int y = ConfigHandler.Y_OFFSET.get();
        float scale = ConfigHandler.SCALE.get().floatValue();
        int seasonColor = getSeasonColor(state.getSeason());
        int infoColor = (0xFF << 24) | (ConfigHandler.INFO_R.get() << 16) | (ConfigHandler.INFO_G.get() << 8) | (ConfigHandler.INFO_B.get());

        int lineH = 11;
        int barWidth = 2;
        int gap = 6;
        int contentX = x + barWidth + gap;

        // Box Breite Berechnung (Berücksichtigt die kleine Skalierung der InfoLine)
        int boxWidth = Math.max(font.width(seasonTitle), Math.max(font.width(dayLine), (int)(font.width(infoLine) * 0.7F))) + 16 + barWidth;
        int boxHeight = (lineH * 4) - 2;

        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        // Hintergrund Progress Bar
        int barBottom = y + boxHeight;
        int fillHeight = (int) (boxHeight * progress);
        GuiComponent.fill(poseStack, x, y, x + barWidth, barBottom, 0x44000000);
        GuiComponent.fill(poseStack, x, barBottom - fillHeight, x + barWidth, barBottom, seasonColor | 0xFF000000);

        // Zeile 1: Saison
        draw8x8(poseStack, mainIcon, contentX, y);
        font.drawShadow(poseStack, Component.literal(seasonTitle), contentX + 12, y, seasonColor);

        // Zeile 2: Zeit
        draw8x8(poseStack, CAL, contentX, y + lineH);
        font.drawShadow(poseStack, Component.literal(dayLine), contentX + 12, y + lineH, 0xE0E0E0);

        // Zeile 3: Klima
        draw8x8(poseStack, THERMO, contentX, y + (lineH * 2));
        font.drawShadow(poseStack, Component.literal(tempText), contentX + 12, y + (lineH * 2), 0xE0E0E0);
        int humX = contentX + 12 + font.width(tempText) + 8;
        draw8x8(poseStack, DROP, humX, y + (lineH * 2));
        font.drawShadow(poseStack, Component.literal(humText), humX + 12, y + (lineH * 2), 0xE0E0E0);

        // ZEILE 4: Deutlich kleiner (70%) und etwas nach oben gerückt für kompakte Optik
        poseStack.pushPose();
        float subScale = 0.7F;
        poseStack.scale(subScale, subScale, 1.0F);

        // Koordinaten-Korrektur für 0.7 Scale
        int subX = (int) (contentX / subScale);
        int subY = (int) ((y + (lineH * 3) - 1) / subScale);

        ResourceLocation infoIcon = isFertile ? POPPY : DEAD_BUSH;
        draw8x8(poseStack, infoIcon, subX, subY);
        font.drawShadow(poseStack, Component.literal(infoLine), subX + 12, subY, infoColor);

        poseStack.popPose();

        if (isPreview) {
            float pulse = (float) (Math.sin(System.currentTimeMillis() / 400.0) * 0.5 + 0.5);
            int color = ((int) (30 + (pulse * 50)) << 24) | 0xFFFFFF;
            GuiComponent.fill(poseStack, x - 5, y - 5, x + boxWidth + 5, y + boxHeight + 5, color);
        }

        RenderSystem.disableBlend();
        poseStack.popPose();

        PositionEditor.updateHudDimensions((int)((boxWidth + 10) * scale), (int)((boxHeight + 10) * scale));
    }

    private void draw8x8(PoseStack poseStack, ResourceLocation loc, int x, int y) {
        RenderSystem.setShaderTexture(0, loc);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // Da es nun lokale GUI-Texturen sind, nutzen wir wieder die Standard 8x8 Parameter
        GuiComponent.blit(poseStack, x, y, 0, 0, 8, 8, 8, 8);
    }

    private ResourceLocation getSeasonIcon(Season season) {
        return new ResourceLocation(NS, "textures/gui/" + season.toString().toLowerCase() + ".png");
    }

    private String formatSubSeasonPrefix(Season.SubSeason sub) {
        String n = sub.toString();
        if (n.contains("EARLY")) return "Early";
        if (n.contains("MID")) return "Mid";
        if (n.contains("LATE")) return "Late";
        return "";
    }

    private int getSeasonColor(Season season) {
        return switch (season) {
            case SPRING -> ((ConfigHandler.SPRING_R.get() & 0xFF) << 16) | ((ConfigHandler.SPRING_G.get() & 0xFF) << 8) | (ConfigHandler.SPRING_B.get() & 0xFF);
            case SUMMER -> ((ConfigHandler.SUMMER_R.get() & 0xFF) << 16) | ((ConfigHandler.SUMMER_G.get() & 0xFF) << 8) | (ConfigHandler.SUMMER_B.get() & 0xFF);
            case AUTUMN -> ((ConfigHandler.AUTUMN_R.get() & 0xFF) << 16) | ((ConfigHandler.AUTUMN_G.get() & 0xFF) << 8) | (ConfigHandler.AUTUMN_B.get() & 0xFF);
            case WINTER -> ((ConfigHandler.WINTER_R.get() & 0xFF) << 16) | ((ConfigHandler.WINTER_G.get() & 0xFF) << 8) | (ConfigHandler.WINTER_B.get() & 0xFF);
        };
    }
}