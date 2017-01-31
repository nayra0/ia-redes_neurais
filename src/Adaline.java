import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Adaline {

	public static void main(String[] args) {

		try {
			int qtdEntradas = 5;
			int qtdAmostras = 35;
			int qtdAmostrasOperacao = 15;
			double taxaAprendizagem = 0.0025;
			double precisao = 0.000001;

			double[][] amostras = new double[qtdAmostras][qtdEntradas + 1];
			double[][] amostrasOperacao = new double[qtdAmostrasOperacao][qtdEntradas];
			Scanner sc;
			sc = new Scanner(new File("src/Adaline - Tabela Treinamento.txt"));

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
				pesosFinais = treinamento(qtdEntradas, qtdAmostras, taxaAprendizagem, precisao, amostras);

				sc = new Scanner(new File("src/amostra-adaline.txt"));
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

	private static double[] treinamento(int qtdEntradas, int qtdAmostras, double taxaAprendizagem, double precisao,
			double[][] amostras) {
		int i;
		double[] pesos = new double[qtdEntradas];
		for (i = 0; i < qtdEntradas; i++) {
			pesos[i] = randomDouble(0, 1);
		}

		System.err.print(Arrays.toString(pesos));

		double eqmAnterior = 0, eqmAtual = 0;
		int epocas = 0;
		double combinadorLinear = 0;
		do {
			eqmAnterior = calcularEQM(amostras, qtdEntradas, qtdAmostras, pesos);
			for (i = 0; i < qtdAmostras; i++) {
				for (int j = 0; j < qtdEntradas; j++) {
					combinadorLinear = combinadorLinear + pesos[j] * amostras[i][j];
				}
				for (int j = 0; j < qtdEntradas; j++) {
					pesos[j] = pesos[j]
							+ taxaAprendizagem * (amostras[i][qtdEntradas] - combinadorLinear) * amostras[i][j];
				}
				combinadorLinear = 0;
			}
			eqmAtual = calcularEQM(amostras, qtdEntradas, qtdAmostras, pesos);
			epocas = epocas + 1;
		} while (Math.abs(eqmAtual - eqmAnterior) > precisao);

		System.out.println(Arrays.toString(pesos));
		System.out.println(epocas);
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

	public static double calcularEQM(double[][] amostras, int qtdEntradas, int qtdAmostras, double[] pesos) {
		double combinadorLinear = 0;
		double eqm = 0;
		for (int i = 0; i < qtdAmostras; i++) {
			for (int j = 0; j < qtdEntradas; j++) {
				combinadorLinear = combinadorLinear + pesos[j] * amostras[i][j];
			}
			eqm = eqm + Math.pow((amostras[i][qtdEntradas] - combinadorLinear), 2);
			combinadorLinear = 0;
		}
		eqm = eqm / qtdAmostras;
		return eqm;
	}

	private static double randomDouble(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

}
