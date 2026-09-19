package dev.tokarz.astro.kosmokopacz;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class k1 extends Screen {
    private static final int CW = 296, PW = 616;

    private z3 lis;
    private boolean gear;
    private long lt, ot;
    private float ap;

    public k1() {
        super(Text.literal("KosmoKopacz"));
        lt = ot = Util.getMeasuringTimeMs();
    }

    public TextRenderer tr() {
        return textRenderer;
    }

    private void an() {
        long now = Util.getMeasuringTimeMs();
        float d = Math.min(1f, (now - lt) * 0.009f);
        lt = now;

        ap = Math.min(1f, (now - ot) / 220f);
        ap = 1f - (1f - ap) * (1f - ap) * (1f - ap);

        for (z3 m : z1.mgr.list) {
            float te = m.exp && m.sh() > 0 ? 1f : 0f;
            m.ea += (te - m.ea) * d;
            if (Math.abs(te - m.ea) < 0.02f) m.ea = te;
            float to = m.on ? 1f : 0f;
            m.oa += (to - m.oa) * d;
            if (Math.abs(to - m.oa) < 0.02f) m.oa = to;
        }
    }

    private int eh(z3 m) {
        return (int) ((m.sh() + 6) * m.ea);
    }

    private int ph() {
        int h0 = 0, h1 = 0;
        for (z3 m : z1.mgr.list) {
            if (m.col == 0) h0 += 32 + eh(m);
            else h1 += 32 + eh(m);
        }
        return 44 + Math.max(h0, h1);
    }

    private int px() {
        return (width - PW) / 2;
    }

    private int py() {
        return (height - ph()) / 2;
    }

    private int fa(int c) {
        if (ap >= 1f) return c;
        return (int) ((c >>> 24) * ap) << 24 | c & 0xFFFFFF;
    }

    private static int lc(int a, int b, float t) {
        int ar = a >>> 24, br = b >>> 24;
        int r1 = a >> 16 & 255, r2 = b >> 16 & 255;
        int g1 = a >> 8 & 255, g2 = b >> 8 & 255;
        int b1 = a & 255, b2 = b & 255;
        return MathHelper.clamp(ar + (int) ((br - ar) * t), 0, 255) << 24
                | MathHelper.clamp(r1 + (int) ((r2 - r1) * t), 0, 255) << 16
                | MathHelper.clamp(g1 + (int) ((g2 - g1) * t), 0, 255) << 8
                | MathHelper.clamp(b1 + (int) ((b2 - b1) * t), 0, 255);
    }

    public static void rr(DrawContext g, int x, int y, int w, int h, int r, int c) {
        g.fill(x, y + r, x + w, y + h - r, c);
        for (int t = 0; t < r; t++) {
            int in = r - (int) Math.ceil(Math.sqrt(r * r - (r - t) * (r - t)));
            g.fill(x + in, y + t, x + w - in, y + t + 1, c);
            g.fill(x + in, y + h - 1 - t, x + w - in, y + h - t, c);
        }
    }

    public static void rrb(DrawContext g, int x, int y, int w, int h, int r, int bc, int fc) {
        rr(g, x, y, w, h, r, bc);
        rr(g, x + 1, y + 1, w - 2, h - 2, Math.max(1, r - 1), fc);
    }

    @Override
    public void renderBackground(DrawContext g, int mx, int my, float dt) {
    }

    @Override
    public void render(DrawContext g, int mx, int my, float dt) {
        an();
        if (ap <= 0f) return;
        int x = px(), y = py(), w = PW, h = ph();

        rr(g, x + 4, y + 5, w, h, 9, fa(0x33000000));
        rr(g, x + 2, y + 3, w, h, 9, fa(0x44000000));
        rrb(g, x, y, w, h, 8, fa(0xFF24242B), fa(0xF90C0C0F));
        g.fill(x + 12, y + 1, x + w - 12, y + 2, fa(0x14FFFFFF));

        rr(g, x + 1, y + 1, w - 2, 28, 7, fa(0xFF111116));
        g.fill(x + 1, y + 15, x + w - 1, y + 29, fa(0xFF111116));
        rrb(g, x + 11, y + 11, 6, 6, 2, fa(0xFFCCCCCC), fa(0xFF111116));
        g.drawTextWithShadow(textRenderer, "KOSMO KOPACZ", x + 22, y + 10, fa(0xFFFFFFFF));
        g.drawTextWithShadow(textRenderer, "v1.0", x + w - 32, y + 10, fa(0xFF4A4A52));
        g.fill(x + 10, y + 28, x + w - 10, y + 29, fa(0xFF1F1F26));

        int gx = x + w - 56, gy = y + 8;
        boolean gh = mx >= gx - 2 && mx <= gx + 14 && my >= gy - 2 && my <= gy + 14;
        int gc = fa(gear || gh ? 0xFF9A9AA4 : 0xFF55555F);
        rr(g, gx + 3, gy, 6, 12, 3, gc);
        rr(g, gx, gy + 3, 12, 6, 3, gc);
        rr(g, gx + 2, gy + 2, 8, 8, 4, gc);
        rr(g, gx + 4, gy + 4, 4, 4, 2, fa(0xFF111116));

        int[] rys = {y + 38, y + 38};
        for (z3 m : z1.mgr.list) {
            int cx = x + 8 + m.col * (CW + 8);
            int ry = rys[m.col];

            boolean rh = mx >= cx && mx <= cx + CW && my >= ry && my <= ry + 26;
            rrb(g, cx, ry, CW, 26, 6, fa(0xFF1F1F26), fa(rh ? 0xFF181820 : 0xFF131317));
            if (m.oa > 0.5f) rr(g, cx + 3, ry + 8, 2, 10, 1, fa(0xFFDDDDDD));

            int nx = cx + 10;
            if (m.sh() > 0) {
                g.drawTextWithShadow(textRenderer, m.exp ? "v" : ">", cx + 10, ry + 9, fa(0xFF55555F));
                nx = cx + 22;
            }
            g.drawTextWithShadow(textRenderer, m.n, nx, ry + 9, fa(rh ? 0xFFFFFFFF : 0xFFBFBFBF));

            if (!m.nb) {
                int bx = cx + CW - 122;
                boolean kh = mx >= bx && mx <= bx + 56 && my >= ry + 4 && my <= ry + 22;
                int bf = lis == m ? 0xFF30303A : kh ? 0xFF23232B : 0xFF1B1B21;
                rrb(g, bx, ry + 4, 56, 18, 4, fa(lis == m ? 0xFF55555F : 0xFF33333B), fa(bf));
                String kn = lis == m ? "..." : m.k < 0 ? "-" : InputUtil.fromKeyCode(m.k, 0).getLocalizedText().getString().toUpperCase();
                g.drawCenteredTextWithShadow(textRenderer, kn, bx + 28, ry + 9, fa(0xFFE0E0E0));
            }

            int tx = cx + CW - 50, tw = 32;
            boolean th = mx >= tx && mx <= tx + tw && my >= ry + 7 && my <= ry + 19;
            rr(g, tx, ry + 7, tw, 12, 6, fa(lc(th ? 0xFF26262E : 0xFF1F1F26, th ? 0xFFFFFFFF : 0xFFE8E8E8, m.oa)));
            int knob = tx + 3 + (int) ((tw - 14) * m.oa);
            rr(g, knob, ry + 9, 8, 8, 4, fa(lc(0xFF555555, 0xFF0A0A0A, m.oa)));

            ry += 32;
            int eh = eh(m);
            if (eh > 4) {
                g.enableScissor(cx, ry - 4, cx + CW, ry - 4 + eh);
                rrb(g, cx, ry - 4, CW, m.sh() + 4, 6, fa(0xFF1C1C22), fa(0xFF0F0F13));
                m.sr(this, g, cx, ry, CW, mx, my, dt);
                g.disableScissor();
            }
            rys[m.col] = ry + eh;
        }

        if (gear) {
            int dx = x + w - 186, dy = y + 34, dw = 178;
            rr(g, dx + 2, dy + 2, dw, 24, 6, fa(0x44000000));
            rrb(g, dx, dy, dw, 24, 6, fa(0xFF2A2A30), fa(0xF8101014));
            g.drawTextWithShadow(textRenderer, "Zalezy od Kopacza", dx + 8, dy + 8, fa(0xFFBFBFBF));
            int sx2 = dx + dw - 32;
            boolean sh2 = mx >= sx2 && mx <= sx2 + 24 && my >= dy + 7 && my <= dy + 17;
            rr(g, sx2, dy + 7, 24, 10, 5, fa(z1.cfg.dep ? sh2 ? 0xFFFFFFFF : 0xFFE8E8E8 : sh2 ? 0xFF26262E : 0xFF1B1B21));
            rr(g, z1.cfg.dep ? sx2 + 16 : sx2 + 2, dy + 9, 6, 6, 3, fa(z1.cfg.dep ? 0xFF0A0A0A : 0xFF555555));
        }

        super.render(g, mx, my, dt);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int x = px(), y = py();
        int gx = x + PW - 56, gy = y + 8;
        if (mx >= gx - 2 && mx <= gx + 14 && my >= gy - 2 && my <= gy + 14) {
            gear = !gear;
            return true;
        }
        if (gear) {
            int dx = x + PW - 186, dy = y + 34;
            if (my >= dy && my <= dy + 24 && mx >= dx && mx <= dx + 178) {
                if (mx >= dx + 146) {
                    z1.cfg.dep = !z1.cfg.dep;
                    z1.cfg.w();
                }
                return true;
            }
            gear = false;
        }
        int[] rys = {y + 38, y + 38};
        for (z3 m : z1.mgr.list) {
            int cx = x + 8 + m.col * (CW + 8);
            int ry = rys[m.col];
            if (my >= ry && my <= ry + 26) {
                int tx = cx + CW - 50;
                if (mx >= tx && mx <= tx + 32) {
                    m.on = !m.on;
                    if (m.on) m.e(client); else m.d(client);
                    z1.cfg.s(m);
                    return true;
                }
                if (!m.nb) {
                    int kx = cx + CW - 122;
                    if (mx >= kx && mx <= kx + 56) {
                        lis = lis == m ? null : m;
                        return true;
                    }
                    if (m.sh() > 0 && mx >= cx && mx <= kx - 6) {
                        m.exp = !m.exp;
                        return true;
                    }
                } else if (m.sh() > 0 && mx >= cx && mx <= tx - 6) {
                    m.exp = !m.exp;
                    return true;
                }
            }
            ry += 32;
            int eh = eh(m);
            if (eh > 0 && m.ea > 0.6f && m.sc(this, mx, my, btn, cx, ry, CW)) return true;
            rys[m.col] = ry + eh;
        }
        lis = null;
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        for (z3 m : z1.mgr.list)
            if (m.ea > 0.5f && m.sd(this, mx, my, dx, dy, btn)) return true;
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        for (z3 m : z1.mgr.list) m.rl(this);
        return super.mouseReleased(mx, my, btn);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mod) {
        if (lis != null) {
            if (key == GLFW.GLFW_KEY_DELETE || key == GLFW.GLFW_KEY_BACKSPACE) lis.k = -1;
            else if (key != GLFW.GLFW_KEY_ESCAPE) lis.k = key;
            z1.cfg.s(lis);
            lis = null;
            return true;
        }
        for (z3 m : z1.mgr.list)
            if (m.ea > 0.5f && m.sk(this, key, scan, mod)) return true;
        return super.keyPressed(key, scan, mod);
    }

    @Override
    public boolean charTyped(char chr, int mod) {
        if (lis != null) return true;
        for (z3 m : z1.mgr.list)
            if (m.ea > 0.5f && m.st(this, chr, mod)) return true;
        return super.charTyped(chr, mod);
    }

    @Override
    public void removed() {
        z1.mgr.list.forEach(m -> z1.cfg.s(m));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
