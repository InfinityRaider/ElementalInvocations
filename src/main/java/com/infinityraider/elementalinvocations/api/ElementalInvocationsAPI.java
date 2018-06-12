package com.infinityraider.elementalinvocations.api;

import java.util.Optional;

public class ElementalInvocationsAPI {
    private static IElementalInvocationsAPI API;

    public static Optional<IElementalInvocationsAPI> getApi() {
        return Optional.ofNullable(API);
    }
}