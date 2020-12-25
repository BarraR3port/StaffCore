package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker{
    private final main plugin;
    private final int resourceId;

    public UpdateChecker( main plugin , int resourceId ){
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getLatestVersion( Consumer < String > consumer ){
        Bukkit.getScheduler( ).runTaskAsynchronously( plugin , ( ) -> {
            try (InputStream inputStream = new URL( "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId ).openStream( );
                 Scanner scanner = new Scanner( inputStream )) {
                if ( scanner.hasNext( ) ) {
                    consumer.accept( scanner.next( ) );
                }
            } catch ( IOException exception ) {
                plugin.getLogger( ).info( "Update checker is broken " + exception.getMessage( ) );
            }
        } );
    }
}
