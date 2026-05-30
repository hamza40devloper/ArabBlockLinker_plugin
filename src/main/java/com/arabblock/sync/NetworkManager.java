package com.arabblock.sync;

public class NetworkManager {

    public void registerPlayer(String playerName, String password) {
        Main.getInstance().getLogger().info("[Network] جاري إرسال بيانات تسجيل اللاعب " + playerName + " إلى الشبكة...");
        // هنا سيتم ربط الكود ببوت الديسكورد أو قاعدة البيانات لاحقاً
    }

    public boolean verifyLogin(String playerName, String password) {
        Main.getInstance().getLogger().info("[Network] جاري التحقق من بيانات دخول اللاعب " + playerName);
        return true; 
    }
}
