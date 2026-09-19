package dev.tokarz.astro.kosmokopacz;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;

public class z2 {
    private final Path dir = Path.of(System.getenv("APPDATA") != null ? System.getenv("APPDATA") : System.getProperty("user.home"), "zKosmodrom", "KosmoKopacz");
    private final Path file = dir.resolve("cfg.json");
    private JsonObject root = new JsonObject();
    public boolean dep;

    public void l() {
        try {
            if (Files.exists(file)) {
                root = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            }
            dep = root.has("_dep") && root.get("_dep").getAsBoolean();
        } catch (Exception e) {
            root = new JsonObject();
        }
    }

    public boolean h(String key) {
        return root.has(key);
    }

    public void a(z3 m) {
        if (!root.has(m.n) || !root.get(m.n).isJsonObject()) return;
        JsonObject o = root.getAsJsonObject(m.n);
        if (o.has("k")) m.k = o.get("k").getAsInt();
        if (o.has("o")) m.on = o.get("o").getAsBoolean();
        m.rj(o);
    }

    public void s(z3 m) {
        JsonObject o = root.has(m.n) ? root.getAsJsonObject(m.n) : new JsonObject();
        o.addProperty("k", m.k);
        o.addProperty("o", m.on);
        m.pj(o);
        root.add(m.n, o);
        w();
    }

    public void w() {
        try {
            root.addProperty("_dep", dep);
            Files.createDirectories(dir);
            Files.writeString(file, root.toString());
        } catch (Exception e) {
        }
    }
}
