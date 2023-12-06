package com.financeTracker.trackerAPIService.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "UserData")
public class DataModel {
    
    @Id
    @Field("_id")
    public String UserId;
    
    public long MutualFundId;
    public String MutualFundName;
    public float InvestedUnits;
    public float InvestedValue;
    
}
