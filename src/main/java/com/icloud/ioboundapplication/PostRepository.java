package com.icloud.ioboundapplication;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PostRepository extends ElasticsearchRepository<Post, String> {

    List<Post> findByContent(String content);
}
