package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.utils.Items;

public abstract class InvSeePlayerMenu extends InventoryMenu{

    protected int page = 0;
    protected int maxItemsPerPage = 45;
    protected int index = 0;

    public InvSeePlayerMenu( PlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }

    public void addMenuBorder( ){
        inventory.setItem( 27 , Items.greenPanel( ) );
    }

    public int getMaxItemsPerPage( ){
        return maxItemsPerPage;
    }
}

