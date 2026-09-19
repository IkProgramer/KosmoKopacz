package dev.tokarz.astro.kosmokopacz;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class m1 extends z3 {
    public m1() {
        super("Kopacz", GLFW.GLFW_KEY_O);
    }

    @Override
    public void t(MinecraftClient c) {
        c.options.attackKey.setPressed(c.player != null && c.interactionManager != null && c.currentScreen == null);
    }

    @Override
    public void d(MinecraftClient c) {
        c.options.attackKey.setPressed(false);
    }
}
