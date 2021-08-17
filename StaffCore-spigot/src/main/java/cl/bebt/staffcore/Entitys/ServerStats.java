/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Entitys;

import java.util.UUID;

public class ServerStats {
    
    private UUID ServerUUID;
    
    private Integer CurrentBans;
    
    private Integer BansCount;
    
    private Integer WarnsCount;
    
    private Integer ReportsCount;
    
    
    public ServerStats( UUID serverUUID ){
        ServerUUID = serverUUID;
    }
    
    public UUID getServerUUID( ){
        return ServerUUID;
    }
    
    public void setServerUUID( UUID serverUUID ){
        ServerUUID = serverUUID;
    }
    
    public Integer getCurrentBans( ){
        return CurrentBans;
    }
    
    public void setCurrentBans( Integer currentBans ){
        CurrentBans = currentBans;
    }
    
    public Integer getBansCount( ){
        return BansCount;
    }
    
    public void setBansCount( Integer bansCount ){
        BansCount = bansCount;
    }
    
    public Integer getWarnsCount( ){
        return WarnsCount;
    }
    
    public void setWarnsCount( Integer warnsCount ){
        WarnsCount = warnsCount;
    }
    
    public Integer getReportsCount( ){
        return ReportsCount;
    }
    
    public void setReportsCount( Integer reportsCount ){
        ReportsCount = reportsCount;
    }
}