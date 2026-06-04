package com.omnitranslate;

import org.bukkit.entity.Player;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

public class DiscordWebhookManager {

    private final OmniTranslate plugin;

    public DiscordWebhookManager(OmniTranslate plugin) {
        this.plugin = plugin;
    }

    public void checkAndReport(Player player, String message) {
        List<String> suspiciousWords = plugin.getConfig().getStringList("discord-security-keywords");
        boolean isSuspicious = suspiciousWords.stream().anyMatch(word -> message.toLowerCase().contains(word.toLowerCase()));

        if (isSuspicious) {
            sendToDiscordWebhook(player, message);
        }
    }

    private void sendToDiscordWebhook(Player player, String message) {
        String webhookUrl = plugin.getConfig().getString("discord-security.webhook-url");
        if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.contains("YOUR_URL")) return;

        // تنفيذ الإرسال بشكل Async لعدم التسبب في تجميد السيرفر
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // بناء ملف Json غني بالمعلومات المطلوبة للإدارة
                String jsonPayload = "{"
                        + "\"embeds\": [{"
                        + "\"title\": \"⚠️ Suspicious Message Detected\","
                        + "\"color\": 16711680," // لون أحمر
                        + "\"fields\": ["
                        + "{\"name\": \"Player\", \"value\": \"" + player.getName() + "\", \"inline\": true},"
                        + "{\"name\": \"World\", \"value\": \"" + player.getWorld().getName() + "\", \"inline\": true},"
                        + "{\"name\": \"Location\", \"value\": \"" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "\", \"inline\": true},"
                        + "{\"name\": \"Message\", \"value\": \"" + message + "\", \"inline\": false},"
                        + "{\"name\": \"Time\", \"value\": \"" + Instant.now().toString() + "\", \"inline\": false}"
                        + "]"
                        + "}]"
                        + "}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                connection.getResponseCode(); // لإرسال البيانات فعلياً
                connection.disconnect();

            } catch (Exception e) {
                plugin.getLogger().warning("Failed to send security alert to Discord: " + e.getMessage());
            }
        });
    }
}
