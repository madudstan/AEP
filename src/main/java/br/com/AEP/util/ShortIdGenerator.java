package br.com.AEP.util;

import java.util.UUID;

public final class ShortIdGenerator {
    private ShortIdGenerator() {
    }

    public static String next() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
    }
}
