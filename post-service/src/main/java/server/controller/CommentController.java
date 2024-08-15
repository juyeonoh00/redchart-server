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
import server.dto.request.RequestCommentDto;
import server.dto.request.RequestDto;
import server.service.CommentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/comments")
public class CommentController {

    private final CommentService commentService;
    @Operation(summary = "댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/{post_id}/add")
    public ResponseEntity<?> addcomment(@Valid @RequestBody RequestCommentDto request, @PathVariable("post_id") Long postId){
        commentService.addcomment(request);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }

    @Operation(summary = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/{post_id}/{comment_id}/delete")
    public ResponseEntity<?> updatecomment(@Valid @RequestBody RequestCommentDto request, @PathVariable("post_id") Long postId, @PathVariable("comment_id") Long commentId){
        commentService.updatecomment(postId, request.getUserId(), commentId, request);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 수정되었습니다.");
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/{post_id}/{comment_id}/update")
    public ResponseEntity<?> deletecomment(@Valid @RequestBody RequestDto request, @PathVariable("post_id") Long postId,@PathVariable("comment_id") Long commentId){
        commentService.deletecomment(postId, request.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}


