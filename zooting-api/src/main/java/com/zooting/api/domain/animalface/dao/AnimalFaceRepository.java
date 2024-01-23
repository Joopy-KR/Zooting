package com.zooting.api.domain.animalface.dao;

import com.zooting.api.domain.animalface.entity.AnimalFace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalFaceRepository extends JpaRepository<AnimalFace, Long> {
}
