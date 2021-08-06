package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.util.SmtpMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Locale;
import org.springframework.core.env.Environment;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;



@RestController
public class MailController {

    @Autowired
    SmtpMailSender smtpMailSender;

    @Autowired
    private Environment env;


    @RequestMapping(path = "/api/mail/send")
    public void sendMail() throws MessagingException, javax.mail.MessagingException {
        smtpMailSender.sendMail("sai.kovouri@amd.com", "Reset Password for EPYC Dashboard", "hello!");
    }



    public SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = "Reset password using below link";
        return constructEmail("Reset Password", message + " \r\n\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user)  {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


    public String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
