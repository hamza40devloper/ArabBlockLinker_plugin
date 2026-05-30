package com.arabblock.sync;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    // تعريف المديرين (Managers)
    private static Main instance;
    private DatabaseManager dbManager;
    private WorldManager worldManager;
    private NetworkManager networkManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("====================================");
        getLogger().info("جاري تشغيل نظام ArabBlock Sync...");
        
        // 1. تشغيل قاعدة البيانات (حفظ بيانات اللاعبين)
        dbManager = new DatabaseManager();
        dbManager.setup();

        // 2. تحميل أو إنشاء "عالم تسجيل الدخول"
        worldManager = new WorldManager();
        worldManager.loadLoginWorld();

        // 3. تشغيل نظام الحماية والأكواد العشوائية
        authManager = new AuthManager();

        // 4. تشغيل خادم محلي صغير (HTTP Server) لانتظار أوامر البوت مثل /setup
        networkManager = new NetworkManager();
        networkManager.startServer();

        // تسجيل الأحداث (Listeners)
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        // getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        // getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        getLogger().info("تم التشغيل بنجاح! جاهز للربط مع ديسكورد.");
        getLogger().info("====================================");
    }

    @Override
    public void onDisable() {
        // إغلاق الاتصال بقاعدة البيانات والشبكة عند إطفاء السيرفر
        if (networkManager != null) networkManager.stopServer();
        getLogger().info("تم إيقاف نظام ArabBlock Sync.");
    }

    // دوال لجلب المديرين من الكلاسات الأخرى
    public static Main getInstance() { return instance; }
    public AuthManager getAuthManager() { return authManager; }
    public DatabaseManager getDbManager() { return dbManager; }
    public WorldManager getWorldManager() { return worldManager; }
}
