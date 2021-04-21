package com.moviebooking.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    public ErrorResponse(String message) {
        this.errorMessage = message;
    }

    // FIXME: handle error codes
    private String errorCode = "01";
    private String errorMessage;

}
