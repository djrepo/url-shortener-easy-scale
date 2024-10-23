package com.upwork.tinyurl.repository;


import com.upwork.tinyurl.model.UrlEntity;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<UrlEntity, String> {

    @AllowFiltering
    @Query("Select * from url where oldUrl=?0")
    Optional<UrlEntity> findByOldUrl(String sOldUrl);
}
