package com.zooting.api.domain.member.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ExtractObj {
    String userId;
    String gender;
    List<String> memberInterests;
    List<String> blockToList;
    List<String> blockFromList;
    List<String> friendList;
    List<String> memberIdeals;
    Integer memberBirthYear;
    Integer rangeYear;
}
