package com.atechmedia.moonropeclient;

/**
 * Created by jonathan on 17/02/16.
 */

// Returned when there was a failure actually communicating successfully
// with the remote server.
public class MoonropeResponseFailure extends MoonropeResponse {
    private final String message;

    public MoonropeResponseFailure(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public MoonropeResponseType getType() {
        return MoonropeResponseType.Failure;
    }
}
