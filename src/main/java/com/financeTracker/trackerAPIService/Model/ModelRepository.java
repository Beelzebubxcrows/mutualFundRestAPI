package com.financeTracker.trackerAPIService.Model;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.financeTracker.trackerAPIService.Model.Models.DataModel;

public interface ModelRepository extends MongoRepository<DataModel, String> 
{}
