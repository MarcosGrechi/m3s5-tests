package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.ConsultaResponseDTO;
import br.com.exemplo.aula.services.ConsultaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarConsultaPorId_Sucesso() throws Exception {
        ConsultaResponseDTO consultaResponse = new ConsultaResponseDTO(
                1L,
                null, // Nutricionista (opcional para o teste)
                null, // Paciente (opcional para o teste)
                LocalDate.of(2024, 10, 06),
                "Observações da consulta"
        );

        when(consultaService.buscarConsulta(anyLong())).thenReturn(consultaResponse);

        mockMvc.perform(get("/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Verifica se o status é 200
    }

    @Test
    public void testBuscarConsultaPorId_NaoEncontrado() throws Exception {
        when(consultaService.buscarConsulta(anyLong())).thenReturn(null); // Simula consulta não encontrada

        mockMvc.perform(get("/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verifica se o status é 404
    }

    @Test
    public void testDeletarConsultaPorId_Sucesso() throws Exception {
        // Configura o comportamento esperado para o serviço
        doNothing().when(consultaService).deletarConsulta(1L);

        mockMvc.perform(delete("/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Verifica se o status é 204
    }

    @Test
    public void testDeletarConsultaPorId_NaoEncontrado() throws Exception {
        // Simula que a consulta não existe
        doThrow(new RuntimeException("Consulta não encontrada")).when(consultaService).deletarConsulta(1L);

        mockMvc.perform(delete("/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verifica se o status é 404
    }


}
