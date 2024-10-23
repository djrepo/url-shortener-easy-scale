package com.upwork.tinyurl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Range {
    private final long start;
    private final long end;
}
