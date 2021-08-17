/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu;

public abstract class PaginatedMenu extends Menu {
    protected int page = 0;
    
    protected int maxItemsPerPage = 28;
    
    protected int index = 0;
    
    public PaginatedMenu( PlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    public void addMenuBorder( ){
        this.inventory.setItem( 48 , back( ) );
        this.inventory.setItem( 49 , close( ) );
        this.inventory.setItem( 50 , next( ) );
        int i;
        for ( i = 0; i < 10; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , redPanel( ) );
        }
        this.inventory.setItem( 17 , redPanel( ) );
        this.inventory.setItem( 18 , redPanel( ) );
        this.inventory.setItem( 26 , redPanel( ) );
        this.inventory.setItem( 27 , redPanel( ) );
        this.inventory.setItem( 35 , redPanel( ) );
        this.inventory.setItem( 36 , redPanel( ) );
        for ( i = 44; i < 54; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , redPanel( ) );
        }
    }
    
    
    public int getMaxItemsPerPage( ){
        return this.maxItemsPerPage;
    }
}
