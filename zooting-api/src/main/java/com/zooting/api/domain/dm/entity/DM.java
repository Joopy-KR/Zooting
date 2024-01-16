package com.zooting.api.domain.dm.entity;

import com.zooting.api.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DM {
    @Id
    @Column(name = "dm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    @JoinColumn(name = "dm_id")
    @Builder.Default
    private List<File> files = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "dm_room_id")
    private DMRoom dmRoom;
    private String message;
    private Boolean status;

    @Builder
    public DM(List<File> files, DMRoom dmRoom, String message, Boolean status) {
        this.files = files;
        this.dmRoom = dmRoom;
        this.message = message;
        this.status = status;
    }
}
