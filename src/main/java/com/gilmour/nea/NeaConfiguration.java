package com.gilmour.nea;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class NeaConfiguration extends Configuration {

    @NotEmpty
    private String uploadLocation;


    @JsonProperty
    public String getUploadLocation() {
        return uploadLocation;
    }

    @JsonProperty
    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }

}
