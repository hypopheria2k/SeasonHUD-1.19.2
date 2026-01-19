package com.worador.seasonhud.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.worador.seasonhud.config.ConfigHandler;
import com.worador.seasonhud.render.SeasonRenderer;
import com.worador.seasonhud.util.PositionEditor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiPositionEdit extends Screen {
    private final SeasonRenderer renderer = new SeasonRenderer();

    public GuiPositionEdit() {
        super(Component.literal("Move HUD"));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);

        // Rendert das HUD mit dem pulsierenden Schleier beim verschieben.
        renderer.renderMainHUD(poseStack, this.minecraft, true);

        drawCenteredString(poseStack, this.font, "Drag to Move - ESC to Save", this.width / 2, this.height - 20, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int newX = (int) mouseX;
        int newY = (int) mouseY;
        int snapMargin = 12;
        int edgePadding = 4; // <-- Puffer zum Rand, damit es nicht wie hingeklatsch aussieht =)

        // Aktuelle Größe des HUDs abrufen
        int hudW = PositionEditor.getWidth();
        int hudH = PositionEditor.getHeight();

        // --- Snap Logik X-Achse (Horizontal) ---
        // Linker Rand mit Puffer
        if (newX < snapMargin + edgePadding) {
            newX = edgePadding;
        }
        // Mitte (hier bleibt es exakt mittig, kein Puffer nötig)
        else if (Math.abs(newX - (this.width / 2 - hudW / 2)) < snapMargin) {
            newX = this.width / 2 - hudW / 2;
        }
        // Rechter Rand mit Puffer
        else if (newX > (this.width - hudW - snapMargin - edgePadding)) {
            newX = this.width - hudW - edgePadding;
        }

        // --- Snap Logik Y-Achse (Vertikal) ---
        // Oberer Rand mit Puffer
        if (newY < snapMargin + edgePadding) {
            newY = edgePadding;
        }
        // Mitte
        else if (Math.abs(newY - (this.height / 2 - hudH / 2)) < snapMargin) {
            newY = this.height / 2 - hudH / 2;
        }
        // Unterer Rand mit Puffer
        else if (newY > (this.height - hudH - snapMargin - edgePadding)) {
            newY = this.height - hudH - edgePadding;
        }

        ConfigHandler.X_OFFSET.set(newX);
        ConfigHandler.Y_OFFSET.set(newY);

        return true;
    }

    @Override
    public void onClose() {
        // Speichert die Position beim Verlassen dauerhaft, unglaublich wa ^^
        ConfigHandler.SPEC.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}