/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.Items.Items;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.DataExporter;
import cl.bebt.staffcore.sql.Queries.StaffQuery;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class StaffManager {
    
    private static main plugin;
    
    public StaffManager( main plugin ){
        StaffManager.plugin = plugin;
    }
    
    public static void enable( UUID uuid ){
        DataExporter.updateServerStats( "staff" );
        try {
            Player p = Bukkit.getPlayer( uuid );
            UserUtils.setStaffInventory( uuid , Serializer.itemStackArrayToBase64( p.getInventory( ).getContents( ) ) );
            UserUtils.setStaffArmor( uuid , Serializer.itemStackArrayToBase64( p.getInventory( ).getArmorContents( ) ) );
            p.getInventory( ).clear( );
            p.getInventory( ).setArmorContents( null );
            if ( !UserUtils.findUser( uuid ).getVanish( ) ) {
                VanishManager.enable( uuid );
            }
            p.getInventory( ).setItem( 0 , Items.vanishOn( ) );
            p.getInventory( ).setItem( 2 , Items.freeze( ) );
            p.getInventory( ).setItem( 3 , Items.serverManager( ) );
            p.getInventory( ).setItem( 5 , Items.randomTp( ) );
            p.getInventory( ).setItem( 6 , Items.InvSee( ) );
            p.getInventory( ).setItem( 8 , Items.staffOff( ) );
            p.setInvulnerable( true );
            if ( utils.mysqlEnabled( ) ) {
                StaffQuery.enable( p.getName( ) );
            }
            p.setAllowFlight( true );
            p.setFlying( true );
            UserUtils.setStaff( uuid , true );
        } catch ( PlayerNotFundException | NullPointerException ignored ) {
            ignored.printStackTrace( );
        }
    }
    
    public static void disable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                p.getInventory( ).clear( );
                try {
                    ItemStack[] inventory = Serializer.itemStackArrayFromBase64Inventory( UserUtils.getStaffInventory( uuid ) );
                    ItemStack[] armor = Serializer.itemStackArrayFromBase64Inventory( UserUtils.getStaffArmor( uuid ) );
                    p.getInventory( ).setContents( inventory );
                    p.getInventory( ).setArmorContents( armor );
                    UserUtils.setStaffInventory( uuid , "" );
                    UserUtils.setStaffArmor( uuid , "" );
                } catch ( IOException | NullPointerException | PlayerNotFundException ignored ) {
                }
                
                p.getInventory( ).removeItem( Items.vanishOn( ) );
                p.getInventory( ).removeItem( Items.vanishOff( ) );
                p.getInventory( ).removeItem( Items.staffOff( ) );
                p.getInventory( ).removeItem( Items.InvSee( ) );
                p.getInventory( ).removeItem( Items.serverManager( ) );
                p.getInventory( ).removeItem( Items.freeze( ) );
                p.getInventory( ).removeItem( Items.randomTp( ) );
                p.updateInventory( );
            } , 2L );
            if ( p.getGameMode( ).equals( GameMode.SURVIVAL ) || p.getGameMode( ).equals( GameMode.ADVENTURE ) ) {
                p.setAllowFlight( false );
                p.setFlying( false );
            }
            p.setInvulnerable( false );
            VanishManager.disable( uuid );
            if ( utils.mysqlEnabled( ) ) {
                StaffQuery.disable( p.getName( ) );
            }
            DataExporter.updateServerStats( "staff" );
            UserUtils.setStaff( uuid , false );
        } catch ( PlayerNotFundException | NullPointerException error ) {
            error.printStackTrace( );
        }
    }
    
    
}
