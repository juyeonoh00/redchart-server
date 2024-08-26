package server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import server.kafka.UpbitTickerEventDto;

@Slf4j
@Service
public class StockService {
    @KafkaListener(topics = "stock-event", groupId = "stock-service-group")
    public void listenStockEvent(UpbitTickerEventDto upbitTickerEventDto) {
        //1분에 한번씩 db에 저장
        log.info(upbitTickerEventDto.getMarket());
        //웹소켓에 전달
    }

}
