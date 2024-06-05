package com.example.demo.webController;

import com.example.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaCorreoController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/enviar-correo-prueba")
    public ResponseEntity<String> enviarCorreoPrueba(@RequestParam String destinatario) {
        try {
            emailService.sendEmail(
                    destinatario,
                    "Prueba de Envío de Correo",
                    "Este es un correo de prueba para verificar el envío de correos desde la aplicación."
            );
            return new ResponseEntity<>("Correo enviado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar el correo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
