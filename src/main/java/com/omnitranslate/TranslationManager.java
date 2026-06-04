package com.omnitranslate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TranslationManager {

    private final OmniTranslate plugin;
    // كاش لحفظ الجمل المترجمة لتوفير الأداء
    private final Cache<String, String> translationCache;
    // حفظ لغة اللاعب المختارة (UUID -> Language String)
    private final Cache<UUID, String> playerLanguages;

    public TranslationManager(OmniTranslate plugin) {
        this.plugin = plugin;
        int expiry = plugin.getConfig().getInt("settings.cache-expiry-minutes", 30);
        
        this.translationCache = CacheBuilder.newBuilder()
                .expireAfterAccess(expiry, TimeUnit.MINUTES)
                .build();
                
        this.playerLanguages = CacheBuilder.newBuilder().build();
    }

    public String getPlayerLanguage(UUID uuid) {
        String lang = playerLanguages.getIfPresent(uuid);
        return lang != null ? lang : "ENGLISH"; // اللغة الافتراضية
    }

    public void setPlayerLanguage(UUID uuid, String lang) {
        playerLanguages.put(uuid, lang);
    }

    // دالة محاكاة الترجمة الفورية بصيغة Async
    public void translateAsync(String text, String targetLang, TranslationCallback callback) {
        String cacheKey = text + "_" + targetLang;
        String cachedValue = translationCache.getIfPresent(cacheKey);

        if (cachedValue != null) {
            callback.onResult(cachedValue);
            return;
        }

        // تشغيل الطلب في الخلفية لحماية السيرفر من اللاج
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // هنا يتم الربط مع الـ API (مثل LibreTranslate أو Google)
            // سأقوم بمحاكاة الترجمة للـ MVP كمثال
            String translatedText = "[Translated to " + targetLang + "] " + text; 
            
            // حفظ النتيجة في الكاش
            translationCache.put(cacheKey, translatedText);
            
            // العودة للمسار الرئيسي لإرسال النتيجة للاعبين
            Bukkit.getScheduler().runTask(plugin, () -> callback.onResult(translatedText));
        });
    }

    public interface TranslationCallback {
        void onResult(String result);
    }
}
