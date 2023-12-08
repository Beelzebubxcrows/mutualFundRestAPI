package com.financeTracker.trackerAPIService.Controller;
import java.util.List;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Response CreateMutualFundData(@RequestBody DataModel dataModel)
    {
       if(IsRequestAuthenticated()==false){
        
        var response  = new Response();
        response.addHeader("Status", "403");
        response.addHeader("Data", "");
        
        return response;
       }

        dataModel.UserId = GetUserId()+"_"+dataModel.MutualFundId;
        modelRepository.save(dataModel);
        var response  = new Response();
        response.addHeader("Status", "HTTPResponse.SC_OK");
        response.addHeader("Data", dataModel.UserId.toString());


        return response;
    }

    @GetMapping("mutualFundData/update")
    public int UpdateMutualFundData(@RequestBody DataModel dataModel)
    {
        if(IsRequestAuthenticated()==false) {
            return HTTPResponse.SC_FORBIDDEN;
        }
        List<DataModel> databaseData = modelRepository.findAll();
        for(DataModel data : databaseData)
        {
            if(data.UserId.equals(GetUserId()) && data.MutualFundId==dataModel.MutualFundId){
                modelRepository.delete(data);
                modelRepository.save(dataModel);
            }
        }

        return HTTPResponse.SC_OK;
    }


    @GetMapping("mutualFundData/read/{mutualFundCode}")
    public DataModel ReadUserMutualFundData(@PathVariable long mutualFundCode)
    {
        if(!IsRequestAuthenticated()){
            return null;
        }
       
        List<DataModel> dataModels =  modelRepository.findAll();

        for(DataModel data : dataModels)
        {
            if(data.MutualFundId == mutualFundCode){
                return data;
            }
        }

        return null;
    }


    
    @GetMapping("mutualFundData/delete/{mutualFundCode}")
    public void DeleteUserMutualFund(@PathVariable long mutualFundCode)
    {
        if(IsRequestAuthenticated()==false) {
            return;
        }

        List<DataModel> databaseData = modelRepository.findAll();
        for(DataModel data : databaseData)
        {
            if(data.UserId.equals(GetUserId())&& data.MutualFundId==mutualFundCode){
                modelRepository.delete(data);
    
            }
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
