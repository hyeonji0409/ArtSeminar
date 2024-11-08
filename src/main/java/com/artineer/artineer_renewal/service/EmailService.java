package com.artineer.artineer_renewal.service;

import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
// todo 트랜젝셔널이 뭔가
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final ConcurrentHashMap<String, String> verificationStore = new ConcurrentHashMap<>();

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
//            logger.debug("MailService.sendEmail exception occur toEmail: {}, " +
//                    "title: {}, text: {}", toEmail, title, text);
//            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
            throw e;
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public String createVerificationCode(String email) {

        String randomString = new Random().ints(48, 122)
                .filter(i -> Character.isLetterOrDigit(i))
                .limit(5)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());

        verificationStore.put(email, randomString);

        return randomString;
    }

    public String getVerificationCode(String email) {
        return verificationStore.get(email);
    }

    public void removeVerificationCode(String email) {
        verificationStore.remove(email);
    }


}
