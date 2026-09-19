package dev.tokarz.astro.kosmokopacz;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class d3 {
    private static final String DC = "https://discord.gg/GbNn4NwkZ6";

    public static void h() {
        ClientCommandRegistrationCallback.EVENT.register((d, r) ->
                d.register(ClientCommandManager.literal("kk").executes(c -> {
                    MinecraftClient mc = c.getSource().getClient();
                    mc.send(() -> mc.setScreen(new k1()));
                    return 1;
                })));

        ClientPlayConnectionEvents.JOIN.register((h, s, c) -> c.execute(() -> {
            if (c.player == null) return;
            String[] motd = {
                    "§8--------------------------",
                    "§fKosmoKopacz",
                    "§7WEB: §fthemoonv2.dev",
                    "§7DISCORD: §fdiscord.gg/GbNn4NwkZ6",
                    "§7COMMAND: §f/kk",
                    "§8--------------------------"
            };
            for (String l : motd) c.player.sendMessage(Text.literal(l), false);
        }));

        HudRenderCallback.EVENT.register((g, tc) -> {
            MinecraftClient c = MinecraftClient.getInstance();
            if (c.player == null || z1.mgr == null) return;
            m1 kb = z1.mgr.f(m1.class);
            boolean kbOn = kb != null && kb.on;
            int y = 6;
            for (z3 m : z1.mgr.list) {
                if (!m.on) continue;
                boolean blk = z1.cfg.dep && !(m instanceof m1) && !kbOn;
                g.drawTextWithShadow(c.textRenderer, m.n + " ON", 6, y, blk ? 0xFF666666 : -1);
                y += 10;
            }
        });

        ScreenEvents.AFTER_INIT.register((c, s, w, h) -> {
            if (!(s instanceof TitleScreen)) return;
            Screens.getButtons(s).add(ButtonWidget.builder(Text.literal("DISCORD"),
                            b -> Util.getOperatingSystem().open(DC))
                    .dimensions(5, h - 25, 62, 20)
                    .build());
        });
    }
}
