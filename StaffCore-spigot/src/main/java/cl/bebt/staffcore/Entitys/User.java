/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Entitys;

import cl.bebt.staffcore.utils.UUID.UUIDGetter;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class User {
    
    private String Name;
    
    private UUID UUID;
    
    private String Skin;
    
    private HashMap < String, String > Alts;
    
    private Date dateCreated;
    
    private Boolean Staff = false;
    
    private Boolean Vanish = false;
    
    private Boolean Frozen = false;
    
    private Boolean Ban = false;
    
    private Boolean Mute = false;
    
    private Boolean FakeJoinLeave = false;
    
    private Boolean Fly = false;
    
    private Boolean StaffChat = false;
    
    private String StaffInventory;
    
    private String StaffArmor;
    
    private String FreezeHelmet;
    
    private Double mutedTime = 0D;
    
    private int Reports;
    
    private int Bans;
    
    private int Warns;
    
    private int Mutes;
    
    public User( String name , String Skin , HashMap < String, String > Alts ){
        this.Name = name;
        this.UUID = UUIDGetter.getUUID( Name );
        this.Skin = Skin;
        this.Alts = Alts;
        this.dateCreated = new Date( );
    }
    
    public String getName( ){
        return Name;
    }
    
    public void setName( String name ){
        Name = name;
    }
    
    public java.util.UUID getUUID( ){
        return UUID;
    }
    
    public void setUUID( java.util.UUID UUID ){
        this.UUID = UUID;
    }
    
    public String getSkin( ){
        return Skin;
    }
    
    public void setSkin( String skin ){
        Skin = skin;
    }
    
    public HashMap < String, String > getAlts( ){
        return Alts;
    }
    
    public void setAlts( HashMap < String, String > alts ){
        Alts = alts;
    }
    
    public Date getDateCreated( ){
        return dateCreated;
    }
    
    public void setDateCreated( Date dateCreated ){
        this.dateCreated = dateCreated;
    }
    
    public Boolean getStaff( ){
        return Staff;
    }
    
    public void setStaff( Boolean staff ){
        Staff = staff;
    }
    
    public Boolean getVanish( ){
        return Vanish;
    }
    
    public void setVanish( Boolean vanish ){
        Vanish = vanish;
    }
    
    public Boolean getFrozen( ){
        return Frozen;
    }
    
    public void setFrozen( Boolean frozen ){
        Frozen = frozen;
    }
    
    public Boolean getBan( ){
        return Ban;
    }
    
    public void setBan( Boolean ban ){
        Ban = ban;
    }
    
    public Boolean getMute( ){
        return Mute;
    }
    
    public void setMute( Boolean mute ){
        Mute = mute;
    }
    
    public int getReports( ){
        return Reports;
    }
    
    public void setReports( int reports ){
        Reports = reports;
    }
    
    public int getBans( ){
        return Bans;
    }
    
    public void setBans( int bans ){
        Bans = bans;
    }
    
    public int getWarns( ){
        return Warns;
    }
    
    public void setWarns( int warns ){
        Warns = warns;
    }
    
    public int getMutes( ){
        return Mutes;
    }
    
    public void setMutes( int mutes ){
        Mutes = mutes;
    }
    
    public String getStaffInventory( ){
        return StaffInventory;
    }
    
    public void setStaffInventory( String staffInventory ){
        StaffInventory = staffInventory;
    }
    
    public String getStaffArmor( ){
        return StaffArmor;
    }
    
    public void setStaffArmor( String staffArmor ){
        StaffArmor = staffArmor;
    }
    
    public Boolean getFakeJoinLeave( ){
        return FakeJoinLeave;
    }
    
    public void setFakeJoinLeave( Boolean fakeJoinLeave ){
        FakeJoinLeave = fakeJoinLeave;
    }
    
    public Boolean getFly( ){
        return Fly;
    }
    
    public void setFly( Boolean fly ){
        Fly = fly;
    }
    
    public String getFreezeHelmet( ){
        return FreezeHelmet;
    }
    
    public void setFreezeHelmet( String freezeHelmet ){
        FreezeHelmet = freezeHelmet;
    }
    
    public Double getMutedTime( ){
        return mutedTime;
    }
    
    public void setMutedTime( Double mutedTime ){
        this.mutedTime = mutedTime;
    }
    
    public Boolean getStaffChat( ){
        return StaffChat;
    }
    
    public void setStaffChat( Boolean staffChat ){
        StaffChat = staffChat;
    }
}
