package org.cayman.service;

import lombok.extern.slf4j.Slf4j;
import org.cayman.dto.Message;
import org.cayman.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MessageService {
    private final Constants constants;
    private final RestTemplate restTemplate;

    @Autowired
    public MessageService(Constants constants, RestTemplate restTemplate) {
        this.constants = constants;
        this.restTemplate = restTemplate;
    }

    public int getNewMessageCount() {
        String url = constants.getMailServiceUrl() + "/message/new/count";
        return restTemplate.getForObject(url, Integer.class);
    }

    public List<Message> getAllMessages() {
        String url = constants.getMailServiceUrl() + "/message";
        List<Message> messages = Arrays.asList(restTemplate.getForObject(url, Message[].class));
        Collections.sort(messages);
        return messages;
    }

    public void changeStatus(int id) {
        String url = constants.getMailServiceUrl() + "/message/mark?id={id}";
        boolean changeStatusSuccess = restTemplate.getForObject(url, Boolean.class, id);
        if (changeStatusSuccess) {
            log.info("Status for message (id = " + id + ") has changed.");
        } else {
            log.info("Something went wrong. Status for message (id = " + id + ") hasn't changed.");
        }
    }

    public void deleteMessage(int id) {
        String url = constants.getMailServiceUrl() + "/message/delete?id={id}";
        boolean deleteMessageSuccess = restTemplate.getForObject(url, Boolean.class, id);
        if (deleteMessageSuccess) {
            log.info("Successfully delete message (id = " + id + ").");
        } else {
            log.info("Something went wrong. Message (id = " + id + ") hasn't deleted.");
        }
    }
}
