package com.arabblock.sync;

import com.arabblock.sync.commands.LoginCommand;
import com.arabblock.sync.commands.RegisterCommand;
import com.arabblock.sync.listeners.PlayerChat;
import com.arabblock.sync.listeners.PlayerJoin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

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
        
        // 1. تشغيل قاعدة البيانات
        dbManager = new DatabaseManager();
        dbManager.setup();

        // 2. تحميل أو إنشاء "عالم تسجيل الدخول"
        worldManager = new WorldManager();
        worldManager.loadLoginWorld();

        // 3. تشغيل نظام الحماية
        authManager = new AuthManager();

        // 4. تشغيل خادم الاتصال لانتظار البوت
        networkManager = new NetworkManager();
        networkManager.startServer();

        // 5. تسجيل الأحداث (Listeners)
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);

        // 6. تسجيل الأوامر (Commands)
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());

        getLogger().info("تم التشغيل بنجاح! جاهز للربط مع ديسكورد.");
        getLogger().info("====================================");
    }

    @Override
    public void onDisable() {
        if (networkManager != null) networkManager.stopServer();
        getLogger().info("تم إيقاف نظام ArabBlock Sync.");
    }

    public static Main getInstance() { return instance; }
    public AuthManager getAuthManager() { return authManager; }
    public DatabaseManager getDbManager() { return dbManager; }
    public WorldManager getWorldManager() { return worldManager; }
}
