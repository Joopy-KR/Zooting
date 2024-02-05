package com.zooting.api.domain.meeting.dao;

import com.zooting.api.domain.meeting.dto.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRedisRepository extends CrudRepository<Room, String> {

}
