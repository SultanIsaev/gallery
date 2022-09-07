package com.gallery.app.interfaces;

import com.gallery.app.interfaces.request.PerfumerCreateUpdateRequest;
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

import java.util.List;
import java.util.Optional;

import static com.gallery.app.interfaces.TestData.getAromaDefaultCountry;
import static com.gallery.app.interfaces.TestData.getDefaultAroma;
import static com.gallery.app.interfaces.TestData.getDefaultPerfumer;
import static com.gallery.app.interfaces.TestData.getPerfumerDefaultCountry;
import static com.gallery.app.problem.ProblemCodes.ERROR_BAD_REQUEST;
import static com.gallery.app.problem.ProblemCodes.ERROR_PERFUMER_NOT_FOUND;
import static org.hamcrest.Matchers.empty;
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
class PerfumerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AromaRepository aromaRepository;

    @Autowired
    private PerfumerRepository perfumerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${server.rest.endpoints.perfumers.path}")
    private String perfumersPath;
    private Aroma aroma;
    private Country aromaCountry;
    private Perfumer perfumer;
    private Country perfumerCountry;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        aromaCountry = countryRepository.save(getAromaDefaultCountry().build());
        perfumerCountry = countryRepository.save(getPerfumerDefaultCountry().build());

        Perfumer defaultPerfumer = getDefaultPerfumer().build();
        defaultPerfumer.setCountry(perfumerCountry);
        this.perfumer = perfumerRepository.save(defaultPerfumer);

        Aroma defaultAroma = getDefaultAroma().build();
        defaultAroma.setPerfumer(defaultPerfumer);
        defaultPerfumer.setAromas(List.of(defaultAroma));
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
    void findPerfumerById_Success() throws Exception {
        mockMvc.perform(get(perfumersPath + "/" + perfumer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alberto")))
                .andExpect(jsonPath("$.lastName", is("Morillas")))
                .andExpect(jsonPath("$.email", is("Alberto_Morillas@gmail.com")))
                .andExpect(jsonPath("$.description", is("Good description")))
                .andExpect(jsonPath("$.country.code", is("ITA")))
                .andExpect(jsonPath("$.country.name", is("Italy")))
                .andExpect(jsonPath("$.aromas[0].name", is("Montale")))
                .andExpect(jsonPath("$.aromas[0].description", is("Good description")))
                .andExpect(jsonPath("$.aromas[0].country.code", is("FRA")))
                .andExpect(jsonPath("$.aromas[0].country.name", is("France")));
    }

    @Test
    void findAllPerfumers_Success() throws Exception {
        Perfumer secondPerfumer = Perfumer.builder()
                .firstName("Second perfumer firstname")
                .lastName("Second perfumer lastname")
                .email("SecondPerfumer@google.com")
                .description("Second perfumer description")
                .country(perfumerCountry)
                .build();

        Country country2 = Country.builder()
                .code("AAA")
                .name("Country A")
                .build();
        Country country3 = Country.builder()
                .code("BBB")
                .name("Country B")
                .build();

        Aroma aroma2 = Aroma.builder()
                .name("aroma 2")
                .description("Cool aroma 2")
                .country(country2)
                .perfumer(secondPerfumer)
                .build();
        Aroma aroma3 = Aroma.builder()
                .name("aroma 3")
                .description("Cool aroma 3")
                .country(country3)
                .perfumer(secondPerfumer)
                .build();

        perfumerRepository.save(secondPerfumer);
        countryRepository.saveAll(List.of(country2, country3));
        aromaRepository.saveAll(List.of(aroma2, aroma3));

        mockMvc.perform(get(perfumersPath))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName", is("Alberto")))
                .andExpect(jsonPath("$.[0].lastName", is("Morillas")))
                .andExpect(jsonPath("$.[0].email", is("Alberto_Morillas@gmail.com")))
                .andExpect(jsonPath("$.[0].description", is("Good description")))
                .andExpect(jsonPath("$.[0].country.code", is("ITA")))
                .andExpect(jsonPath("$.[0].country.name", is("Italy")))
                .andExpect(jsonPath("$.[0].aromas[0].name", is("Montale")))
                .andExpect(jsonPath("$.[0].aromas[0].description", is("Good description")))
                .andExpect(jsonPath("$.[0].aromas[0].country.code", is("FRA")))
                .andExpect(jsonPath("$.[0].aromas[0].country.name", is("France")))

                .andExpect(jsonPath("$.[1].firstName", is("Second perfumer firstname")))
                .andExpect(jsonPath("$.[1].lastName", is("Second perfumer lastname")))
                .andExpect(jsonPath("$.[1].email", is("SecondPerfumer@google.com")))
                .andExpect(jsonPath("$.[1].description", is("Second perfumer description")))
                .andExpect(jsonPath("$.[1].country.code", is("ITA")))
                .andExpect(jsonPath("$.[1].country.name", is("Italy")))
                .andExpect(jsonPath("$.[1].aromas[0].name", is("aroma 2")))
                .andExpect(jsonPath("$.[1].aromas[0].description", is("Cool aroma 2")))
                .andExpect(jsonPath("$.[1].aromas[0].country.code", is("AAA")))
                .andExpect(jsonPath("$.[1].aromas[0].country.name", is("Country A")))
                .andExpect(jsonPath("$.[1].aromas[1].name", is("aroma 3")))
                .andExpect(jsonPath("$.[1].aromas[1].description", is("Cool aroma 3")))
                .andExpect(jsonPath("$.[1].aromas[1].country.code", is("BBB")))
                .andExpect(jsonPath("$.[1].aromas[1].country.name", is("Country B")));
    }

    @Test
    void findPerfumerById_notExistingPerfumer_notFoundError() throws Exception {
        mockMvc.perform(get(perfumersPath + "/" + 99))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist")))
                .andExpect(jsonPath("title-i18n", is(ERROR_PERFUMER_NOT_FOUND)));
    }

    @Test
    void findPerfumerById_noAromaProvided_shouldReturnAllDataButAromas() throws Exception {
        perfumer.setAromas(null);
        perfumerRepository.save(perfumer);

        mockMvc.perform(get(perfumersPath + "/" + perfumer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alberto")))
                .andExpect(jsonPath("$.lastName", is("Morillas")))
                .andExpect(jsonPath("$.email", is("Alberto_Morillas@gmail.com")))
                .andExpect(jsonPath("$.description", is("Good description")))
                .andExpect(jsonPath("$.country.code", is("ITA")))
                .andExpect(jsonPath("$.country.name", is("Italy")))
                .andExpect(jsonPath("$.aromas", is(empty())));
    }

    @Test
    void findPerfumerById_countryNotExists_shouldReturnAllDataButCountry() throws Exception {
        perfumer.setCountry(null);
        perfumerRepository.save(perfumer);

        mockMvc.perform(get(perfumersPath + "/" + perfumer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alberto")))
                .andExpect(jsonPath("$.lastName", is("Morillas")))
                .andExpect(jsonPath("$.email", is("Alberto_Morillas@gmail.com")))
                .andExpect(jsonPath("$.description", is("Good description")))
                .andExpect(jsonPath("$.country", is(nullValue())))
                .andExpect(jsonPath("$.aromas[0].name", is("Montale")))
                .andExpect(jsonPath("$.aromas[0].description", is("Good description")))
                .andExpect(jsonPath("$.aromas[0].country.code", is("FRA")))
                .andExpect(jsonPath("$.aromas[0].country.name", is("France")));
    }

    @Test
    void findPerfumerById_perfumerAndCountryNotExists_shouldReturnAllDataButPerfumerAndCountry() throws Exception {
        perfumer.setAromas(null);
        perfumer.setCountry(null);
        perfumerRepository.save(perfumer);

        mockMvc.perform(get(perfumersPath + "/" + perfumer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alberto")))
                .andExpect(jsonPath("$.lastName", is("Morillas")))
                .andExpect(jsonPath("$.email", is("Alberto_Morillas@gmail.com")))
                .andExpect(jsonPath("$.description", is("Good description")))
                .andExpect(jsonPath("$.country", is(nullValue())))
                .andExpect(jsonPath("$.aromas", is(empty())));
    }

    @Test
    void createPerfumer_Success() throws Exception {
        PerfumerCreateUpdateRequest newPerfumer = PerfumerCreateUpdateRequest.builder()
                .firstName("New perfumer firstName")
                .lastName("New perfumer lastName")
                .email("test@gmail.com")
                .description("Cool perfumer")
                .countryId(perfumerCountry.getId())
                .build();

        mockMvc.perform(post(perfumersPath)
                        .content(mapper.writeValueAsString(newPerfumer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.firstName", is(newPerfumer.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newPerfumer.getLastName())))
                .andExpect(jsonPath("$.email", is(newPerfumer.getEmail())))
                .andExpect(jsonPath("$.description", is(newPerfumer.getDescription())))
                .andExpect(jsonPath("$.country.code", is(perfumerCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(perfumerCountry.getName())))
                .andExpect(jsonPath("$.aromas").doesNotExist());
    }

    @Test
    void createPerfumer_notExistingCountrySentInRequest_Error() throws Exception {
        PerfumerCreateUpdateRequest newPerfumer = PerfumerCreateUpdateRequest.builder()
                .firstName("New perfumer firstName")
                .lastName("New perfumer lastName")
                .email("test@gmail.com")
                .description("Cool perfumer")
                .countryId((short) 99)
                .build();

        mockMvc.perform(post(perfumersPath)
                        .content(mapper.writeValueAsString(newPerfumer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updatePerfumer_Success() throws Exception {
        Perfumer existingPerfumer = Perfumer.builder()
                .firstName("another Perfumer firstName")
                .lastName("another Perfumer lastName")
                .email("another Perfumer email")
                .description("another Perfumer description")
                .build();
        existingPerfumer = perfumerRepository.save(existingPerfumer);

        Country newCountry = Country.builder()
                .code("AAA")
                .name("another Country")
                .build();
        newCountry = countryRepository.save(newCountry);

        PerfumerCreateUpdateRequest updateRequest = PerfumerCreateUpdateRequest.builder()
                .firstName("New perfumer firstName")
                .lastName("New perfumer lastName")
                .email("test@gmail.com")
                .description("Cool perfumer")
                .countryId(newCountry.getId())
                .build();

        mockMvc.perform(put(perfumersPath + "/" + existingPerfumer.getId())
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updateRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateRequest.getLastName())))
                .andExpect(jsonPath("$.email", is(updateRequest.getEmail())))
                .andExpect(jsonPath("$.description", is(updateRequest.getDescription())))
                .andExpect(jsonPath("$.country.code", is(newCountry.getCode())))
                .andExpect(jsonPath("$.country.name", is(newCountry.getName())))
                .andExpect(jsonPath("$.aromas").doesNotExist());
    }

    @Test
    void updatePerfumer_newCountryDoesNotExist_Error() throws Exception {
        PerfumerCreateUpdateRequest updateRequest = PerfumerCreateUpdateRequest.builder()
                .countryId(((short) 99))
                .build();

        mockMvc.perform(put(perfumersPath + "/" + perfumer.getId())
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Country does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updatePerfumer_perfumerDoesNotExist_Error() throws Exception {
        PerfumerCreateUpdateRequest updateRequest = PerfumerCreateUpdateRequest.builder()
                .firstName("test")
                .build();

        mockMvc.perform(put(perfumersPath + "/" + 99)
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Perfumer does not exist")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void updatePerfumer_noChangesProvided_Error() throws Exception {
        PerfumerCreateUpdateRequest updateRequest = PerfumerCreateUpdateRequest.builder()
                .firstName(perfumer.getFirstName())
                .lastName(perfumer.getLastName())
                .email(perfumer.getEmail())
                .description(perfumer.getDescription())
                .countryId(perfumer.getCountry().getId())
                .build();

        mockMvc.perform(put(perfumersPath + "/" + perfumer.getId())
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("No changes provided")))
                .andExpect(jsonPath("$.title-i18n", is(ERROR_BAD_REQUEST)));
    }

    @Test
    void deletePerfumer_Success() throws Exception {
        mockMvc.perform(delete(perfumersPath + "/" + perfumer.getId()))
                .andExpect(status().isNoContent());

        Optional<Perfumer> opt = perfumerRepository.findById(perfumer.getId());
        assertTrue(opt.isEmpty());
    }

    @Test
    void deleteAroma_notExistingPerfumerSentInRequest_Error() throws Exception {
        mockMvc.perform(delete(perfumersPath + "/" + 99))
                .andExpect(status().isNoContent());

        Optional<Perfumer> opt = perfumerRepository.findById(perfumer.getId());
        assertTrue(opt.isPresent());
        assertEquals(perfumer.getId(), opt.get().getId());
    }
}
