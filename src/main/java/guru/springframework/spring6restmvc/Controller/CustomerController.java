package guru.springframework.spring6restmvc.Controller;

import guru.springframework.spring6restmvc.Model.Customer;
import guru.springframework.spring6restmvc.Service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/customer/")
@AllArgsConstructor
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listAllCustomers(){
        return customerService.getListOfCustomers();
    }

    @RequestMapping(value = "{custId}", method = RequestMethod.GET)
    public Customer findCustomerById(@PathVariable("custId") UUID custId){
        return customerService.getCustomerById(custId);
    }



}
