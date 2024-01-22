package com.zooting.api.domain.mask.entity;


import com.zooting.api.domain.BaseEntity;
import com.zooting.api.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Mask  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mask_id")
    private Long id;
    private String animal;
    private String description;
    @OneToOne
    @JoinColumn(name = "file_id")
    private File file; // 마스크 한개 당 (이미지)파일 한개
    private Long price;

    @Builder
    public Mask(String animal, String description, File file, Long price) {
        this.animal = animal;
        this.description = description;
        this.file = file;
        this.price = price;
    }
}

