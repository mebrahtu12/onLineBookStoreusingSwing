package bookstore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserRegistry {
    private final Path usersFile;
    private final Map<String, String> users = new LinkedHashMap<>();

    public UserRegistry(Path usersFile) {
        this.usersFile = usersFile;
        load();
    }

    public boolean isRegistered(String username) {
        return users.containsKey(username.trim().toLowerCase());
    }

    public boolean register(String username, String password) {
        String key = normalize(username);
        if (key.isEmpty() || password == null || password.isBlank()) {
            return false;
        }
        if (users.containsKey(key)) {
            return false;
        }
        users.put(key, password);
        save();
        return true;
    }

    public boolean authenticate(String username, String password) {
        String key = normalize(username);
        if (!users.containsKey(key)) {
            return false;
        }
        return users.get(key).equals(password);
    }

    private String normalize(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }

    private void load() {
        users.clear();
        if (!Files.exists(usersFile)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(usersFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int sep = trimmed.indexOf(':');
                if (sep > 0) {
                    users.put(trimmed.substring(0, sep), trimmed.substring(sep + 1));
                }
            }
        } catch (IOException ignored) {
        }
    }

    private void save() {
        try {
            Files.createDirectories(usersFile.getParent());
            StringBuilder content = new StringBuilder("# username:password\n");
            for (Map.Entry<String, String> entry : users.entrySet()) {
                content.append(entry.getKey()).append(':').append(entry.getValue()).append('\n');
            }
            Files.writeString(usersFile, content.toString());
        } catch (IOException ignored) {
        }
    }
}
