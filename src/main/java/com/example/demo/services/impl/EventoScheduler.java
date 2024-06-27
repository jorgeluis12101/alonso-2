package com.example.demo.services.impl;

import com.example.demo.domain.entity.Evento;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.EventoRepository;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoScheduler {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EmailService emailService;


    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventos = eventoRepository.findByRecordatorioBeforeAndCompletadoFalse(ahora);
        for (Evento evento : eventos) {
            Usuario usuario = evento.getUsuario();
            try {
                emailService.sendEmail(
                        usuario.getCorreo(),
                        "Recordatorio de Evento: " + evento.getTitulo(),
                        "Hola " + usuario.getNombre() + ",\n\nTienes un recordatorio para el evento: " + evento.getTitulo() +
                                "\nDescripción: " + evento.getContenido() +
                                "\nFecha: " + evento.getFecha() +
                                "\n\n¡No olvides completar tu evento!\n\nSaludos,\nTu Aplicación de Eventos"
                );
                evento.setCompletado(true);
                eventoRepository.save(evento);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
