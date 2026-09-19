package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class m5 extends z3 {
    public int fwd = 10, side = 2;
    public boolean sprint = true;
    private int seg;
    private double sx, sz;
    private float yaw0;
    private TextFieldWidget tf, bf;
    private int swx, swy;

    public m5() {
        super("Biegacz", -1);
        nb = true;
        col = 1;
    }

    @Override
    public void e(MinecraftClient c) {
        seg = 0;
        sx = c.player.getX();
        sz = c.player.getZ();
        yaw0 = Math.round(c.player.getYaw() / 90f) * 90f;
    }

    @Override
    public void d(MinecraftClient c) {
        rel(c);
    }

    private void rel(MinecraftClient c) {
        c.options.forwardKey.setPressed(false);
        c.options.sprintKey.setPressed(false);
    }

    @Override
    public void t(MinecraftClient c) {
        if (c.player == null) return;
        if (c.currentScreen != null || (fwd <= 0 && side <= 0)) {
            rel(c);
            return;
        }
        int g = 0;
        while (g++ < 4) {
            int len = seg % 2 == 0 ? fwd : side;
            float rad = (float) Math.toRadians(yaw0 + seg * 90f);
            double tr = (c.player.getX() - sx) * -Math.sin(rad) + (c.player.getZ() - sz) * Math.cos(rad);
            if (len > 0 && tr < len) break;
            adv(c);
        }
        c.player.setYaw(yaw0 + seg * 90f);
        c.options.forwardKey.setPressed(true);
        c.options.sprintKey.setPressed(sprint);
    }

    private void adv(MinecraftClient c) {
        seg++;
        sx = c.player.getX();
        sz = c.player.getZ();
        if (seg > 3) {
            seg = 0;
            m4 d = z1.mgr.f(m4.class);
            if (d != null && d.on && d.pb) d.dump(c);
        }
    }

    @Override
    public int sh() {
        return 70;
    }

    @Override
    public void sr(k1 s, DrawContext g, int x, int y, int w, int mx, int my, float dt) {
        if (tf == null) tf = mk(s);
        if (bf == null) bf = mk(s);

        row(s, g, tf, fwd, "Do przodu", x, y + 5, w, mx, my, dt);
        row(s, g, bf, side, "W bok", x, y + 26, w, mx, my, dt);

        swx = x + w - 34;
        swy = y + 47;
        boolean h = mx >= swx && mx <= swx + 24 && my >= swy && my <= swy + 10;
        g.drawTextWithShadow(s.tr(), "Sprint", x + 7, swy + 1, 0xFF8A8A8A);
        k1.rr(g, swx, swy, 24, 10, 5, sprint ? h ? 0xFFFFFFFF : 0xFFE8E8E8 : h ? 0xFF26262E : 0xFF1B1B21);
        k1.rr(g, sprint ? swx + 16 : swx + 2, swy + 2, 6, 6, 3, sprint ? 0xFF0A0A0A : 0xFF555555);
    }

    @Override
    public boolean sc(k1 s, double mx, double my, int b, int x, int y, int w) {
        if (my >= swy && my <= swy + 10 && mx >= swx && mx <= swx + 24) {
            sprint = !sprint;
            z1.cfg.s(this);
            return true;
        }
        if (tf != null && mx >= x + w - 48 && mx <= x + w - 4 && my >= y + 3 && my <= y + 19) {
            tf.setFocused(true);
            tf.mouseClicked(mx, my, b);
            return true;
        }
        if (bf != null && mx >= x + w - 48 && mx <= x + w - 4 && my >= y + 24 && my <= y + 40) {
            bf.setFocused(true);
            bf.mouseClicked(mx, my, b);
            return true;
        }
        if (tf != null && tf.isFocused()) {
            tf.setFocused(false);
            pf();
        }
        if (bf != null && bf.isFocused()) {
            bf.setFocused(false);
            pb();
        }
        return false;
    }

    @Override
    public boolean sk(k1 s, int key, int scan, int mod) {
        TextFieldWidget f = foc();
        if (f == null) return false;
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER || key == GLFW.GLFW_KEY_ESCAPE) {
            if (f == tf) pf(); else pb();
            f.setFocused(false);
        } else f.keyPressed(key, scan, mod);
        return true;
    }

    @Override
    public boolean st(k1 s, char chr, int mod) {
        TextFieldWidget f = foc();
        if (f == null) return false;
        if (Character.isDigit(chr)) f.charTyped(chr, mod);
        return true;
    }

    private TextFieldWidget foc() {
        if (tf != null && tf.isFocused()) return tf;
        if (bf != null && bf.isFocused()) return bf;
        return null;
    }

    private TextFieldWidget mk(k1 s) {
        TextFieldWidget f = new TextFieldWidget(s.tr(), 0, 0, 44, 16, Text.literal(""));
        f.setMaxLength(4);
        f.setDrawsBackground(false);
        f.setEditableColor(0xFFE0E0E0);
        return f;
    }

    private void row(k1 s, DrawContext g, TextFieldWidget f, int v, String l, int x, int y, int w, int mx, int my, float dt) {
        g.drawTextWithShadow(s.tr(), l, x + 7, y + 2, 0xFF8A8A8A);
        f.setX(x + w - 44);
        f.setY(y);
        f.setWidth(36);
        k1.rrb(g, x + w - 48, y - 2, 44, 16, 4, f.isFocused() ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        if (!f.isFocused()) f.setText(String.valueOf(v));
        f.render(g, mx, my, dt);
    }

    private void pf() {
        try {
            fwd = Math.max(0, Math.min(9999, Integer.parseInt(tf.getText().trim())));
        } catch (NumberFormatException ignored) {
        }
        z1.cfg.s(this);
    }

    private void pb() {
        try {
            side = Math.max(0, Math.min(9999, Integer.parseInt(bf.getText().trim())));
        } catch (NumberFormatException ignored) {
        }
        z1.cfg.s(this);
    }

    @Override
    public void pj(JsonObject o) {
        o.addProperty("fwd", fwd);
        o.addProperty("side", side);
        o.addProperty("sprint", sprint);
    }

    @Override
    public void rj(JsonObject o) {
        if (o.has("fwd")) fwd = Math.max(0, Math.min(9999, o.get("fwd").getAsInt()));
        if (o.has("side")) side = Math.max(0, Math.min(9999, o.get("side").getAsInt()));
        if (o.has("sprint")) sprint = o.get("sprint").getAsBoolean();
    }
}
