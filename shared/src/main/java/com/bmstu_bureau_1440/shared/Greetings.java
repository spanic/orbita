package com.bmstu_bureau_1440.shared;

/**
 * Tiny demonstration utility that both services share. Replace with real shared
 * code (DTOs, constants, helpers) as the project grows.
 */
public final class Greetings {

    private Greetings() {
    }

    public static String hello(String who) {
        return "Hello from %s!".formatted(who);
    }
}
