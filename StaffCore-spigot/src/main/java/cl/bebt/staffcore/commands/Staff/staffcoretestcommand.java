/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.PersistentData.PersistentDataType;
import cl.bebt.staffcore.PersistentData.PersistentDataUtils;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class staffcoretestcommand implements CommandExecutor {
    
    private final main plugin;
    
    public staffcoretestcommand( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "staffcoretestcommand" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command command , String label , String[] args ){
        Player p = ( Player ) sender;
        if ( !p.getItemInHand( ).getType( ).equals( Material.AIR ) ) {
            ItemStack item = p.getInventory( ).getItemInMainHand( );
            PersistentDataUtils.save( "test" , "test" , item , p.getUniqueId( ) , PersistentDataType.STRING );
            utils.tell( p , "&aItem guardado" );
            utils.tell( p , "&aItem:" + PersistentDataUtils.getString( item , "test" ) );
        } else {
            utils.tell( p , "&aItem no guardado" );
        }
        return false;
    }
}
