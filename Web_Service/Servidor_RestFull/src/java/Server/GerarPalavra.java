/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author thiago
 */


public class GerarPalavra {
	
	// quantas palavras em italiano o jogo teras
	private final int num_palavras_file;
	
	private String palavra_italiano;
	private String palavra_portugues;
	
	private static Random rand = new Random();
	
	public GerarPalavra(){
		
		num_palavras_file = 90;
		
		// sorteia uma palavra dentre as 90
		int  index_random_palavra = rand.nextInt(90) + 1;
		// usado para chegar ate o index sorteado
		int count =0;
		
		Scanner scanner = null;
		
		// pega a palavra em intaliano
		try {
			scanner = new Scanner(new FileReader("/Users/thiago/Documents/Netbeans/Tradutor_Italiano_Web_Rest/src/java/Server/palavras_italiano.txt"))
			        .useDelimiter("\\||\\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (scanner.hasNext() && count < index_random_palavra) {
			palavra_italiano = scanner.next();
			//System.out.println(palavra);
			count++;
		}
		
		count = 0;
		// pega a palavra em portugues
		try {
			scanner = new Scanner(new FileReader("/Users/thiago/Documents/Netbeans/Tradutor_Italiano_Web_Rest/src/java/Server/palavras_portugues.txt"))
					 .useDelimiter("\\||\\n");
		} catch (FileNotFoundException e) {
					e.printStackTrace();
		}
				
		while (scanner.hasNext() && count < index_random_palavra) {
			palavra_portugues = scanner.next();
			//System.out.println(palavra);
			count++;
		}
		
	}	
	
	// imprime a palavra a ser traduzida pelos jogadores
	public String imprimirDesafio() {
	      return palavra_italiano;
	}
	
	public String getTraducao(){
		
		return palavra_portugues;
	}
	

}
