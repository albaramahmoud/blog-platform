package com.devtiro.blog.controllers;

import com.devtiro.blog.dto.tags.CreateTagsRequest;
import com.devtiro.blog.dto.tags.TagResponse;
import com.devtiro.blog.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/tags")
@AllArgsConstructor
public class TagController {


    TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> listTags() {
        return ResponseEntity.ok(tagService.getTags());
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTag(
            @RequestBody CreateTagsRequest createTagsRequest){
        return new ResponseEntity<>(tagService.createTags(createTagsRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {

        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();

    }
}
