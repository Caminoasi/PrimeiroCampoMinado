package visao;

import modelo.Tabuleiro;

public class Temp {

	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(3, 3, 9);
		tabuleiro.registrarObservador(e -> {
			if (e.isGanhou()) {
				System.out.println("Você ganhou!!!");
			} else {
				System.out.println("Você perdeu!");
			}
		});
		tabuleiro.marcar(0, 0);
		tabuleiro.marcar(0, 1);
		tabuleiro.marcar(0, 2);
		tabuleiro.marcar(1, 0);
		tabuleiro.marcar(1, 1);
		tabuleiro.marcar(1, 2);
		tabuleiro.marcar(2, 0);
		tabuleiro.marcar(2, 1);
		tabuleiro.marcar(2, 2);
	}
}
