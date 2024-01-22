package com.zooting.api.domain.notice.entity;

import com.zooting.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Builder
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
