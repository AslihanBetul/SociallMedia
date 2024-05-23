package com.abm.controller;

import com.abm.constant.EndPoints;

import com.abm.dto.request.PostSaveDto;
import com.abm.dto.request.PostUpdateDto;
import com.abm.dto.response.PostResponseDto;
import com.abm.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoints.POST)
public class PostController {
    private final PostService postService;

    @PostMapping(EndPoints.SAVE)
    public ResponseEntity<String>savePost(@RequestBody PostSaveDto dto){
        return ResponseEntity.ok(postService.savePost(dto));
    }


    @GetMapping("/getmypost")
    public ResponseEntity<List<PostResponseDto>>getMyPost(String token){
        return ResponseEntity.ok(postService.getMyPost(token));


    }
    @GetMapping(EndPoints.FINDALL)
    public ResponseEntity<List<PostResponseDto>>findAll(String token){
        return ResponseEntity.ok(postService.findAll(token));
    }
    @DeleteMapping(EndPoints.DELETE)
    public ResponseEntity<String>deletePost(String token, String postId){
        return ResponseEntity.ok(postService.deletePost(token,postId));
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<PostResponseDto>updatePost(@RequestBody PostUpdateDto dto){
        return ResponseEntity.ok(postService.updatePost(dto));
    }
}
