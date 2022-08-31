package com.gallery.app.interfaces.request;

import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class AromaCreateUpdateRequest {

    private Integer perfumerId;

    private Short countryId;

    private String name;

    private String description;

    public boolean hasChangesForUpdate(Aroma request) {
        boolean isNameTheSame = Objects.equals(name, request.getName());
        boolean isDescriptionTheSame = Objects.equals(description, request.getDescription());

        boolean isCountryTheSame = true;
        boolean isPerfumerTheSame = true;

        Country actualCountry = request.getCountry();
        if (actualCountry != null) {
            isCountryTheSame = Objects.equals(countryId, actualCountry.getId());
        }
        Perfumer actualPerfumer = request.getPerfumer();
        if (actualCountry != null) {
            isPerfumerTheSame = Objects.equals(perfumerId, actualPerfumer.getId());
        }

        return !(isNameTheSame && isDescriptionTheSame && isCountryTheSame && isPerfumerTheSame);
    }
}
