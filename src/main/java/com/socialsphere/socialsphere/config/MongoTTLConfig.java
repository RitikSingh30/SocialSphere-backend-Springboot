package com.socialsphere.socialsphere.config;

import com.socialsphere.socialsphere.entity.OtpEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class MongoTTLConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps(OtpEntity.class).ensureIndex(
                new Index()
                        .on("createdAt", org.springframework.data.domain.Sort.Direction.ASC)
                        .expire(5, TimeUnit.MINUTES)
        );
    }
}
