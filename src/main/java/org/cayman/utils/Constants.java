package org.cayman.utils;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Constants {
    @Value("${book.service.url}")
    private String bookServiceUrl;

    @Value("${file.service.url}")
    private String fileServiceUrl;

    @Value("${mail.service.url}")
    private String mailServiceUrl;
}
