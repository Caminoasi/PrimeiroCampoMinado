package modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int linha;
	private final int coluna;

	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	private boolean minado = false;
	private boolean aberto = false;
	private boolean marcado = false;
	private List<Campo> vizinhos = new ArrayList<>();
	// Existem estas duas formas
	private List<CampoObservador> observadores = new ArrayList<>();
//	private List<BiConsumer<Campo, CampoEvento>> observadores2 = new ArrayList<>();

	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}

	private void notificarObservador(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}

	boolean adicionarVizinho(Campo candidatoVizinho) {
		boolean linhaDif = linha != candidatoVizinho.linha;
		boolean colunaDif = coluna != candidatoVizinho.coluna;
		boolean diagonal = linhaDif && colunaDif;

		int deltaLinha = Math.abs(linha - candidatoVizinho.linha);
		int deltaColuna = Math.abs(coluna - candidatoVizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		// Inserção dos vizinhos
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else {
			return false;
		}

	}

	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			if (marcado) {
				notificarObservador(CampoEvento.MARCAR);
			} else {
				notificarObservador(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrir() {
		if (!aberto && !marcado) {
			if (minado) {
				notificarObservador(CampoEvento.EXPLODIR);
				return true;
			}
			setAberto(true);

			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	void minar() {
		minado = true;
	}

	public boolean isMinado() {
		return minado;
	}

	public boolean isNaoMinado() {
		return !minado;
	}

	public boolean isMarcado() {
		return marcado;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if (aberto) {
			notificarObservador(CampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}

	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservador(CampoEvento.REINICIAR);
	}
}
