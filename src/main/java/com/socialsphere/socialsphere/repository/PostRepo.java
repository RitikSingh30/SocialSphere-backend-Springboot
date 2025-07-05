package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.PostEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends MongoRepository<PostEntity, ObjectId> {
}
