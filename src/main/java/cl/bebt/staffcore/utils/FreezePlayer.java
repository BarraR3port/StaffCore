package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class FreezePlayer {
    
    private static final Plugin plugin = main.plugin;
    
    public static void FreezePlayer( Player p , String freezer , Boolean bol ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        String status = "";
        if ( bol ) {
            p.setAllowFlight( true );
            p.setInvulnerable( true );
            utils.PlaySound( p , "freeze" );
            utils.PlayParticle( p , "unfreeze_player" );
            PlayerData.set( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING , "frozen" );
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.set( p.getName( ) , "frozen" , "true" );
            }
            status = utils.getString( "freeze.freeze" , "lg" , null );
            try {
                PlayerData.set( new NamespacedKey( plugin , "frozen_helmet" ) , PersistentDataType.STRING , Serializer.serialize( p.getInventory( ).getHelmet( ) ) );
            } catch ( NullPointerException ignored ) { }
            if ( utils.getBoolean( "freeze.set_ice_block") ) {
                p.getInventory( ).setItem( 39 , new ItemStack( Material.BLUE_ICE ) );
            }
        } else {
            utils.PlaySound( p , "un_freeze" );
            utils.PlayParticle( p , "freeze_player" );
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR ||
                    PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ||
                    PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ||
                    PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )) ) {
                p.setAllowFlight( false );
                p.setInvulnerable( false );
            }
            PlayerData.remove( new NamespacedKey( plugin , "frozen" ) );
            if ( utils.mysqlEnabled( ) ) {
                SQLGetter.set( p.getName( ) , "frozen" , "false" );
            }
            status = utils.getString( "freeze.unfreeze" , "lg" , null );
            
            if ( utils.getBoolean( "freeze.set_ice_block") ) {
                try {
                    ItemStack helmet = Serializer.deserialize( PlayerData.get( new NamespacedKey( plugin , "frozen_helmet" ) , PersistentDataType.STRING ) );
                    p.getInventory( ).setHelmet( helmet );
                    PlayerData.remove( new NamespacedKey( plugin , "frozen_helmet" ) );
                } catch ( NullPointerException ignored ) {
                    p.getInventory( ).setHelmet( null );
                }
            }
        }
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.freeze") || people.hasPermission( "staffcore.staff" ) ) {
                for ( String key : utils.getStringList( "freeze" , "alerts" ) ) {
                    key = key.replace( "%frozen%" , p.getName( ) );
                    key = key.replace( "%freezer%" , freezer );
                    key = key.replace( "%status%" , status );
                    utils.tell( people , key );
                }
            }
        }
        SendMsg.sendFreezeAlert( freezer , p.getName( ) , bol , utils.getServer( ) );
    }
    
}
