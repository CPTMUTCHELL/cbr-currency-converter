package com.example.converter.repository;

import com.example.converter.entity.PresentationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DtoRepo extends JpaRepository<PresentationDto,Integer>, JpaSpecificationExecutor<PresentationDto> {
}
