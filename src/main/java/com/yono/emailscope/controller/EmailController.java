package com.yono.emailscope.controller;

import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @PostMapping("/parse")
    public String parseEmail(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            DefaultMessageBuilder builder = new DefaultMessageBuilder();
            Message message = builder.parseMessage(is);

            String subject = message.getSubject();
            String from = message.getFrom().get(0).getName();
            String to = message.getTo().get(0).toString();
            String body = message.getBody().toString();

            // 파싱된 데이터를 간단하게 JSON 형식의 문자열로 반환
            return String.format("{\"subject\": \"%s\", \"from\": \"%s\", \"to\": \"%s\", \"body\": \"%s\"}",
                    subject, from, to, body);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to parse EML file\"}";
        }
    }
}
