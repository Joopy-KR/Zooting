package com.zooting.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public enum Privilege {
    ANONYMOUS, USER, MANAGER, ADMIN
}
