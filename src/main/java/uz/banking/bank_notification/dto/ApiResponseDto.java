package uz.banking.bank_notification.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto {
    private int code;
    private String message;
    private boolean success;
    private UserDto data;
}
