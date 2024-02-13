package com.zooting.api.domain.meeting.dao;

import com.zooting.api.domain.meeting.application.WaitingRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRoomRedisRepository extends CrudRepository<WaitingRoom, String> {
}
