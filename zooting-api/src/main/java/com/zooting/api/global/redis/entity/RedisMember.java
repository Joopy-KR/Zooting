package com.zooting.api.global.redis.entity;

import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.background.entity.BackgroundInventory;
import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.friend.entity.Friend;
import com.zooting.api.domain.friend.entity.FriendRequest;
import com.zooting.api.domain.mask.entity.MaskInventory;
import com.zooting.api.domain.meeting.entity.MeetingLog;
import com.zooting.api.domain.member.entity.AdditionalInfo;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.redis.dto.response.RedisMemberDto;
import io.swagger.v3.oas.annotations.info.Contact;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class RedisMember {
    @Id
    private String email;
    private String gender;
    private String nickname;
    private String address;
    private String personality;
    private String animal;
    private String introduce;
    private String interest;
    private String idealAnimal;
    private AnimalFace animalFace;
    private List<Block> blockFromList;  // 내가 차단한 리스트

}
