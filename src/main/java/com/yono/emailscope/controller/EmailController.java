package com.yono.emailscope.controller;

import com.yono.emailscope.dto.ParsedEmail;
import com.yono.emailscope.utils.EmailUtils;
import org.apache.james.mime4j.dom.*;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.util.MimeUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5173")  // 클라이언트가 실행 중인 주소
public class EmailController {

    @PostMapping("/parse")
    public ParsedEmail parseEmail(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            DefaultMessageBuilder builder = new DefaultMessageBuilder();
            Message message = builder.parseMessage(is);

            String subject = message.getSubject();
            String from = message.getFrom().get(0).getName();
            String to = message.getTo().get(0).toString();
            String body = "";
            Map<String, String> inlineImages = new HashMap<>();

            if (message.getBody() instanceof Multipart) {
                Multipart multipart = (Multipart) message.getBody();
                for (Entity entity : multipart.getBodyParts()) {
                    if (entity.getDispositionType() != null && entity.getDispositionType().equals(MimeUtil.ENC_BINARY)) {
                        // 처리해야 할 이미지나 파일 첨부물
                        BinaryBody binaryBody = (BinaryBody) entity.getBody();
                        byte[] bytes = binaryBody.getInputStream().readAllBytes();
                        String base64Image = Base64.getEncoder().encodeToString(bytes);
                        inlineImages.put(entity.getFilename(), "data:image/jpeg;base64," + base64Image); // 이미지 형식에 따라 MIME 타입 조정
                    } else if (entity.getMimeType().startsWith("text/")) {
                        // 처리해야 할 텍스트 부분 (HTML 또는 일반 텍스트)
                        TextBody textBody = (TextBody) entity.getBody();
                        body = EmailUtils.textBodyToString(textBody);
                    }
                }
            } else if (message.getBody() instanceof TextBody) {
                TextBody textBody = (TextBody) message.getBody();
                body = EmailUtils.textBodyToString(textBody);
            }

            return new ParsedEmail(subject, from, to, body, inlineImages);

        } catch (Exception e) {
            e.printStackTrace();
            return new ParsedEmail("Error", "Error", "Error", "Failed to parse EML file", null);
        }
    }
}
