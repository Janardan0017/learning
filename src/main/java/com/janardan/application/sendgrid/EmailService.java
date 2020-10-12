package com.janardan.application.sendgrid;// using SendGrid's Java Library
// https://github.com/sendgrid/sendgrid-java

import com.sendgrid.*;

import java.io.IOException;

public class EmailService {
    public static void main(String[] args) {
        Email from = new Email("janardan.c@finoit.co.in");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("aaaa@yopmail.com");
        Content content = new Content("text/plain", "Test for email");
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}