package com.icloud.ioboundapplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "post")
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;

    private String content;
}
