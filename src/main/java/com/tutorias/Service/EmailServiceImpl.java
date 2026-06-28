package com.tutorias.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tutorias.Service.exception.EmailSendException;

import jakarta.mail.MessagingException;

/**
 * Envío de correos vía la API HTTP de Brevo (https://api.brevo.com/v3/smtp/email).
 *
 * Se usa HTTP (HTTPS/443) en lugar de SMTP porque el plan free de Render bloquea
 * los puertos SMTP salientes (25/465/587) desde sep-2025.
 *
 * Variables de entorno necesarias (configurar en Render):
 *  - BREVO_API_KEY      : la API key de Brevo (Settings -> SMTP & API)
 *  - BREVO_SENDER_EMAIL : remitente verificado (por defecto usa MAIL_USERNAME)
 *  - BREVO_SENDER_NAME  : nombre visible del remitente (opcional)
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${brevo.api-key:}")
    private String brevoApiKey;

    @Value("${brevo.sender-email:}")
    private String senderEmail;

    @Value("${brevo.sender-name:Programa de Tutorías}")
    private String senderName;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    @Async("mailExecutor")
    public void sendnewTutorOrAdmin(String to, String password, String role) throws MessagingException {

        String link = frontendUrl + "/login";

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

                <table width="100%%" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="20" cellspacing="0"
                                style="background-color: #ffffff; border-radius: 8px;">

                                <tr>
                                    <td>
                                        <h2 style="color: #2c3e50; margin-top: 0;">
                                            Bienvenido al Programa de Tutorías
                                        </h2>

                                        <p style="color: #333;">
                                            Le damos la bienvenida al <strong>Programa de Tutorías</strong>.
                                            Su cuenta ha sido creada correctamente.
                                        </p>

                                        <p style="color: #333;">
                                            Para ingresar a la plataforma, haga clic en el siguiente botón
                                            utilizando su correo electrónico y la contraseña asignada:
                                        </p>

                                        <div style="text-align: center; margin: 30px 0;">
                                            <a href="%s"
                                            style="
                                                background-color: #1a73e8;
                                                color: #ffffff;
                                                padding: 12px 24px;
                                                text-decoration: none;
                                                border-radius: 5px;
                                                font-weight: bold;
                                                display: inline-block;
                                            ">
                                                Acceder a la plataforma
                                            </a>
                                        </div>

                                        <p style="color: #333;">
                                            <strong>Contraseña:</strong> %s
                                        </p>

                                        <p style="font-size: 0.9em; color: #666;">
                                            Este correo ha sido generado automáticamente.
                                            Por favor, no responda a este mensaje.
                                        </p>

                                        <p style="font-size: 0.9em; color: #666;">
                                            Atentamente,<br>
                                            <strong>Equipo del Programa de Tutorías</strong>
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """.formatted(link, password);

        sendEmail(to, "Acceso al Programa de Tutorías", html);
    }

    @Override
    @Async("mailExecutor")
    public void sendRequestRecoveryPassword(String to, String resetToken) throws MessagingException {

        String link = frontendUrl + "/cambiarContrasenia?token="
                + URLEncoder.encode(resetToken, StandardCharsets.UTF_8);

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">

                <table width="100%%" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="20" cellspacing="0"
                                style="background-color: #ffffff; border-radius: 8px;">

                                <tr>
                                    <td>
                                        <h2 style="color: #2c3e50; margin-top: 0;">
                                            Recuperación de contraseña
                                        </h2>

                                        <p style="color: #333;">
                                            Se ha solicitado un <strong>cambio de contraseña</strong> para su cuenta
                                            en el <strong>Programa de Tutorías</strong>.
                                        </p>

                                        <p style="color: #333;">
                                            Si usted no realizó esta solicitud, puede ignorar este mensaje.
                                            De lo contrario, haga clic en el siguiente botón para continuar
                                            con el proceso de recuperación:
                                        </p>

                                        <div style="text-align: center; margin: 30px 0;">
                                            <a href="%s"
                                            style="
                                                background-color: #1a73e8;
                                                color: #ffffff;
                                                padding: 12px 24px;
                                                text-decoration: none;
                                                border-radius: 5px;
                                                font-weight: bold;
                                                display: inline-block;
                                            ">
                                                Cambiar contraseña
                                            </a>
                                        </div>

                                        <p style="font-size: 0.9em; color: #666;">
                                            Por su seguridad, no comparta este enlace con terceros.
                                            Si no solicitó el cambio, no es necesario realizar ninguna acción.
                                        </p>

                                        <p style="font-size: 0.9em; color: #666;">
                                            Este correo ha sido generado automáticamente.
                                            Por favor, no responda a este mensaje.
                                        </p>

                                        <p style="font-size: 0.9em; color: #666;">
                                            Atentamente,<br>
                                            <strong>Equipo del Programa de Tutorías</strong>
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """.formatted(link);

        sendEmail(to, "Recuperación de contraseña", html);
    }

    /**
     * Realiza el POST a la API de Brevo. Se ejecuta dentro de un método @Async,
     * así que cualquier excepción se registra por el manejador de async y no
     * bloquea ni rompe la creación del usuario.
     */
    private void sendEmail(String to, String subject, String htmlContent) {
        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            throw new EmailSendException("BREVO_API_KEY no está configurada.");
        }
        if (senderEmail == null || senderEmail.isBlank()) {
            throw new EmailSendException("No hay remitente configurado (BREVO_SENDER_EMAIL/MAIL_USERNAME).");
        }

        String payload = "{"
                + "\"sender\":{\"name\":" + jsonString(senderName) + ",\"email\":" + jsonString(senderEmail) + "},"
                + "\"to\":[{\"email\":" + jsonString(to) + "}],"
                + "\"subject\":" + jsonString(subject) + ","
                + "\"htmlContent\":" + jsonString(htmlContent)
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BREVO_URL))
                .timeout(Duration.ofSeconds(15))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("api-key", brevoApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status < 200 || status >= 300) {
                throw new EmailSendException("Brevo respondió " + status + ": " + response.body());
            }
        } catch (EmailSendException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailSendException("No se pudo enviar el correo a " + to + ": " + e.getMessage());
        }
    }

    /** Devuelve el valor como literal JSON (entre comillas) con los escapes necesarios. */
    private static String jsonString(String value) {
        if (value == null) return "\"\"";
        StringBuilder sb = new StringBuilder(value.length() + 16);
        sb.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
