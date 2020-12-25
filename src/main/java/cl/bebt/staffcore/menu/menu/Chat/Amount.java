package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Amount extends PaginatedMenu {
    private final main plugin;

    private final Player player;

    public Amount(PlayerMenuUtility playerMenuUtility, main plugin, Player player) {
        super(playerMenuUtility);
        this.plugin = plugin;
        this.player = player;
    }

    public String getMenuName() {
        return utils.chat("&cChose the amount:");
    }

    public int getSlots() {
        return 54;
    }

    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        int amount = 49;
        if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "amount"), PersistentDataType.INTEGER)) {
            p.closeInventory();
            int yep = ((Integer)e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "amount"), PersistentDataType.INTEGER)).intValue();
            this.player.getPersistentDataContainer().set(new NamespacedKey((Plugin)main.plugin, "amount"), PersistentDataType.INTEGER, Integer.valueOf(yep));
            (new Quantity(this.playerMenuUtility, this.plugin, this.player)).open(p);
        } else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
            p.closeInventory();
            (new MutePlayer(this.playerMenuUtility, main.plugin)).open(p);
        } else if (e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Back")) {
                if (this.page == 0) {
                    p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                } else {
                    this.page--;
                    p.closeInventory();
                    open(p);
                }
            } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next")) {
                if (this.index + 1 < amount) {
                    this.page++;
                    p.closeInventory();
                    open(p);
                } else {
                    p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }
        }
    }

    public void setMenuItems() {
        addMenuBorder();
        int amount = 49;
        this.player.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "seconds"));
        this.player.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "amount"));
        for (int i = 1; i < getMaxItemsPerPage() + 1; i++) {
            this.index = getMaxItemsPerPage() * this.page + i;
            if (this.index >= amount)
                break;
            ItemStack clock = new ItemStack(Material.CLOCK, this.index);
            ItemMeta meta = clock.getItemMeta();
            meta.setDisplayName(utils.chat("&a") + this.index);
            meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)main.plugin, "amount"), PersistentDataType.INTEGER, Integer.valueOf(this.index));
            clock.setItemMeta(meta);
            this.inventory.addItem(new ItemStack[] { clock });
        }
    }
}
