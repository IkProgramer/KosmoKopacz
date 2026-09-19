package dev.tokarz.astro.kosmokopacz;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class k2 extends Screen {
    private static final int W = 400, H = 250, CS = 18;
    private final m4 m;
    private final List<ItemStack> all = new ArrayList<>();
    private final long t0 = Util.getMeasuringTimeMs();
    private List<ItemStack> vis;
    private TextFieldWidget tf;
    private int srow;
    private double sacc;
    private String lq;

    public k2(m4 m) {
        super(Text.literal("Przedmioty"));
        this.m = m;
        for (Item i : Registries.ITEM) all.add(new ItemStack(i));
    }

    private int px() {
        return (width - W) / 2;
    }

    private int py() {
        return (height - H) / 2;
    }

    private int cols() {
        return (W - 24) / CS;
    }

    private int rows() {
        return (H - 108) / CS;
    }

    private float ap() {
        float a = Math.min(1f, (Util.getMeasuringTimeMs() - t0) / 160f);
        return a * (2f - a);
    }

    private static int fa(int c, float a) {
        return (int) ((c >>> 24) * a) << 24 | c & 0xFFFFFF;
    }

    @Override
    protected void init() {
        tf = new TextFieldWidget(textRenderer, 0, 0, 200, 16, Text.literal(""));
        tf.setMaxLength(40);
        tf.setDrawsBackground(false);
        tf.setEditableColor(0xFFE0E0E0);
        tf.setFocused(true);
    }

    private List<ItemStack> fl() {
        String q = tf.getText().trim().toLowerCase();
        if (vis == null || !q.equals(lq)) {
            lq = q;
            srow = 0;
            vis = new ArrayList<>();
            for (ItemStack s : all)
                if (q.isEmpty() || s.getName().getString().toLowerCase().contains(q)
                        || Registries.ITEM.getId(s.getItem()).toString().contains(q))
                    vis.add(s);
        }
        return vis;
    }

    @Override
    public void renderBackground(DrawContext g, int mx, int my, float dt) {
    }

    @Override
    public void render(DrawContext g, int mx, int my, float dt) {
        float a = ap();
        int x = px(), y = py();

        k1.rr(g, x + 4, y + 5, W, H, 9, fa(0x33000000, a));
        k1.rr(g, x + 2, y + 3, W, H, 9, fa(0x44000000, a));
        k1.rrb(g, x, y, W, H, 8, fa(0xFF24242B, a), fa(0xF90C0C0F, a));
        g.fill(x + 12, y + 1, x + W - 12, y + 2, fa(0x14FFFFFF, a));

        k1.rr(g, x + 1, y + 1, W - 2, 28, 7, fa(0xFF111116, a));
        g.fill(x + 1, y + 15, x + W - 1, y + 29, fa(0xFF111116, a));
        k1.rrb(g, x + 11, y + 11, 6, 6, 2, fa(0xFFCCCCCC, a), fa(0xFF111116, a));
        g.drawTextWithShadow(textRenderer, "PRZEDMIOTY", x + 22, y + 10, fa(0xFFFFFFFF, a));
        g.drawTextWithShadow(textRenderer, m.sel.size() + " wybranych", x + W - 90, y + 10, fa(0xFF4A4A52, a));

        tf.setX(x + 16);
        tf.setY(y + 38);
        tf.setWidth(W - 32);
        tf.setEditableColor(fa(0xFFE0E0E0, a));
        k1.rrb(g, x + 12, y + 36, W - 24, 18, 5, fa(tf.isFocused() ? 0xFF55555F : 0xFF33333B, a), fa(0xFF141419, a));
        tf.render(g, mx, my, dt);

        List<ItemStack> f = fl();
        int cols = cols(), rows = rows();
        int trows = (f.size() + cols - 1) / cols;
        int msr = Math.max(0, trows - rows);
        srow = Math.min(srow, msr);

        int gx = x + 12, gy = y + 62;
        g.enableScissor(gx, gy, x + W - 12, gy + rows * CS);
        for (int i = srow * cols; i < Math.min(f.size(), (srow + rows) * cols); i++) {
            ItemStack s = f.get(i);
            int cx = gx + (i % cols) * CS, cy = gy + (i / cols - srow) * CS;
            boolean sel = m.sel.contains(Registries.ITEM.getId(s.getItem()).toString());
            boolean hov = mx >= cx && mx <= cx + CS - 1 && my >= cy && my <= cy + CS - 1;
            if (sel) k1.rrb(g, cx, cy, CS - 1, CS - 1, 4, fa(0xFF7A7A88, a), fa(0xFF2C2C38, a));
            else k1.rr(g, cx, cy, CS - 1, CS - 1, 4, fa(hov ? 0xFF1F1F27 : 0xFF141419, a));
            g.drawItem(s, cx, cy);
        }
        g.disableScissor();

        if (msr > 0) {
            int sh = Math.max(20, rows * rows * CS / trows);
            int sy = gy + (int) ((rows * CS - sh) * (srow / (float) msr));
            k1.rr(g, x + W - 6, sy, 3, sh, 1, fa(0xFF3A3A44, a));
        }

        int by = y + H - 28;
        boolean h1 = mx >= x + 12 && mx <= x + 112 && my >= by && my <= by + 16;
        k1.rrb(g, x + 12, by, 100, 16, 5, fa(0xFF2A2A30, a), fa(h1 ? 0xFF26262E : 0xFF1B1B21, a));
        g.drawCenteredTextWithShadow(textRenderer, "Wyczysc liste", x + 62, by + 4, fa(0xFF9A9A9A, a));

        boolean h2 = mx >= x + W - 92 && mx <= x + W - 12 && my >= by && my <= by + 16;
        k1.rrb(g, x + W - 92, by, 80, 16, 5, fa(0xFF2A2A30, a), fa(h2 ? 0xFF2A2A34 : 0xFF1B1B21, a));
        g.drawCenteredTextWithShadow(textRenderer, "Gotowe", x + W - 52, by + 4, fa(0xFFE0E0E0, a));

        super.render(g, mx, my, dt);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int x = px(), y = py();
        int by = y + H - 28;
        if (my >= by && my <= by + 16) {
            if (mx >= x + 12 && mx <= x + 112) {
                m.sel.clear();
                z1.cfg.s(m);
                return true;
            }
            if (mx >= x + W - 92 && mx <= x + W - 12) {
                close();
                return true;
            }
        }
        int gx = x + 12, gy = y + 62, cols = cols(), rows = rows();
        if (mx >= gx && mx < gx + cols * CS && my >= gy && my <= gy + rows * CS) {
            List<ItemStack> f = fl();
            int i = srow * cols + ((int) (my - gy) / CS) * cols + (int) (mx - gx) / CS;
            if (i < f.size()) {
                String id = Registries.ITEM.getId(f.get(i).getItem()).toString();
                if (!m.sel.remove(id)) m.sel.add(id);
                z1.cfg.s(m);
            }
            return true;
        }
        return tf.mouseClicked(mx, my, btn) || super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double ha, double va) {
        sacc -= va;
        int d = (int) sacc;
        sacc -= d;
        srow = Math.max(0, srow + d);
        return true;
    }

    @Override
    public boolean keyPressed(int key, int scan, int mod) {
        return tf.keyPressed(key, scan, mod) || super.keyPressed(key, scan, mod);
    }

    @Override
    public boolean charTyped(char chr, int mod) {
        return tf.charTyped(chr, mod) || super.charTyped(chr, mod);
    }

    @Override
    public void close() {
        z1.cfg.s(m);
        client.setScreen(new k1());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
