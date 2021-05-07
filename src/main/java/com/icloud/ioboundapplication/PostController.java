package com.icloud.ioboundapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private final Producer producer;
    private final ObjectMapper objectMapper;

    /* 1. 글을 작성한다. */
    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
        String jsonPost = null;
        try {
            jsonPost = objectMapper.writeValueAsString(post);
            producer.sendTo(jsonPost);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postDto);
    }




    /* 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글 */
    @GetMapping("/search")
    public ResponseEntity<Result<List<PostDto>>> findPostsByContent(@RequestParam String content) {
        List<PostDto> collect = postRepository.findByContent(content)
                .stream().map(post -> mapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Result<>(collect.size(), collect));
    }

    @Data
    @AllArgsConstructor @NoArgsConstructor
    static class Result<E> {
        private int count;
        private E data;
    }

}
