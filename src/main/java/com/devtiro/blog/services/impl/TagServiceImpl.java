package com.devtiro.blog.services.impl;

import com.devtiro.blog.dto.tags.CreateTagsRequest;
import com.devtiro.blog.dto.tags.TagResponse;
import com.devtiro.blog.entities.Tag;
import com.devtiro.blog.mappers.TagMapper;
import com.devtiro.blog.repositories.TagRepo;
import com.devtiro.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponse> getTags() {
        return tagRepo.findAllTagsWithCount();
//        return tagRepo.findAllWithPostCount()
//                .stream()
//                .map(tagMapper::toDto)
//                .toList();
    }

    @Transactional
    @Override
    public List<TagResponse> createTags(CreateTagsRequest createTagsRequest) {


        List<Tag> existingTags = tagRepo.findByNameIn(createTagsRequest.getNames());

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName).collect(Collectors.toSet());

//        createTagsRequest.getNames().removeAll(existingTagNames);

        List<Tag> newTags = createTagsRequest.getNames().stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                                .name(name)
                                .posts(new HashSet<>())
                                .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()){
            savedTags = tagRepo.saveAll(newTags);
        }
        existingTags.addAll(savedTags);
        return existingTags.stream().map(tagMapper::toDto).toList();

    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        tagRepo.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepo.deleteById(id);
        });
    }
}
