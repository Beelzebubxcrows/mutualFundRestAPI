package com.financeTracker.trackerAPIService.Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeTracker.Utility.CustomResponses;
import com.financeTracker.trackerAPIService.Model.DataModel;
import com.financeTracker.trackerAPIService.Model.ModelRepository;
import com.financeTracker.trackerAPIService.Model.MutualFundData;
import com.financeTracker.trackerAPIService.Model.MutualFundReport;

@RestController
public class DatabaseController {

    @Autowired
    ModelRepository modelRepository;
  
    @PostMapping("mutualFundData/create")
    public HashMap<String, String> CreateMutualFundData(@RequestBody MutualFundData mutualFundData)
    {
       if(IsRequestAuthenticated()==false)  {
            return CustomResponses.getForbiddenReponse();
       }
    
       float currentMarketValue = ApplicationController.GetMutualFundNavValue(mutualFundData.mutualFundId);
       if(currentMarketValue==-1)
       {
        return CustomResponses.getErrorResponse("Mutual fund code is wrong.");
       }
       boolean isAlreadyPresent = false;
        List<DataModel> dataModels =  modelRepository.findAll();
        for(DataModel dataModel : dataModels)
        {
            if(dataModel.id.equals(GetUserId()))
            {
                dataModel.mutualFundData.add(mutualFundData);
                modelRepository.save(dataModel);
                isAlreadyPresent = true;
            }
        }

        if(!isAlreadyPresent){
            DataModel datamodel = new DataModel();
            datamodel.id = GetUserId();
            datamodel.mutualFundData = new ArrayList<>();
            datamodel.mutualFundData.add(mutualFundData);
            modelRepository.save(datamodel);
        }

    
        return CustomResponses.getSuccessResponse(mutualFundData.toString());
    }

    @PostMapping("mutualFundData/update")
    public HashMap<String, String> UpdateMutualFundData(@RequestBody MutualFundData mutualFundDataRequest)
    {
        if(IsRequestAuthenticated()==false)  {
            return CustomResponses.getForbiddenReponse();
       }

       
        List<DataModel> dataModels =  modelRepository.findAll();
        for(DataModel dataModel : dataModels)
        {
            if(dataModel.id.equals(GetUserId()))
            {
                for(MutualFundData mutualFundData : dataModel.mutualFundData)
                {
                    if(mutualFundData.mutualFundId == (mutualFundDataRequest.mutualFundId))
                    {
                        mutualFundData.investedUnits = mutualFundDataRequest.investedUnits;
                        mutualFundData.investedValue = mutualFundDataRequest.investedValue;
                        mutualFundData.mutualFundId = mutualFundDataRequest.mutualFundId;
                        mutualFundData.mutualFundName = mutualFundDataRequest.mutualFundName;
                        modelRepository.save(dataModel);
                        return CustomResponses.getSuccessResponse(mutualFundData.toString());
                    }
                }
            
            }
        }



        return CustomResponses.getErrorResponse("No data with this code is present.");
    }

    


    @GetMapping("mutualFundData/read/{mutualFundCode}")
    public HashMap<String, String> ReadUserMutualFundData(@PathVariable long mutualFundCode)
    {
        if(!IsRequestAuthenticated()){
            return CustomResponses.getForbiddenReponse();
        }
       
        List<DataModel> dataModels =  modelRepository.findAll();

        for(DataModel data : dataModels)
        {
            if(data.id.equals(GetUserId()))  {
                for (MutualFundData mutualFundData : data.mutualFundData) {
                    if(mutualFundData.mutualFundId == mutualFundCode){
                        var response  = CustomResponses.getSuccessResponse(mutualFundData.toString());
                        return response;
                    }
                }
            }
        }

        return CustomResponses.getErrorResponse("You do not have any mutual fund with this code yet.");
    }

    @GetMapping("mutualFundData/readAll")
    public HashMap<String, String> ReadAllUserMutualFundData()
    {
       if(!IsRequestAuthenticated()){
            return CustomResponses.getForbiddenReponse();
        }
       
        List<DataModel> dataModels =  modelRepository.findAll();

        for(DataModel data : dataModels)
        {
            if(data.id.equals(GetUserId()))  {
                var response  = CustomResponses.getSuccessResponse(data.mutualFundData.toString());
                return response;
            }
        }

        return CustomResponses.getErrorResponse("You do not have any mutual fund entry yet.");
    }


    
    @PostMapping("mutualFundData/delete/{mutualFundCode}")
    public HashMap<String, String> DeleteUserMutualFund(@PathVariable long mutualFundCode)
    {
        if(IsRequestAuthenticated()==false) {
            return CustomResponses.getForbiddenReponse();
        }

         List<DataModel> dataModels =  modelRepository.findAll();

        for(DataModel data : dataModels)
        {
            if(data.id.equals(GetUserId()))  {
                List<MutualFundData> mutualFundDatas = new ArrayList<>(data.mutualFundData);

                for (MutualFundData mutualFundData : data.mutualFundData) {
                    if(mutualFundData.mutualFundId == mutualFundCode){
                        mutualFundDatas.remove(mutualFundData);
                    }
                }
                data.mutualFundData = mutualFundDatas;
                
                if(mutualFundDatas.size()==0)
                {
                    modelRepository.delete(data);
                }
                else
                {
                    modelRepository.save(data);
                }

                return CustomResponses.getSuccessResponse("Deleted : "+mutualFundCode);
            }
        }

        return CustomResponses.getErrorResponse("You do not have any mutual fund entry with this code.");
    }


    @GetMapping("mutualFundData/getMutualFundReport")
    public HashMap<String, String> GetMutualFundReport()
    {
        if(IsRequestAuthenticated()==false) {
            return CustomResponses.getForbiddenReponse();
        }

        List<MutualFundReport> mutualFundReports = new ArrayList<>();
        List<DataModel> dataModels =  modelRepository.findAll();

        for(DataModel data : dataModels)
        {
            if(data.id.equals(GetUserId()))  {

                for(MutualFundData mutualFundData : data.mutualFundData)
                {
                    MutualFundReport mutualFundReport = new MutualFundReport();
                    mutualFundReport.name = mutualFundData.mutualFundName;
                    mutualFundReport.investedValue = mutualFundData.investedValue;
                    float currentMarketValue = ApplicationController.GetMutualFundNavValue(mutualFundData.mutualFundId);
                    mutualFundReport.currentValue = mutualFundData.investedUnits * currentMarketValue;
                    mutualFundReport.returnValue = mutualFundReport.currentValue - mutualFundReport.investedValue;
                    mutualFundReport.returnPercentage = ((mutualFundReport.returnValue)/mutualFundReport.currentValue)*100;
                    mutualFundReports.add(mutualFundReport);
                }
            
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return CustomResponses.getSuccessResponse(objectMapper.writeValueAsString(mutualFundReports));
        } catch (JsonProcessingException e) {
            return CustomResponses.getErrorResponse("Report Generation failed");
        }
    }

   


    private boolean IsRequestAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication!=null && authentication.isAuthenticated();
    }

    private String _userId;

    private String GetUserId()
    {
        if(_userId==null || _userId.length()==0)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            _userId = authentication.getName();
        }
        
        return _userId;
    }

    
}
