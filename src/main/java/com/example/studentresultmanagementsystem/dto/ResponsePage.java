package com.example.studentresultmanagementsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.List;

//ResponsePage is a generic class that helps to provide paginated response.
@Data
@NoArgsConstructor
public class ResponsePage<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResponsePage.class);

    private List<T> content;
    private Long totalElements;
    private int totalPages;
    private int size;
    private int number;

    public ResponsePage(List<T> content, Long totalElements, int totalPages, int size, int number) {
        logger.debug("Creating ResponsePage with content size: {}, totalElements: {}, totalPages: {}, size: {}, number: {}",
                     content.size(), totalElements, totalPages, size, number);
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

    public ResponsePage(Page<T> page){
        logger.debug("Creating ResponsePage from Page object with totalElements: {}, totalPages: {}, size: {}, number: {}",
                     page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
        this.content = page.get().toList();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.number = page.getNumber();
    }
}
