package com.zooting.api.domain.dm.dao;

import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.file.dao.FileRepository;
import com.zooting.api.domain.file.entity.File;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest
class DMRepositoryTest {

    @Autowired
    DMRepository dmRepository;
    @Autowired
    FileRepository fileRepository;
    @Test
    @Transactional
    void getFilesById() {
        DM dm = DM.builder().message("test").build();
        File file = File.builder().dm(dm).fileName("test1").build();
        File file2 = File.builder().dm(dm).fileName("test2").build();
        File file3 = File.builder().dm(dm).fileName("test3").build();
        dm.setFiles(List.of(file, file2, file3));
        fileRepository.saveAll(List.of(file, file2, file3));
        dmRepository.save(dm);

        List<File> files = dmRepository.getFilesById(dm.getId());
        files.forEach(file1 -> {
            System.out.println(file1.getFileName());
        });
        assertNotNull(files);
        assertEquals(3, files.size());
    }
}