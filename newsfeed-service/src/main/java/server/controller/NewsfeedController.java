package server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newsfeeds")
@RequiredArgsConstructor
public class NewsfeedController {

//    private NewsfeedService newsfeedService;
//
//    @GetMapping("/details/{userId}")
//    public ResponseEntity<NewsfeedDTO> getNewsfeedDetails(@PathVariable Long userId) {
//        NewsfeedDTO newsfeedDTO = newsfeedService.getNewsfeedDetails(userId);
//        return ResponseEntity.ok(newsfeedDTO);
//    }
}