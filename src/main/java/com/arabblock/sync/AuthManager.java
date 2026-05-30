package com.arabblock.sync;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class AuthManager {

    // ذاكرة مؤقتة لحفظ أكواد اللاعبين الذين يحاولون الدخول (UUID -> Code)
    private final HashMap<UUID, String> pendingCodes = new HashMap<>();
    
    // ذاكرة لحفظ حالة اللاعبين (هل قاموا بتسجيل الدخول أم لا)
    private final HashMap<UUID, Boolean> loggedInPlayers = new HashMap<>();

    // دالة: توليد كود من 6 أرقام للاعب
    public String generateDiscordCode(Player player) {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // توليد رقم عشوائي بين 100000 و 999999
        String codeString = String.valueOf(code);
        
        // حفظ الكود في الذاكرة مع حساب اللاعب
        pendingCodes.put(player.getUniqueId(), codeString);
        return codeString;
    }

    // دالة: التحقق من الكود الذي كتبه اللاعب في الشات
    public boolean verifyDiscordCode(Player player, String inputCode) {
        UUID playerId = player.getUniqueId();
        
        if (pendingCodes.containsKey(playerId)) {
            String correctCode = pendingCodes.get(playerId);
            
            if (correctCode.equals(inputCode)) {
                pendingCodes.remove(playerId); // حذف الكود بعد إدخاله بشكل صحيح لمنع إعادة استخدامه
                return true;
            }
        }
        return false;
    }

    // دالة: تعيين اللاعب كـ "مسجل دخوله بنجاح"
    public void setPlayerLoggedIn(Player player, boolean status) {
        loggedInPlayers.put(player.getUniqueId(), status);
    }

    // دالة: التحقق مما إذا كان اللاعب قد سجل دخوله بالفعل ليتمكن من اللعب
    public boolean isPlayerLoggedIn(Player player) {
        return loggedInPlayers.getOrDefault(player.getUniqueId(), false);
    }

    // دالة: التحقق مما إذا كان البلوجين ينتظر من هذا اللاعب إدخال كود
    public boolean isWaitingForCode(Player player) {
        return pendingCodes.containsKey(player.getUniqueId());
    }
}
