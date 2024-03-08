package com.example.Notification.model;

import com.example.Notification.configuration.RabbitMQConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RabbitMQConsumer {

    @Autowired
    private JavaMailSender javaMailSender; // Injeção do JavaMailSender

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        // Aqui você pode processar a mensagem recebida do RabbitMQ
        // Por exemplo, enviar por e-mail
        sendEmail(message);
    }

    private void sendEmail(String message) {
        try {
            // Criação da mensagem MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("seu-email@gmail.com");
            helper.setTo("email-de-destino@gmail.com");
            helper.setSubject("Pagamento");
            helper.setText("Conteúdo do e-mail: " + message);

            // Envio do e-mail
            javaMailSender.send(mimeMessage);

            System.out.println("E-mail enviado com sucesso!");

        } catch (MessagingException e) {
            // Lidar com exceções de envio de e-mail
            e.printStackTrace();
        }
    }


}
