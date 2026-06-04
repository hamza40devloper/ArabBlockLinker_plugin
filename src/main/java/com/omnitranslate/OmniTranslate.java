package com.omnitranslate;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class OmniTranslate extends JavaPlugin {

    private TranslationManager translationManager;
    private HeadTranslationManager headTranslationManager;
    private DiscordWebhookManager discordWebhookManager;
    private NamespacedKey toolKey;

    @Override
    public void onEnable() {
        // حفظ ملف الـ Config الافتراضي
        saveDefaultConfig();
        
        this.toolKey = new NamespacedKey(this, "omni_tool");
        this.translationManager = new TranslationManager(this);
        this.headTranslationManager = new HeadTranslationManager(this);
        this.discordWebhookManager = new DiscordWebhookManager(this);

        // تسجيل الأحداث (Listeners)
        getServer().getPluginManager().registerEvents(new ToolListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuManager(this), this);
        
        getLogger().info("OmniTranslate MVP Has Been Enabled Successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("OmniTranslate Has Been Disabled.");
    }

    public TranslationManager getTranslationManager() { return translationManager; }
    public HeadTranslationManager getHeadTranslationManager() { return headTranslationManager; }
    public DiscordWebhookManager getDiscordWebhookManager() { return discordWebhookManager; }
    public NamespacedKey getToolKey() { return toolKey; }
}
