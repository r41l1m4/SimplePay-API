package dev.ironia.simplepay.api.services;

import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.dtos.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class NotificationService {

    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        String userEmail = user.getEmail();
        NotificationDto notificationRequest = new NotificationDto(userEmail, message);

//        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("http://o4d9z.mocklab.io/notify", notificationRequest, String.class);
//
//        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
//            System.out.println("Erro ao enviar notificação");
//            throw new Exception("Serviço de notificação está fora do ar");
//
//        }
    }
}
