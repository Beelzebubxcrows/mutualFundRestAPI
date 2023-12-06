package com.financeTracker.trackerAPIService.Model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelRepository extends MongoRepository<DataModel, String> 
{}
