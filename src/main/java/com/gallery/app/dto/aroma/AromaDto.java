package com.gallery.app.dto.aroma;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gallery.app.dto.CountryDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AromaDto {

    private String name;

    private String description;

    @JsonProperty("country")
    private CountryDto countryDto;

    @JsonProperty("perfumer")
    private PerfumerForAromaDto perfumerForAromaDto;
}
