package guru.springframework.spring6restmvc.Service;

import guru.springframework.spring6restmvc.Model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List <Customer> getListOfCustomers();

    Customer getCustomerById(UUID id);


}
