package com.arabblock.sync;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldManager {
    
    private final String LOGIN_WORLD_NAME = "login_world";

    public void loadLoginWorld() {
        Main.getInstance().getLogger().info("[World] جاري التحقق من عالم تسجيل الدخول...");

        World loginWorld = Bukkit.getWorld(LOGIN_WORLD_NAME);

        if (loginWorld == null) {
            Main.getInstance().getLogger().info("[World] عالم تسجيل الدخول غير موجود، جاري إنشاؤه...");
            
            // إنشاء عالم مسطح (Flat) وفارغ ليكون خفيفاً على السيرفر
            WorldCreator creator = new WorldCreator(LOGIN_WORLD_NAME);
            creator.type(WorldType.FLAT);
            creator.generateStructures(false); // إيقاف توليد القرى والكهوف
            
            loginWorld = creator.createWorld();
            
            if (loginWorld != null) {
                // إعدادات العالم ليكون مناسباً للوبي الدخول
                loginWorld.setPVP(false);
                loginWorld.setSpawnFlags(false, false); // إيقاف رسبنة الوحوش والحيوانات
                loginWorld.setTime(6000); // جعل الوقت نهاراً دائماً
                loginWorld.setStorm(false); // إيقاف المطر
                
                Main.getInstance().getLogger().info("[World] تم إنشاء وتحميل عالم تسجيل الدخول بنجاح!");
            } else {
                Main.getInstance().getLogger().severe("[World] حدث خطأ أثناء إنشاء عالم تسجيل الدخول!");
            }
        } else {
            Main.getInstance().getLogger().info("[World] تم العثور على عالم تسجيل الدخول وهو جاهز.");
        }
    }
    
    public String getLoginWorldName() {
        return LOGIN_WORLD_NAME;
    }
}
