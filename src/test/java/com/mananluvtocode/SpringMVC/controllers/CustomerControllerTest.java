package com.mananluvtocode.SpringMVC.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mananluvtocode.SpringMVC.api.model.CustomerDTO;
import com.mananluvtocode.SpringMVC.domain.Customer;
import com.mananluvtocode.SpringMVC.repositories.CustomerRepository;
import com.mananluvtocode.SpringMVC.services.CustomerService;
import com.mananluvtocode.SpringMVC.services.ResourceNotFoundException;
import org.jruby.anno.Coercion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;

    // this Object Mapper is used for binding the POJO class to the json as this uses the Jackson binding technique for doing the work.
    private ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
        // mockMvc = MockMvcBuilders.standaloneSetup(customerController).setControllerAdvice(new RestResponseEntityHandler()).build();
    }

    @Test
    void getAllCustomers() throws Exception {
        List<CustomerDTO> customerList = Arrays.asList(new CustomerDTO(), new CustomerDTO(), new CustomerDTO());
        when(customerService.getAllCustomers()).thenReturn(customerList);
        mockMvc.perform(get("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(3)));
    }

    @Test
    void getCustomerByFirstName() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        when(customerService.getCustomerByFirstName("John")).thenReturn(customerDTO);
        mockMvc.perform(get("/api/v1/customers/John")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void createNewCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("Customer");
        customer.setLastName("Aggarwal");
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customer.getFirstName());
        returnDTO.setLastName(customer.getLastName());
        returnDTO.setCustomer_url("/api/v1/customers/1");
        returnDTO.setId(1L);
        ConstrainedFields constrainedFields= new ConstrainedFields(CustomerDTO.class);
        // object mapper is used for binding the pojo to the json manually as this uses the jakson binding for doing  this work.
        String finalvalue = mapper.writeValueAsString(customer);
        when(customerService.createNewCustomer(customer)).thenReturn(returnDTO);
//        mockMvc.perform(post("/api/v1/customers/")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName").value("Customer"))
//                .andExpect(jsonPath("$.lastName").value("Aggarwal"))
//                .andExpect(jsonPath("$.customer_url").value("/api/v1/customers/1"));
//        mockMvc.perform(post("/api/v1/customers/")
//                .contentType(MediaType.APPLICATION_JSON).content(finalvalue))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(customer.getLastName()))
//                .andExpect(jsonPath("$.customer_url").value(returnDTO.getCustomer_url()));

        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON).content(finalvalue).accept(MediaType.APPLICATION_JSON))
                .andDo(document("/v1/allValues", requestFields(
                        constrainedFields.withPath("id").description("Id of the customer"),
                        constrainedFields.withPath("firstName").description("Customer First Name is being made"),
                        constrainedFields.withPath("lastName").description("Customer Last Name is documented"),
                        constrainedFields.withPath("customer_url").description("Customer URL is being documented")
                )));

//        System.out.println("The response of this request is :- " + response);

    }

    // for checking for the update operation
    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Flinder Super Doe");
        customerDTO.setLastName("Doe");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customerDTO.getFirstName());
        returnDTO.setLastName(customerDTO.getLastName());
        returnDTO.setCustomer_url("/api/v1/customers/1");

        String finalvalue = mapper.writeValueAsString(customerDTO);
        when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);
        String responseResult = mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(finalvalue))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(customerDTO.getFirstName()))
                .andExpect(jsonPath("$.customer_url").value("/api/v1/customers/1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseResult);

    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Manan");
        customerDTO.setLastName("Aggarwal");
        CustomerDTO returnedCustomer = new CustomerDTO();
        returnedCustomer.setFirstName(customerDTO.getFirstName());
        returnedCustomer.setLastName(customerDTO.getLastName());
        returnedCustomer.setCustomer_url("/api/v1/customers/1");
        returnedCustomer.setId(1L);
        String resultMapper = mapper.writeValueAsString(customerDTO);
        when(customerService.patchCustomer(1L, customerDTO)).thenReturn(returnedCustomer);

        String responseString = mockMvc.perform(patch("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON).content(resultMapper).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(returnedCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(returnedCustomer.getLastName()))
                .andExpect(jsonPath("$.customer_url").value(returnedCustomer.getCustomer_url()))
                .andReturn().getResponse().getContentAsString();
        System.out.println("The Response of this Request is:- ");
        System.out.println(responseString);
    }

    @Test
    void DeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(customerService).deleteCustomerById(anyLong());
    }

    @Test
    void resourceNotFoundException() throws Exception {
        when(customerService.getCustomerByFirstName(anyString())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/api/v1/customers/foobar"))
                .andExpect(status().isNotFound());
    }


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}