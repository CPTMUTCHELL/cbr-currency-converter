package com.example.historyservice.specification;

import com.example.entity.PresentationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends JpaRepository<PresentationDto,Integer>, JpaSpecificationExecutor<PresentationDto> {
}
