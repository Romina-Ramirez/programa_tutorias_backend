package com.tutorias.Config;

import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityAccessHelper {

    public void requireSameUser(Integer expectedUserId) {
        AuthenticatedUser currentUser = getCurrentUser();

        if (!Objects.equals(currentUser.userId(), expectedUserId)) {
            throw new AccessDeniedException("No tiene permisos para acceder a este recurso.");
        }
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            throw new AccessDeniedException("Debe iniciar sesión para continuar.");
        }

        return authenticatedUser;
    }
}
