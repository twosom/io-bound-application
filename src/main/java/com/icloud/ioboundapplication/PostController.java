package com.icloud.ioboundapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final Integer PAGE_SIZE = 20;

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
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        PostDto findPost = mapper.map(postRepository.findById(id)
                .orElse(new Post(Long.MIN_VALUE, "해당하는 게시글이 존재하지 않습니다. 게시글 id = " + id)), PostDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findPost);
    }

    /* 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글 */
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> findPostsByContent(@RequestParam String content) {
        List<PostDto> collect = postRepository.findByContentContains(content)
                .stream().map(post -> mapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collect);
    }

    /* 5. 적합한 테스트를 위해 모든 데이터 제거 */
    @DeleteMapping("/delete_all")
    public ResponseEntity<String> deleteAll() {
        postRepository.deleteAllInBatch();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("All Data Deleted");
    }

}
