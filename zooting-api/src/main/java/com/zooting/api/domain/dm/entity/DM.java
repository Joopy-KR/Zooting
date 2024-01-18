package com.zooting.api.domain.dm.entity;

import com.zooting.api.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DM {
    @Id
    @Column(name = "dm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "dm")
    private List<File> files;
    @ManyToOne
    @JoinColumn(name = "dm_room_id")
    private DMRoom dmRoom;
    private String message;
    private Boolean status;
    private String sender;

    @Builder
    public DM(List<File> files, DMRoom dmRoom, String message, Boolean status, String sender) {
        this.files = Objects.nonNull(files) ? files : new ArrayList<>();
        this.dmRoom = dmRoom;
        this.message = message;
        this.status = status;
        this.sender = sender;
    }
}
