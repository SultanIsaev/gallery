package com.gallery.app.dto.perfumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gallery.app.dto.CountryDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PerfumerDto {

    private String firstName;

    private String lastName;

    private String email;

    private String description;

    private CountryDto country;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AromaForPerfumerDto> aromas;
}
