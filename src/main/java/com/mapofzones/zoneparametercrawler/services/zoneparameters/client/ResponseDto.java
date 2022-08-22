package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

public class ResponseDto {

    private Boolean isSuccess;
    private String value;

    protected ResponseDto(Boolean isSuccess, String value) {
        this.isSuccess = isSuccess;
        this.value = value;
    }

    protected ResponseDto(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    protected Boolean getSuccess() {
        return isSuccess;
    }

    protected String getValue() {
        return value;
    }
}
