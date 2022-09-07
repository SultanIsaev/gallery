package com.gallery.app.interfaces;

import com.gallery.app.interfaces.request.AromaCreateUpdateRequest;
import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import com.gallery.app.repository.AromaRepository;
import com.gallery.app.repository.CountryRepository;
import com.gallery.app.repository.PerfumerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static com.gallery.app.interfaces.TestData.getAromaDefaultCountry;
import static com.gallery.app.interfaces.TestData.getDefaultAroma;
import static com.gallery.app.interfaces.TestData.getDefaultPerfumer;
import static com.gallery.app.interfaces.TestData.getPerfumerDefaultCountry;
import static com.gallery.app.problem.ProblemCodes.ERROR_BAD_REQUEST;
import static com.gallery.app.problem.ProblemCodes.ERROR_PRODUCT_NOT_FOUND;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(PER_METHOD)
@ComponentScan("com.gallery")
@ActiveProfiles({"integration-test"})
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AromaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AromaRepository aromaRepository;

    @Autowired
    private PerfumerRepository perfumerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${server.rest.endpoints.aromas.path}")
    private String aromasPath;
    private Aroma aroma;
    private Country aromaCountry;
    private Perfumer perfumer;
    private Country perfumerCountry;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        aromaCountry = countryRepository.save(getAromaDefaultCountry().build());
        perfumerCountry = countryRepository.save(getPerfumerDefaultCountry().build());

        Perfumer defaultPerfumer = getDefaultPerfumer().build();
        defaultPerfumer.setCountry(perfumerCountry);
        this.perfumer = perfumerRepository.save(defaultPerfumer);

        Aroma defaultAroma = getDefaultAroma().build();
        defaultAroma.setPerfumer(defaultPerfumer);
        defaultAroma.setCountry(aromaCountry);
        this.aroma = aromaRepository.save(defaultAroma);
    }

    @AfterEach
    void clear() {
        aromaRepository.deleteAll();
        perfumerRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findAromaById_Success() throws Exception {
        mockMvc.perform(get(aromasPath + "/" + aroma.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(aroma.getName())))
                .andExpect(jsonPath("$.description", is(aroma.getDescription())))
                .andExpect(jsonPath("$.country.code", is(aromaCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(aromaCountry.getName())))
                .andExpect(jsonPath("$.perfumer.firstName", is(perfumer.getFirstName())))
                .andExpect(jsonPath("$.perfumer.lastName", is(perfumer.getLastName())))
                .andExpect(jsonPath("$.perfumer.email", is(perfumer.getEmail())))
                .andExpect(jsonPath("$.perfumer.description", is(perfumer.getDescription())));
    }

    @Test
    void findAllAromas_Success() throws Exception {
        Aroma secondAroma = Aroma.builder()
                .name("Second aroma name")
                .description("Second aroma description")
                .build();
//        secondAroma.setPerfumerId(this.perfumer.getId());
//        secondAroma.setCountryId(aromaCountry.getId());
        secondAroma.setPerfumer(perfumer);
        secondAroma.setCountry(aromaCountry);
        secondAroma = aromaRepository.save(secondAroma);

        mockMvc.perform(get(aromasPath))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.[0].name", is(aroma.getName())))
                .andExpect(jsonPath("$.[0].description", is(aroma.getDescription())))
                .andExpect(jsonPath("$.[0].country.code", is(aromaCountry.getCode())))
                .andExpect(jsonPath("$.[0].country.name", is(aromaCountry.getName())))
                .andExpect(jsonPath("$.[0].perfumer.firstName", is(perfumer.getFirstName())))
                .andExpect(jsonPath("$.[0].perfumer.lastName", is(perfumer.getLastName())))
                .andExpect(jsonPath("$.[0].perfumer.email", is(perfumer.getEmail())))
                .andExpect(jsonPath("$.[0].perfumer.description", is(perfumer.getDescription())))

                .andExpect(jsonPath("$.[1].name", is(secondAroma.getName())))
                .andExpect(jsonPath("$.[1].description", is(secondAroma.getDescription())))
                .andExpect(jsonPath("$.[1].country.code", is(aromaCountry.getCode())))
                .andExpect(jsonPath("$.[1].country.name", is(aromaCountry.getName())))
                .andExpect(jsonPath("$.[1].perfumer.firstName", is(perfumer.getFirstName())))
                .andExpect(jsonPath("$.[1].perfumer.lastName", is(perfumer.getLastName())))
                .andExpect(jsonPath("$.[1].perfumer.email", is(perfumer.getEmail())))
                .andExpect(jsonPath("$.[1].perfumer.description", is(perfumer.getDescription())));
    }

    @Test
    void findAromaById_aromaNotExists_shouldReturnNotFoundError() throws Exception {
        aromaRepository.deleteAll();

        mockMvc.perform(get(aromasPath + "/" + aroma.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Aroma does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_PRODUCT_NOT_FOUND)));
    }

    @Test
    void findAromaById_perfumerNotExists_shouldReturnAllDataButPerfumer() throws Exception {
        perfumerRepository.deleteById(perfumer.getId());

        mockMvc.perform(get(aromasPath + "/" + aroma.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(aroma.getName())))
                .andExpect(jsonPath("$.description", is(aroma.getDescription())))
                .andExpect(jsonPath("$.country.code", is(aromaCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(aromaCountry.getName())))
                .andExpect(jsonPath("$.perfumer", nullValue()));
    }

    @Test
    void findAromaById_countryNotExists_shouldReturnAllDataButCountry() throws Exception {
        aroma.setCountry(null);
        aromaRepository.save(aroma);

        mockMvc.perform(get(aromasPath + "/" + aroma.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(aroma.getName())))
                .andExpect(jsonPath("$.description", is(aroma.getDescription())))
                .andExpect(jsonPath("$.country", nullValue()))
                .andExpect(jsonPath("$.perfumer.firstName", is(perfumer.getFirstName())))
                .andExpect(jsonPath("$.perfumer.lastName", is(perfumer.getLastName())))
                .andExpect(jsonPath("$.perfumer.email", is(perfumer.getEmail())))
                .andExpect(jsonPath("$.perfumer.description", is(perfumer.getDescription())));
    }

    @Test
    void findAromaById_perfumerAndCountryNotExists_shouldReturnAllDataButPerfumerAndCountry() throws Exception {
        aroma.setCountry(null);
        aroma.setPerfumer(null);
        aromaRepository.save(aroma);

        mockMvc.perform(get(aromasPath + "/" + aroma.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(aroma.getName())))
                .andExpect(jsonPath("$.description", is(aroma.getDescription())))
                .andExpect(jsonPath("$.country", nullValue()))
                .andExpect(jsonPath("$.perfumer", nullValue()));
    }

    @Test
    void createAroma_Success() throws Exception {
        AromaCreateUpdateRequest newAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(post(aromasPath)
                        .content(mapper.writeValueAsString(newAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.name", is(newAroma.getName())))
                .andExpect(jsonPath("$.description", is(newAroma.getDescription())))
                .andExpect(jsonPath("$.country.code", is(aromaCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(aromaCountry.getName())))
                .andExpect(jsonPath("$.perfumer.firstName", is(perfumer.getFirstName())))
                .andExpect(jsonPath("$.perfumer.lastName", is(perfumer.getLastName())))
                .andExpect(jsonPath("$.perfumer.email", is(perfumer.getEmail())))
                .andExpect(jsonPath("$.perfumer.description", is(perfumer.getDescription())));
    }

    @Test
    void createAroma_notExistingPerfumerSentInRequest_Error() throws Exception {
        perfumer.setId(99);
        AromaCreateUpdateRequest newAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(post(aromasPath)
                        .content(mapper.writeValueAsString(newAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void createAroma_notExistingCountrySentInRequest_Error() throws Exception {
        aromaCountry.setId((short) 99);
        AromaCreateUpdateRequest newAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(post(aromasPath)
                        .content(mapper.writeValueAsString(newAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void createAroma_notExistingPerfumerAndCountrySentInRequest_Error() throws Exception {
        perfumer.setId(99);
        aromaCountry.setId((short) 99);
        AromaCreateUpdateRequest newAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(post(aromasPath)
                        .content(mapper.writeValueAsString(newAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist,Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_Success() throws Exception {
        Perfumer newPerfumer = Perfumer.builder()
                .firstName("another Perfumer firstName")
                .lastName("another Perfumer lastName")
                .email("another Perfumer email")
                .description("another Perfumer description")
                .build();
        newPerfumer = perfumerRepository.save(newPerfumer);

        Country newCountry = Country.builder()
                .code("AAA")
                .name("another Country")
                .build();
        newCountry = countryRepository.save(newCountry);

        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(newPerfumer.getId())
                .countryId(newCountry.getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedAroma.getName())))
                .andExpect(jsonPath("$.description", is(updatedAroma.getDescription())))
                .andExpect(jsonPath("$.perfumer.firstName", is(newPerfumer.getFirstName())))
                .andExpect(jsonPath("$.perfumer.lastName", is(newPerfumer.getLastName())))
                .andExpect(jsonPath("$.perfumer.email", is(newPerfumer.getEmail())))
                .andExpect(jsonPath("$.perfumer.description", is(newPerfumer.getDescription())))
                .andExpect(jsonPath("$.country.code", is(newCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(newCountry.getName())));
    }

    @Test
    void updateAroma_notExistingAromaSentInRequest_Error() throws Exception {
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + 99)
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Aroma does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_notExistingPerfumerSentInRequest_Error() throws Exception {
        perfumer.setId(99);
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_notExistingCountrySentInRequest_Error() throws Exception {
        aromaCountry.setId((short) 99);
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_onlyNotExistingCountrySentInRequest_Error() throws Exception {
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name(perfumer.getFirstName())
                .description(perfumer.getDescription())
                .perfumerId(perfumer.getId())
                .countryId(((short) 99))
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_notExistingPerfumerAndCountrySentInRequest_Error() throws Exception {
        perfumer.setId(99);
        aromaCountry.setId((short) 99);
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name("New aroma name")
                .description("New aroma description")
                .perfumerId(perfumer.getId())
                .countryId(aromaCountry.getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist,Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updateAroma_noChangesProvided_Error() throws Exception {
        AromaCreateUpdateRequest updatedAroma = AromaCreateUpdateRequest.builder()
                .name(aroma.getName())
                .description(aroma.getDescription())
                .perfumerId(aroma.getPerfumer().getId())
                .countryId(aroma.getCountry().getId())
                .build();

        mockMvc.perform(put(aromasPath + "/" + aroma.getId())
                        .content(mapper.writeValueAsString(updatedAroma))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("No changes provided")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void deleteAroma_Success() throws Exception {
        mockMvc.perform(delete(aromasPath + "/" + aroma.getId()))
                .andExpect(status().isNoContent());

        Optional<Aroma> opt = aromaRepository.findById(aroma.getId());
        assertTrue(opt.isEmpty());
    }

    @Test
    void deleteAroma_notExistingAromaSentInRequest_Error() throws Exception {
        mockMvc.perform(delete(aromasPath + "/" + 99))
                .andExpect(status().isNoContent());

        Optional<Aroma> opt = aromaRepository.findById(aroma.getId());
        assertTrue(opt.isPresent());
        assertEquals(aroma.getId(), opt.get().getId());
    }
}
