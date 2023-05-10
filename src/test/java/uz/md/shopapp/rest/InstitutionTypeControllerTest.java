package uz.md.shopapp.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.md.shopapp.IntegrationTest;
import uz.md.shopapp.controller.InstitutionTypeController;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.repository.InstitutionTypeRepository;
import uz.md.shopapp.util.MockDataGenerator;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class InstitutionTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockDataGenerator mockDataGenerator;

    public static final String BASE_URL = InstitutionTypeController.BASE_URL;
    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;

    @Test
    void shouldGetAllTypes() throws Exception {

        List<InstitutionType> types = mockDataGenerator.getInstitutionTypesSaved(10);
        long beforeCall = institutionTypeRepository.count();
        Assertions.assertEquals(beforeCall, 10);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(10))
                .andExpect(jsonPath("$.data[0].id").value(types.get(0).getId()))
                .andReturn();
        Assertions.assertNotNull(result);
        long afterCall = institutionTypeRepository.count();
        Assertions.assertEquals(afterCall, beforeCall);
    }

    @Test
    void shouldGetAllByPage() throws Exception {
        List<InstitutionType> types = mockDataGenerator.getInstitutionTypesSaved(20);
        long beforeCall = institutionTypeRepository.count();
        Assertions.assertEquals(beforeCall, 20);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/by-page/0-10"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(10));

        testForEquality(perform, types.subList(0,10));

        long afterCall = institutionTypeRepository.count();
        Assertions.assertEquals(afterCall, beforeCall);
    }

    @Test
    void shouldGetAllByPage2() throws Exception {
        List<InstitutionType> types = mockDataGenerator.getInstitutionTypesSaved(20);
        long beforeCall = institutionTypeRepository.count();
        Assertions.assertEquals(beforeCall, 20);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/by-page/1-10"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(10));

        testForEquality(perform, types.subList(10,20));

        long afterCall = institutionTypeRepository.count();
        Assertions.assertEquals(afterCall, beforeCall);
    }



    private void testForEquality(ResultActions perform, List<InstitutionType> types) {
        for (int i = 0; i < types.size(); i++) {
            try {
                perform.andExpect(jsonPath("$.data[" + i + "].id").value(types.get(i).getId()));
                perform.andExpect(jsonPath("$.data[" + i + "].nameUz").value(types.get(i).getNameUz()));
                perform.andExpect(jsonPath("$.data[" + i + "].nameRu").value(types.get(i).getNameRu()));
                perform.andExpect(jsonPath("$.data[" + i + "].descriptionRu").value(types.get(i).getDescriptionRu()));
                perform.andExpect(jsonPath("$.data[" + i + "].descriptionUz").value(types.get(i).getDescriptionUz()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
