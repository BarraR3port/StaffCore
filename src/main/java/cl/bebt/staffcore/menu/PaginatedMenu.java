package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public abstract class PaginatedMenu extends Menu{

    protected int page = 0;

    protected int maxItemsPerPage = 28;

    protected int index = 0;

    public PaginatedMenu( PlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }

    public void addMenuBorder( ){
        inventory.setItem( 48 , makeItem( Material.DARK_OAK_BUTTON , ChatColor.GREEN + "Back" ) );

        inventory.setItem( 49 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );

        inventory.setItem( 50 , makeItem( Material.DARK_OAK_BUTTON , ChatColor.GREEN + "Next" ) );

        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        inventory.setItem( 17 , super.redPanel( ) );
        inventory.setItem( 18 , super.redPanel( ) );
        inventory.setItem( 26 , super.redPanel( ) );
        inventory.setItem( 27 , super.redPanel( ) );
        inventory.setItem( 35 , super.redPanel( ) );
        inventory.setItem( 36 , super.redPanel( ) );

        for ( int i = 44; i < 54; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
    }

    public int currentBans( ){
        int current = 0;
        for ( int i = 0; i <= main.plugin.baned.getConfig( ).getInt( "count" ) + main.plugin.baned.getConfig( ).getInt( "current" ); i++ ) {
            try {
                if ( main.plugin.baned.getConfig( ).get( "bans." + i + ".status" ) != null ) {
                    current++;
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        main.plugin.baned.getConfig( ).set( "current" , current );
        return current;
    }

    public int getMaxItemsPerPage( ){
        return maxItemsPerPage;
    }
}

