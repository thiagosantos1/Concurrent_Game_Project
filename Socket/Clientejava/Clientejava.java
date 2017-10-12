package clientejava;



import java.io.BufferedReader;

import java.io.DataOutputStream;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintStream;



import java.io.PrintWriter;



import java.net.Socket;



import java.net.UnknownHostException;



import java.util.Scanner;











  public class Clientejava implements Runnable{



	



    private Socket cliente;	 

    

    private BufferedReader receptorServidor; 

    

    private PrintStream mandarParaServidor;

    

    private Scanner msg; 



    public Clientejava(Socket cliente){



    	this.cliente = cliente;

    	

    	open();



	 }



    

    public void open() {

    	

    	msg = new Scanner(System.in); 

    	

    	try{

    		

    		receptorServidor = new BufferedReader(

    	                new InputStreamReader(

    	                cliente.getInputStream()

    	             )

    	         );

    		

    		

    		mandarParaServidor = new PrintStream(cliente.getOutputStream());

     

    	    }catch(Exception e){

    	    	

    	    	e.printStackTrace();

    	    	



    	    }

    	

    	

    }

    

    public void close(){

    	

    	try{

    		

    		mandarParaServidor.close();



    	    receptorServidor.close();



    		this.cliente.close(); //Fechando a thread

    		

    	}catch (Exception e){

    		

    		e.printStackTrace();

    		

    	}

    	

    	

    }

	

    @Override

    public void run(){



	try {		



	      String textoServidor;

	      String arrayTextoServidor[] = new String[10];

	     

          String enviarParaServidor; 

          

          textoServidor = receptorServidor.readLine();

          arrayTextoServidor = textoServidor.split(" ");

          textoServidor = arrayTextoServidor[0];

          



          while(textoServidor != null){

        	  

       

	         if(textoServidor.equals("/recusado_iniciou")){ // conexao recusa, jogo ja iniciado

	        	 

	        	 System.out.println("\n**********************************************************\n");

	        	 System.out.println("Conexao recusada! Jogo ja iniciado. Aguarde o proximo\n");

	        	 System.out.println("**********************************************************\n\n");

	        	 close();

                     	                     		        	    

			 }

	         

	         if(textoServidor.equals("/recusado_full")){ // conexao recusa, capaxidade maxima de jogadores atingidas

	        	 

	        	 System.out.println("\n*******************************************************************\n");

	        	 System.out.println("Conexao recusada! Jogo ja possui 5 jogadores, Aguarde o proximo\n");

	        	 System.out.println("*******************************************************************\n\n");

	        	 close();

	         }

	         

	         if(textoServidor.equals("/conectado")) { // conectado com exito - msg d boas vindas

	        	 

	        	 System.out.println("\n******************************************************************\n"); 

	        	 System.out.println("\t     Bem vindo ao Tradutor Italino\n");

	        	 System.out.println("\t     Numero de jogadores necessarios: 2 a 5\n");

	        	 System.out.println("\n******************************************************************\n");

	

	         }

	         

	         if(textoServidor.equals("/nome")){ // receber nome do jogador

	        	 

	        	 System.out.print("Digite seu nome :");

		

	        	 enviarParaServidor=msg.nextLine();

	        	 

	        	 mandarParaServidor.println(enviarParaServidor);

	        	  	 

	         }

	         

	         if(textoServidor.equals("/faltanto_jogadores")){ // necessario minimo 2 jogadores

	        	 

	        	 System.out.println("\nO jogo nao pode ser ainda inicializado, e necessario no minimo 2 e\n"+

				 			"no maximo 5 jogadores.Aguarde os proximos jogadores por favor\n"+

				 		   "\n******************************************************************\n" +

				 		  "\t\tMenu Principal" +

                        "\nDigite '/players' para listar os jogadores conectados ao jogo,\n" +

                        "'/regras' para listar as regras do jogo ou,\n" +

                        "'/desistir' para desistir do jogo\n"+

		 					"******************************************************************\n");

	        	 

	         }

	         

	         if(textoServidor.equals("/jogadores_ready")){ 

	        	 

	        	 System.out.println("\nO jogo ainda nao comecou, porem ja se tem no minimo 2 jogadores conectados\n"+

        				 "\n******************************************************************\n" +

        				 "\t\tMenu Principal" +

        				 "\nDigite '/iniciar' para comecar, \n"+

        				 "/players' para listar os jogadores, \n" +

        				 "'/regras' para listar as regras do jogo ou,\n" +

        				 "'/desistir' para desistir do jogo\n"+

        				 "******************************************************************\n");

	        	 

	         }

	         

	         // cliente deve escolher uma opcao

	         if(textoServidor.equals("/opcao")){ 

	        	 

	        	 System.out.print("Digite opcao: ");

	        	 enviarParaServidor=msg.nextLine();

	        	 

	        	 mandarParaServidor.println(enviarParaServidor);

	        	 

	         }

	         

	         if(textoServidor.equals("/players")){ // cliente deve informar q iras mostrar todos os jogadores conectados

	        	 

	        	 System.out.println("\n**********************************");

	        	 System.out.println("\tJogadores Conectados");

	        	 System.out.println("**********************************");

	        	 

	         }

                 



	         if(textoServidor.equals("/jogadores")){ // tag para cada jogador, a ser imprimido 

	        	 

	        	 System.out.println(arrayTextoServidor[1]); // nome do jogador enviado, e separado por ' " " ' no split

	        	 

	         }

                 

	         

	         if(textoServidor.equals("/regras")){ // 

	        	 

	        	 System.out.println("\n**********************************************************************************");

	        	 System.out.println("O jogo tem uma duracao de 3 minutos, e voce começa com 25 pontos.\n"

	        	 					+ "Para cada palavra acertada, voce ganha 1 ponto, e mais 1 ponto de cada um dos outros\n"

	        	 					+ "jogadores. A cada erro, voce perde 1 ponto. Caso sua pontuação chege a 0, voce\n"

	        	 					+ "é eliminado do jogo. No final, ganha quem fizer mais pontos.");

	        	 System.out.println("**********************************************************************************\n");

	            	 

	        	 

	         }

	         

	         if(textoServidor.equals("/jogo_iniciado")){ // 

	        	 

	        	 System.out.println("\nO jogo Tradutor começou!");

	        	 System.out.println("\tBoa Sorte!\n");

	        	 

	         }



	         

	         if(textoServidor.equals("/desistir")){ // 

	        	 

	        	 System.out.println("\n*****************************************");

	        	 System.out.println("\tObrigado pela sua participação\n\tJogo Desconectado");

	        	 System.out.println("*****************************************");

	        	 close();

	        	 

	         }

	         

	         

	         

	         if(textoServidor.equals("/invalido")){ // usuario digitou opcao errada

	        	 

	        	 System.out.println("\n******************************");

	        	 System.out.println("\tOpcao invalida");

	        	 System.out.println("******************************");

	        	 

	         }

	         

	         if(textoServidor.equals("/desafio")){

	        	 

	        	 System.out.println("\n" + arrayTextoServidor[1] + " Significa ?"); // palavra a ser traduzida

	        	 

	         }

	         

	         if(textoServidor.equals("/pontuacao")){

	        	 

	        	 System.out.println("Pontuação: " + arrayTextoServidor[1]);

	        	 

	         }

	         

	         if(textoServidor.equals("/menuJogo")){

	        	 

	        	 System.out.println("\n**********************************************************");

	        	 System.out.println("\tEscolha umas das opcões abaixo");

	        	 System.out.println("**********************************************************");

	        	 System.out.println("/responder");

	        	 System.out.println("/placar");

	        	 System.out.println("/players");

	        	 System.out.println("/desistir\n");

	        	 

	         }

	         

	         if(textoServidor.equals("/responder")){ // cliente escolheu respoder a questao

	        	 

	        	 System.out.print("Resposta: ");

	        	 

	        	 enviarParaServidor=msg.nextLine();

	        	 

	        	 mandarParaServidor.println(enviarParaServidor);

	        	 

	        	 

	        	 

	         }

	         

	         if(textoServidor.equals("/acertou")){ // cliente acertou a pergunta

	        	 

	        	 System.out.println("\n***************************\n     Acertou, Braaavo!!!!\n***************************\n");

	        	 

	         }

                 

                 if(textoServidor.equals("/placar")){ 

                          

	        	 System.out.println("Placar do jogo : \n" +arrayTextoServidor[1]+ "-  " +arrayTextoServidor[2]); 

                          

                         

	         }

	         

	         if(textoServidor.equals("/errou")){

	        	 

	        	 System.out.println("\nerrou, essa nao e a traducao correta!\n");

	        	 

	         }

	         

	         if(textoServidor.equals("/fim_rodada")){ // a rodada atual acabou, alguem acertou a palavra

	        	 

	        	 System.out.println("A palavra "+ arrayTextoServidor[1] + " foi traduzida"+

						"\n    Vamos para a proxima!!!");

	         }

	         

	         if(textoServidor.equals("/ganhou")){

	        	 

	        	 System.out.println("\n**********************************************************");

	        	 System.out.println("\tParabéns, você foi o ganhador!!!");

	        	 System.out.println("**********************************************************");

	        	 

	         }

	         

	         if(textoServidor.equals("/game_over")){

	        	 

	        	 System.out.println("\n****************************************************************");

	        	 System.out.println("Fim de jogo, o jogador " + arrayTextoServidor[1] + " Ganho o jogo!!!!" );

	        	 System.out.println("****************************************************************");

	        	 

	         }

	         

	         if(textoServidor.equals("/perdeu")){

	        	 

	        	 System.out.println("\n**********************************************************");

	        	 System.out.println("\tGame Over, você foi eliminado!!!");

	        	 System.out.println("**********************************************************");

	        	 

	         }

	         

	         if(textoServidor.equals("/jogador_eliminado")){

	        	 

	        	 System.out.println("\n**********************************************************");

	        	 System.out.println("O jogador " + arrayTextoServidor[1] + " foi eliminado!!!");

	        	 System.out.println("**********************************************************");

	        	 

	         }

	



	        textoServidor = receptorServidor.readLine();

	        arrayTextoServidor = textoServidor.split(" ");

	        textoServidor = arrayTextoServidor[0];

	  

        }	



        close(); // fechar conexoes e threads abertas	



		} catch (Exception e) {



			// game over

			

			

			//System.out.println("Erro inesperado");

			//e.printStackTrace();



		}		



		



	}



	



    public static void main(String[] args) throws UnknownHostException,IOException {

	



    Socket cliente =  new Socket("localhost", 6789);  //Criando socket cliente



	Clientejava c = new Clientejava(cliente);



	Thread th = new Thread(c);



	th.start();



	}







}
