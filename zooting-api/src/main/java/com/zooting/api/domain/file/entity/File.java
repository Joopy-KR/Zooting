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
    @Column(length = 1000)
    private String fileName; // 변환된 파일명
    private String originFileName; // 파일 원본 이름
    private String imgUrl; // 파일 링크
    private String fileDir; // S3 파일 경로
    private String thumbnailUrl; // 썸네일 파일 링크

    @Builder
    public File(DM dm, String fileName, String imgUrl, String fileDir, String thumbnailUrl, String originFileName) {
        this.dm = dm;
        this.fileName = fileName;
        this.imgUrl = imgUrl;
        this.fileDir = fileDir;
        this.thumbnailUrl = thumbnailUrl;
        this.originFileName = originFileName;
    }
}
