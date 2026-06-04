package com.omnitranslate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MenuManager implements Listener {

    private final OmniTranslate plugin;

    public MenuManager(OmniTranslate plugin) {
        this.plugin = plugin;
    }

    public void openLanguageMenu(Player player) {
        String title = plugin.getConfig().getString("gui.title", "Languages");
        Inventory inv = Bukkit.createInventory(null, 27, Component.text(title));

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("gui.languages");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Material mat = Material.valueOf(section.getString(key + ".material"));
                int slot = section.getInt(key + ".slot");
                String name = section.getString(key + ".display-name");

                ItemStack glassPane = new ItemStack(mat);
                ItemMeta meta = glassPane.getItemMeta();
                if (meta != null) {
                    meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
                    glassPane.setItemMeta(meta);
                }
                inv.setItem(slot, glassPane);
            }
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = plugin.getConfig().getString("gui.title", "Languages");
        if (event.getView().title().equals(Component.text(title))) {
            event.setCancelled(true); // منع أخذ الزجاج
            
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            
            Player player = (Player) event.getWhoClicked();
            int clickedSlot = event.getSlot();

            // معرفة أي لغة تم اختيارها بناءً على الـ Slot
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("gui.languages");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    if (section.getInt(key + ".slot") == clickedSlot) {
                        plugin.getTranslationManager().setPlayerLanguage(player.getUniqueId(), key);
                        player.sendMessage("§a[OmniTranslate] Your language is now set to: " + key);
                        player.closeInventory();
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK") && event.getItem() != null) {
            Material configMaterial = Material.valueOf(plugin.getConfig().getString("settings.tool-material"));
            if (event.getItem().getType() == configMaterial) {
                openLanguageMenu(event.getPlayer());
            }
        }
    }
}
