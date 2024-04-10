package guru.springframework.spring6restmvc.Service;

import guru.springframework.spring6restmvc.Model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        Customer cust1 = Customer.builder()
                .customerName("Dayday Wynn")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer cust2 = Customer.builder()
                .customerName("Dayday")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer cust3 = Customer.builder()
                .customerName("Wynn")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(cust1.getCustomerId(),cust1);
        customerMap.put(cust2.getCustomerId(), cust2);
        customerMap.put(cust3.getCustomerId(), cust3);
    }

    @Override
    public List<Customer> getListOfCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerMap.get(id);
    }
}
