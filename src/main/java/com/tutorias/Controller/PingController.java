package com.tutorias.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint público y liviano para "despertar" el servicio (cold start de Render
 * free) y para health checks / keep-alive externo. No toca la base de datos.
 * URL pública (con context-path): /API/Tutorias/ping
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
