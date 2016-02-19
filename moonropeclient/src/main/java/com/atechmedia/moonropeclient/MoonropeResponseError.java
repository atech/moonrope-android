package com.atechmedia.moonropeclient;

/**
 * Created by jonathan on 17/02/16.
 */

// Returned when there is a Moonrope error. Contains the details of the error
// and the full data for the response.
public class MoonropeResponseError extends MoonropeResponse {
    private final String errorType;
    private final Object data;

    public MoonropeResponseError(String errorType, Object data) {
        this.errorType = errorType;
        this.data = data;
    }

    public String getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }

    @Override
    public MoonropeResponseType getType() {
        return MoonropeResponseType.Error;
    }
}
