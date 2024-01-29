package com.zooting.api.domain.file.dao;

import com.zooting.api.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    void deleteByFileName(String fileName);

    Optional<File> findByFileName(String fileName);
}
