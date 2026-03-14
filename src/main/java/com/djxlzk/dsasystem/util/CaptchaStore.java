package com.djxlzk.dsasystem.util;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaStore {
    private static final long TTL_SECONDS = 180;

    private static class Entry {
        private final String code;
        private final long expireAtEpochSeconds;

        private Entry(String code, long expireAtEpochSeconds) {
            this.code = code;
            this.expireAtEpochSeconds = expireAtEpochSeconds;
        }
    }

    private static final Map<String, Entry> STORE = new ConcurrentHashMap<>();

    public static String put(String code) {
        cleanup();
        String id = UUID.randomUUID().toString().replace("-", "");
        STORE.put(id, new Entry(code, Instant.now().getEpochSecond() + TTL_SECONDS));
        return id;
    }

    public static boolean verifyAndRemove(String captchaId, String input) {
        cleanup();
        if (captchaId == null || captchaId.isEmpty() || input == null) {
            return false;
        }
        Entry entry = STORE.get(captchaId);
        if (entry == null) {
            return false;
        }
        long now = Instant.now().getEpochSecond();
        if (entry.expireAtEpochSeconds < now) {
            STORE.remove(captchaId);
            return false;
        }
        boolean ok = entry.code.equalsIgnoreCase(input.trim());
        if (ok) {
            STORE.remove(captchaId);
        }
        return ok;
    }

    private static void cleanup() {
        long now = Instant.now().getEpochSecond();
        STORE.entrySet().removeIf(e -> e.getValue().expireAtEpochSeconds < now);
    }
}
