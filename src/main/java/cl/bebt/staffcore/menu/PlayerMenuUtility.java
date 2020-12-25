package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {
    private final Player owner;
    
    public PlayerMenuUtility( Player owner ){
        this.owner = owner;
    }
    
    public Player getOwner( ){
        return this.owner;
    }
    
    public int current( ){
        int current = 0;
        try {
            ConfigurationSection inventorySection = main.plugin.reports.getConfig( ).getConfigurationSection( "reports" );
            for ( String key : inventorySection.getKeys( false ) )
                current++;
        } catch ( NullPointerException nullPointerException ) {
        }
        return current;
    }
    
    public int currentBans( ){
        int current = 0;
        try {
            ConfigurationSection inventorySection = main.plugin.bans.getConfig( ).getConfigurationSection( "bans" );
            for ( String key : inventorySection.getKeys( false ) )
                current++;
        } catch ( NullPointerException nullPointerException ) {
        }
        return current;
    }
    
    public int currentWarns( ){
        int current = 0;
        try {
            ConfigurationSection inventorySection = main.plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            for ( String key : inventorySection.getKeys( false ) )
                current++;
        } catch ( NullPointerException nullPointerException ) {
        }
        return current;
    }
    
    public int currentPlayerWarns( String warned ){
        int warnings = 0;
        try {
            for ( int i = 1; i < currentWarns( ); i++ ) {
                if ( main.plugin.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) )
                    warnings++;
            }
        } catch ( NullPointerException nullPointerException ) {
        }
        return warnings;
    }
}
