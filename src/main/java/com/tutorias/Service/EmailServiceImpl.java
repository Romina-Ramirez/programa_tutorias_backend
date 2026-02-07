package com.tutorias.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendnewTutor(String to, String password) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

        String link = "http://tutorias.com/login";

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

        helper.setTo(to);
        helper.setSubject("Acceso al Programa de Tutorías");
        helper.setText(html, true);

        mailSender.send(msg);
    }

    @Override
    public void sendRequestRecoveryPassword(String to) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

        String link = "http://tutorias.com/changePassword";

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

        helper.setTo(to);
        helper.setSubject("Recuperación de contraseña");
        helper.setText(html, true);

        mailSender.send(msg);
    }
    
}
