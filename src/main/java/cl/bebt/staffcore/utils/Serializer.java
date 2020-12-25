package cl.bebt.staffcore.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serializer{

    /**
     * Converts the player inventory to a String array of Serializer strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException
     */
    public static String[] playerInventoryToBase64( PlayerInventory playerInventory ) throws IllegalStateException{
        //get the main content part, this doesn't return the armor
        String content = toBase64( playerInventory );
        String armor = itemStackArrayToBase64( playerInventory.getArmorContents( ) );

        return new String[]{content , armor};
    }

    /**
     * A method to serialize an {@link ItemStack} array to Serializer String.
     * <p>
     * <p/>
     * <p>
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Serializer String.
     * @return Serializer string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64( ItemStack[] items ) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream( outputStream );

            // Write the size of the inventory
            dataOutput.writeInt( items.length );

            // Save every element in the list
            for ( int i = 0; i < items.length; i++ ) {
                dataOutput.writeObject( items[i] );
            }

            // Serialize that array
            dataOutput.close( );
            return Base64Coder.encodeLines( outputStream.toByteArray( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( "Unable to save item stacks." , e );
        }
    }


    /**
     * A method to serialize an inventory to Serializer string.
     * <p>
     * <p/>
     * <p>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Serializer string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64( Inventory inventory ) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream( outputStream );

            // Write the size of the inventory
            dataOutput.writeInt( inventory.getSize( ) );

            // Save every element in the list
            for ( int i = 0; i < inventory.getSize( ); i++ ) {
                dataOutput.writeObject( inventory.getItem( i ) );
            }

            // Serialize that array
            dataOutput.close( );
            return Base64Coder.encodeLines( outputStream.toByteArray( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( "Unable to save item stacks." , e );
        }
    }

    /**
     * A method to get an {@link Inventory} from an encoded, Serializer, string.
     * <p>
     * <p/>
     * <p>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Serializer string of data containing an inventory.
     * @return Inventory created from the Serializer string.
     * @throws IOException
     */
    public static Inventory fromBase64( String data ) throws IOException{
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream( Base64Coder.decodeLines( data ) );
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream( inputStream );
            Inventory inventory = Bukkit.getServer( ).createInventory( null , dataInput.readInt( ) );

            // Read the serialized inventory
            for ( int i = 0; i < inventory.getSize( ); i++ ) {
                inventory.setItem( i , ( ItemStack ) dataInput.readObject( ) );
            }

            dataInput.close( );
            return inventory;
        } catch ( ClassNotFoundException e ) {
            throw new IOException( "Unable to decode class type." , e );
        }
    }

    /**
     * Gets an array of ItemStacks from Serializer string.
     * <p>
     * <p/>
     * <p>
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Serializer string to convert to ItemStack array.
     * @return ItemStack array created from the Serializer string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64( String data ) throws IOException{
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream( Base64Coder.decodeLines( data ) );
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream( inputStream );
            ItemStack[] items = new ItemStack[dataInput.readInt( )];

            // Read the serialized inventory
            for ( int i = 0; i < items.length; i++ ) {
                items[i] = ( ItemStack ) dataInput.readObject( );
            }

            dataInput.close( );
            return items;
        } catch ( ClassNotFoundException e ) {
            throw new IOException( "Unable to decode class type." , e );
        }
    }

    /**
     * Serializes and deserializes ItemStacks
     *
     * @author xMrPoi
     */

    public static String serialize( ItemStack item ){
        StringBuilder builder = new StringBuilder( );
        builder.append( item.getType( ).toString( ) );
        if ( item.getDurability( ) != 0 ) builder.append( ":" + item.getDurability( ) );
        builder.append( " " + item.getAmount( ) );
        for ( Enchantment enchant : item.getEnchantments( ).keySet( ) )
            builder.append( " " + enchant.getName( ) + ":" + item.getEnchantments( ).get( enchant ) );
        String name = getName( item );
        if ( name != null ) builder.append( " name:" + name );
        String lore = getLore( item );
        if ( lore != null ) builder.append( " lore:" + lore );
        Color color = getArmorColor( item );
        if ( color != null )
            builder.append( " rgb:" + color.getRed( ) + "|" + color.getGreen( ) + "|" + color.getBlue( ) );
        String owner = getOwner( item );
        if ( owner != null ) builder.append( " owner:" + owner );
        return builder.toString( );
    }

    public static ItemStack deserialize( String serializedItem ){
        String[] strings = serializedItem.split( " " );
        Map < Enchantment, Integer > enchants = new HashMap < Enchantment, Integer >( );
        String[] args;
        ItemStack item = new ItemStack( Material.AIR );
        for ( String str : strings ) {
            args = str.split( ":" );
            if ( Material.matchMaterial( args[0] ) != null && item.getType( ) == Material.AIR ) {
                item.setType( Material.matchMaterial( args[0] ) );
                if ( args.length == 2 ) item.setDurability( Short.parseShort( args[1] ) );
                break;
            }
        }
        if ( item.getType( ) == Material.AIR ) {
            Bukkit.getLogger( ).info( "Could not find a valid material for the item in \"" + serializedItem + "\"" );
            return null;
        }
        for ( String str : strings ) {
            args = str.split( ":" , 2 );
            if ( isNumber( args[0] ) ) item.setAmount( Integer.parseInt( args[0] ) );
            if ( args.length == 1 ) continue;
            if ( args[0].equalsIgnoreCase( "name" ) ) {
                setName( item , ChatColor.translateAlternateColorCodes( '&' , args[1] ) );
                continue;
            }
            if ( args[0].equalsIgnoreCase( "lore" ) ) {
                setLore( item , ChatColor.translateAlternateColorCodes( '&' , args[1] ) );
                continue;
            }
            if ( args[0].equalsIgnoreCase( "rgb" ) ) {
                setArmorColor( item , args[1] );
                continue;
            }
            if ( args[0].equalsIgnoreCase( "owner" ) ) {
                setOwner( item , args[1] );
                continue;
            }
            if ( Enchantment.getByName( args[0].toUpperCase( ) ) != null ) {
                enchants.put( Enchantment.getByName( args[0].toUpperCase( ) ) , Integer.parseInt( args[1] ) );
                continue;
            }
        }
        item.addUnsafeEnchantments( enchants );
        return item.getType( ).equals( Material.AIR ) ? null : item;
    }

    private static String getOwner( ItemStack item ){
        if ( !(item.getItemMeta( ) instanceof SkullMeta) ) return null;
        return (( SkullMeta ) item.getItemMeta( )).getOwner( );
    }

    private static void setOwner( ItemStack item , String owner ){
        try {
            SkullMeta meta = ( SkullMeta ) item.getItemMeta( );
            meta.setOwner( owner );
            item.setItemMeta( meta );
        } catch ( Exception exception ) {
            return;
        }
    }

    private static String getName( ItemStack item ){
        if ( !item.hasItemMeta( ) ) return null;
        if ( !item.getItemMeta( ).hasDisplayName( ) ) return null;
        return item.getItemMeta( ).getDisplayName( ).replace( " " , "_" ).replace( ChatColor.COLOR_CHAR , '&' );
    }

    private static void setName( ItemStack item , String name ){
        name = name.replace( "_" , " " );
        ItemMeta meta = item.getItemMeta( );
        meta.setDisplayName( name );
        item.setItemMeta( meta );
    }

    private static String getLore( ItemStack item ){
        if ( !item.hasItemMeta( ) ) return null;
        if ( !item.getItemMeta( ).hasLore( ) ) return null;
        StringBuilder builder = new StringBuilder( );
        List < String > lore = item.getItemMeta( ).getLore( );
        for ( int ind = 0; ind < lore.size( ); ind++ ) {
            builder.append( (ind > 0 ? "|" : "") + lore.get( ind ).replace( " " , "_" ).replace( ChatColor.COLOR_CHAR , '&' ) );
        }
        return builder.toString( );
    }

    private static void setLore( ItemStack item , String lore ){
        lore = lore.replace( "_" , " " );
        ItemMeta meta = item.getItemMeta( );
        meta.setLore( Arrays.asList( lore.split( "\\|" ) ) );
        item.setItemMeta( meta );
    }

    private static Color getArmorColor( ItemStack item ){
        if ( !(item.getItemMeta( ) instanceof LeatherArmorMeta) ) return null;
        return (( LeatherArmorMeta ) item.getItemMeta( )).getColor( );
    }

    private static void setArmorColor( ItemStack item , String str ){
        try {
            String[] colors = str.split( "\\|" );
            int red = Integer.parseInt( colors[0] );
            int green = Integer.parseInt( colors[1] );
            int blue = Integer.parseInt( colors[2] );
            LeatherArmorMeta meta = ( LeatherArmorMeta ) item.getItemMeta( );
            meta.setColor( Color.fromRGB( red , green , blue ) );
            item.setItemMeta( meta );
        } catch ( Exception exception ) {
            return;
        }
    }

    private static boolean isNumber( String str ){
        try {
            Integer.parseInt( str );
        } catch ( NumberFormatException exception ) {
            return false;
        }
        return true;
    }


}
