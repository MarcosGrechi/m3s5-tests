package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.services.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
/*
import org.springframework.security.test.context.support.WithMockUser;
*/
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
/*
    @WithMockUser(username = "admin", authorities = {"SCOPE_ADMIN"})
*/
    void testListarPacientes() throws Exception {
        // Arrange
        PacienteResponseDTO paciente1 = new PacienteResponseDTO(1L, "Carlos Silva", LocalDate.of(1990, 5, 15), "12345678910", "12345678", "carlos@gmail.com");
        PacienteResponseDTO paciente2 = new PacienteResponseDTO(2L, "Ana Oliveira", LocalDate.of(1985, 9, 23), "98765432101", "87654321", "ana@gmail.com");

        when(pacienteService.listarPacientes()).thenReturn(Arrays.asList(paciente1, paciente2));

        // Act
        ResultActions result = mockMvc.perform(get("/pacientes")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Carlos Silva")))
                .andExpect(jsonPath("$[0].cpf", is("12345678910")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nome", is("Ana Oliveira")))
                .andExpect(jsonPath("$[1].cpf", is("98765432101")));
    }

    @Test
/*
    @WithMockUser(username = "admin", authorities = {"SCOPE_ADMIN"})
*/
    void testBuscarPaciente() throws Exception {
        // Arrange
        PacienteResponseDTO paciente = new PacienteResponseDTO(1L, "Carlos Silva", LocalDate.of(1990, 5, 15), "12345678910", "12345678", "carlos@gmail.com");

        when(pacienteService.buscarPaciente(1L)).thenReturn(paciente);

        // Act
        ResultActions result = mockMvc.perform(get("/pacientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Carlos Silva")))
                .andExpect(jsonPath("$.cpf", is("12345678910")));
    }


}
