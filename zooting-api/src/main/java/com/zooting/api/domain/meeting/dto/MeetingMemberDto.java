package com.zooting.api.domain.meeting.dto;

import com.zooting.api.domain.animalface.entity.AnimalFace;
import com.zooting.api.domain.block.entity.Block;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
public class MeetingMemberDto {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeetingMemberDto that)) {
            return false;
        }

        if (!email.equals(that.email)) {
            return false;
        }
        return gender.equals(that.gender);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + gender.hashCode();
        return result;
    }
}
