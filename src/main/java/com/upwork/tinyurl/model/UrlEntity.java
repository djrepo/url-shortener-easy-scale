package com.upwork.tinyurl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("URL_ENTITY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UrlEntity {
    @PrimaryKey
    @Column
    private String newUrl;

    @Column
    private String oldUrl;
}
