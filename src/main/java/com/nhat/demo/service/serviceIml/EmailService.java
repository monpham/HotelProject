package com.nhat.demo.service.serviceIml;

import com.nhat.demo.entity.BookingPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailService {
    @Autowired
    private  TemplateEngine templateEngine;
    @Autowired
    private  JavaMailSender javaMailSender;

    public void sendMail(BookingPerson bookingPerson, String bookingCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstName", bookingPerson.getFirstName());
        context.setVariable("bookingCode", bookingCode);

        String html = templateEngine.process("client/mail-template", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Đặt Phòng thành công");
        helper.setText(html, true);
        helper.setTo(bookingPerson.getEmail());
        helper.setFrom("ducvuong25@gmail.com");
        javaMailSender.send(mimeMessage);

    }


}
