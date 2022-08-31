package com.gallery.app.dto.aroma;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerfumerForAromaDto {

    private String firstName;

    private String lastName;

    private String email;

    private String description;
}
