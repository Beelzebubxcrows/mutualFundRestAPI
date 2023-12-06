package com.financeTracker.trackerAPIService.Controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.financeTracker.trackerAPIService.Model.DataModel;
import com.financeTracker.trackerAPIService.Model.ModelRepository;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

@RestController
public class DatabaseController {

    @Autowired
    ModelRepository modelRepository;
  
    @PostMapping("mutualFundData/create")
    public int CreateMutualFundData(@RequestBody DataModel dataModel)
    {
       if(IsRequestAuthenticated()==false){
        return HTTPResponse.SC_FORBIDDEN;
       }

        dataModel.UserId = GetUserId()+"_"+dataModel.MutualFundId;
        modelRepository.save(dataModel);

        return HTTPResponse.SC_OK;
    }

    @GetMapping("mutualFundData/read/")
    public List<DataModel> ReadUserMutualFundData()
    {
        if(!IsRequestAuthenticated()){
            return null;
        }
       
      return modelRepository.findAll();
    }


    @GetMapping("mutualFundData/update")
    public void UpdateMutualFundData(@RequestBody DataModel dataModel)
    {
        if(IsRequestAuthenticated()==false) {
            return;
        }
        List<DataModel> databaseData = modelRepository.findAll();
        for(DataModel data : databaseData)
        {
            if(data.UserId.equals(dataModel.UserId)){

            }
        }
    }


    @GetMapping("mutualFundData/delete")
    public void DeleteUserMutualFund()
    {
        if(IsRequestAuthenticated()==false) {
            return;
        }


    }

   


    private boolean IsRequestAuthenticated()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication!=null && authentication.isAuthenticated();
    }

    private String GetUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    
}
