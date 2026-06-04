package com.omnitranslate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class ToolListener implements Listener {

    private final OmniTranslate plugin;

    public ToolListener(OmniTranslate plugin) {
        this.plugin = plugin;
    }

    // منع رمي الأداة في الأرض
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isOmniTool(item)) {
            event.setCancelled(true);
        }
    }

    // منع سقوط الأداة عند الموت وحفظها في الانفنتوري
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        drops.removeIf(item -> {
            if (isOmniTool(item)) {
                event.getItemsToKeep().add(item); // حفظ الأداة للاعب
                return true;
            }
            return false;
        });
    }

    private boolean isOmniTool(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        Material configMaterial = Material.valueOf(plugin.getConfig().getString("settings.tool-material"));
        return item.getType() == configMaterial && item.getItemMeta().getPersistentDataContainer().has(plugin.getToolKey());
    }
}
