package com.icloud.ioboundapplication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Getter
public class PostCacheService {

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    private Page<PostDto> firstPostPage;

    @Scheduled(cron = "* * * * * *")
    public void updateFirstPostPage() {
        Page<Post> postList = postRepository.findAll(
                PageRequest.of(0, 20, Sort.by("id").descending())
        );

        List<PostDto> result = postList.getContent()
                .stream().map(post -> mapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        firstPostPage = new PageImpl<>(result);
    }
}
