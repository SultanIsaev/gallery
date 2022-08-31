package com.gallery.app.util;

import com.gallery.app.dto.CountryDto;
import com.gallery.app.dto.aroma.AromaDto;
import com.gallery.app.dto.aroma.PerfumerForAromaDto;
import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoUtil {

    public static AromaDto toDto(Aroma aroma) {
        Perfumer perfumer = aroma.getPerfumer();
        Country country = aroma.getCountry();

        return AromaDto.builder()
                .name(aroma.getName())
                .description(aroma.getDescription())
                .countryDto(country != null ? toDto(country) : null)
                .perfumerForAromaDto(perfumer != null ? toDtoForAroma(perfumer) : null)
                .build();
    }

    public static PerfumerForAromaDto toDtoForAroma(Perfumer perfumer) {
        return PerfumerForAromaDto.builder()
                .firstName(perfumer.getFirstName())
                .lastName(perfumer.getLastName())
                .email(perfumer.getEmail())
                .description(perfumer.getDescription())
                .build();
    }

    public static CountryDto toDto(Country country) {
        return CountryDto.builder()
                .code(country.getCode())
                .name(country.getName())
                .build();
    }
}
