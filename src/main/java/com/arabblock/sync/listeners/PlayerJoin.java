package com.arabblock.sync.listeners;

import com.arabblock.sync.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 1. تعيين حالة اللاعب كغير مسجل دخوله بعد في النظام المؤقت
        Main.getInstance().getAuthManager().setPlayerLoggedIn(player, false);

        // 2. نقل اللاعب إلى عالم تسجيل الدخول تلقائياً
        String worldName = Main.getInstance().getWorldManager().getLoginWorldName();
        World loginWorld = Bukkit.getWorld(worldName);
        
        if (loginWorld != null) {
            Location spawnLocation = loginWorld.getSpawnLocation();
            player.teleport(spawnLocation);
        }

        // 3. التحقق من حالة اللاعب في قاعدة البيانات لإرسال الرسالة المناسبة
        boolean isRegistered = Main.getInstance().getDbManager().isPlayerRegistered(player.getUniqueId());

        player.sendMessage("§e====================================");
        player.sendMessage("§aأهلاً بك في سيرفر §lArabBlock§r§a!");
        
        if (isRegistered) {
            // اللاعب مسجل حسابه مسبقاً، يطلب منه كتابة كلمة السر
            player.sendMessage("§6حسابك محمي! يرجى تسجيل الدخول باستخدام الأمر التالي:");
            player.sendMessage("§b/login <كلمة_السر>");
        } else {
            // اللاعب جديد، يطلب منه ربط حسابه بالديسكورد أولاً لحمايته
            player.sendMessage("§6لحماية حسابك وتفعيله، يجب ربطه بحساب الديسكورد أولاً.");
            player.sendMessage("§cيرجى كتابة اسم حسابك في الديسكورد (Username) في الشات مباشرة:");
        }
        player.sendMessage("§e====================================");
    }
}
