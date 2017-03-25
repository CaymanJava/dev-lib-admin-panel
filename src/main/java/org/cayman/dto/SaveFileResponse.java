package org.cayman.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveFileResponse {
    private int pageCount;
    private String fileId;
    private String imgPath;
}
