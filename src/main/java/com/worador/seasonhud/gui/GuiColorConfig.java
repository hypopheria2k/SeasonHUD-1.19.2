package com.worador.seasonhud.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.worador.seasonhud.config.ConfigHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class GuiColorConfig extends Screen {

    public GuiColorConfig() {
        super(Component.literal("Season HUD Settings"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 35;
        int colL = centerX - 160;
        int colR = centerX + 10;

        addRGBRow("Spring", colL, y, ConfigHandler.SPRING_R, ConfigHandler.SPRING_G, ConfigHandler.SPRING_B);
        addRGBRow("Summer", colR, y, ConfigHandler.SUMMER_R, ConfigHandler.SUMMER_G, ConfigHandler.SUMMER_B);
        y += 50;
        addRGBRow("Autumn", colL, y, ConfigHandler.AUTUMN_R, ConfigHandler.AUTUMN_G, ConfigHandler.AUTUMN_B);
        addRGBRow("Winter", colR, y, ConfigHandler.WINTER_R, ConfigHandler.WINTER_G, ConfigHandler.WINTER_B);
        y += 50;
        // Neue Zeile für Info
        addRGBRow("Info Line", centerX - 75, y, ConfigHandler.INFO_R, ConfigHandler.INFO_G, ConfigHandler.INFO_B);

        y += 45;
        this.addRenderableWidget(new Button(centerX - 75, y, 150, 20, Component.literal("Reset Colors"), (btn) -> {
            ConfigHandler.SPRING_R.set(85);  ConfigHandler.SPRING_G.set(255); ConfigHandler.SPRING_B.set(85);
            ConfigHandler.SUMMER_R.set(255); ConfigHandler.SUMMER_G.set(255); ConfigHandler.SUMMER_B.set(85);
            ConfigHandler.AUTUMN_R.set(255); ConfigHandler.AUTUMN_G.set(170); ConfigHandler.AUTUMN_B.set(0);
            ConfigHandler.WINTER_R.set(85);  ConfigHandler.WINTER_G.set(255); ConfigHandler.WINTER_B.set(255);
            ConfigHandler.INFO_R.set(255);   ConfigHandler.INFO_G.set(255);   ConfigHandler.INFO_B.set(255);
            this.minecraft.setScreen(new GuiColorConfig());
        }));

        y += 25;
        this.addRenderableWidget(new Button(centerX - 160, y, 150, 20, Component.literal("Move HUD"), (btn) -> {
            this.minecraft.setScreen(new GuiPositionEdit());
        }));

        this.addRenderableWidget(new ForgeSlider(centerX + 10, y, 150, 20, Component.literal("Scale: "), Component.literal("x"), 0.5D, 3.0D, ConfigHandler.SCALE.get(), 0.1D, 1, true) {
            @Override protected void applyValue() { ConfigHandler.SCALE.set(this.getValue()); }
        });

        this.addRenderableWidget(new Button(centerX - 75, this.height - 30, 150, 20, Component.literal("Save & Quit"), (btn) -> {
            ConfigHandler.SPEC.save();
            this.onClose();
        }));
    }

    private void addRGBRow(String label, int x, int y, net.minecraftforge.common.ForgeConfigSpec.IntValue r, net.minecraftforge.common.ForgeConfigSpec.IntValue g, net.minecraftforge.common.ForgeConfigSpec.IntValue b) {
        int sW = 48;
        this.addRenderableWidget(new ForgeSlider(x, y + 12, sW, 20, Component.empty(), Component.empty(), 0, 255, r.get(), 1, 0, true) {
            @Override protected void applyValue() { r.set((int)this.getValue()); }
        });
        this.addRenderableWidget(new ForgeSlider(x + 51, y + 12, sW, 20, Component.empty(), Component.empty(), 0, 255, g.get(), 1, 0, true) {
            @Override protected void applyValue() { g.set((int)this.getValue()); }
        });
        this.addRenderableWidget(new ForgeSlider(x + 102, y + 12, sW, 20, Component.empty(), Component.empty(), 0, 255, b.get(), 1, 0, true) {
            @Override protected void applyValue() { b.set((int)this.getValue()); }
        });
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 8, 0xFFFFFF);
        int c = this.width / 2;

        this.font.draw(poseStack, "Spring", c - 160, 37, (0xFF << 24) | (ConfigHandler.SPRING_R.get() << 16) | (ConfigHandler.SPRING_G.get() << 8) | ConfigHandler.SPRING_B.get());
        this.font.draw(poseStack, "Summer", c + 10, 37, (0xFF << 24) | (ConfigHandler.SUMMER_R.get() << 16) | (ConfigHandler.SUMMER_G.get() << 8) | ConfigHandler.SUMMER_B.get());
        this.font.draw(poseStack, "Autumn", c - 160, 87, (0xFF << 24) | (ConfigHandler.AUTUMN_R.get() << 16) | (ConfigHandler.AUTUMN_G.get() << 8) | ConfigHandler.AUTUMN_B.get());
        this.font.draw(poseStack, "Winter", c + 10, 87, (0xFF << 24) | (ConfigHandler.WINTER_R.get() << 16) | (ConfigHandler.WINTER_G.get() << 8) | ConfigHandler.WINTER_B.get());
        this.font.draw(poseStack, "Info Line Color", c - 75, 137, (0xFF << 24) | (ConfigHandler.INFO_R.get() << 16) | (ConfigHandler.INFO_G.get() << 8) | ConfigHandler.INFO_B.get());

        super.render(poseStack, mouseX, mouseY, partialTick);
    }
}