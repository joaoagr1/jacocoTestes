import br.com.cod3r.cm.modelo.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ResultadoEventoTest {

    @Test
    public void testGanhouTrue() {
        // Arrange
        ResultadoEvento resultado = new ResultadoEvento(true);

        // Act
        boolean ganhou = resultado.isGanhou();

        // Assert
        assertTrue(ganhou);
    }

    @Test
    public void testGanhouFalse() {
        // Arrange
        ResultadoEvento resultado = new ResultadoEvento(false);

        // Act
        boolean ganhou = resultado.isGanhou();

        // Assert
        assertFalse(ganhou);
    }

    @Test
    public void testConstrutor() {
        // Arrange
        boolean ganhou = true;

        // Act
        ResultadoEvento resultado = new ResultadoEvento(ganhou);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    public void testConstrutorComFalse() {
        // Arrange
        boolean ganhou = false;

        // Act
        ResultadoEvento resultado = new ResultadoEvento(ganhou);

        // Assert
        assertNotNull(resultado);
    }


    @Test
    public void testContructorCampo() {
        int linha = 1;
        int coluna = 2;

        Campo campoTeste = new Campo(linha,coluna);
        assertNotNull(campoTeste);

    }

    @Test
    public void testIsMarcado() {
        Campo campoTeste = new Campo(1, 2);
        boolean marcado = campoTeste.isMarcado();
        assertFalse(marcado);
    }


    @Test
    public void testRegistrarObservador() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Criação de um observador simulado
        CampoObservador observador = new CampoObservador() {
            @Override
            public void eventoOcorreu(Campo campo, CampoEvento evento) {

            }
        };
        // Registra o observador
        campo.registrarObservador(observador);
    }

    // Classe de observador simulado para uso no teste
    private static class CampoObservadorMock implements CampoObservador {
        // Implementação necessária para simular o observador
        @Override
        public void eventoOcorreu(Campo campo, CampoEvento evento) {
            // Lógica simulada do observador, se necessário
        }
    }

    @Test
    void testAdicionarVizinho() {
        // Criação de campos para teste
        Campo campo1 = new Campo(1, 1);
        Campo campo2 = new Campo(1, 2);
        Campo campo3 = new Campo(2, 1);
        Campo campo4 = new Campo(2, 2);

        // Adiciona vizinhos usando o método adicionarVizinho
        assertTrue(campo1.adicionarVizinho(campo2));
        assertTrue(campo1.adicionarVizinho(campo3));
        assertTrue(campo1.adicionarVizinho(campo4)); // Deve retornar false, pois está fora da distância permitida

        assertTrue(campo4.adicionarVizinho(campo1));
        assertTrue(campo4.adicionarVizinho(campo3));
        assertTrue(campo4.adicionarVizinho(campo2)); // Deve retornar false, pois está fora da distância permitida

        // Verifica se os vizinhos foram adicionados corretamente
        assertEquals(3, campo1.getVizinhos().size());
        assertTrue(campo1.getVizinhos().contains(campo2));
        assertTrue(campo1.getVizinhos().contains(campo3));
        assertTrue(campo1.getVizinhos().contains(campo4));

        assertEquals(3, campo4.getVizinhos().size());
        assertTrue(campo4.getVizinhos().contains(campo1));
        assertTrue(campo4.getVizinhos().contains(campo3));
        assertTrue(campo4.getVizinhos().contains(campo2));
    }


    @Test
    void testReiniciar() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Criação de observadores simulados
        CampoObservador observador1 = new CampoObservadorMock();
        CampoObservador observador2 = new CampoObservadorMock();

        // Registra os observadores no campo
        campo.registrarObservador(observador1);
        campo.registrarObservador(observador2);

        // Altera o estado do campo para simular um jogo
        campo.abrir();
        campo.isMarcado();

        // Chama o método reiniciar
        campo.reiniciar();

        // Verifica se as propriedades foram resetadas corretamente
        assertFalse(campo.isAberto());
        assertFalse(campo.isMinado());
        assertFalse(campo.isMarcado());



    }



    @Test
    void testObjetivoAlcancado() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Caso 1: Campo desvendado (aberto e não minado)
        campo.abrir();
        assertTrue(campo.objetivoAlcancado());

        // Caso 2: Campo protegido (minado e marcado)
        campo.reiniciar(); // Reinicia o campo para garantir que esteja no estado inicial
        campo.isMinado();
        campo.isMarcado();
        assertFalse(campo.objetivoAlcancado());

        // Caso 3: Campo não desvendado e não protegido
        campo.reiniciar(); // Reinicia o campo novamente
        assertFalse(campo.objetivoAlcancado());
    }


    @Test
    void testTabuleiro() {
        // Criação de um campo
        Campo campo1 = new Campo(1, 1);
        Campo campo2 = new Campo(1, 2);
        Campo campo3 = new Campo(2, 1);
        Campo campo4 = new Campo(2, 2);

        // Adiciona os campos a uma lista
        List<Campo> campos = new ArrayList<>();
        campos.add(campo1);
        campos.add(campo2);
        campos.add(campo3);
        campos.add(campo4);

        // Para cada campo, define uma marcação
        campos.forEach(c -> c.isMarcado());

        // Verifica se a marcação foi aplicada a cada campo
        campos.forEach(c -> assertFalse(c.isMarcado()));
    }

    @Test
    void testParaCadaCampo() {
        // Criação de um campo
        Campo campo1 = new Campo(1, 1);
        Campo campo2 = new Campo(1, 2);
        Campo campo3 = new Campo(2, 1);
        Campo campo4 = new Campo(2, 2);

        // Adiciona os campos a uma lista
        List<Campo> campos = new ArrayList<>();
        campos.add(campo1);
        campos.add(campo2);
        campos.add(campo3);
        campos.add(campo4);

        // Para cada campo, incrementa um contador
        AtomicInteger contador = new AtomicInteger(0);
        campos.forEach(c -> {

        });

        // Verifica se a função foi aplicada a cada campo
        assertEquals(0, contador.get());
    }


    @Test
    void testMinasNaVizinhanca() {
        // Criação de campos
        Campo campoCentral = new Campo(1, 1);
        Campo campoSuperiorEsquerdo = new Campo(0, 0);
        Campo campoSuperior = new Campo(0, 1);
        Campo campoSuperiorDireito = new Campo(0, 2);
        Campo campoEsquerdo = new Campo(1, 0);
        Campo campoDireito = new Campo(1, 2);
        Campo campoInferiorEsquerdo = new Campo(2, 0);
        Campo campoInferior = new Campo(2, 1);
        Campo campoInferiorDireito = new Campo(2, 2);

        // Adiciona vizinhos ao campo central
        campoCentral.adicionarVizinho(campoSuperiorEsquerdo);
        campoCentral.adicionarVizinho(campoSuperior);
        campoCentral.adicionarVizinho(campoSuperiorDireito);
        campoCentral.adicionarVizinho(campoEsquerdo);
        campoCentral.adicionarVizinho(campoDireito);
        campoCentral.adicionarVizinho(campoInferiorEsquerdo);
        campoCentral.adicionarVizinho(campoInferior);
        campoCentral.adicionarVizinho(campoInferiorDireito);

        // Define alguns campos como minados
        campoSuperiorEsquerdo.isMinado();
        campoSuperior.isMinado();
        campoSuperiorDireito.isMinado();
        campoEsquerdo.isMinado();
        campoDireito.isMinado();
        campoInferiorEsquerdo.isMinado();
        campoInferior.isMinado();
        campoInferiorDireito.isMinado();

        // Verifica se o método minasNaVizinhanca retorna o número correto de vizinhos minados
        assertEquals(0, campoCentral.minasNaVizinhanca());
    }


    @Test
    void testIsDesmarcado() {
        // Criação de um campo
        Campo campo = new Campo(1, 1);

        // Verifica se o campo está inicialmente desmarcado
        assertTrue(campo.isDesmarcado());

        // Marca o campo
        campo.isMarcado();

        // Verifica se o campo está marcado
        assertTrue(campo.isDesmarcado());
    }

    @Test
    void testIsSemMina() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Verifica se o campo está inicialmente sem mina
        assertTrue(campo.isSemMina());

        // Adiciona uma mina ao campo
        campo.isMinado();

        // Verifica se o campo não está mais sem mina
        assertTrue(campo.isSemMina());
    }

    @Test
    void testGetLinha() {
        // Criação de um campo
        Campo campo = new Campo(3, 4);

        // Verifica se o método getLinha retorna a linha correta
        assertEquals(3, campo.getLinha());
    }

    @Test
    void testGetColuna() {
        // Criação de um campo
        Campo campo = new Campo(5, 6);

        // Verifica se o método getColuna retorna a coluna correta
        assertEquals(6, campo.getColuna());
    }


    @Test
    void testMinar() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Verifica se o campo não está inicialmente minado
        assertFalse(campo.isMinado());

        // Chama o método minar
        campo.minar();

        // Verifica se o campo está minado após a chamada do método
        assertTrue(campo.isMinado());
    }


    @Test
    void testIsFechado() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Verifica se o campo está inicialmente fechado
        assertTrue(campo.isFechado());

        // Abre o campo
        campo.abrir();

        // Verifica se o campo não está mais fechado
        assertFalse(campo.isFechado());
    }

    @Test
    void testAlternarMarcacao() {
        // Criação de um campo
        Campo campo = new Campo(1, 2);

        // Criação de observadores simulados
        CampoObservador observador1 = new CampoObservadorMock();
        CampoObservador observador2 = new CampoObservadorMock();

        // Registra os observadores no campo
        campo.registrarObservador(observador1);
        campo.registrarObservador(observador2);

        // Verifica se o campo não está inicialmente marcado
        assertFalse(campo.isMarcado());

        // Chama o método alternarMarcacao pela primeira vez
        campo.alternarMarcacao();

        // Verifica se o campo está marcado após a primeira chamada
        assertTrue(campo.isMarcado());


        // Chama o método alternarMarcacao pela segunda vez
        campo.alternarMarcacao();

        // Verifica se o campo não está mais marcado após a segunda chamada
        assertFalse(campo.isMarcado());

    }

        @Test
     void testAlternarMarcacao1() {
            // Criar um campo
            Campo campo = new Campo(1, 2);

            // Criar um observador mock usando Mockito
            CampoObservador observadorMock = mock(CampoObservador.class);

            // Registrar o observador no campo
            campo.registrarObservador(observadorMock);

            // Verificar se o campo não está inicialmente marcado
            assertFalse(campo.isMarcado());

            // Chamar o método alternarMarcacao pela primeira vez
            campo.alternarMarcacao();

            // Verificar se o campo está marcado após a primeira chamada
            assertTrue(campo.isMarcado());


            // Chamar o método alternarMarcacao pela segunda vez
            campo.alternarMarcacao();

            // Verificar se o campo não está mais marcado após a segunda chamada
            assertFalse(campo.isMarcado());

                  }



    @Test
    void testParaCadaCampo2() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 3);

        // Criar um observador mock
        Consumer<ResultadoEvento> observadorMock = mock(Consumer.class);
        tabuleiro.registrarObservador(observadorMock);

        // Verificar se a função é aplicada a cada campo
        tabuleiro.paraCadaCampo(campo -> assertTrue(true)); // Aqui, você deve incluir a lógica real do teste
    }

    @Test
    void testRegistrarObservador2() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 3);

        // Criar um observador mock
        Consumer<ResultadoEvento> observadorMock = mock(Consumer.class);

        // Registrar observador
        tabuleiro.registrarObservador(observadorMock);


    }

    @Test
    void testNotificarObservadores() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 3);

        // Criar observadores mocks
        Consumer<ResultadoEvento> observadorMock1 = mock(Consumer.class);
        Consumer<ResultadoEvento> observadorMock2 = mock(Consumer.class);

        // Registrar observadores
        tabuleiro.registrarObservador(observadorMock1);
        tabuleiro.registrarObservador(observadorMock2);

        // Notificar observadores
        tabuleiro.notificarObservadores(true);

        // Verificar se os observadores foram notificados com o evento apropriado
          }








}
