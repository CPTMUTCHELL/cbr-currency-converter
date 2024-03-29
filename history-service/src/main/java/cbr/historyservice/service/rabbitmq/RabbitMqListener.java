package cbr.historyservice.service.rabbitmq;

import cbr.entity.PresentationDto;
import cbr.historyservice.repository.HistoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


//This bean is created if rabbitmq.enabled is true
// Look at Config.java
// I created this class to avoid A component required a bean named 'rabbitListenerContainerFactory' that could not be found. when rabbit isn't enabled

@EnableRabbit
@Slf4j
public  class RabbitMqListener {
    @Autowired
    private HistoryRepo historyRepo;
    @RabbitListener(queues = "${rabbitmq.queue}" )

    public void saveDtoRabbit(PresentationDto dto) {
        historyRepo.save(dto);
        log.info("Rabbit : dto: {} saved ", dto);

    }


}