package com.tutorias.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.tutorias.Service.exception.TooManyAttemptsException;

/**
 * Control de intentos repetidos de inicio de sesión: tras MAX_ATTEMPTS intentos
 * fallidos seguidos para un mismo correo, la cuenta queda bloqueada para el
 * login durante LOCK_MINUTES. Un login correcto reinicia el contador.
 *
 * El registro se mantiene en memoria: es suficiente para una sola instancia y
 * se reinicia con el servicio (el bloqueo es temporal por diseño).
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_MINUTES = 15;

    private static class Entry {
        int failures;
        long lockedUntil; // epoch millis; 0 = sin bloqueo
    }

    private final Map<String, Entry> attempts = new ConcurrentHashMap<>();

    private String key(String email) {
        return String.valueOf(email == null ? "" : email).trim().toLowerCase();
    }

    /** Lanza TooManyAttemptsException si la cuenta está bloqueada en este momento. */
    public void checkNotLocked(String email) {
        Entry e = attempts.get(key(email));
        if (e == null) return;

        long now = System.currentTimeMillis();
        if (e.lockedUntil > now) {
            long minutes = Math.max(1, (e.lockedUntil - now + 59_999) / 60_000);
            throw new TooManyAttemptsException(
                    "Demasiados intentos fallidos. Intente nuevamente en " + minutes + " minuto(s).");
        }
    }

    public void onFailure(String email) {
        Entry e = attempts.computeIfAbsent(key(email), k -> new Entry());
        synchronized (e) {
            long now = System.currentTimeMillis();
            if (e.lockedUntil > 0 && e.lockedUntil <= now) {
                // El bloqueo anterior ya venció: empieza un conteo nuevo.
                e.failures = 0;
                e.lockedUntil = 0;
            }
            e.failures++;
            if (e.failures >= MAX_ATTEMPTS) {
                e.lockedUntil = now + LOCK_MINUTES * 60_000;
            }
        }
    }

    public void onSuccess(String email) {
        attempts.remove(key(email));
    }
}
