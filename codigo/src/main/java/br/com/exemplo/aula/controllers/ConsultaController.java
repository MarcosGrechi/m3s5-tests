package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.services.ConsultaService;
import br.com.exemplo.aula.controllers.dto.ConsultaRequestDTO;
import br.com.exemplo.aula.controllers.dto.ConsultaResponseDTO;
import br.com.exemplo.aula.controllers.dto.ConsultaResponseListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping()
    public ConsultaResponseDTO salvarConsulta(@RequestBody ConsultaRequestDTO request) {
        return consultaService.salvarConsulta(request);
    }

    @GetMapping()
    public List<ConsultaResponseListDTO> listarConsultas() {
        var consultas = consultaService.listarConsultas();
        if (consultas.isEmpty()){
            return null;
        } else {
            return consultas;
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarConsultaPorId(@PathVariable Long id) {
        ConsultaResponseDTO consultaResponse = consultaService.buscarConsulta(id);
        if (consultaResponse != null) {
            return ResponseEntity.ok(consultaResponse); // Retorna 200 com a consulta
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se não encontrar
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsultaPorId(@PathVariable Long id) {
        try {
            consultaService.deletarConsulta(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a exclusão for bem-sucedida
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se não encontrado
        }
    }


}

