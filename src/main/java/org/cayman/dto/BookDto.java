package org.cayman.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private int id;
    private int categoryId;
    private String name;
    private String language;
    private int year;
    private String author;
    private String publisher;
    private String image;
    private String fileId;
    private String description;
    private String category;
    private int pageCount;
    private String rating;
    private int views;
    private LocalDateTime addDate;
}

