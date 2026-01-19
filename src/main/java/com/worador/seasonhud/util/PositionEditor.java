package com.worador.seasonhud.util;

public class PositionEditor {
    private static int hudWidth = 100;
    private static int hudHeight = 50;

    public static void updateHudDimensions(int w, int h) {
        hudWidth = w;
        hudHeight = h;
    }

    public static int getWidth() {
        return hudWidth;
    }

    public static int getHeight() {
        return hudHeight;
    }

}