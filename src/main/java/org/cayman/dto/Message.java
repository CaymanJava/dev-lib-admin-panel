package org.cayman.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    private int id;
    private int userId;
    private String email;
    private String message;
    private String ip;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendDateTime;
    private boolean newOne;
}
