package com.icloud.ioboundapplication;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    /* 1. 글을 작성한다. */
    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        Post savedPost = postRepository.save(mapper.map(postDto, Post.class));
        PostDto result = mapper.map(savedPost, PostDto.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    /* 2-1. 글 목록을 조회한다. */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPostList() {
        List<PostDto> collect = postRepository.findAll()
                .stream().map(post -> mapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collect);
    }


}
