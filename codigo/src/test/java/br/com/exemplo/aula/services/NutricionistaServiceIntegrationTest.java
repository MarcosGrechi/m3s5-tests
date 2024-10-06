package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NutricionistaServiceIntegrationTest {

    @Autowired
    private NutricionistaService nutricionistaService;

    @MockBean
    private NutricionistaRepository nutricionistaRepository;

    @BeforeEach
    void setUp() {
        // Não é necessário configurar nada aqui porque o @MockBean já injeta o mock
    }

    @Test
    void testListarNutricionistas() {
        // Arrange: Simular a lista de nutricionistas no banco
        Nutricionista nutri1 = new Nutricionista();
        nutri1.setId(1L);
        nutri1.setNome("Carlos Nutri");
        nutri1.setMatricula("12345");
        nutri1.setTempoExperiencia(7);
        nutri1.setCrn("CRN12345");
        nutri1.setEspecialidade("Esportiva");

        Nutricionista nutri2 = new Nutricionista();
        nutri2.setId(2L);
        nutri2.setNome("Joana Nutri");
        nutri2.setMatricula("67890");
        nutri2.setTempoExperiencia(4);
        nutri2.setCrn("CRN67890");
        nutri2.setEspecialidade("Pediatria");

        when(nutricionistaRepository.findAll()).thenReturn(Arrays.asList(nutri1, nutri2));

        // Act: Chamar o método listarNutricionistas
        List<NutricionistaResponseDTO> responseList = nutricionistaService.listarNutricionistas();

        // Assert: Verificar se o método do serviço retorna os valores corretos
        assertEquals(2, responseList.size());
        assertEquals("Carlos Nutri", responseList.get(0).getNome());
        assertEquals("Joana Nutri", responseList.get(1).getNome());

        // Verifica se o repositório foi chamado
        verify(nutricionistaRepository, times(1)).findAll();
    }

    @Test
    void testSalvarNutricionista() {
        // Arrange: Criar o DTO de request e a entidade esperada
        NutricionistaRequestDTO requestDTO = new NutricionistaRequestDTO();
        requestDTO.setNome("Ana Nutri");
        requestDTO.setMatricula("54321");
        requestDTO.setTempoExperiencia(5);
        requestDTO.setCrn("CRN12345");
        requestDTO.setEspecialidade("Esportiva");

        Nutricionista nutricionistaSalvo = new Nutricionista();
        nutricionistaSalvo.setId(1L);
        nutricionistaSalvo.setNome("Ana Nutri");
        nutricionistaSalvo.setMatricula("54321");
        nutricionistaSalvo.setTempoExperiencia(5);
        nutricionistaSalvo.setCrn("CRN12345");
        nutricionistaSalvo.setEspecialidade("Esportiva");

        // Mocking: Quando findByNome é chamado, retorna Optional.empty() (não existe nome duplicado)
        when(nutricionistaRepository.findByNome(requestDTO.getNome())).thenReturn(Optional.empty());

        // Mocking: Quando save() é chamado, retorna a entidade simulada
        when(nutricionistaRepository.save(any(Nutricionista.class))).thenReturn(nutricionistaSalvo);

        // Act: Chamar o método salvarNutricionista
        NutricionistaResponseDTO responseDTO = nutricionistaService.salvarNutricionista(requestDTO);

        // Assert: Verificar se o resultado está correto
        assertEquals(1L, responseDTO.getId());
        assertEquals("Ana Nutri", responseDTO.getNome());

        // Verifica se os métodos do repositório foram chamados
        verify(nutricionistaRepository, times(1)).findByNome(requestDTO.getNome());
        verify(nutricionistaRepository, times(1)).save(any(Nutricionista.class));
    }
}
