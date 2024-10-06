package br.com.exemplo.aula.services;

import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutricionistaServiceTest {

    @Mock
    private NutricionistaRepository nutricionistaRepository;

    @InjectMocks
    private NutricionistaService nutricionistaService;

    @BeforeEach
    void setUp() {
        // Setup automático do MockitoExtension
    }

    @Test
    void testSalvarNutricionista() {
        // Arrange: Criar o DTO de request e a entidade esperada
        NutricionistaRequestDTO requestDTO = new NutricionistaRequestDTO();
        requestDTO.setNome("Ana Nutri");
        requestDTO.setMatricula("12345");
        requestDTO.setTempoExperiencia(5);
        requestDTO.setCrn("CRN12345");
        requestDTO.setEspecialidade("Esportiva");

        Nutricionista nutricionistaSalvo = new Nutricionista();
        nutricionistaSalvo.setId(1L);
        nutricionistaSalvo.setNome("Ana Nutri");
        nutricionistaSalvo.setMatricula("12345");
        nutricionistaSalvo.setTempoExperiencia(5);
        nutricionistaSalvo.setCrn("CRN12345");
        nutricionistaSalvo.setEspecialidade("Esportiva");

        // Mocking: Quando o método save() do repositório for chamado, retornamos a entidade salva
        when(nutricionistaRepository.save(any(Nutricionista.class))).thenReturn(nutricionistaSalvo);
        when(nutricionistaRepository.findByNome(requestDTO.getNome())).thenReturn(Optional.empty());

        // Act: Chamar o método salvarNutricionista
        NutricionistaResponseDTO responseDTO = nutricionistaService.salvarNutricionista(requestDTO);

        // Assert: Verificar se os métodos foram chamados corretamente e se o resultado está correto
        verify(nutricionistaRepository, times(1)).save(any(Nutricionista.class));
        verify(nutricionistaRepository, times(1)).findByNome(requestDTO.getNome());
        assertEquals(1L, responseDTO.getId());
        assertEquals("Ana Nutri", responseDTO.getNome());
    }

    @Test
    void testListarNutricionistas() {
        // Arrange: Criar uma lista de nutricionistas simulada
        List<Nutricionista> nutricionistas = new ArrayList<>();
        Nutricionista nutri1 = new Nutricionista();
        nutri1.setId(1L);
        nutri1.setNome("Carlos Nutri");
        nutricionistas.add(nutri1);

        Nutricionista nutri2 = new Nutricionista();
        nutri2.setId(2L);
        nutri2.setNome("Joana Nutri");
        nutricionistas.add(nutri2);

        // Mocking: Quando findAll for chamado, retornar essa lista
        when(nutricionistaRepository.findAll()).thenReturn(nutricionistas);

        // Act: Chamar o método listarNutricionistas
        List<NutricionistaResponseDTO> responseList = nutricionistaService.listarNutricionistas();

        // Assert: Verificar se o método foi chamado corretamente e se a resposta está correta
        verify(nutricionistaRepository, times(1)).findAll();
        assertEquals(2, responseList.size());
        assertEquals("Carlos Nutri", responseList.get(0).getNome());
        assertEquals("Joana Nutri", responseList.get(1).getNome());
    }

    @Test
    void testBuscarNutricionista() {
        // Arrange: Preparar um nutricionista de exemplo
        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setId(1L);
        nutricionista.setNome("Carlos Nutri");
        nutricionista.setMatricula("12345");
        nutricionista.setTempoExperiencia(7);
        nutricionista.setCrn("CRN12345");
        nutricionista.setEspecialidade("Esportiva");

        // Mocking: findById retorna um nutricionista
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));

        // Act: Chamar o método buscarNutricionista
        NutricionistaResponseDTO response = nutricionistaService.buscarNutricionista(1L);

        // Assert: Verificar se o método foi chamado corretamente e se o retorno está correto
        verify(nutricionistaRepository, times(1)).findById(1L);
        assertEquals(1L, response.getId());
        assertEquals("Carlos Nutri", response.getNome());
    }

    @Test
    void testRemoverNutricionista() {
        // Act: Chamar o método removerNutricionista
        nutricionistaService.removerNutricionista(1L);

        // Assert: Verificar se o método deleteById foi chamado corretamente
        verify(nutricionistaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAtualizarNutricionista() {
        // Arrange: Criar um nutricionista existente e um request de atualização
        Nutricionista nutricionistaExistente = new Nutricionista();
        nutricionistaExistente.setId(1L);
        nutricionistaExistente.setNome("Nutri Antiga");
        nutricionistaExistente.setMatricula("54321");
        nutricionistaExistente.setTempoExperiencia(3);
        nutricionistaExistente.setCrn("CRN54321");
        nutricionistaExistente.setEspecialidade("Pediatria");

        NutricionistaRequestDTO requestDTO = new NutricionistaRequestDTO();
        requestDTO.setNome("Nutri Atualizada");
        requestDTO.setMatricula("54321");
        requestDTO.setTempoExperiencia(4);
        requestDTO.setCrn("CRN54321");
        requestDTO.setEspecialidade("Esportiva");

        // Mocking: Simulando o retorno do findById e do save
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionistaExistente));
        when(nutricionistaRepository.save(any(Nutricionista.class))).thenReturn(nutricionistaExistente);

        // Act: Chamar o método atualizarNutricionista
        NutricionistaResponseDTO responseDTO = nutricionistaService.atualizarNutricionista(1L, requestDTO);

        // Assert: Verificar se os métodos do repositório foram chamados corretamente
        verify(nutricionistaRepository, times(1)).findById(1L);
        verify(nutricionistaRepository, times(1)).save(any(Nutricionista.class));

        // Verificar se os dados foram atualizados corretamente
        assertEquals("Nutri Atualizada", responseDTO.getNome());
        assertEquals(4, responseDTO.getTempoExperiencia());
    }
}
