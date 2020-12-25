package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class utils{

    private static main plugin;
    private static final ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ).size( ) );

    public utils( main plugin ){
        utils.plugin = plugin;
    }

    public static String chat( String s ){
        return ChatColor.translateAlternateColorCodes( '&' , s );
    }

    public static void tell( CommandSender sender , String message ){
        sender.sendMessage( utils.chat( message ) );
    }

    public static void tell( Player player , String message ){
        player.sendMessage( utils.chat( message ) );
    }

    public static void ccAll( ){
        for ( Player p : Bukkit.getOnlinePlayers( ) ) {
            for ( int i = 0; i < 99; i++ ) {
                p.sendMessage( "\n" );
                p.sendMessage( " " );
            }
        }
    }

    public static void ccPlayer( Player p ){
        for ( int i = 0; i < 99; i++ ) {
            p.sendMessage( "\n" );
            p.sendMessage( " " );
        }
    }

    public static ItemStack getPlayerHead( String p ){
        boolean isNewVersion = Arrays.stream( Material.values( ) )
                .map( Material::name ).collect( Collectors.toList( ) ).contains( "PLAYER_HEAD" );
        Material type = Material.matchMaterial( isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM" );
        ItemStack item = new ItemStack( type , 1 );
        if ( !(isNewVersion) ) {
            item.setDurability( ( short ) 3 );
        }
        SkullMeta meta = ( SkullMeta ) item.getItemMeta( );
        meta.setOwner( p );
        item.setItemMeta( meta );
        return item;
    }

    public static void PlaySound( Player p , String path ){
        if ( plugin.getConfig( ).getBoolean( "sounds" ) ) {
            Sound sound = Sound.valueOf( plugin.getConfig( ).getString( "custom_sounds." + path ) );
            p.playSound( p.getLocation( ) , sound , 1 , 1 );
        }
    }

    public static void PlayParticle( Player p , String path ){
        if ( plugin.getConfig( ).getBoolean( "custom_particles." + path + ".enabled" ) ) {
            Particle particle = Particle.valueOf( plugin.getConfig( ).getString( "custom_particles." + path + ".particle" ) );
            int count = plugin.getConfig( ).getInt( "custom_particles." + path + ".count" );
            int times = plugin.getConfig( ).getInt( "custom_particles." + path + ".number_of_times" );
            int offSetX = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetX" );
            int offSetY = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetY" );
            int offSetZ = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetZ" );
            for ( int i = 0; i < times; i++ ) {
                p.getWorld( ).spawnParticle( particle , p.getLocation( ) , count , offSetX , offSetY , offSetZ );
            }
        }
    }

    public static String stringify( List < String > l , String Ip ){
        StringBuilder rs = new StringBuilder( Ip );
        for ( String marker : l ) {
            if ( !Ip.equalsIgnoreCase( marker ) && !"127.0.0.1".equalsIgnoreCase( marker ) ) {
                rs.append( ',' ).append( marker );
            }
        }
        return rs.toString( );
    }

    public static HashMap < String, String > makeHashMap( String s ){
        HashMap < String, String > map = new HashMap <>( );
        s = s.replace( "{" , "" ).replace( "}" , "" );
        //split the String by a comma
        String[] parts = s.split( "," );

        //iterate the parts and add them to a map

        for ( String part : parts ) {

            //split the employee data by : to get id and name
            String[] empdata = part.split( "=" );

            String strId = empdata[0].trim( );
            String strName = empdata[1].trim( );
            strId = strId.replace( " " , "" );
            strName = strName.replace( " " , "" );
            //add to map
            map.put( strId , strName );
        }
        return map;
    }

    public static List < String > makeList( String rs ){
        List < String > rl = new LinkedList <>( );
        String[] a = rs.split( "," );
        rl.addAll( Arrays.asList( a ) );
        return rl;
    }

    public static boolean isRegistered( String p ){
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getPlayersNames( ).contains( p );
        } else {
            return Objects.requireNonNull( plugin.alts.getConfig( ).getConfigurationSection( "alts" ) ).contains( p );
        }
    }

    public static boolean mysqlEnabled( ){
        return plugin.getConfig( ).getBoolean( "mysql.enabled" );
    }

    public static Boolean isPlayer( String target ){
        return Bukkit.getPlayer( target ) != null;
    }

    public static int getPing( Player p ){
        try {
            Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
            int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
            return ping;
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
            e.printStackTrace( );
            return 0;
        }
    }

    public static String getString( String s ){
        return plugin.getConfig( ).getString( s );
    }

    public static Boolean getBoolean( String s ){
        return plugin.getConfig( ).getBoolean( s );
    }

    public static int getInt( String s ){
        return plugin.getConfig( ).getInt( s );
    }

    public static Player randomPlayer( Player p ){
        int count = 0;
        while (count < 10) {
            if ( !Bukkit.getOnlinePlayers( ).isEmpty( ) ) {
                players.addAll( Bukkit.getOnlinePlayers( ) );
                int random = new Random( ).nextInt( players.size( ) );
                Player selected = ( Player ) Bukkit.getServer( ).getOnlinePlayers( ).toArray( )[random];
                if ( !selected.equals( p ) ) {
                    players.clear( );
                    return selected;
                } else {
                    players.clear( );
                    count++;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public static int currentWarns( ){
        int current = 0;
        try {
            ConfigurationSection inventorySection = main.plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            for ( String key : inventorySection.getKeys( false ) ) {
                current++;
            }
        } catch ( NullPointerException ignored ) {
        }
        return current;
    }

    public static int currentPlayerWarns( String warned ){
        int warnings = 0;
        if ( mysqlEnabled( ) ) {
            return SQLGetter.getCurrentWarns( warned );
        } else {
            main.plugin.warns.reloadConfig( );
            for ( int i = 1; i <= plugin.warns.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( main.plugin.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) ) {
                        warnings++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return warnings;
        }
    }

    public static int currentPlayerReports( String reported ){
        int reports = 0;
        if ( mysqlEnabled( ) ) {
            return SQLGetter.getCurrentReports( reported );
        } else {
            main.plugin.reports.reloadConfig( );
            for ( int i = 1; i <= plugin.reports.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( main.plugin.reports.getConfig( ).getString( "reports." + i + ".name" ).equalsIgnoreCase( reported ) ) {
                        reports++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return reports;
        }
    }

    public static String getServerVersion( ){
        return Bukkit.getServer( ).getClass( ).getPackage( ).getName( ).substring( 23 );
    }

    public static double getTPS( ){
        return TPS.getTPS( );
    }

    public static ArrayList < String > getUsers( ){
        ArrayList < String > Users = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            Users.addAll( SQLGetter.getPlayersNames( ) );
        } else {
            ConfigurationSection inventorySection = plugin.alts.getConfig( ).getConfigurationSection( "alts" );
            for ( String key : inventorySection.getKeys( false ) ) {
                Users.add( key );
            }
        }
        return Users;
    }

    public static ArrayList < String > getWarnedPlayers( ){
        ArrayList < String > Users = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            Users.addAll( SQLGetter.getWarnedPlayers( ) );
        } else {
            ConfigurationSection inventorySection = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            try{
                for ( String key : inventorySection.getKeys( false ) ) {
                    String name = plugin.warns.getConfig( ).getString( "warns." + key + ".name" );
                    if ( !Users.contains( name ) ) {
                        Users.add( name );
                    }
                }
            }catch(NullPointerException ignored){

            }

        }
        return Users;
    }
}
