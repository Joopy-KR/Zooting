package com.zooting.api.global.redis.dao;

import com.zooting.api.global.redis.entity.RedisMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisMatchRepository extends CrudRepository<RedisMember, String> {

}
