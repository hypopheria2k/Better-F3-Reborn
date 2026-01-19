package com.worador.f3hud.layout;

public class LayoutEngine {
    // Wir lassen diese Variablen als 1.0f bestehen, damit der restliche Code nicht bricht
    public static float leftScale = 1.0f;
    public static float rightScale = 1.0f;

    public static void update(int screenWidth) {
        // Hier passiert keine Magie mehr.
        leftScale = 1.0f;
        rightScale = 1.0f;
    }
} 
