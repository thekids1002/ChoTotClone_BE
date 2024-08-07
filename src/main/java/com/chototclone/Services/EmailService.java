package com.chototclone.Services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Sends a plain text email.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param text    the plain text content of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Sends an HTML email.
     *
     * @param to          the recipient's email address
     * @param subject     the subject of the email
     * @param htmlContent the HTML content of the email
     */
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an activation email with a provided token.
     *
     * @param to    the recipient's email address
     * @param token the activation token to include in the email content
     * @return true if the email was sent successfully, false otherwise
     */
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

    /**
     * Generates the content for an activation email.
     *
     * @param token the activation token to be included in the email link
     * @return the email content with an activation link and instructions
     */
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

    /**
     * Sends an activation email with an HTML content.
     *
     * @param to         the recipient's email address
     * @param entryToken the activation token to include in the activation link
     */
    public void sendHtmlActivationEmail(String to, String entryToken) {
//        String subject = "Activate Your Account";
//        String htmlContent = "<html><body>" +
//                "<h1>Welcome!</h1>" +
//                "<p>Please <a href='http://localhost:8080/activate?entryToken=" + entryToken + "'>click here</a> to activate your account.</p>" +
//                "</body></html>";
//
//        emailService.sendHtmlMessage(to, subject, htmlContent);
    }


}
