package server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.kafka.UpbitTickerEventDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "stock-event", groupId = "stock-service-group")
    public void listenStockEvent(UpbitTickerEventDto upbitTickerEventDto) {

        //웹소켓에 전달
        messagingTemplate.convertAndSend("/sub/topic", upbitTickerEventDto);
        //timestamp가 1분 00초이면 저장
        if ((upbitTickerEventDto.getTimestamp() / 1000) % 60 == 0) {
            Long n = upbitTickerEventDto.getTimestamp();
//            log.info(String.valueOf(n));
            // 각 db에 transaction 및 병렬으로 저장


            // db에 있는 데이터를 병렬적으로 보냄
        }
        log.info(String.valueOf(upbitTickerEventDto.getTimestamp()));
        //웹소켓에 전달
    }

//    public String a() {
////        StockCircularBuffer buffer = new StockCircularBuffer();
//        messagingTemplate.convertAndSend("/sub/topic", "Event triggered: ");
////        buffer.addEntityId(a);
//        return "---------------";
//    }

}
