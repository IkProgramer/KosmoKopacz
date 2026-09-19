package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class m3 extends z3 {
    public int lim = 300;
    public String cmd = "/repair";
    private int cd;
    private boolean drg;
    private TextFieldWidget tf, cf;
    private int sx, sw;

    public m3() {
        super("AutoRepair", -1);
        nb = true;
    }

    @Override
    public void t(MinecraftClient c) {
        if (cd > 0) {
            cd--;
            return;
        }
        if (c.player == null) return;
        ItemStack stack = c.player.getMainHandStack();
        if (!stack.isDamageable() || stack.getMaxDamage() - stack.getDamage() > lim) return;
        String s = cmd.startsWith("/") ? cmd.substring(1) : cmd;
        s = s.trim();
        if (!s.isEmpty()) {
            c.player.networkHandler.sendChatCommand(s);
            c.player.sendMessage(Text.literal("AutoRepair: " + cmd), true);
        }
        cd = 100;
    }

    @Override
    public int sh() {
        return 48;
    }

    @Override
    public void sr(k1 s, DrawContext g, int x, int y, int w, int mx, int my, float dt) {
        g.drawTextWithShadow(s.tr(), "Limit", x + 7, y + 7, 0xFF8A8A8A);
        sx = x + 44;
        sw = w - 98;
        int p = sx + (int) (sw * (lim - 1) / 2999.0);
        k1.rr(g, sx, y + 8, sw, 4, 2, 0xFF1B1B21);
        if (p > sx) k1.rr(g, sx, y + 8, p - sx, 4, 2, 0xFF9A9A9A);
        k1.rr(g, p - 2, y + 5, 4, 10, 2, 0xFFFFFFFF);

        if (tf == null) {
            tf = new TextFieldWidget(s.tr(), 0, 0, 44, 16, Text.literal(""));
            tf.setMaxLength(4);
            tf.setDrawsBackground(false);
            tf.setEditableColor(0xFFE0E0E0);
        }
        tf.setX(x + w - 44);
        tf.setY(y + 5);
        tf.setWidth(36);
        k1.rrb(g, x + w - 48, y + 3, 44, 16, 4, tf.isFocused() ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        if (!tf.isFocused()) tf.setText(Integer.toString(lim));
        tf.render(g, mx, my, dt);

        g.drawTextWithShadow(s.tr(), "Komenda", x + 7, y + 28, 0xFF8A8A8A);
        if (cf == null) {
            cf = new TextFieldWidget(s.tr(), 0, 0, 10, 16, Text.literal(""));
            cf.setMaxLength(60);
            cf.setDrawsBackground(false);
            cf.setEditableColor(0xFFE0E0E0);
            cf.setText(cmd);
        }
        cf.setX(x + 70);
        cf.setY(y + 26);
        cf.setWidth(w - 82);
        k1.rrb(g, x + 66, y + 24, w - 74, 16, 4, cf.isFocused() ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        cf.render(g, mx, my, dt);
    }

    @Override
    public boolean sc(k1 s, double mx, double my, int b, int x, int y, int w) {
        if (my >= y + 3 && my <= y + 19 && mx >= sx && mx <= sx + sw) {
            drg = true;
            sl(mx);
            return true;
        }
        if (tf != null && mx >= x + w - 48 && mx <= x + w - 4 && my >= y + 3 && my <= y + 19) {
            tf.setFocused(true);
            tf.mouseClicked(mx, my, b);
            return true;
        }
        if (cf != null && mx >= x + 66 && mx <= x + w - 8 && my >= y + 24 && my <= y + 40) {
            cf.setFocused(true);
            cf.mouseClicked(mx, my, b);
            return true;
        }
        if (tf != null && tf.isFocused()) {
            tf.setFocused(false);
            pt();
        }
        if (cf != null && cf.isFocused()) {
            cf.setFocused(false);
            cmd = cf.getText();
            z1.cfg.s(this);
        }
        return false;
    }

    @Override
    public boolean sd(k1 s, double mx, double my, double ddx, double ddy, int b) {
        if (!drg) return false;
        sl(mx);
        return true;
    }

    @Override
    public void rl(k1 s) {
        if (!drg) return;
        drg = false;
        z1.cfg.s(this);
    }

    @Override
    public boolean sk(k1 s, int key, int scan, int mod) {
        if (tf != null && tf.isFocused()) {
            if (ent(key) || key == GLFW.GLFW_KEY_ESCAPE) {
                pt();
                tf.setFocused(false);
            } else tf.keyPressed(key, scan, mod);
            return true;
        }
        if (cf != null && cf.isFocused()) {
            if (ent(key) || key == GLFW.GLFW_KEY_ESCAPE) {
                cmd = cf.getText();
                cf.setFocused(false);
                z1.cfg.s(this);
            } else cf.keyPressed(key, scan, mod);
            return true;
        }
        return false;
    }

    private static boolean ent(int k) {
        return k == GLFW.GLFW_KEY_ENTER || k == GLFW.GLFW_KEY_KP_ENTER;
    }

    @Override
    public boolean st(k1 s, char chr, int mod) {
        if (tf != null && tf.isFocused()) {
            if (Character.isDigit(chr)) tf.charTyped(chr, mod);
            return true;
        }
        if (cf != null && cf.isFocused()) {
            cf.charTyped(chr, mod);
            cmd = cf.getText();
            return true;
        }
        return false;
    }

    private void sl(double mx) {
        lim = MathHelper.clamp((int) Math.round((mx - sx) / sw * 2999.0) + 1, 1, 3000);
    }

    private void pt() {
        try {
            lim = MathHelper.clamp(Integer.parseInt(tf.getText().trim()), 1, 3000);
        } catch (NumberFormatException ignored) {}
        z1.cfg.s(this);
    }

    @Override
    public void pj(JsonObject o) {
        if (cf != null) cmd = cf.getText();
        o.addProperty("lim", lim);
        o.addProperty("cmd", cmd);
    }

    @Override
    public void rj(JsonObject o) {
        if (o.has("lim")) lim = MathHelper.clamp(o.get("lim").getAsInt(), 1, 3000);
        if (o.has("cmd")) {
            cmd = o.get("cmd").getAsString();
            if (cf != null) cf.setText(cmd);
        }
    }
}
