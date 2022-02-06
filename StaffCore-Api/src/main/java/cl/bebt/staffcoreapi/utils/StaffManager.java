/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.Items.Items;
import cl.bebt.staffcoreapi.SQL.Queries.StaffQuery;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class StaffManager {
    
    
    public static void enable( UUID uuid ){
        DataExporter.updateServerStats( UpdateType.STAFF );
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
            if ( Utils.mysqlEnabled( ) ) {
                StaffQuery.enable( p.getName( ) );
            }
            p.setAllowFlight( true );
            p.setFlying( true );
            UserUtils.setStaff( uuid , true );
        } catch ( NullPointerException ignored ) {
            ignored.printStackTrace( );
        }
    }
    
    public static void disable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            Utils.runSyncDelayed( ( ) -> {
                p.getInventory( ).clear( );
                try {
                    ItemStack[] inventory = Serializer.itemStackArrayFromBase64Inventory( UserUtils.getStaffInventory( uuid ) );
                    ItemStack[] armor = Serializer.itemStackArrayFromBase64Inventory( UserUtils.getStaffArmor( uuid ) );
                    p.getInventory( ).setContents( inventory );
                    p.getInventory( ).setArmorContents( armor );
                    UserUtils.setStaffInventory( uuid , "" );
                    UserUtils.setStaffArmor( uuid , "" );
                } catch ( IOException | NullPointerException ignored ) {
                    ignored.printStackTrace();
                }
                
                p.getInventory( ).removeItem( Items.vanishOn( ) );
                p.getInventory( ).removeItem( Items.vanishOff( ) );
                p.getInventory( ).removeItem( Items.staffOff( ) );
                p.getInventory( ).removeItem( Items.InvSee( ) );
                p.getInventory( ).removeItem( Items.serverManager( ) );
                p.getInventory( ).removeItem( Items.freeze( ) );
                p.getInventory( ).removeItem( Items.randomTp( ) );
                p.updateInventory( );
            } , 6L );
            if ( p.getGameMode( ).equals( GameMode.SURVIVAL ) || p.getGameMode( ).equals( GameMode.ADVENTURE ) ) {
                p.setAllowFlight( false );
                p.setFlying( false );
            }
            p.setInvulnerable( false );
            VanishManager.disable( uuid );
            if ( Utils.mysqlEnabled( ) ) {
                StaffQuery.disable( p.getName( ) );
            }
            DataExporter.updateServerStats( UpdateType.STAFF );
            UserUtils.setStaff( uuid , false );
        } catch ( NullPointerException error ) {
            error.printStackTrace( );
        }
    }
    
    
}
