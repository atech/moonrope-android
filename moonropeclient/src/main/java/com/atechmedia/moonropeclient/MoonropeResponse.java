package com.atechmedia.moonropeclient;

import java.util.Map;

/**
 * Created by jonathan on 17/02/16.
 */
public abstract class MoonropeResponse {
    abstract public MoonropeResponseType getType();

    public static final MoonropeResponse newSuccess(Object data, Map<String, Object> flags) {
        return new MoonropeResponseSuccess(data, flags);
    }

    public static final MoonropeResponse newError(String errorType, Object data) {
        return new MoonropeResponseError(errorType, data);
    }

    public static final MoonropeResponse newFailure(String message) {
        return new MoonropeResponseFailure(message);
    }
}
