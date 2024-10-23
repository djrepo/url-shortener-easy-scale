package com.upwork.tinyurl.service;

import com.upwork.tinyurl.model.UrlEntity;
import com.upwork.tinyurl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlMappingService {

    private final UrlRepository urlMapRepo;

    @Autowired
    public UrlMappingService(UrlRepository urlMapRepo) {
        this.urlMapRepo = urlMapRepo;
    }

    @Cacheable(value = "UrlMapping", key = "#newUrl")
    public UrlEntity getByNewUrl(String newUrl) {
        return urlMapRepo.findById(newUrl).orElse(null);
    }

    @CachePut(value = "UrlMapping", key = "#urlEntity.newUrl")
    public UrlEntity save(UrlEntity urlEntity) {
        return urlMapRepo.save(urlEntity);
    }
}