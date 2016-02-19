package com.atechmedia.moonropeclient;

import java.util.Map;

/**
 * Created by jonathan on 17/02/16.
 */

// Returned when the request has been successful. Provides the data and flags.
public class MoonropeResponseSuccess extends MoonropeResponse {
    private final Object data;
    private final Map<String, Object> flags;

    public MoonropeResponseSuccess(Object data, Map<String, Object> flags) {
        this.data = data;
        this.flags = flags;
    }

    public Object getData() {
        return data;
    }

    public Map<String, Object> getFlags() {
        return flags;
    }

    @Override
    public MoonropeResponseType getType() {
        return MoonropeResponseType.Success;
    }
}
