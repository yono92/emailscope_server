package com.yono.emailscope.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParsedEmail {
    private String subject;
    private String from;
    private String to;
    private String body;
    private Map<String, String> inlineImages; // 이미지가 base64로 인코딩된 맵

    public ParsedEmail(String subject, String from, String to, String body) {
    }
}
