package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class m2 extends z3 {
    private static final String[] OPS = {"Przerwij", "Zmien narzedzie"};

    private static Method gsel, ssel;
    private static Field fsel;

    static {
        try {
            gsel = PlayerInventory.class.getMethod("getSelectedSlot");
            ssel = PlayerInventory.class.getMethod("setSelectedSlot", int.class);
        } catch (Throwable ignored) {}
        if (gsel == null) {
            try {
                fsel = PlayerInventory.class.getField("selectedSlot");
            } catch (Throwable ignored) {}
        }
    }

    public int lim = 100;
    public int mode;
    private int cd;
    private boolean dd, drg;
    private TextFieldWidget tf;
    private int sx, sw, dx, dw, dy;

    public m2() {
        super("Uchron narzedzie", -1);
        nb = true;
    }

    @Override
    public void t(MinecraftClient c) {
        if (cd > 0) {
            cd--;
            return;
        }
        if (c.player == null) return;
        ItemStack st = c.player.getMainHandStack();
        if (!st.isDamageable() || st.getMaxDamage() - st.getDamage() > lim) return;

        if (mode == 1) {
            int best = -1, bd = lim;
            for (int i = 0; i < 9; i++) {
                ItemStack s = c.player.getInventory().getStack(i);
                if (!s.contains(DataComponentTypes.TOOL) || !s.isDamageable()) continue;
                int r = s.getMaxDamage() - s.getDamage();
                if (r > bd) {
                    bd = r;
                    best = i;
                }
            }
            if (best >= 0) {
                try {
                    PlayerInventory inv = c.player.getInventory();
                    if (best != sel(inv)) {
                        sel(inv, best);
                        c.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(best));
                        c.player.sendMessage(Text.literal("Uchron: zmieniono narzedzie"), true);
                    }
                } catch (Exception ignored) {}
                cd = 20;
                return;
            }
        }
        stop(c);
    }

    private static int sel(PlayerInventory inv) throws Exception {
        return gsel != null ? (int) gsel.invoke(inv) : fsel.getInt(inv);
    }

    private static void sel(PlayerInventory inv, int s) throws Exception {
        if (ssel != null) ssel.invoke(inv, s); else fsel.setInt(inv, s);
    }

    private void stop(MinecraftClient c) {
        m1 k = z1.mgr.f(m1.class);
        if (k != null && k.on) {
            k.on = false;
            k.d(c);
            z1.cfg.s(k);
        }
        c.player.sendMessage(Text.literal("Uchron: przerwano"), true);
        cd = 60;
    }

    @Override
    public int sh() {
        return dd ? 82 : 48;
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
            tf.setText(String.valueOf(lim));
        }
        tf.setX(x + w - 44);
        tf.setY(y + 5);
        tf.setWidth(36);
        k1.rrb(g, x + w - 48, y + 3, 44, 16, 4, tf.isFocused() ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        if (!tf.isFocused()) tf.setText(String.valueOf(lim));
        tf.render(g, mx, my, dt);

        dy = y + 24;
        g.drawTextWithShadow(s.tr(), "Co robic", x + 7, dy + 4, 0xFF8A8A8A);
        dx = x + 62;
        dw = w - 70;
        k1.rrb(g, dx, dy, dw, 16, 4, dd ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        g.drawTextWithShadow(s.tr(), OPS[mode], dx + 6, dy + 4, 0xFFE0E0E0);
        g.drawTextWithShadow(s.tr(), dd ? "^" : "v", dx + dw - 11, dy + 4, 0xFF888888);

        if (dd) {
            for (int i = 0; i < OPS.length; i++) {
                int oy = dy + 16 + i * 16;
                boolean oh = mx >= dx && mx <= dx + dw && my >= oy && my <= oy + 16;
                k1.rrb(g, dx, oy, dw, 16, 4, 0xFF2A2A30, oh ? 0xFF26262E : 0xFF141419);
                g.drawTextWithShadow(s.tr(), OPS[i], dx + 6, oy + 4, i == mode ? 0xFFFFFFFF : 0xFF9A9A9A);
            }
        }
    }

    @Override
    public boolean sc(k1 s, double mx, double my, int b, int x, int y, int w) {
        if (dd && my >= dy + 16 && my <= dy + 16 + OPS.length * 16 && mx >= dx && mx <= dx + dw) {
            mode = Math.min(OPS.length - 1, (int) (my - dy - 16) / 16);
            dd = false;
            z1.cfg.s(this);
            return true;
        }
        if (my >= dy && my <= dy + 16 && mx >= dx && mx <= dx + dw) {
            dd = !dd;
            return true;
        }
        dd = false;
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
        if (tf != null && tf.isFocused()) {
            tf.setFocused(false);
            pt();
        }
        return false;
    }

    @Override
    public boolean sd(k1 s, double mx, double my, double ddx, double ddy, int b) {
        if (drg) {
            sl(mx);
            return true;
        }
        return false;
    }

    @Override
    public void rl(k1 s) {
        if (drg) {
            drg = false;
            z1.cfg.s(this);
        }
    }

    @Override
    public boolean sk(k1 s, int key, int scan, int mod) {
        if (tf != null && tf.isFocused()) {
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER || key == GLFW.GLFW_KEY_ESCAPE) {
                pt();
                tf.setFocused(false);
            } else tf.keyPressed(key, scan, mod);
            return true;
        }
        return false;
    }

    @Override
    public boolean st(k1 s, char chr, int mod) {
        if (tf != null && tf.isFocused()) {
            if (Character.isDigit(chr)) tf.charTyped(chr, mod);
            return true;
        }
        return false;
    }

    private void sl(double mx) {
        lim = Math.max(1, Math.min(3000, (int) Math.round((mx - sx) / sw * 2999.0) + 1));
    }

    private void pt() {
        try {
            lim = Math.max(1, Math.min(3000, Integer.parseInt(tf.getText().trim())));
        } catch (NumberFormatException ignored) {
        }
        z1.cfg.s(this);
    }

    @Override
    public void pj(JsonObject o) {
        o.addProperty("lim", lim);
        o.addProperty("mode", mode);
    }

    @Override
    public void rj(JsonObject o) {
        if (o.has("lim")) lim = Math.max(1, Math.min(3000, o.get("lim").getAsInt()));
        if (o.has("mode")) mode = Math.max(0, Math.min(OPS.length - 1, o.get("mode").getAsInt()));
    }
}
