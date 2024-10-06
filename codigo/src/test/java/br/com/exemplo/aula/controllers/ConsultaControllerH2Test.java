package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.ConsultaRequestDTO;
import br.com.exemplo.aula.controllers.dto.ConsultaResponseDTO;
import br.com.exemplo.aula.entities.Consulta;
import br.com.exemplo.aula.repositories.ConsultaRepository;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import br.com.exemplo.aula.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConsultaControllerH2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private NutricionistaRepository nutricionistaRepository; // Para dados fictícios
    @Autowired
    private PacienteRepository pacienteRepository; // Para dados fictícios

    @BeforeEach
    void setUp() {
        consultaRepository.deleteAll(); // Limpa a base de dados antes de cada teste

        // Adiciona dados fictícios para nutricionista e paciente se necessário
        // exemplo:
        // nutricionistaRepository.save(new Nutricionista(...));
        // pacienteRepository.save(new Paciente(...));
    }

    @Test
    public void testSalvarConsulta() throws Exception {
        // Configurar DTO de solicitação
        ConsultaRequestDTO request = new ConsultaRequestDTO(1L, 1L, LocalDate.now(), "Observação de teste");

        // Realiza a requisição POST para salvar a consulta
        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idNutricionista\":1,\"idPaciente\":1,\"data\":\"06/10/2024\",\"observacoes\":\"Observação de teste\"}"))
                .andExpect(status().isOk()); // Verifica se o status é 200
    }

    @Test
    public void testBuscarConsultaPorId_Sucesso() throws Exception {
        // Configurar dados fictícios
        Consulta consulta = new Consulta();
        consulta.setNutricionista(nutricionistaRepository.findById(1L).orElse(null)); // Defina o nutricionista
        consulta.setPaciente(pacienteRepository.findById(1L).orElse(null)); // Defina o paciente
        consulta.setData(LocalDate.now());
        consulta.setObservacoes("Observações da consulta");
        consulta = consultaRepository.save(consulta); // Salva a consulta

        // Verifica se a busca por ID funciona corretamente
        mockMvc.perform(get("/consultas/" + consulta.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Verifica se o status é 200
    }

    @Test
    public void testBuscarConsultaPorId_NaoEncontrado() throws Exception {
        mockMvc.perform(get("/consultas/999") // ID que não existe
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verifica se o status é 404
    }

    @Test
    public void testDeletarConsultaPorId_Sucesso() throws Exception {
        // Configurar dados fictícios
        Consulta consulta = new Consulta();
        consulta.setNutricionista(nutricionistaRepository.findById(1L).orElse(null)); // Defina o nutricionista
        consulta.setPaciente(pacienteRepository.findById(1L).orElse(null)); // Defina o paciente
        consulta.setData(LocalDate.now());
        consulta.setObservacoes("Observação de teste");
        consulta = consultaRepository.save(consulta); // Salva a consulta

        // Verifica se a exclusão por ID funciona corretamente
        mockMvc.perform(delete("/consultas/" + consulta.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Verifica se o status é 204
    }

    @Test
    public void testDeletarConsultaPorId_NaoEncontrado() throws Exception {
        // Verifica que a exclusão de uma consulta não existente retorna 404
        mockMvc.perform(delete("/consultas/999") // ID que não existe
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verifica se o status é 404
    }
}
