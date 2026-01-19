package com.worador.seasonhud.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue X_OFFSET, Y_OFFSET;
    public static final ForgeConfigSpec.DoubleValue SCALE;
    public static final ForgeConfigSpec.BooleanValue SHOW_BACKGROUND;

    public static final ForgeConfigSpec.IntValue SPRING_R, SPRING_G, SPRING_B;
    public static final ForgeConfigSpec.IntValue SUMMER_R, SUMMER_G, SUMMER_B;
    public static final ForgeConfigSpec.IntValue AUTUMN_R, AUTUMN_G, AUTUMN_B;
    public static final ForgeConfigSpec.IntValue WINTER_R, WINTER_G, WINTER_B;
    // Neue Info-Zeilen Farbe
    public static final ForgeConfigSpec.IntValue INFO_R, INFO_G, INFO_B;

    static {
        BUILDER.push("General");
        X_OFFSET = BUILDER.defineInRange("xOffset", 5, 0, 4000);
        Y_OFFSET = BUILDER.defineInRange("yOffset", 5, 0, 4000);
        SCALE = BUILDER.defineInRange("scale", 1.0, 0.5, 3.0);
        SHOW_BACKGROUND = BUILDER.define("showBackground", true);
        BUILDER.pop();

        BUILDER.push("Season Colors");
        SPRING_R = BUILDER.defineInRange("springRed", 85, 0, 255);
        SPRING_G = BUILDER.defineInRange("springGreen", 255, 0, 255);
        SPRING_B = BUILDER.defineInRange("springBlue", 85, 0, 255);

        SUMMER_R = BUILDER.defineInRange("summerRed", 255, 0, 255);
        SUMMER_G = BUILDER.defineInRange("summerGreen", 255, 0, 255);
        SUMMER_B = BUILDER.defineInRange("summerBlue", 85, 0, 255);

        AUTUMN_R = BUILDER.defineInRange("autumnRed", 255, 0, 255);
        AUTUMN_G = BUILDER.defineInRange("autumnGreen", 170, 0, 255);
        AUTUMN_B = BUILDER.defineInRange("autumnBlue", 0, 0, 255);

        WINTER_R = BUILDER.defineInRange("winterRed", 85, 0, 255);
        WINTER_G = BUILDER.defineInRange("winterGreen", 255, 0, 255);
        WINTER_B = BUILDER.defineInRange("winterBlue", 255, 0, 255);

        INFO_R = BUILDER.defineInRange("infoRed", 255, 0, 255);
        INFO_G = BUILDER.defineInRange("infoGreen", 255, 0, 255);
        INFO_B = BUILDER.defineInRange("infoBlue", 255, 0, 255);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}