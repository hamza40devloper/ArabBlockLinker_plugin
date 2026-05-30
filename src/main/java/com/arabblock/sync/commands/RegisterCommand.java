package com.arabblock.sync.commands;

import com.arabblock.sync.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        // التحقق مما إذا كان اللاعب مسجلاً بالفعل
        if (Main.getInstance().getDbManager().isPlayerRegistered(player.getUniqueId())) {
            player.sendMessage("§cأنت تمتلك حساباً بالفعل! استخدم الأمر /login <كلمة_السر>");
            return true;
        }

        // التأكد من أن اللاعب قد أكمل خطوة التحقق عبر الديسكورد ولم يعد البلوجين ينتظر منه كوداً
        if (Main.getInstance().getAuthManager().isWaitingForCode(player)) {
            player.sendMessage("§cيجب عليك تأكيد حساب الديسكورد أولاً بإدخال الكود المكون من 6 أرقام!");
            return true;
        }

        // التحقق من أن اللاعب كتب كلمة سر مع الأمر
        if (args.length == 0) {
            player.sendMessage("§cالاستخدام الصحيح: /register <كلمة_السر>");
            return true;
        }

        String password = args[0];
        
        // حفظ حساب اللاعب وكلمة السر في قاعدة البيانات
        Main.getInstance().getDbManager().registerPlayer(player.getUniqueId(), password);
        
        // تعيين حالة اللاعب كـ "مسجل دخوله" ليتمكن من الحركة والكلام
        Main.getInstance().getAuthManager().setPlayerLoggedIn(player, true);

        player.sendMessage("§a§lتم إنشاء حسابك بنجاح! مرحباً بك في السيرفر.");

        // نقل اللاعب من عالم تسجيل الدخول إلى العالم الأساسي للسيرفر (العالم رقم 0)
        Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        player.teleport(spawnLocation);

        return true;
    }
}
