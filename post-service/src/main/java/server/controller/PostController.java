package server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dto.request.RequestDto;
import server.dto.request.RequestPostDto;
import server.dto.response.PostResponseDto;
import server.service.PostService;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "post 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/create")
    public ResponseEntity<?> creatpost(@Valid @RequestBody RequestPostDto request) {

        postService.createpost(request);
        return ResponseEntity.status(HttpStatus.OK).body("게시물이 생성되었습니다.");
    }

    @Operation(summary = "post 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/update/{post_id}")
    public ResponseEntity<?> updatepost(@Valid @RequestBody RequestPostDto request, @PathVariable("post_id") Long postId) {
        postService.updatepost(request, postId);
        return ResponseEntity.status(HttpStatus.OK).body("게시물이 수정되었습니다.");
    }
    @Operation(summary = "post 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/details/{post_id}")
    public ResponseEntity<?> detailspost(@Valid @RequestBody RequestDto request, @PathVariable("post_id") Long postId) {
        PostResponseDto postResponseDto = postService.detailspost(postId, request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    @Operation(summary = "post 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/delete/{post_id}")
    public ResponseEntity<?> deletepost(@Valid @RequestBody RequestDto request,@PathVariable("post_id") Long postId) {
        postService.deletepost(postId, request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body("게시물이 삭제되었습니다.");
    }

}
