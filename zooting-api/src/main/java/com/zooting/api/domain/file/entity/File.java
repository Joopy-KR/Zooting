package com.zooting.api.domain.file.entity;

import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.dm.entity.DM;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class File extends BaseEntity {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dm_id")
    private DM dm;
    private String fileName; // 변환된 파일명
    private String img_url; // 파일 링크

    @Builder
    public File(DM dm, String fileName, String img_url) {
        this.dm = dm;
        this.fileName = fileName;
        this.img_url = img_url;
    }
}
