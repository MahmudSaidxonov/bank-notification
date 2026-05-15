package uz.banking.bank_notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.banking.bank_notification.config.FeignConfig;
import uz.banking.bank_notification.dto.ApiResponseDto;

@FeignClient(name = "bank-core", configuration = FeignConfig.class)
public interface BankCoreClient {

    @GetMapping("internal/users/get-by-id/{id}")
    ApiResponseDto getUserInfo(@PathVariable("id") Long userId);
}
