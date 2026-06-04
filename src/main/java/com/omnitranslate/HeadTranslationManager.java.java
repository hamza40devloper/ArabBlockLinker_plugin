package com.omnitranslate;

import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;

public class HeadTranslationManager {

    private final OmniTranslate plugin;

    public HeadTranslationManager(OmniTranslate plugin) {
        this.plugin = plugin;
    }

    public void spawnTranslationAboveHead(Player speaker, String translatedText) {
        // استدعاء الكيان مباشرة فوق اللاعب بصيغة متوافقة مع الأداء
        speaker.getWorld().spawn(speaker.getLocation().add(0, 2.4, 0), TextDisplay.class, textDisplay -> {
            textDisplay.text(Component.text("§b" + translatedText));
            textDisplay.setBillboard(Display.Billboard.CENTER); // يلتف تلقائياً مع حركة رؤية اللاعبين
            textDisplay.setShadowed(true);
            
            // إعداد الخلفية المسطحة بناء على كود الـ Hex في الكوبفيج
            String argb = plugin.getConfig().getString("text-display.background-color", "000000");
            textDisplay.setBackgroundColor(Color.fromRGB(Integer.parseInt(argb, 16)));
            
            // جعل النص يتبع اللاعب برمجياً أو عبر التثبيت (Riding)
            speaker.addPassenger(textDisplay);

            // تدمير الكيان تلقائياً بعد 6 ثوانٍ من انتهاء المحادثة لتجنب تراكم الكيانات (Entities lag)
            plugin.getServer().getScheduler().runTaskLater(plugin, textDisplay::remove, 120L);
        });
    }
}
