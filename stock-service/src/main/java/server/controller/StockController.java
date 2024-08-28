package server.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.repository.StockRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StockController {
    private final StockRepository stockRepository;
    private final SimpMessagingTemplate messagingTemplate;

    //    //    StockCircularBuffer buffer = new StockCircularBuffer();
////    CircularBufferEntityIndex buffer = new CircularBufferEntityIndex(5);
//    private final StockCircularBuffer stockCircularBuffer;
//
//    @Operation(summary = "댓글 작성")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK")
//    })
//    @PostMapping("/add")
//    public ResponseEntity<?> addcomment(@RequestBody Long a) {
////        StockCircularBuffer buffer = new StockCircularBuffer();
//        Stock stock = new Stock(a);
//        stockCircularBuffer.addStock(stock);
////        buffer.addEntityId(a);
//        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
//    }
    @Operation(summary = "댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addcomment() {
//        StockCircularBuffer buffer = new StockCircularBuffer();
        messagingTemplate.convertAndSend("/topic/events", "Event triggered: ");
//        buffer.addEntityId(a);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }

    @MessageMapping("/a")
    public ResponseEntity<?> a() {
//        StockCircularBuffer buffer = new StockCircularBuffer();
        messagingTemplate.convertAndSend("/topic/events", "Event triggered: ");
//        buffer.addEntityId(a);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }
}
