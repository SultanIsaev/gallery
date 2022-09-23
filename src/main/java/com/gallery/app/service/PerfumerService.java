package com.gallery.app.service;

import com.gallery.app.dto.perfumer.PerfumerDto;
import com.gallery.app.interfaces.request.PerfumerCreateUpdateRequest;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import com.gallery.app.repository.AromaRepository;
import com.gallery.app.repository.CountryRepository;
import com.gallery.app.repository.PerfumerRepository;
import com.gallery.app.util.DtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gallery.app.problem.LocalizableException.of;
import static com.gallery.app.problem.ProblemCodes.ERROR_BAD_REQUEST;
import static com.gallery.app.problem.ProblemCodes.ERROR_PERFUMER_NOT_FOUND;
import static com.gallery.app.util.DtoUtil.toDto;

@Service
@RequiredArgsConstructor
public class PerfumerService {

    private final PerfumerRepository perfumerRepository;

    private final AromaRepository aromaRepository;

    private final CountryRepository countryRepository;

    public List<PerfumerDto> findAll() {
        return perfumerRepository.findAll()
                .stream()
                .map(DtoUtil::toDto)
                .toList();
    }

    public PerfumerDto findById(Integer id) {
        return perfumerRepository.findById(id)
                .map(DtoUtil::toDto)
                .orElseThrow(() -> of(HttpStatus.NOT_FOUND.value(), "Perfumer does not exist", ERROR_PERFUMER_NOT_FOUND));
    }

    @Transactional
    public PerfumerDto createPerfumer(PerfumerCreateUpdateRequest request) {
        Perfumer.PerfumerBuilder perfumerBuilder = Perfumer.builder();
        perfumerBuilder.firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .description(request.getDescription());
        bindRelations(perfumerBuilder, request);

        return toDto(perfumerRepository.save(perfumerBuilder.build()));
    }

    @Transactional
    public PerfumerDto updatePerfumer(Integer id, PerfumerCreateUpdateRequest request) {
        Optional<Perfumer> perfumerFromDbOpt = perfumerRepository.findById(id);
        if (perfumerFromDbOpt.isPresent()) {
            Perfumer perfumerFromDb = perfumerFromDbOpt.get();
            if (request.hasChangesForUpdate(perfumerFromDb)) {
                Perfumer.PerfumerBuilder perfumerBuilder = Perfumer.builder();
                perfumerBuilder.firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .description(request.getDescription());
                bindRelations(perfumerBuilder, request);

                return toDto(perfumerRepository.save(perfumerBuilder.build()));
            } else {
                throw of(HttpStatus.BAD_REQUEST.value(), "No changes provided", ERROR_BAD_REQUEST);
            }
        }
        throw of(HttpStatus.BAD_REQUEST.value(), "Perfumer does not exist", ERROR_BAD_REQUEST);
    }

    @Transactional
    public void deletePerfumer(Integer id) {
        if (perfumerRepository.existsById(id)) {
            perfumerRepository.deleteById(id);
        }
    }

    private void bindRelations(Perfumer.PerfumerBuilder perfumerBuilder, PerfumerCreateUpdateRequest request) {
        Short countryId = request.getCountryId();

        List<String> problems = new ArrayList<>();
        if (countryId != null) {
            Optional<Country> cOpt = countryRepository.findById(countryId);
            if (cOpt.isPresent()) {
                perfumerBuilder.country(cOpt.get());
            } else {
                problems.add("Country does not exist");
            }
        }

        if (!problems.isEmpty()) {
            throw of(HttpStatus.BAD_REQUEST.value(), problems, ERROR_BAD_REQUEST);
        }
    }
}
