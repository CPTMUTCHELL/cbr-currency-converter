package com.example.historyservice.service;

import com.example.entity.PresentationDto;
import com.example.historyservice.repository.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private HistoryRepo historyRepo;
    @Autowired
    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }


        public PresentationDto saveDto(PresentationDto dto) {
        return historyRepo.save(dto);
    }
        public Page<PresentationDto> findPaginated(Specification<PresentationDto> spec,
                                                   int pageNumber, int pageSize,
                                                   String sortField, String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNumber-1,pageSize,sort);
        return historyRepo.findAll(spec,pageable);
    }
}
