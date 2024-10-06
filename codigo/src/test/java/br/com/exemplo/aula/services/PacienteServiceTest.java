package br.com.exemplo.aula.services;

import br.com.exemplo.aula.entities.Paciente;
import br.com.exemplo.aula.repositories.PacienteRepository;
import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Extende o Mockito para este teste
class PacienteServiceTest {

    @InjectMocks
    private PacienteService pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;

    @BeforeEach
    void setUp() {
        // Não é mais necessário chamar o openMocks, pois @ExtendWith cuida disso
    }

    @Test
    void testSalvarPaciente() {
        // Arrange: Preparar o cenário do teste
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setDataNascimento(LocalDate.of(1990, 5, 10));
        requestDTO.setCpf("12345678910");
        requestDTO.setTelefone("999999999");
        requestDTO.setEmail("joao.silva@email.com");

        Paciente pacienteSalvo = new Paciente();
        pacienteSalvo.setId(1L);
        pacienteSalvo.setNome("João Silva");
        pacienteSalvo.setDataNascimento(LocalDate.of(1990, 5, 10));
        pacienteSalvo.setCpf("12345678910");
        pacienteSalvo.setTelefone("999999999");
        pacienteSalvo.setEmail("joao.silva@email.com");

        // Mocking do comportamento do repositório
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteSalvo);

        // Act: Executar o método a ser testado
        PacienteResponseDTO responseDTO = pacienteService.salvarPaciente(requestDTO);

        // Assert: Verificar se o método save foi chamado e se o retorno é correto
        verify(pacienteRepository, times(1)).save(any(Paciente.class));  // Verifica se o método foi chamado 1 vez
        assertEquals(1L, responseDTO.getId());
        assertEquals("João Silva", responseDTO.getNome());
        assertEquals("12345678910", responseDTO.getCpf());
    }

    @Test
    void testBuscarPaciente() {
        // Arrange: Preparar um paciente de exemplo
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Maria Souza");
        paciente.setDataNascimento(LocalDate.of(1985, 7, 15));
        paciente.setCpf("98765432100");
        paciente.setTelefone("888888888");
        paciente.setEmail("maria.souza@email.com");

        // Mockando o comportamento do repositório
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // Act: Executar o método buscarPaciente
        PacienteResponseDTO responseDTO = pacienteService.buscarPaciente(1L);

        // Assert: Verificar se o repositório foi chamado e se o retorno é correto
        verify(pacienteRepository, times(1)).findById(1L);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Maria Souza", responseDTO.getNome());
        assertEquals("98765432100", responseDTO.getCpf());
    }

    @Test
    void testRemoverPaciente() {
        // Act: Executar o método removerPaciente
        pacienteService.removerPaciente(1L);

        // Assert: Verificar se o repositório foi chamado para deletar
        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAtualizarPaciente() {
        // Arrange: Preparar um paciente e um requestDTO
        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setId(1L);
        pacienteExistente.setNome("Carlos Souza");
        pacienteExistente.setDataNascimento(LocalDate.of(1990, 4, 20));
        pacienteExistente.setCpf("11122233344");
        pacienteExistente.setTelefone("988888888");
        pacienteExistente.setEmail("carlos.souza@email.com");

        PacienteRequestDTO requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("Carlos Alberto");
        requestDTO.setDataNascimento(LocalDate.of(1990, 4, 20));
        requestDTO.setCpf("11122233344");
        requestDTO.setTelefone("988888888");
        requestDTO.setEmail("carlos.alberto@email.com");

        // Mockando o comportamento do repositório
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteExistente);

        // Act: Executar o método atualizarPaciente
        PacienteResponseDTO responseDTO = pacienteService.atualizarPaciente(1L, requestDTO);

        // Assert: Verificar se o repositório foi chamado corretamente
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));

        assertEquals("Carlos Alberto", responseDTO.getNome());
        assertEquals("11122233344", responseDTO.getCpf());
    }
}
