package com.mananluvtocode.SpringMVC.controllers;

import com.mananluvtocode.SpringMVC.api.model.CategoryDTO;
import com.mananluvtocode.SpringMVC.services.CategoryService;
import com.mananluvtocode.SpringMVC.services.ResourceNotFoundException;
import jakarta.validation.metadata.ElementDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
// this is the second method for using the MockMvc class for doing the testing.
@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {
    @MockBean
    CategoryService categoryService;
    // this will inject the service into the controller for doing the further work.

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // for setting up the controller advice for handling the wrong request for doing the work correctly.
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(categoryController)
//                .setControllerAdvice(new RestResponseEntityHandler())
//                .build();
    }

    @Test
    void getAllCategoriesList() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Pari");

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(2L);
        categoryDTO1.setName("Bob");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(3L);
        categoryDTO2.setName("Mary");

        List<CategoryDTO> categoryDTOS = Arrays.asList(categoryDTO, categoryDTO1, categoryDTO2);
        when(categoryService.getAllCategories()).thenReturn(categoryDTOS);
        System.out.println(categoryService.getAllCategories());
        mockMvc.perform(get("/api/v1/categories/")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(3)));

        assertEquals(categoryDTOS.size(), 3);
    }

    @Test
    public void getCategoryByName() throws Exception {
        CategoryDTO finalclass = new CategoryDTO();
        finalclass.setName("john");
        finalclass.setId(3L);
        when(categoryService.getCategoryByName(anyString())).thenReturn(finalclass);
        String name = "john";

        mockMvc.perform(get("/api/v1/categories/{categoryId}", name)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("extended Category", "Category Extension"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("john"))
                .andDo(document("/v1/categoryByName", pathParameters(
                                parameterWithName("categoryId").description("Id of the category")
                        ), responseFields(
                                fieldWithPath("id").description("This is the Id of the john category class"),
                                fieldWithPath("name").description("Name of the Category in the document.")
                        )
//                        requestParamters(
//                        parameterWithName("extended Category").description("This is the request parameter introduction")
//                )
                ));
    }

    @Test
    public void TestByNameNotFound() throws Exception {
        when(categoryService.getCategoryByName(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/categories/john")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}