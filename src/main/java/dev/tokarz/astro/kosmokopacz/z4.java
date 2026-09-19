package dev.tokarz.astro.kosmokopacz;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class z4 {
    public final List<z3> list = new ArrayList<>();

    public void r(z3 m) {
        list.add(m);
    }

    public <T extends z3> T f(Class<T> type) {
        for (z3 m : list) {
            if (type.isInstance(m)) return type.cast(m);
        }
        return null;
    }

    public void t(MinecraftClient c) {
        long window = c.getWindow().getHandle();
        m1 kb = f(m1.class);
        boolean kbOn = kb != null && kb.on;
        for (z3 m : list) {
            boolean pressed = c.currentScreen == null && m.k >= 0 && InputUtil.isKeyPressed(window, m.k);
            if (pressed && !m.held) {
                m.on = !m.on;
                if (m.on) m.e(c); else m.d(c);
                c.player.sendMessage(Text.literal(m.n + " " + (m.on ? "ON" : "OFF")), true);
                z1.cfg.s(m);
            }
            m.held = pressed;
            if (m.on) {
                boolean blocked = z1.cfg.dep && !(m instanceof m1) && !kbOn;
                if (blocked) m.d(c); else m.t(c);
            }
        }
    }
}
