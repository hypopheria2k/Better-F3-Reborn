package com.worador.f3hud.render;

import com.worador.f3hud.InfoModule;

public interface IModuleRenderer {
    void render(InfoModule module, int x, int y, float animProgress);
}
