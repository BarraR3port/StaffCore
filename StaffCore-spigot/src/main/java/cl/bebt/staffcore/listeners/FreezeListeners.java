/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.*;
import cl.bebt.staffcore.menu.menu.Inventory.openChest;
import cl.bebt.staffcore.menu.menu.Staff.ServerManager;
import cl.bebt.staffcore.utils.OpenInvSee;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.Items.Items;
import cl.bebt.staffcoreapi.utils.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FreezeListeners implements Listener {
    
    private final main plugin;
    
    public FreezeListeners( main plugin ){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public < chart > void onPlayerMove( PlayerMoveEvent e ){
        Player p = e.getPlayer( );
        if ( UserUtils.getFrozen( p.getUniqueId( ) ) ) {
            chart block = ( chart ) "⬛";
            String white = "&f&l" + block;
            String yellow = "&6&l" + block;
            String red = "&4&l" + block;
            String black = "&0&l" + block;
            Utils.tell( p , white + white + white + white + white + white + white + white + white );
            Utils.tell( p , white + white + white + white + red + white + white + white + white );
            Utils.tell( p , white + white + white + red + yellow + red + white + white + white );
            Utils.tell( p , white + white + red + yellow + black + yellow + red + white + white );
            Utils.tell( p , white + white + red + yellow + black + yellow + red + white + white );
            Utils.tell( p , white + red + yellow + yellow + black + yellow + yellow + red + white );
            Utils.tell( p , white + red + yellow + yellow + yellow + yellow + yellow + red + white );
            Utils.tell( p , red + yellow + yellow + yellow + black + yellow + yellow + yellow + red );
            Utils.tell( p , red + red + red + red + red + red + red + red + red );
            ComponentBuilder cb = new ComponentBuilder( Utils.chat( Utils.getString( "freeze.freeze_message_description" , "lg" , null ) ) );
            TextComponent dis = new TextComponent( Utils.chat( Utils.getString( "freeze.freeze_message" , "lg" , null ) ) );
            dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
            dis.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , "https://" + Utils.getString( "freeze.freeze_ds" , "lg" , null ) ) );
            p.spigot( ).sendMessage( dis );
            e.setCancelled( true );
        }
        
    }
    
    //                       Vanish and Freeze                              \\
    @SuppressWarnings("ConstantConditions")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract( PlayerInteractEvent e ){
        Player p = e.getPlayer( );
        Action a = e.getAction( );
        UUID uuid = p.getUniqueId( );
        
        if ( UserUtils.getVanish( uuid ) ) {
            if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                try {
                    if ( e.getAction( ).equals( Action.RIGHT_CLICK_BLOCK ) ) {
                        switch (e.getClickedBlock( ).getType( )) {
                            case CHEST: {
                                e.setCancelled( true );
                                HashMap < Integer, ItemStack > chest_slots = new HashMap <>( );
                                Chest chest = ( Chest ) e.getClickedBlock( ).getState( );
                                int size = chest.getInventory( ).getSize( );
                                for ( int i = 0; i < size; i++ ) {
                                    try {
                                        chest_slots.put( i , chest.getInventory( ).getItem( i ) );
                                    } catch ( NullPointerException ignored ) {
                                    }
                                }
                                new openChest( main.getPlayerMenuUtility( p ) , chest_slots , size ).open( );
                            }
                            case CHEST_MINECART:
                                e.setCancelled( true );
                            case TRAPPED_CHEST: {
                                e.setCancelled( true );
                                HashMap < Integer, ItemStack > chest_slots = new HashMap <>( );
                                Chest chest = ( Chest ) e.getClickedBlock( ).getState( );
                                int size = chest.getInventory( ).getSize( );
                                for ( int i = 0; i < size; i++ ) {
                                    try {
                                        chest_slots.put( i , chest.getInventory( ).getItem( i ) );
                                    } catch ( NullPointerException ignored ) {
                                    }
                                }
                                new openChest( main.getPlayerMenuUtility( p ) , chest_slots , size ).open( );
                            }
                            case ENDER_CHEST , DARK_OAK_DOOR , OAK_DOOR , ACACIA_DOOR , BIRCH_DOOR , IRON_DOOR , JUNGLE_DOOR ,
                                    SPRUCE_DOOR , ACACIA_TRAPDOOR , BIRCH_TRAPDOOR , DARK_OAK_TRAPDOOR ,
                                    IRON_TRAPDOOR , JUNGLE_TRAPDOOR , OAK_TRAPDOOR , SPRUCE_TRAPDOOR , ACACIA_FENCE_GATE ,
                                    BIRCH_FENCE_GATE , JUNGLE_FENCE_GATE , OAK_FENCE_GATE , SPRUCE_FENCE_GATE ,
                                    DARK_OAK_FENCE_GATE , LEVER:
                                e.setCancelled( true );
                            case CRIMSON_DOOR , CRIMSON_TRAPDOOR , WARPED_DOOR , WARPED_FENCE_GATE , CRIMSON_FENCE_GATE ,
                                    WARPED_TRAPDOOR:
                                e.setCancelled( true );
                                break;
                        }
                    }
                } catch ( NullPointerException | NoClassDefFoundError ignored ) {
                }
            }
        }
        if ( e.getHand( ) == EquipmentSlot.OFF_HAND ) return;
        if ( UserUtils.getStaff( uuid ) ) {
            if ( a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK && UserUtils.getStaff( uuid ) ) {
                ItemStack itemInMainHand = p.getInventory( ).getItemInMainHand( );
                InventoryHolder holder = p.getOpenInventory( ).getTopInventory( ).getHolder( );
                if ( holder instanceof Menu ||
                        holder instanceof MenuC ||
                        holder instanceof InventoryMenu ||
                        holder instanceof PaginatedMenu ||
                        holder instanceof ReportPlayerMenu ||
                        holder instanceof WarnPlayerMenu ||
                        holder instanceof InvSeePlayerMenu ||
                        holder instanceof BanPlayerMenu ) {
                    return;
                }
                if ( Items.staffOff( ).equals( itemInMainHand ) ) {
                    StaffManager.disable( p.getUniqueId( ) );
                    Utils.tell( p , Utils.getString( "staff.disabled" , "lg" , "staff" ) );
                    return;
                }
                if ( Items.serverManager( ).equals( itemInMainHand ) ) {
                    new ServerManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
                    return;
                }
                if ( CountdownManager.checkMuteCountdown( uuid ) ) {
                    if ( Items.vanishOn( ).equals( itemInMainHand ) ) {
                        Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                            VanishManager.disable( uuid );
                            p.getInventory( ).remove( Items.vanishOn( ) );
                            p.getInventory( ).setItemInMainHand( Items.vanishOff( ) );
                            Utils.tell( p , Utils.getString( "vanish.disabled" , "lg" , "staff" ) );
                            CountdownManager.setCountDown( uuid , 0.2D );
                        } , 1L );
                    }
                    if ( Items.vanishOff( ).equals( itemInMainHand ) ) {
                        Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                            VanishManager.enable( uuid );
                            p.getInventory( ).remove( Items.vanishOff( ) );
                            p.getInventory( ).setItemInMainHand( Items.vanishOn( ) );
                            Utils.tell( p , Utils.getString( "vanish.enabled" , "lg" , "staff" ) );
                            CountdownManager.setCountDown( uuid , 0.2D );
                        } , 1L );
                    }
                    if ( Items.randomTp( ).equals( itemInMainHand ) ) {
                        Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
                            Player random = Utils.randomPlayer( p );
                            if ( random != null ) {
                                p.teleport( random.getLocation( ) );
                                Utils.tell( p , Utils.getString( "tp.random" , "lg" , "staff" ).replace( "%player%" , random.getName( ) ) );
                            } else {
                                Utils.tell( p , Utils.getString( "tp.not_enough" , "lg" , "staff" ) );
                            }
                            CountdownManager.setCountDown( uuid , 0.2D );
                        } , 1L );
                    }
                }
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange( FoodLevelChangeEvent e ){
        if ( e.getEntity( ) instanceof Player ) {
            Player p = ( Player ) e.getEntity( );
            UUID uuid = p.getUniqueId( );
            
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) || UserUtils.getStaff( uuid ) ) {
                e.setCancelled( true );
            }
            
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkDamage( EntityDamageEvent event ){
        if ( event.getEntity( ) instanceof Player ) {
            Player p = ( Player ) event.getEntity( );
            UUID uuid = p.getUniqueId( );
            
            if ( event.getCause( ) == EntityDamageEvent.DamageCause.FIRE || event.getCause( ) == EntityDamageEvent.DamageCause.FIRE_TICK ) {
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) || UserUtils.getStaff( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        event.setCancelled( true );
                    }
                }
            }
            
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerRespawnEvent( PlayerRespawnEvent e ){
        Player p = e.getPlayer( );
        
        if ( UserUtils.getStaff( p.getUniqueId( ) ) ) {
            StaffManager.enable( p.getUniqueId( ) );
        }
        
    }
    
    @SuppressWarnings("ConstantConditions")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakBlock( BlockBreakEvent e ){
        Player p = e.getPlayer( );
        
        UUID uuid = p.getUniqueId( );
        if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
            if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.vanishOn( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.vanishOn( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.staffOff( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.serverManager( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.freeze( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.randomTp( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.reportManager( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.InvSee( ) ) ) {
            e.setCancelled( true );
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlaceBlock( BlockPlaceEvent e ){
        Player p = e.getPlayer( );
        
        UUID uuid = p.getUniqueId( );
        if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
            if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.freeze( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.reportManager( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( Items.InvSee( ) ) ) {
            e.setCancelled( true );
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit( EntityDamageByEntityEvent e ){
        Entity mob = e.getDamager( );
        if ( mob instanceof Player ) {
            try {
                Player p = ( Player ) e.getDamager( );
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage( EntityDamageEvent e ){
        Entity p = e.getEntity( );
        if ( p instanceof Player ) {
            try {
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop( PlayerDropItemEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
        if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.staffOff( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.vanishOn( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.vanishOff( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.serverManager( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.freeze( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.reportManager( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.InvSee( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( Items.randomTp( ) ) ) {
            Utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickUp( EntityPickupItemEvent e ){
        Entity p = e.getEntity( );
        if ( p instanceof Player ) {
            try {
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract( PlayerInteractEntityEvent e ){
        try {
            Player player = e.getPlayer( );
            UUID uuid = player.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( player.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
            if ( e.getHand( ) == EquipmentSlot.OFF_HAND ) {
                return;
            }
            if ( player.getInventory( ).getItemInMainHand( ).isSimilar( Items.freeze( ) ) ) {
                if ( e.getRightClicked( ) instanceof Player ) {
                    Player p = ( Player ) e.getRightClicked( );
                    UUID uuid2 = p.getUniqueId( );
                    if ( UserUtils.getFrozen( uuid2 ) ) {
                        FreezeManager.disable( uuid2 , player.getName( ) );
                        
                    } else {
                        if ( p.hasPermission( "staffcore.freeze.bypass" ) ) {
                            Utils.tell( player , Utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                        } else {
                            FreezeManager.enable( uuid2 , player.getName( ) );
                            Utils.tell( player , Utils.getString( "freeze.freeze_message" , "lg" , "staff" ) );
                        }
                    }
                }
            }
            if ( player.getInventory( ).getItemInMainHand( ).isSimilar( Items.InvSee( ) ) ) {
                if ( e.getRightClicked( ) instanceof Player ) {
                    Player p = ( Player ) e.getRightClicked( );
                    new OpenInvSee( player , p );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract( PlayerCommandPreprocessEvent e ){
        Player player = e.getPlayer( );
        UUID uuid = player.getUniqueId( );
        
        if ( UserUtils.getFrozen( uuid ) ) {
            if ( !player.hasPermission( "staffcore.unfreeze.himself" ) ) {
                e.setCancelled( true );
            }
        }
        
        if ( Utils.getBoolean( "discord.type.debug.enabled_debugs.commands" ) ) {
            ArrayList < String > dc = new ArrayList <>( );
            dc.add( "**Player:** " + player.getName( ) );
            dc.add( "**Command:** " + e.getMessage( ) );
            Utils.sendDiscordDebugMsg( player , "⚠ Commands ⚠" , dc );
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBowUse( EntityShootBowEvent e ){
        if ( e.getEntity( ) instanceof Player && e.getProjectile( ) instanceof Arrow ) {
            Player p = ( Player ) e.getEntity( );
            try {
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTarget( EntityTargetEvent e ){
        if ( e.getTarget( ) instanceof Player ) {
            Player p = ( Player ) e.getTarget( );
            try {
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
            
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketEmpty( PlayerBucketEmptyEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketFill( PlayerBucketFillEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBed( PlayerBedEnterEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish( PlayerFishEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsume( PlayerItemConsumeEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickArrow( PlayerPickupArrowEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRecipeDiscoverEvent( PlayerRecipeDiscoverEvent e ){
        Player p = e.getPlayer( );
        try {
            UUID uuid = p.getUniqueId( );
            if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        } catch ( ClassCastException ignored ) {
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTrow( ProjectileLaunchEvent e ){
        if ( e.getEntity( ).getShooter( ) instanceof Player ) {
            Player p = ( Player ) e.getEntity( ).getShooter( );
            try {
                UUID uuid = p.getUniqueId( );
                if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                    if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract( PlayerInteractEvent e ){
        Player p = e.getPlayer( );
        if ( e.getAction( ) == Action.RIGHT_CLICK_BLOCK ) {
            ItemStack hand = e.getPlayer( ).getInventory( ).getItemInMainHand( );
            String word = hand.getType( ).name( );
            String substring = word.substring( word.length( ) - 3 );
            try {
                if ( substring.equals( "EGG" ) ) {
                    UUID uuid = p.getUniqueId( );
                    if ( UserUtils.getVanish( uuid ) || UserUtils.getFrozen( uuid ) ) {
                        if ( !Utils.getTrollStatus( p.getName( ) ) ) {
                            e.setCancelled( true );
                        }
                    }
                }
            } catch ( StringIndexOutOfBoundsException ignored ) {
            }
        }
    }
}


