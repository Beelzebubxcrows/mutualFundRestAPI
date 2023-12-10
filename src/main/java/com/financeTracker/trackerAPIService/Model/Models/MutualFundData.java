package com.financeTracker.trackerAPIService.Model.Models;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MutualFundData
{
    public long mutualFundId;  
    public String mutualFundName;
    public float investedUnits;
    public float investedValue;

}
