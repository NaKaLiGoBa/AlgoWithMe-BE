package com.nakaligoba.backend.tag.application;

import com.nakaligoba.backend.tag.domain.Tag;
import com.nakaligoba.backend.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateTag(String tagName) {
        Optional<Tag> tag = tagRepository.findByName(tagName);
        return tag.orElseGet(() -> createTag(tagName));
    }

    @Transactional
    public Tag createTag(String tagName) {
        Tag newTag = new Tag(tagName);
        tagRepository.save(newTag);
        return newTag;
    }
}
