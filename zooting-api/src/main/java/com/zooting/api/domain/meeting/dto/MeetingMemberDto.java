package com.zooting.api.domain.meeting.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<String> blockFromList;  // 내가 차단한 리스트

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
