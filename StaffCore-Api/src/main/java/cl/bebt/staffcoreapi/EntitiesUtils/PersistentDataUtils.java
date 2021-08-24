/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.EntitiesUtils;


import cl.bebt.staffcoreapi.Entities.PersistentData;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.utils.Utils;
import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class PersistentDataUtils {
    
    private static final HashMap < ItemStack, PersistentData > ItemsData = new HashMap <>( );
    private static ArrayList < PersistentData > Items = new ArrayList <>( );
    
    public static ArrayList < PersistentData > getPersistentData( ){
        return Items;
    }
    
    public static HashMap < ItemStack, PersistentData > getPersistentDataItems( ){
        return ItemsData;
    }
    
    public static void save( String string , Object value , ItemStack itemStack , UUID ownerUUID , PersistentDataType type ){
        HashMap < String, Object > values = new HashMap <>( );
        values.put( string , value );
        PersistentData persistentData = new PersistentData( values , itemStack , UUID.randomUUID( ) , ownerUUID , type );
        ItemsData.put( itemStack , persistentData );
    }
    
    public static void save( String string , Object value , UUID ownerUUID , PersistentDataType type ){
        HashMap < String, Object > values = new HashMap <>( );
        values.put( string , value );
        PersistentData persistentData = new PersistentData( values , UUID.randomUUID( ) , ownerUUID , type );
        Items.add( persistentData );
        saveItems( );
    }
    
    
    public static ArrayList < PersistentData > getSavedItems( UUID OwnerUUID ){
        ArrayList < PersistentData > items = new ArrayList <>( );
        for ( PersistentData item : Items ) {
            if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                items.add( item );
            }
        }
        return items;
    }
    
    public static void clearItem( ItemStack itemStack ){
        ItemsData.remove( itemStack );
    }
    
    public static void remove( UUID OwnerUUID , String key ){
        if ( has( OwnerUUID , key ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                    
                    HashMap < String, String > StringValues = item.getStringValues( );
                    StringValues.remove( key );
                    item.setStringValues( StringValues );
                    
                    HashMap < String, Boolean > BooleanValues = item.getBooleanValues( );
                    StringValues.remove( key );
                    item.setBooleanValues( BooleanValues );
                    
                    HashMap < String, Long > LongValues = item.getLongValues( );
                    StringValues.remove( key );
                    item.setLongValues( LongValues );
                    
                    saveItems( );
                }
            }
        }
    }
    
    
    public static PersistentData getPersistentData( ItemStack item ){
        for ( ItemStack itemStack : ItemsData.keySet( ) ) {
            if ( item.equals( itemStack ) ) {
                return ItemsData.get( item );
            }
        }
        return null;
    }
    
    
    public static String getString( ItemStack itemStack , String key ){
        if ( has( itemStack , key , PersistentDataType.STRING ) ) {
            for ( PersistentData item : ItemsData.values( ) ) {
                if ( item.getItemStack( ).equals( itemStack ) ) {
                    return item.getStringValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static String getString( UUID OwnerUUID , String key ){
        if ( has( OwnerUUID , key ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                    return item.getStringValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Integer getInteger( ItemStack itemStack , String key ){
        if ( has( itemStack , key , PersistentDataType.INTEGER ) ) {
            for ( PersistentData item : ItemsData.values( ) ) {
                if ( item.getItemStack( ).equals( itemStack ) ) {
                    return item.getIntegerValues( ).get( key );
                }
            }
        }
        return 0;
    }
    
    public static Integer getInteger( UUID OwnerUUID , String key ){
        if ( has( OwnerUUID , key ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                    return item.getIntegerValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Boolean getBoolean( ItemStack itemStack , String key ){
        if ( has( itemStack , key , PersistentDataType.BOOLEAN ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getItemStack( ).equals( itemStack ) ) {
                    return item.getBooleanValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Boolean getBoolean( UUID OwnerUUID , String key ){
        if ( has( OwnerUUID , key ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                    return item.getBooleanValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Long getLong( ItemStack itemStack , String key ){
        if ( has( itemStack , key , PersistentDataType.LONG ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getItemStack( ).equals( itemStack ) ) {
                    return item.getLongValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Long getLong( UUID OwnerUUID , String key ){
        if ( has( OwnerUUID , key ) ) {
            for ( PersistentData item : Items ) {
                if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                    return item.getLongValues( ).get( key );
                }
            }
        }
        return null;
    }
    
    public static Boolean has( ItemStack itemStack , String key , PersistentDataType type ){
        if ( type.equals( PersistentDataType.STRING ) ) {
            return ItemsData.containsKey( itemStack ) && ItemsData.get( itemStack ).getStringValues( ).containsKey( key );
        } else if ( type.equals( PersistentDataType.BOOLEAN ) ) {
            return ItemsData.containsKey( itemStack ) && ItemsData.get( itemStack ).getBooleanValues( ).containsKey( key );
        } else if ( type.equals( PersistentDataType.INTEGER ) ) {
            return ItemsData.containsKey( itemStack ) && ItemsData.get( itemStack ).getIntegerValues( ).containsKey( key );
        } else if ( type.equals( PersistentDataType.LONG ) ) {
            return ItemsData.containsKey( itemStack ) && ItemsData.get( itemStack ).getLongValues( ).containsKey( key );
        } else {
            return false;
        }
        
    }
    
    public static Boolean has( UUID OwnerUUID , String key ){
        for ( PersistentData item : Items ) {
            if ( item.getOwnerUUID( ).equals( OwnerUUID ) ) {
                if ( item.getStringValues( ).containsKey( key ) ) {
                    return true;
                } else if ( item.getIntegerValues( ).containsKey( key ) ) {
                    return true;
                } else if ( item.getBooleanValues( ).containsKey( key ) ) {
                    return true;
                } else return item.getLongValues( ).containsKey( key );
            }
        }
        return false;
    }
    
    public static void saveItems( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/items.json" );
            file.getParentFile( ).mkdir( );
            file.createNewFile( );
            Writer writer = new FileWriter( file , false );
            gson.toJson( Items , writer );
            writer.flush( );
            writer.close( );
        } catch ( IOException error ) {
            error.printStackTrace( );
        }
    }
    
    public static void loadItems( ){
        try {
            Gson gson = new Gson( );
            File file = new File( Utils.getDataFolder( ) + "/items.json" );
            if ( file.exists( ) ) {
                Reader reader = new FileReader( file );
                PersistentData[] data = gson.fromJson( reader , PersistentData[].class );
                Items = new ArrayList <>( Arrays.asList( data ) );
            } else {
                saveItems( );
            }
        } catch ( IOException | NullPointerException ignored ) {
        
        }
    }
    
}
