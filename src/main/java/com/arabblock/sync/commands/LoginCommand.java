package com.arabblock.sync.commands;

import com.arabblock.sync.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        // التحقق مما إذا كان اللاعب قد سجل دخوله بالفعل في هذه الجلسة
        if (Main.getInstance().getAuthManager().isPlayerLoggedIn(player)) {
            player.sendMessage("§cأنت مسجل الدخول بالفعل وتلعب الآن!");
            return true;
        }

        // التحقق مما إذا كان اللاعب لا يمتلك حساباً من الأساس
        if (!Main.getInstance().getDbManager().isPlayerRegistered(player.getUniqueId())) {
            player.sendMessage("§cأنت غير مسجل! يرجى ربط حسابك بالديسكورد أولاً ثم استخدام /register");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cالاستخدام الصحيح: /login <كلمة_السر>");
            return true;
        }

        String inputPassword = args[0];
        String realPassword = Main.getInstance().getDbManager().getPlayerPassword(player.getUniqueId());

        // مقارنة كلمة السر
        if (inputPassword.equals(realPassword)) {
            Main.getInstance().getAuthManager().setPlayerLoggedIn(player, true);
            player.sendMessage("§a§lتم تسجيل الدخول بنجاح! عودة حميدة.");
            
            // نقله من عالم تسجيل الدخول إلى العالم الأساسي
            Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
            player.teleport(spawnLocation);
        } else {
            player.sendMessage("§cكلمة السر خاطئة! حاول مجدداً.");
        }

        return true;
    }
}
