package com.gallery.app.service;

import com.gallery.app.dto.aroma.AromaDto;
import com.gallery.app.interfaces.request.AromaCreateUpdateRequest;
import com.gallery.app.model.Aroma;
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
import static com.gallery.app.problem.ProblemCodes.ERROR_PRODUCT_NOT_FOUND;
import static com.gallery.app.util.DtoUtil.toDto;

@Service
@RequiredArgsConstructor
public class AromaService {

    private final AromaRepository aromaRepository;

    private final PerfumerRepository perfumerRepository;

    private final CountryRepository countryRepository;

    public AromaDto findById(Integer id) {
        return aromaRepository.findById(id)
                .map(DtoUtil::toDto)
                .orElseThrow(() -> of(HttpStatus.NOT_FOUND.value(), "Aroma does not exist", ERROR_PRODUCT_NOT_FOUND));
    }

    public List<AromaDto> findAll() {
        return aromaRepository.findAll()
                .stream()
                .map(DtoUtil::toDto)
                .toList();
    }

    @Transactional
    public AromaDto createAroma(AromaCreateUpdateRequest request) {
        Aroma.AromaBuilder aromaBuilder = Aroma.builder();
        aromaBuilder.name(request.getName());
        aromaBuilder.description(request.getDescription());
        bindRelations(aromaBuilder, request);

        return toDto(aromaRepository.save(aromaBuilder.build()));
    }

    @Transactional
    public AromaDto updateAroma(Integer id, AromaCreateUpdateRequest request) {
        Optional<Aroma> aromaFromDbOpt = aromaRepository.findById(id);
        if (aromaFromDbOpt.isPresent()) {
            Aroma aromaFromDb = aromaFromDbOpt.get();
            if (request.hasChangesForUpdate(aromaFromDb)) {
                Aroma.AromaBuilder changedAromaBuilder = Aroma.builder();
                changedAromaBuilder.id(id);
                changedAromaBuilder.name(request.getName());
                changedAromaBuilder.description(request.getDescription());
                bindRelations(changedAromaBuilder, request);

                return toDto(aromaRepository.save(changedAromaBuilder.build()));
            }
        }
        throw of(HttpStatus.BAD_REQUEST.value(), "Aroma does not exist", ERROR_BAD_REQUEST);
    }

    @Transactional
    public void deleteAroma(Integer id) {
        if (aromaRepository.existsById(id)) {
            aromaRepository.deleteById(id);
        }
    }

    private void bindRelations(Aroma.AromaBuilder aromaBuilder, AromaCreateUpdateRequest request) {
        Integer perfumerId = request.getPerfumerId();
        Short countryId = request.getCountryId();

        List<String> problems = new ArrayList<>();
        if (perfumerId != null) {
            Optional<Perfumer> pOpt = perfumerRepository.findById(perfumerId);
            if (pOpt.isPresent()) {
                aromaBuilder.perfumer(pOpt.get());
            } else {
                problems.add("Perfumer does not exist");
            }
        }
        if (countryId != null) {
            Optional<Country> cOpt = countryRepository.findById(countryId);
            if (cOpt.isPresent()) {
                aromaBuilder.country(cOpt.get());
            } else {
                problems.add("Country does not exist");
            }
        }

        if (!problems.isEmpty()) {
            throw of(HttpStatus.BAD_REQUEST.value(), problems, ERROR_BAD_REQUEST);
        }
    }
}
