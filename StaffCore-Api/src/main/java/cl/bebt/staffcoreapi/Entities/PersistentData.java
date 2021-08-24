/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.Entities;

import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersistentData {
    
    private HashMap < String, String > StringValues = new HashMap <>( );
    
    private HashMap < String, Boolean > BooleanValues = new HashMap <>( );
    
    private HashMap < String, Integer > IntegerValues = new HashMap <>( );
    
    private HashMap < String, Long > LongValues = new HashMap <>( );
    
    private ItemStack ItemStack;
    
    private UUID ItemUUID;
    
    private UUID OwnerUUID;
    
    
    public PersistentData( HashMap < String, Object > hashMap , ItemStack itemStack , UUID itemUUID , UUID ownerUUID , PersistentDataType type ){
        if ( type.equals( PersistentDataType.STRING ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof String ) {
                    StringValues.put( entry.getKey( ) , ( String ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.INTEGER ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Integer ) {
                    IntegerValues.put( entry.getKey( ) , ( Integer ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.BOOLEAN ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Boolean ) {
                    BooleanValues.put( entry.getKey( ) , ( Boolean ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.LONG ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Long ) {
                    LongValues.put( entry.getKey( ) , ( Long ) entry.getValue( ) );
                }
            }
        }
        ItemStack = itemStack;
        ItemUUID = itemUUID;
        OwnerUUID = ownerUUID;
    }
    
    public PersistentData( HashMap < String, Object > hashMap , UUID itemUUID , UUID ownerUUID , PersistentDataType type ){
        if ( type.equals( PersistentDataType.STRING ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof String ) {
                    StringValues.put( entry.getKey( ) , ( String ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.INTEGER ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Integer ) {
                    IntegerValues.put( entry.getKey( ) , ( Integer ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.BOOLEAN ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Boolean ) {
                    BooleanValues.put( entry.getKey( ) , ( Boolean ) entry.getValue( ) );
                }
            }
        } else if ( type.equals( PersistentDataType.LONG ) ) {
            for ( Map.Entry < String, Object > entry : (( Map < String, Object > ) hashMap).entrySet( ) ) {
                if ( entry.getValue( ) instanceof Long ) {
                    LongValues.put( entry.getKey( ) , ( Long ) entry.getValue( ) );
                }
            }
        }
        ItemUUID = itemUUID;
        OwnerUUID = ownerUUID;
    }
    
    public HashMap < String, String > getStringValues( ){
        return StringValues;
    }
    
    public void setStringValues( HashMap < String, String > stringValues ){
        StringValues = stringValues;
    }
    
    public HashMap < String, Integer > getIntegerValues( ){
        return IntegerValues;
    }
    
    public void setIntegerValues( HashMap < String, Integer > integerValues ){
        IntegerValues = integerValues;
    }
    
    public HashMap < String, Boolean > getBooleanValues( ){
        return BooleanValues;
    }
    
    public void setBooleanValues( HashMap < String, Boolean > booleanValues ){
        BooleanValues = booleanValues;
    }
    
    public HashMap < String, Long > getLongValues( ){
        return LongValues;
    }
    
    public void setLongValues( HashMap < String, Long > longValues ){
        LongValues = longValues;
    }
    
    public ItemStack getItemStack( ){
        return ItemStack;
    }
    
    public void setItemStack( ItemStack itemStack ){
        ItemStack = itemStack;
    }
    
    public UUID getItemUUID( ){
        return ItemUUID;
    }
    
    public void setItemUUID( UUID itemUUID ){
        ItemUUID = itemUUID;
    }
    
    public UUID getOwnerUUID( ){
        return OwnerUUID;
    }
    
    public void setOwnerUUID( UUID ownerUUID ){
        OwnerUUID = ownerUUID;
    }
    
}
