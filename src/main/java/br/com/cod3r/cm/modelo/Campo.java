package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private final int linha;
	private final int coluna;

	public List<Campo> getVizinhos() {
		return vizinhos;
	}

	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	 
	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream()
			.forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isDesmarcado() {
		return !isMarcado();
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isSemMina() {
		return !isMinado();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	//M�todo para mostrar os vizinhos que n�o tem bomba em volta do campo que foi selecionado.
	public boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = linha != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);	
			return true;
		}else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		}else {
			return false;
		}
	}	
		
	//Marcar & Desmarcar determinado campo.
	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			}else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}	
		}
	}
	
	/*
	 * Se campo n�o estiver aberto e marcado, o campo ent�o � aberto. 
	 * Se estiver minado o Jogo � encerrado. 
	 * Se a vizinha�a do campo for segura, isso � n�o tem minas nos campos pr�ximos, 
	 * ent�o esses campos s�o abertos.
	 */
	public boolean abrir() {
		if(!aberto && !marcado) {
			if(minado) {
				System.out.print("oi");
			}

			setAberto(true);
			notificarObservadores(CampoEvento.ABRIR);

			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}

			return true;

		}else return false;
	}

	//Verificar se os campos vizinhos s�o seguros para se abrir.
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	//Minar campo passado.
	public void minar() {
		minado = true;
	}

	//Verificar se determinado campo foi Desvendado ou est� protegido.
	public boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	//Contas quantas minas tem em volta da jogada feita.
	public int minasNaVizinhanca() {
		return (int)vizinhos.stream().filter(v -> v.minado).count();
	}

	//Reiniciar campos do jogo.
	public void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}







}
