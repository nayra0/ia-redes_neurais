import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {

	public static void main(String[] args) {

		try {
			int qtdEntradas = 4;
			int qtdAmostras = 30;
			int qtdAmostrasOperacao = 10;
			double taxaAprendizagem = 0.01;

			double[][] amostras = new double[qtdAmostras][qtdEntradas + 1];
			double[][] amostrasOperacao = new double[qtdAmostrasOperacao][qtdEntradas];
			Scanner sc;
			sc = new Scanner(new File("src/Perceptron - Tabela Treinamento.txt"));

			// Leitura das amostras e adição de pesos aleatórios
			int i = 0;
			while (sc.hasNext()) {
				amostras[i][0] = -1;
				for (int j = 1; j <= qtdEntradas; j++) {
					amostras[i][j] = sc.nextDouble();
				}
				i++;
			}
			sc.close();

			double[] pesosFinais = null;
			for (int j = 0; j < 5; j++) {
				pesosFinais = treinamento(qtdEntradas, qtdAmostras, taxaAprendizagem, amostras);

				sc = new Scanner(new File("src/amostra-perceptron.txt"));
				i = 0;

				while (sc.hasNext()) {
					amostrasOperacao[i][0] = -1;
					for (int x = 1; x < qtdEntradas; x++) {
						amostrasOperacao[i][x] = sc.nextDouble();
					}
					i++;
				}
				sc.close();
				operacao(qtdEntradas, qtdAmostrasOperacao, amostrasOperacao, pesosFinais);
				System.out.println();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static double[] treinamento(int qtdEntradas, int qtdAmostras, double taxaAprendizagem,
			double[][] amostras) {
		int i;
		double[] pesos = new double[qtdEntradas];
		for (i = 0; i < qtdEntradas; i++) {
			pesos[i] = randomDouble(0, 1);
		}

		System.err.print(Arrays.toString(pesos));

		int epocas = 0;
		int acertos = 0;
		do {
			acertos = 0;
			epocas++;
			for (i = 0; i < qtdAmostras; i++) {
				double combinadorLinear = 0;
				for (int j = 0; j < qtdEntradas; j++) {
					combinadorLinear += amostras[i][j] * pesos[j];
				}

				// Função degrau
				combinadorLinear = combinadorLinear >= 0 ? 1 : -1;

				if (amostras[i][qtdEntradas] != combinadorLinear) {
					for (int j = 0; j < qtdEntradas; j++) {
						pesos[j] = pesos[j]
								+ taxaAprendizagem * (amostras[i][qtdEntradas] - combinadorLinear) * amostras[i][j];
					}
				} else {
					acertos++;
				}
			}
		} while (acertos != qtdAmostras);

		System.err.print(Arrays.toString(pesos));
		System.err.println(epocas);
		return pesos;
	}

	private static void operacao(int qtdEntradas, int qtdAmostras, double[][] amostras, double[] pesos) {
		for (int i = 0; i < qtdAmostras; i++) {
			double combinadorLinear = 0;
			for (int j = 0; j < qtdEntradas; j++) {
				combinadorLinear += amostras[i][j] * pesos[j];
			}
			// Função degrau
			combinadorLinear = combinadorLinear >= 0 ? 1 : -1;
			System.out.print(combinadorLinear + " ");
		}
	}

	private static double randomDouble(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

}