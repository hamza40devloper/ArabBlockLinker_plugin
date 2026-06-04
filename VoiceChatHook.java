package com.omnitranslate;

import org.bukkit.entity.Player;

// هيكلية افتراضية لربط البلوجين مع حزم الـ Voice Chat 
public class VoiceChatHook {

    private final OmniTranslate plugin;

    public VoiceChatHook(OmniTranslate plugin) {
        this.plugin = plugin;
    }

    // يتم استدعاء هذه الدالة عند التقاط حزمة صوتية من لاعب
    public void onVoiceStreamReceived(Player speaker, byte[] audioData) {
        // 1. معالجة الصوت وتحويله لنص عبر Async task
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            
            // محاكاة تحويل الصوت النصي (Speech To Text)
            String rawSpeechText = "Example Voice Text Captured"; 
            
            // 2. معالجة النص المكتشف وترجمته تلقائياً حسب لغة اللاعبين المحيطين
            for (Player receiver : speaker.getWorld().getPlayers()) {
                if (receiver.getLocation().distance(speaker.getLocation()) <= 16) { // مدى الفويس شات
                    String targetLang = plugin.getTranslationManager().getPlayerLanguage(receiver.getUniqueId());
                    
                    plugin.getTranslationManager().translateAsync(rawSpeechText, targetLang, result -> {
                        // 3. عرض الترجمة الفورية كنص عائم فوق رأس اللاعب المتحدث
                        plugin.getHeadTranslationManager().spawnTranslationAboveHead(speaker, result);
                    });
                }
            }
        });
    }
}
