package guru.springframework.spring6restmvc.Model;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Customer {

    private String customerName;
    private UUID customerId;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
