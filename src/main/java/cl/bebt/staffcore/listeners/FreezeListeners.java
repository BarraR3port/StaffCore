package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Inventory.openChest;
import cl.bebt.staffcore.menu.menu.Others.ServerManager;
import cl.bebt.staffcore.menu.menu.Reports.ReportManager;
import cl.bebt.staffcore.utils.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class FreezeListeners implements Listener{
    
    private final main plugin;
    
    public FreezeListeners( main plugin ){
        this.plugin = plugin;
    }
    
    @EventHandler
    public < chart > void onPlayerMove( PlayerMoveEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        chart block = ( chart ) "⬛";
        if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        }
        if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            String white = "&f&l" + block;
            String yellow = "&6&l" + block;
            String red = "&4&l" + block;
            String black = "&0&l" + block;
            p.sendMessage( utils.chat( white + white + white + white + white + white + white + white + white ) );
            p.sendMessage( utils.chat( white + white + white + white + red + white + white + white + white ) );
            p.sendMessage( utils.chat( white + white + white + red + yellow + red + white + white + white ) );
            p.sendMessage( utils.chat( white + white + red + yellow + black + yellow + red + white + white ) );
            p.sendMessage( utils.chat( white + white + red + yellow + black + yellow + red + white + white ) );
            p.sendMessage( utils.chat( white + red + yellow + yellow + black + yellow + yellow + red + white ) );
            p.sendMessage( utils.chat( white + red + yellow + yellow + yellow + yellow + yellow + red + white ) );
            p.sendMessage( utils.chat( red + yellow + yellow + yellow + black + yellow + yellow + yellow + red ) );
            p.sendMessage( utils.chat( red + red + red + red + red + red + red + red + red ) );
            ComponentBuilder cb = new ComponentBuilder( utils.chat( plugin.getConfig( ).getString( "staff.freeze_message_description" ) ) );
            TextComponent dis = new TextComponent( utils.chat( plugin.getConfig( ).getString( "staff.freeze_message" ) ) );
            dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
            dis.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , "https://" + plugin.getConfig( ).getString( "staff.freeze_ds" ) ) );
            p.spigot( ).sendMessage( dis );
        }
    }
    
    //                       Vanish and Freeze                              \\
    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onInteract( PlayerInteractEvent e ){
        Player p = e.getPlayer( );
        Action a = e.getAction( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ){
                try {
                    if ( e.getAction( ).equals( Action.RIGHT_CLICK_BLOCK ) ) {
                        switch (e.getClickedBlock( ).getType( )) {
                            case CRIMSON_DOOR: {
                                e.setCancelled( true );
                            }
                            case CRIMSON_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case WARPED_DOOR: {
                                e.setCancelled( true );
                            }
                            case WARPED_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case CRIMSON_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case WARPED_TRAPDOOR: {
                                e.setCancelled( true );
                            }
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
                                new openChest( main.getPlayerMenuUtility( p ) , chest_slots , size ).open( p );
                            }
                            case CHEST_MINECART: {
                                e.setCancelled( true );
                            }
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
                                new openChest( main.getPlayerMenuUtility( p ) , chest_slots , size ).open( p );
                            }
                            case ENDER_CHEST: {
                                e.setCancelled( true );
                            }
                            case DARK_OAK_DOOR: {
                                e.setCancelled( true );
                            }
                            case OAK_DOOR: {
                                e.setCancelled( true );
                            }
                            case ACACIA_DOOR: {
                                e.setCancelled( true );
                            }
                            case BIRCH_DOOR: {
                                e.setCancelled( true );
                            }
                            case IRON_DOOR: {
                                e.setCancelled( true );
                            }
                            case JUNGLE_DOOR: {
                                e.setCancelled( true );
                            }
                            case SPRUCE_DOOR: {
                                e.setCancelled( true );
                            }
                            case ACACIA_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case BIRCH_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case DARK_OAK_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case IRON_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case JUNGLE_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case OAK_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case SPRUCE_TRAPDOOR: {
                                e.setCancelled( true );
                            }
                            case ACACIA_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case BIRCH_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case JUNGLE_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case OAK_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case SPRUCE_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case DARK_OAK_FENCE_GATE: {
                                e.setCancelled( true );
                            }
                            case LEVER: {
                                e.setCancelled( true );
                            }
                            break;
                        }
                    }
                } catch ( NullPointerException | NoClassDefFoundError ignored ) {
                }
            }
        }
        if ( e.getHand( ) == EquipmentSlot.OFF_HAND ) return;
        if ( PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            if ( a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK && PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                ItemStack itemInMainHand = p.getInventory( ).getItemInMainHand( );
                if ( SetStaffItems.staffOff( ).equals( itemInMainHand ) ) {
                    SetStaffItems.Off( p );
                    return;
                }
                if ( SetStaffItems.serverManager( ).equals( itemInMainHand ) ) {
                    new ServerManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
                    return;
                }
                if ( SetStaffItems.reportManager( ).equals( itemInMainHand ) ) {
                    new ReportManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
                    return;
                }
                if ( CountdownManager.checkCountdown( p ) ) {
                    if ( SetStaffItems.vanishOn( ).equals( itemInMainHand ) ) {
                        SetVanish.setVanish( p , false );
                        p.getInventory( ).remove( SetStaffItems.vanishOn( ) );
                        p.getInventory( ).setItemInMainHand( SetStaffItems.vanishOff( ) );
                        utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) );
                        CountdownManager.setCountDown( p , 0.2D );
                    }
                    if ( SetStaffItems.vanishOff( ).equals( itemInMainHand ) ) {
                        SetVanish.setVanish( p , true );
                        p.getInventory( ).remove( SetStaffItems.vanishOff( ) );
                        p.getInventory( ).setItemInMainHand( SetStaffItems.vanishOn( ) );
                        utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) );
                        CountdownManager.setCountDown( p , 0.2D );
                    }
                    if ( SetStaffItems.randomTp( ).equals( itemInMainHand ) ) {
                        Player random = utils.randomPlayer( p );
                        if ( random != null ) {
                            p.teleport( random.getLocation( ) );
                            utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7Random Teleported to &a" + random.getName( ) );
                        } else {
                            utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cNot enough players to teleport" );
                        }
                        CountdownManager.setCountDown( p , 0.2D );
                    }
                }
            }
        }
    }
    @EventHandler
    public void onFoodLevelChange( FoodLevelChangeEvent e ){
        if ( e.getEntity( ) instanceof Player ) {
            Player p = ( Player ) e.getEntity( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )
                    || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING )
                    || PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )
            ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void checkDamage( EntityDamageEvent event ){
        if ( event.getEntity( ) instanceof Player ) {
            Player p = ( Player ) event.getEntity( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( event.getCause( ) == EntityDamageEvent.DamageCause.FIRE || event.getCause( ) == EntityDamageEvent.DamageCause.FIRE_TICK ) {
                if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )
                        || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING )
                        || PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )
                ) {
                    if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                        event.setCancelled( true );
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void PlayerRespawnEvent( PlayerRespawnEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            SetStaffItems.On( p );
        }
        
    }
    
    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onBreakBlock( BlockBreakEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
        try {
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanishOn" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanishOff" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staffOff" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "servermanager" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "clearChat" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "freeze" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "randomTp" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "report" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            if ( p.getInventory( ).getItemInMainHand( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "invsee" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
        } catch ( NullPointerException ignored ) { }
    }
    @EventHandler
    public void onPlaceBlock( BlockPlaceEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( SetStaffItems.freeze( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( SetStaffItems.reportManager( ) ) ) {
            e.setCancelled( true );
        }
        if ( p.getInventory( ).getItemInMainHand( ).isSimilar( SetStaffItems.InvSee( ) ) ) {
            e.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onHit( EntityDamageByEntityEvent e ){
        Entity mob = e.getDamager( );
        if ( mob instanceof Player ) {
            try {
                Player p = ( Player ) e.getDamager( );
                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                    if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                        e.setCancelled( true );
                    }
                }
            } catch ( ClassCastException ignored ) { }
        }
    }
    
    @EventHandler
    public void onDamage( EntityDamageEvent e ){
        Entity p = e.getEntity( );
        if ( p instanceof Player ) {
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        }
    }
    
    @EventHandler
    public void onDrop( PlayerDropItemEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
                return;
            }
        }
        if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.staffOff( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.vanishOn( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.vanishOff( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.serverManager( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.freeze( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.reportManager( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.InvSee( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        } else if ( e.getItemDrop( ).getItemStack( ).isSimilar( SetStaffItems.randomTp( ) ) ) {
            utils.PlaySound( p , "staff_items_drop" );
            e.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onItemPickUp( EntityPickupItemEvent e ){
        Entity p = e.getEntity( );
        if ( p instanceof Player ) {
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityInteract( PlayerInteractEntityEvent e ){
        Player player = e.getPlayer( );
        PersistentDataContainer PlayerData = player.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( player.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
        if ( e.getHand( ) == EquipmentSlot.OFF_HAND ) {
            return;
        }
        if ( player.getInventory( ).getItemInMainHand( ).isSimilar( SetStaffItems.freeze( ) ) ) {
            if ( e.getRightClicked( ) instanceof Player ) {
                Player p = ( Player ) e.getRightClicked( );
                PersistentDataContainer pData = p.getPersistentDataContainer( );
                if ( pData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                    FreezePlayer.FreezePlayer( p , player.getName( ) , false );
                    
                } else if ( !((pData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ))) ) {
                    if ( p.hasPermission( "staffcore.freeze.bypas" ) ) {
                        player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + p.getName( ) + plugin.getConfig( ).getString( "staff.freeze_bypass" ) ) );
                    } else {
                        FreezePlayer.FreezePlayer( p , player.getName( ) , true );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.freeze_message" ) ) );
                    }
                }
            }
        }
        if ( player.getInventory( ).getItemInMainHand( ).isSimilar( SetStaffItems.InvSee( ) ) ) {
            if ( e.getRightClicked( ) instanceof Player ) {
                Player p = ( Player ) e.getRightClicked( );
                new OpenInvSee( player , p );
            }
        }
    }
    
    @EventHandler
    public void onEntityInteract( PlayerCommandPreprocessEvent e ){
        Player player = e.getPlayer( );
        PersistentDataContainer PlayerData = player.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !player.hasPermission( "staffcore.unfreeze.himself" ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onBowUse( EntityShootBowEvent e ){
        if ( e.getEntity( ) instanceof Player && e.getProjectile( ) instanceof Arrow ) {
            Player p = ( Player ) e.getEntity( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        }
    }
    
    @EventHandler
    public void onTarget( EntityTargetEvent e ){
        if ( e.getTarget( ) instanceof Player ) {
            Player p = ( Player ) e.getTarget( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                e.setCancelled( true );
            }
            
        }
    }
    
    @EventHandler
    public void onBucketEmpty( PlayerBucketEmptyEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onBucketFill( PlayerBucketFillEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onBed( PlayerBedEnterEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onFish( PlayerFishEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onConsume( PlayerItemConsumeEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onPickArrow( PlayerPickupArrowEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                e.setCancelled( true );
            }
        }
    }
    
    @EventHandler
    public void onRecipeDiscoverEvent( PlayerRecipeDiscoverEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        }
    }
    
    @EventHandler
    public void onTrow( ProjectileLaunchEvent e ){
        if ( e.getEntity( ).getShooter( ) instanceof Player ) {
            Player p = ( Player ) e.getEntity( ).getShooter( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                    e.setCancelled( true );
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract( PlayerInteractEvent e ){
        Player p = e.getPlayer( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( e.getAction( ) == Action.RIGHT_CLICK_BLOCK ) {
            ItemStack hand = e.getPlayer( ).getInventory( ).getItemInMainHand( );
            String word = hand.getType( ).name( );
            String substring = word.substring( word.length( ) - 3 );
            try {
                if ( substring.equals( "EGG" ) ) {
                    if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                        if ( !StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                            e.setCancelled( true );
                        }
                    }
                }
            } catch ( StringIndexOutOfBoundsException ignored ) { }
        }
    }
}


