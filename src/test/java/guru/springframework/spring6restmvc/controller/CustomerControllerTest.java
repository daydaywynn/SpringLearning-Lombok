package guru.springframework.spring6restmvc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper; //Use this to create JSon
    @Autowired
    MockMvc mockMvc;

    @Captor
    ArgumentCaptor <UUID> argumentCaptorUUID;
    @Captor
    ArgumentCaptor <CustomerDTO> argumentCaptorCust;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp(){
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void getCustomerByIdNotFound() throws Exception{
        given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID,UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
    @Test
    void createNewCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        System.out.println(objectMapper.writeValueAsString(customer));

        customer.setVersion(null);
        customer.setCreatedDate(null);
        customer.setUpdateDate(null);

        given(customerService.saveNewCustomer(customer)).willReturn(customerServiceImpl.getAllCustomers().get(1));


        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Test
    void listAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        given(customerService.getCustomerById(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(customer.getName())));

    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class)); //verify the method was called using any customer UUID and class
    }

    @Test
    void testDeleteCustomer() throws Exception{
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .content(objectMapper.writeValueAsString(customer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(customerService).deleteCustomerById(any(UUID.class));
    }

    @Test
    void testPatchCustomer() throws Exception{
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Name");
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .content(objectMapper.writeValueAsString(customerMap))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(argumentCaptorUUID.capture(), argumentCaptorCust.capture());

        assertThat(customerMap.get("name")).isEqualTo(argumentCaptorCust.getValue().getName());
        assertThat(customer.getId()).isEqualTo(argumentCaptorUUID.getValue());
    }
}