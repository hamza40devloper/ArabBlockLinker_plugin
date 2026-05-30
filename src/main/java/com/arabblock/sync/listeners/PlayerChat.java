package com.arabblock.sync.listeners;

import com.arabblock.sync.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // 1. التحقق مما إذا كان اللاعب قد سجل دخوله بالفعل
        boolean isLoggedIn = Main.getInstance().getAuthManager().isPlayerLoggedIn(player);
        
        // إذا كان مسجل الدخول، نسمح له بالدردشة بشكل طبيعي وننهي الكود هنا
        if (isLoggedIn) {
            return;
        }

        // 2. إذا لم يكن مسجل الدخول، نقوم بإلغاء إرسال رسالته للشات العام (لكي لا يرى أحد كلمة سره أو حسابه)
        event.setCancelled(true);

        boolean isRegistered = Main.getInstance().getDbManager().isPlayerRegistered(player.getUniqueId());

        if (isRegistered) {
            // اللاعب مسجل ولديه حساب، لكنه يحاول الكتابة في الشات قبل تسجيل الدخول
            player.sendMessage("§cلا يمكنك التحدث في الشات قبل تسجيل الدخول! استخدم: /login <password>");
            return;
        }

        // 3. التعامل مع اللاعب الجديد (الذي لم يسجل بعد)
        boolean isWaitingForCode = Main.getInstance().getAuthManager().isWaitingForCode(player);

        if (!isWaitingForCode) {
            // أ) اللاعب يكتب اسم حساب الديسكورد الخاص به لأول مرة
            String discordUsername = message.trim();
            
            // توليد كود من 6 أرقام
            String code = Main.getInstance().getAuthManager().generateDiscordCode(player);
            
            // (في المستقبل سنقوم بإرسال هذا الكود واسم الديسكورد لبوت بايثون ليرسله للاعب)
            Main.getInstance().getLogger().info("[DiscordSync] طلب ربط: حساب الديسكورد " + discordUsername + " الكود: " + code);
            
            player.sendMessage("§aتم طلب ربط حسابك بالديسكورد: §e" + discordUsername);
            player.sendMessage("§bيرجى التحقق من رسائلك الخاصة (DMs) في الديسكورد، سيصلك كود من 6 أرقام.");
            player.sendMessage("§eاكتب الكود هنا في الشات لتأكيد حسابك:");
            
        } else {
            // ب) اللاعب قام بإدخال كود الـ 6 أرقام
            boolean isCorrect = Main.getInstance().getAuthManager().verifyDiscordCode(player, message.trim());
            
            if (isCorrect) {
                player.sendMessage("§a§lتم التحقق من حسابك بنجاح!");
                player.sendMessage("§6الخطوة الأخيرة: قم بإنشاء كلمة سر لحسابك داخل اللعبة.");
                player.sendMessage("§bاستخدم الأمر: /register <كلمة_السر>");
            } else {
                player.sendMessage("§cالكود خاطئ! يرجى التأكد من الأرقام المرسلة لك في الديسكورد والمحاولة مجدداً.");
            }
        }
    }
}
