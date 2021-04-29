package com.icloud.ioboundapplication;

import lombok.RequiredArgsConstructor;
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
public class PostController {

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    private static final Integer PAGE_SIZE = 20;

    /* 1. 글을 작성한다. */
    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        Post savedPost = postRepository.save(mapper.map(postDto, Post.class));
        PostDto result = mapper.map(savedPost, PostDto.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    /* 2. 글 목록을 페이징하여 반환 */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto>> getPostList(@RequestParam(defaultValue = "1") Integer page) {
        Page<Post> result = postRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending())
        );

        List<PostDto> collect = result.getContent()
                .stream().map(post -> mapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new PageImpl<>(collect));
    }



    /* 3. 글 번호로 조회 */

    /* 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글 */


}
