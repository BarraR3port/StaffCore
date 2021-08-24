/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.Entities;

import java.util.Date;
import java.util.UUID;

public class Ban {
    
    private UUID BanId;
    
    private UUID BannerUUID;
    
    private UUID BannedUUID;
    
    private String Reason;
    
    private Date CreateDate;
    
    private Date ExpDate;
    
    private Boolean Permanent = false;
    
    private Boolean IpBanned = false;
    
    private Boolean Status = false;
    
    public Ban( UUID banId , UUID bannerUUID , UUID bannedUUID , String reason , Date createDate , Date expDate , Boolean permanent , Boolean ipBanned , Boolean status ){
        BanId = banId;
        BannerUUID = bannerUUID;
        BannedUUID = bannedUUID;
        Reason = reason;
        CreateDate = createDate;
        ExpDate = expDate;
        Permanent = permanent;
        IpBanned = ipBanned;
        Status = status;
    }
    
    public UUID getBanId( ){
        return BanId;
    }
    
    public void setBanId( UUID banId ){
        BanId = banId;
    }
    
    public UUID getBannerUUID( ){
        return BannerUUID;
    }
    
    public void setBannerUUID( UUID bannerUUID ){
        BannerUUID = bannerUUID;
    }
    
    public UUID getBannedUUID( ){
        return BannedUUID;
    }
    
    public void setBannedUUID( UUID bannedUUID ){
        BannedUUID = bannedUUID;
    }
    
    public String getReason( ){
        return Reason;
    }
    
    public void setReason( String reason ){
        Reason = reason;
    }
    
    public Date getCreateDate( ){
        return CreateDate;
    }
    
    public void setCreateDate( Date createDate ){
        CreateDate = createDate;
    }
    
    public Date getExpDate( ){
        return ExpDate;
    }
    
    public void setExpDate( Date expDate ){
        ExpDate = expDate;
    }
    
    public Boolean getPermanent( ){
        return Permanent;
    }
    
    public void setPermanent( Boolean permanent ){
        Permanent = permanent;
    }
    
    public Boolean getIpBanned( ){
        return IpBanned;
    }
    
    public void setIpBanned( Boolean ipBanned ){
        IpBanned = ipBanned;
    }
    
    public Boolean getStatus( ){
        return Status;
    }
    
    public void setStatus( Boolean status ){
        Status = status;
    }
}
