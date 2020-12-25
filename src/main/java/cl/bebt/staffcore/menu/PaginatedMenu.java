package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public abstract class PaginatedMenu extends Menu {
    protected int page = 0;

    protected int maxItemsPerPage = 28;

    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {
        this.inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Back", new String[0]));
        this.inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close", new String[0]));
        this.inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Next", new String[0]));
        int i;
        for (i = 0; i < 10; i++) {
            if (this.inventory.getItem(i) == null)
                this.inventory.setItem(i, redPanel());
        }
        this.inventory.setItem(17, redPanel());
        this.inventory.setItem(18, redPanel());
        this.inventory.setItem(26, redPanel());
        this.inventory.setItem(27, redPanel());
        this.inventory.setItem(35, redPanel());
        this.inventory.setItem(36, redPanel());
        for (i = 44; i < 54; i++) {
            if (this.inventory.getItem(i) == null)
                this.inventory.setItem(i, redPanel());
        }
    }

    public int currentBans() {
        int current = 0;
        for (int i = 0; i <= main.plugin.bans.getConfig().getInt("count") + main.plugin.bans.getConfig().getInt("current"); i++) {
            try {
                if (main.plugin.bans.getConfig().get("bans." + i + ".status") != null)
                    current++;
            } catch (NullPointerException nullPointerException) {}
        }
        main.plugin.bans.getConfig().set("current", Integer.valueOf(current));
        return current;
    }

    public int getMaxItemsPerPage() {
        return this.maxItemsPerPage;
    }
}
