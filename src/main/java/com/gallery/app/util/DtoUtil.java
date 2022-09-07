package com.gallery.app.util;

import com.gallery.app.dto.CountryDto;
import com.gallery.app.dto.aroma.AromaDto;
import com.gallery.app.dto.aroma.PerfumerForAromaDto;
import com.gallery.app.dto.perfumer.AromaForPerfumerDto;
import com.gallery.app.dto.perfumer.PerfumerDto;
import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DtoUtil {

    public static AromaDto toDto(Aroma aroma) {
        Perfumer perfumer = aroma.getPerfumer();
        Country country = aroma.getCountry();

        return AromaDto.builder()
                .name(aroma.getName())
                .description(aroma.getDescription())
                .countryDto(country != null ? toDto(country) : null)
                .perfumerForAromaDto(perfumer != null ? toPerfumerDtoForAromaDto(perfumer) : null)
                .build();
    }

    public static CountryDto toDto(Country country) {
        return CountryDto.builder()
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public static PerfumerDto toDto(Perfumer perfumer) {
        List<Aroma> aromas = perfumer.getAromas();
        Country country = perfumer.getCountry();

        return PerfumerDto.builder()
                .firstName(perfumer.getFirstName())
                .lastName(perfumer.getLastName())
                .email(perfumer.getEmail())
                .description(perfumer.getDescription())
                .country(country != null ? toDto(country) : null)
                .aromas(aromas != null ? aromas.stream().map(DtoUtil::toAromaDtoForPerfumerDto).toList() : null)
                .build();
    }

    private static PerfumerForAromaDto toPerfumerDtoForAromaDto(Perfumer perfumer) {
        return PerfumerForAromaDto.builder()
                .firstName(perfumer.getFirstName())
                .lastName(perfumer.getLastName())
                .email(perfumer.getEmail())
                .description(perfumer.getDescription())
                .build();
    }

    private static AromaForPerfumerDto toAromaDtoForPerfumerDto(Aroma aroma) {
        Country country = aroma.getCountry();

        return AromaForPerfumerDto.builder()
                .name(aroma.getName())
                .description(aroma.getDescription())
                .country(country != null ? toDto(country) : null)
                .build();
    }
}
