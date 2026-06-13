package com.ecosort.repository;

import com.ecosort.entity.PickupRequest;
import com.ecosort.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PickupRequestRepository extends MongoRepository<PickupRequest, String> {

    List<PickupRequest> findByUser(User user);

}
