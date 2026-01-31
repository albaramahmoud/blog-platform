package com.devtiro.blog.services;

import com.devtiro.blog.dto.tags.CreateTagsRequest;
import com.devtiro.blog.dto.tags.TagResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TagService {

    public List<TagResponse> getTags();

    public List<TagResponse> createTags(CreateTagsRequest createTagsRequest);

    void deleteTag(UUID id);
}
