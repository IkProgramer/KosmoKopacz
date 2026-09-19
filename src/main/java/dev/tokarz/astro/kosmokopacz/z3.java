package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class z3 {
    public final String n;
    public int k, col;
    public boolean on, held, exp, nb;
    public float ea, oa;

    protected z3(String n, int k) {
        this.n = n;
        this.k = k;
    }

    public void e(MinecraftClient c) {}
    public void d(MinecraftClient c) {}
    public void t(MinecraftClient c) {}

    public int sh() { return 0; }

    public void sr(k1 s, DrawContext g, int x, int y, int w, int mx, int my, float dt) {}
    public void rl(k1 s) {}

    public boolean sc(k1 s, double mx, double my, int b, int x, int y, int w) { return false; }
    public boolean sd(k1 s, double mx, double my, double dx, double dy, int b) { return false; }
    public boolean sk(k1 s, int key, int scan, int mod) { return false; }
    public boolean st(k1 s, char chr, int mod) { return false; }

    public void pj(JsonObject o) {}
    public void rj(JsonObject o) {}
}
