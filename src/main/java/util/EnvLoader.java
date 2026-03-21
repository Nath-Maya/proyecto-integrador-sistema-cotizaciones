package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvLoader {
    private static final String ENV_FILE = ".env";
    private static Map<String, String> cache;

    private EnvLoader() {}

    public static String get(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        Map<String, String> values = load();
        String fileValue = values.get(key);
        if (fileValue != null && !fileValue.isBlank()) {
            return fileValue;
        }

        return defaultValue;
    }

    private static Map<String, String> load() {
        if (cache != null) {
            return cache;
        }

        Map<String, String> values = new HashMap<>();
        Path path = Paths.get(ENV_FILE);

        if (!Files.exists(path)) {
            cache = values;
            return cache;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int idx = line.indexOf('=');
                if (idx <= 0) {
                    continue;
                }

                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();

                if ((value.startsWith("\"") && value.endsWith("\"")) ||
                    (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }

                values.put(key, value);
            }
        } catch (IOException ignored) {
            // En caso de error, se usan valores por defecto
        }

        cache = values;
        return cache;
    }
}
