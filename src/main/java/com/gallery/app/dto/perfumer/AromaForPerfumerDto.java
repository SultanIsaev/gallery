package com.gallery.app.dto.perfumer;

import com.gallery.app.dto.CountryDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AromaForPerfumerDto {

    private String name;

    private String description;

    private CountryDto country;
}
