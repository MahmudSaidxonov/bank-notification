package uz.banking.bank_notification.service;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import uz.banking.bank_notification.client.BankCoreClient;
import uz.banking.bank_notification.dto.ApiResponseDto;
import uz.banking.bank_notification.dto.TransferNotificationDto;
import uz.banking.bank_notification.dto.UserDto;

import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class NotificationListener {

    private final BankCoreClient bankCoreClient;
    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = "notification.queue")
    public void receiveListener(TransferNotificationDto notification) {
        try {
            Long receiverId = notification.getToAccountId();

            ApiResponseDto response = bankCoreClient.getUserInfo(receiverId);
            UserDto receiver = response.getData();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = notification.getCreatedAt().format(formatter);

            String subject = "Inbound Transfer\n";

            String body = String.format(
                    "Dear %s!,\n\n" +
                            "Your account has been successfully credited with: %s UZS.\n" +
                            "Transaction ID: %d\n" +
                            "Date: %s\n\n" +
                            "Best regards,\nYour Bank Team",
                    receiver.getUsername(),
                    notification.getAmount(),
                    notification.getId(),
                    formattedDate
            );

            emailSenderService.sendEmail(receiver.getEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
        }
    }
}
