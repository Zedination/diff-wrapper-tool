package com.zedination.diffwrappertool.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Commit {
    private String sha;
    private String message;
    private String author;
    private String date;
}
