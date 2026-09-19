package dev.tokarz.astro.kosmokopacz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class z1 implements ClientModInitializer {
    public static z2 cfg;
    public static z4 mgr;

    @Override
    public void onInitializeClient() {
        cfg = new z2();
        cfg.l();

        mgr = new z4();
        mgr.r(new m1());
        mgr.r(new m2());
        mgr.r(new m3());
        mgr.r(new m4());
        mgr.r(new m5());
        for (z3 m : mgr.list) {
            cfg.a(m);
            if (!cfg.h(m.n)) cfg.s(m);
        }

        d3.h();

        ClientTickEvents.END_CLIENT_TICK.register(c -> {
            if (c.player == null || c.world == null) return;
            mgr.t(c);
        });
    }
}
