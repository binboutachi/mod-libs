package io.github.binboutachi.libs.minecraft.hud;

import java.util.ArrayList;

import io.github.binboutachi.libs.minecraft.hud.renderable.Renderable;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class HudRenderCallbackHandler implements HudRenderCallback {
    private HudRenderCallbackHandler() {}
    public static final HudRenderCallbackHandler singleton = new HudRenderCallbackHandler();
    private ArrayList<Renderable<?>> renderables = new ArrayList<>();

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        for(int i = 0; i < renderables.size(); i++) {
            renderables.get(i).draw(drawContext);
        }
    }
    void addRenderable(Renderable<?> renderable) {
        if(!renderables.contains(renderable)) {
            renderables.add(renderable);
        }
    }
    void removeRenderable(Renderable<?> renderable) {
        renderables.remove(renderable);
    }
}
