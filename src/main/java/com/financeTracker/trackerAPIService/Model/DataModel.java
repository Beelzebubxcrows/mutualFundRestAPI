package com.financeTracker.trackerAPIService.Model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Document(collection = "UserData")
public class DataModel {
    
    @Id
    @Field("_id")
    public String id;
    
    public List<MutualFundData> mutualFundData;

    public DataModel()
    {
        mutualFundData = new ArrayList<>();
    }
    
}


