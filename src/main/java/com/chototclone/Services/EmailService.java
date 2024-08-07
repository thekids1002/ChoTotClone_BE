package com.chototclone.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


    public boolean sendActivationEmail(String to, String token) {
        String subject = "Kích Hoạt Tài Khoản của Bạn";
        String text = getContentActivationEmail(token);
        try {
            sendSimpleMessage(to, subject, text);
            return true;
        } catch (MailException e) { // Catch specific exception related to email sending
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while sending email to {}: {}", to, e.getMessage(), e);
        }
        return false;
    }

    private String getContentActivationEmail(String token) {
        String activationLink = baseUrl + "/api/v1/auth/active?entryToken=" + token;
        String text = "Chào bạn,\n\n" +
                "Cảm ơn bạn đã đăng ký tài khoản với chúng tôi. Để hoàn tất quy trình đăng ký, vui lòng kích hoạt tài khoản của bạn bằng cách nhấp vào liên kết dưới đây:\n\n" +
                activationLink + "\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này. Tài khoản của bạn sẽ không bị kích hoạt và bạn không cần thực hiện thêm hành động nào.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ hỗ trợ";
        return text;
    }

}
