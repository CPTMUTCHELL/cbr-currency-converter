package com.example.historyservice.service;

import com.example.entity.PresentationDto;
import com.example.historyservice.repository.HistoryRepo;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
//@EnableRabbit
@Slf4j
public class HistoryService {

    private HistoryRepo historyRepo;
    @Autowired
    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

//    @RabbitListener(queues = "${spring.queue}")
        public PresentationDto saveDto(PresentationDto dto) {

        log.info("dto: {} saved ", dto);
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
