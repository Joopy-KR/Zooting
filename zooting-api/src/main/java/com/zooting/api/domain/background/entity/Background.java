package com.zooting.api.domain.background.entity;

import com.zooting.api.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Background {
    @Id
    @Column(name = "background_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;
    private Long price;

    @Builder
    public Background(File file, Long price) {
        this.file = file;
        this.price = price;
    }
}
