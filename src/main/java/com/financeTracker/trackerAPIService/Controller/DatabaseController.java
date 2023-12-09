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

       boolean isAlreadyPresent = false;
        List<DataModel> dataModels =  modelRepository.findAll();
        for(DataModel dataModel : dataModels)
        {
            if(dataModel.UserId.equals(GetUserId()))
            {
                dataModel.mutualFundData.add(mutualFundData);
                modelRepository.save(dataModel);
                isAlreadyPresent = true;
            }
        }

        if(!isAlreadyPresent){
            DataModel datamodel = new DataModel();
            datamodel.UserId = GetUserId();
            datamodel.mutualFundData = new ArrayList<>();
            datamodel.mutualFundData.add(mutualFundData);
            modelRepository.save(datamodel);
        }

    
        return CustomResponses.getSuccessResponse(mutualFundData.toString());
    }

    @PostMapping("mutualFundData/update")
    public HashMap<String, String> UpdateMutualFundData(@RequestBody MutualFundData mutualFundData)
    {
        if(IsRequestAuthenticated()==false)  {
            return CustomResponses.getForbiddenReponse();
       }

       boolean isAlreadyPresent = false;
        List<DataModel> dataModels =  modelRepository.findAll();
        for(DataModel dataModel : dataModels)
        {
            if(dataModel.UserId.equals(GetUserId()))
            {
                dataModel.mutualFundData.add(mutualFundData);
                modelRepository.save(dataModel);
                isAlreadyPresent = true;
            }
        }

        if(!isAlreadyPresent){
            DataModel datamodel = new DataModel();
            datamodel.UserId = GetUserId();
            datamodel.mutualFundData = new ArrayList<>();
            datamodel.mutualFundData.add(mutualFundData);
            modelRepository.save(datamodel);
        }

        return CustomResponses.getSuccessResponse(mutualFundData.toString());
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
            if(data.UserId == GetUserId())  {
                for (MutualFundData mutualFundData : data.mutualFundData) {
                    if(mutualFundData.MutualFundId == mutualFundCode){
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
            if(data.UserId == GetUserId())  {
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
            if(data.UserId == GetUserId())  {
                List<MutualFundData> mutualFundDatas = new ArrayList<>(data.mutualFundData);

                for (MutualFundData mutualFundData : data.mutualFundData) {
                    if(mutualFundData.MutualFundId == mutualFundCode){
                        mutualFundDatas.remove(mutualFundData);
                    }
                }
                data.mutualFundData = mutualFundDatas;
                modelRepository.save(data);
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
            if(data.UserId == GetUserId())  {

                for(MutualFundData mutualFundData : data.mutualFundData)
                {
                    MutualFundReport mutualFundReport = new MutualFundReport();
                    mutualFundReport.name = mutualFundData.MutualFundName;
                    mutualFundReport.investedValue = mutualFundData.InvestedValue;
                    float currentMarketValue = ApplicationController.GetMutualFundNavValue(mutualFundData.MutualFundId);
                    mutualFundReport.currentValue = mutualFundData.InvestedUnits * currentMarketValue;
                    mutualFundReport.returnValue = mutualFundReport.currentValue - mutualFundReport.investedValue;
                    mutualFundReport.returnPercentage = ((mutualFundReport.returnValue)/mutualFundReport.currentValue)*100;
                    mutualFundReports.add(mutualFundReport);
                }
            
            }
        }

        return CustomResponses.getSuccessResponse(mutualFundReports.toString());
    }

   


    private boolean IsRequestAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication!=null && authentication.isAuthenticated();
    }

    private String _userId;

    private String GetUserId()
    {
        if(_userId.length()==0)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            _userId = authentication.getName();
        }
        
        return _userId;
    }

    
}
