package com.upwork.tinyurl.controller;

import com.upwork.tinyurl.model.UrlEntity;
import com.upwork.tinyurl.service.UrlGeneratorService;
import com.upwork.tinyurl.service.UrlMappingService;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/urls/")
public class UrlController {

    private static final Logger log = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    private UrlGeneratorService shortUrlService;

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private RedisTemplate<String, UrlEntity> redisTemplate;

    @Value("${redis.ttl}")
    private long ttl;

    @PostMapping("/shortify")
    public ResponseEntity<String> shortify(@RequestBody String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (!urlValidator.isValid(url)) {
            return ResponseEntity.badRequest().body("Invalid URL format.");
        }

        String newUrl = shortUrlService.createShortUrl(url);
        if (newUrl == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating URL.");
        }

        UrlEntity urlObject = new UrlEntity(newUrl, url);
        UrlEntity savedUrlEntity = urlMappingService.save(urlObject);

        if (savedUrlEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving URL mapping.");
        }

        redisTemplate.opsForValue().set(savedUrlEntity.getNewUrl(), urlObject, ttl, TimeUnit.SECONDS);
        return ResponseEntity.ok("localhost:8080/urls/" + savedUrlEntity.getNewUrl());
    }

    @GetMapping("/{tinyUrl}")
    public void getOriginalUrl(@PathVariable("tinyUrl") String tinyUrl, HttpServletResponse response) {
        try {
            UrlEntity urlMap = urlMappingService.getByNewUrl(tinyUrl);
            if (urlMap != null) {
                response.sendRedirect(urlMap.getOldUrl());
            } else {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Shortened URL does not exist.");
            }
        } catch (Exception e) {
            log.error("Internal server error: ", e);
            try {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error!");
            } catch (Exception ex) {
                log.error("Error sending error response: ", ex);
            }
        }
    }
}
