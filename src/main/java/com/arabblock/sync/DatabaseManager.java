package com.arabblock.sync;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DatabaseManager {

    private File file;
    private FileConfiguration config;

    // دالة: تجهيز وإنشاء ملف قاعدة البيانات (players.yml)
    public void setup() {
        // إنشاء مجلد البلوجين إذا لم يكن موجوداً
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdir();
        }

        // إنشاء الملف الذي ستحفظ فيه البيانات
        file = new File(Main.getInstance().getDataFolder(), "players.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Main.getInstance().getLogger().info("[Database] تم إنشاء ملف بيانات اللاعبين (players.yml) بنجاح.");
            } catch (IOException e) {
                Main.getInstance().getLogger().severe("[Database] حدث خطأ أثناء إنشاء ملف البيانات!");
            }
        }

        // تحميل الملف ليصبح جاهزاً للقراءة والكتابة
        config = YamlConfiguration.loadConfiguration(file);
    }

    // دالة: حفظ البيانات في الملف فعلياً
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("[Database] حدث خطأ، لم يتم حفظ البيانات بنجاح!");
        }
    }

    // دالة: تسجيل حساب جديد (حفظ كلمة السر للاعب بناءً على الـ UUID الخاص به)
    public void registerPlayer(UUID uuid, String password) {
        config.set("players." + uuid.toString() + ".password", password);
        save();
    }

    // دالة: جلب كلمة السر الخاصة باللاعب لمقارنتها عند تسجيل الدخول
    public String getPlayerPassword(UUID uuid) {
        return config.getString("players." + uuid.toString() + ".password");
    }

    // دالة: التحقق مما إذا كان اللاعب مسجلاً بالفعل في السيرفر أم أنه جديد
    public boolean isPlayerRegistered(UUID uuid) {
        return config.contains("players." + uuid.toString() + ".password");
    }
    
    // دالة: ربط حساب ماين كرافت بحساب الديسكورد الخاص باللاعب
    public void linkDiscordAccount(UUID uuid, String discordId) {
        config.set("players." + uuid.toString() + ".discord_id", discordId);
        save();
    }
    
    // دالة: التحقق مما إذا كان اللاعب قد ربط حسابه بالديسكورد مسبقاً
    public boolean hasDiscordLinked(UUID uuid) {
        return config.contains("players." + uuid.toString() + ".discord_id");
    }
}
