package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class m4 extends z3 {
    public final Set<String> sel = new HashSet<>();
    public int mins = 5;
    public boolean pb;
    private int cd;
    private boolean drg;
    private TextFieldWidget tf;
    private int sx, sw, b1x, b1w, b2x, b2w, by;
    private int swx, swy;

    public m4() {
        super("Wyrzuc itemki", -1);
        nb = true;
    }

    @Override
    public void e(MinecraftClient c) {
        cd = Math.max(mins, 1) * 1200;
    }

    @Override
    public void t(MinecraftClient c) {
        if (pb || c.player == null || c.interactionManager == null || c.currentScreen != null) return;
        if (cd > 0) {
            cd--;
            return;
        }
        cd = Math.max(mins, 1) * 1200;
        dump(c);
    }

    public void dump(MinecraftClient c) {
        if (c.player == null || c.interactionManager == null || sel.isEmpty()) return;
        int sync = c.player.playerScreenHandler.syncId;
        for (int i = 0; i < 36; i++) {
            ItemStack st = c.player.getInventory().getStack(i);
            if (st.isEmpty() || !sel.contains(Registries.ITEM.getId(st.getItem()).toString())) continue;
            c.interactionManager.clickSlot(sync, i < 9 ? 36 + i : i, 1, SlotActionType.THROW, c.player);
        }
        c.player.sendMessage(Text.literal("Wyrzuc itemki: wyczyszczono"), true);
    }

    @Override
    public int sh() {
        return 68;
    }

    @Override
    public void sr(k1 s, DrawContext g, int x, int y, int w, int mx, int my, float dt) {
        g.drawTextWithShadow(s.tr(), "Co ile (min)", x + 7, y + 7, 0xFF8A8A8A);
        sx = x + 80;
        sw = w - 134;
        int p = sx + (int) (sw * (mins - 1) / 59.0);
        k1.rr(g, sx, y + 8, sw, 4, 2, 0xFF1B1B21);
        if (p > sx) k1.rr(g, sx, y + 8, p - sx, 4, 2, 0xFF9A9A9A);
        k1.rr(g, p - 2, y + 5, 4, 10, 2, 0xFFFFFFFF);

        if (tf == null) {
            tf = new TextFieldWidget(s.tr(), 0, 0, 36, 16, Text.literal(""));
            tf.setMaxLength(3);
            tf.setDrawsBackground(false);
            tf.setEditableColor(0xFFE0E0E0);
            tf.setText(String.valueOf(mins));
        }
        tf.setX(x + w - 38);
        tf.setY(y + 5);
        tf.setWidth(28);
        k1.rrb(g, x + w - 42, y + 3, 36, 16, 4, tf.isFocused() ? 0xFF55555F : 0xFF33333B, 0xFF1B1B21);
        if (!tf.isFocused()) tf.setText(String.valueOf(mins));
        tf.render(g, mx, my, dt);

        by = y + 24;
        b1x = x + 7;
        b1w = 110;
        boolean h1 = mx >= b1x && mx <= b1x + b1w && my >= by && my <= by + 16;
        k1.rrb(g, b1x, by, b1w, 16, 4, 0xFF33333B, h1 ? 0xFF26262E : 0xFF1B1B21);
        g.drawCenteredTextWithShadow(s.tr(), "Ustaw przedmioty", b1x + b1w / 2, by + 4, 0xFFE0E0E0);

        b2x = x + 124;
        b2w = 74;
        boolean h2 = mx >= b2x && mx <= b2x + b2w && my >= by && my <= by + 16;
        k1.rrb(g, b2x, by, b2w, 16, 4, 0xFF33333B, h2 ? 0xFF26262E : 0xFF1B1B21);
        g.drawCenteredTextWithShadow(s.tr(), "Wyczysc", b2x + b2w / 2, by + 4, 0xFF9A9A9A);

        g.drawTextWithShadow(s.tr(), "Zaznaczone: " + sel.size(), x + 206, by + 4, 0xFF666666);

        swx = x + w - 34;
        swy = y + 47;
        boolean h3 = mx >= swx && mx <= swx + 24 && my >= swy && my <= swy + 10;
        g.drawTextWithShadow(s.tr(), "Przy biegu", x + 7, swy + 1, 0xFF8A8A8A);
        k1.rr(g, swx, swy, 24, 10, 5, pb ? h3 ? 0xFFFFFFFF : 0xFFE8E8E8 : h3 ? 0xFF26262E : 0xFF1B1B21);
        k1.rr(g, pb ? swx + 16 : swx + 2, swy + 2, 6, 6, 3, pb ? 0xFF0A0A0A : 0xFF555555);
    }

    @Override
    public boolean sc(k1 s, double mx, double my, int b, int x, int y, int w) {
        if (my >= swy && my <= swy + 10 && mx >= swx && mx <= swx + 24) {
            pb = !pb;
            z1.cfg.s(this);
            return true;
        }
        if (my >= by && my <= by + 16) {
            if (mx >= b1x && mx <= b1x + b1w) {
                MinecraftClient.getInstance().setScreen(new k2(this));
                return true;
            }
            if (mx >= b2x && mx <= b2x + b2w) {
                sel.clear();
                z1.cfg.s(this);
                return true;
            }
        }
        if (my >= y + 3 && my <= y + 19 && mx >= sx && mx <= sx + sw) {
            drg = true;
            sl(mx);
            return true;
        }
        if (tf != null && mx >= x + w - 42 && mx <= x + w - 6 && my >= y + 3 && my <= y + 19) {
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
        mins = Math.max(1, Math.min(60, (int) Math.round((mx - sx) / sw * 59.0) + 1));
    }

    private void pt() {
        try {
            mins = Math.max(1, Math.min(60, Integer.parseInt(tf.getText().trim())));
        } catch (NumberFormatException ignored) {
        }
        z1.cfg.s(this);
    }

    @Override
    public void pj(JsonObject o) {
        o.addProperty("mins", mins);
        o.addProperty("pb", pb);
        JsonArray a = new JsonArray();
        sel.forEach(a::add);
        o.add("sel", a);
    }

    @Override
    public void rj(JsonObject o) {
        if (o.has("mins")) mins = Math.max(1, Math.min(60, o.get("mins").getAsInt()));
        if (o.has("pb")) pb = o.get("pb").getAsBoolean();
        if (o.has("sel")) {
            sel.clear();
            o.getAsJsonArray("sel").forEach(e -> sel.add(e.getAsString()));
        }
    }
}
