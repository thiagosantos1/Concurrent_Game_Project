
package clientejava;


/**
 *
 * @author Maycon
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import wscliente.JogadorTradutor;
import wscliente.JogoTradutorItaliano;
import wscliente.JogoTradutorItalianoService;


public class Clientejava extends Thread  {
	
	 String nome;
	 static JogoTradutorItaliano server;
	 static BufferedReader reader;
	 static boolean jogoIniciado = false;
	 static boolean gameOver = false;
	 static int idJogador;

	 public Clientejava() {
	 }
	 
	 public static void main(String[] args) {
		 
		 // conexao com o servidor estabelecida
		 JogoTradutorItalianoService entrada = new JogoTradutorItalianoService();
		 server = entrada.getPort(JogoTradutorItaliano.class);
		 
		 JogadorTradutor jogador = new JogadorTradutor();  // objeto jogador
		 String comandoJogador=""; // recebe as opcoes escolhidas pelo jogador
		 
		 //msg de boas vindas
		 System.out.println("***********************************************************************");
		 System.out.println("\tSeja Bem Vindo(a) ao jogo Tradutor Italiano");
		 System.out.println("***********************************************************************");
		 System.out.print("Qual o seu nome: ");
		 
		 comandoJogador = userInput();
		 jogador.setNome(comandoJogador);
		 
		 // setar ID unico para o jogador
		 idJogador = server.setIdJogador();
		 jogador.setIdJogador(idJogador);
		 jogoIniciado = false;
		 gameOver = false;
		 
		 
		 
		 // cadastrar usuario no servidor, se aplicável
		 if (server.getStatus() == 0 && server.getQtdJogadores()<5 ) { // jogo ainda nao inicializado e qtd jogadores for menor q 5
			 server.registrar(jogador);
			
			 
	     } else{
	    	 if(server.getStatus() != 0)
	    		 System.out.println("Jogo ja iniciado.Aguarde iniciar uma nova partida para poder entrar no jogo.");
	    	 else
	    		 System.out.println("Jogo ja possui 5 jogadores.Aguarde iniciar uma nova partida para poder entrar no jogo.");
	    	 
	         while (server.getStatus() != 0 && server.getQtdJogadores()>=5); // espera ate o status do jogo mudar e tbm tiver vaga
	         																  // para entao iniciar uma nova partida
	         if (server.getQtdJogadores()== 0) {
	             System.out.println("Novo jogo");
	         } else {
	             System.out.println("Nova partida encontrada.");
	         }
	         
	         server.registrar(jogador);
	     }
	     
		// disparar o inicio da Thread
		new Clientejava().start();
		 
	 }
	 
	 public void run() {
		 
		 String comandoJogador, respostaJogador;
		 boolean esperaAdmin;
		 boolean esperaJogador;
		 boolean gameOn = true, userInputModeOn = true;
		 boolean acertouDesafio = false, responderDesafio = false;
		 while (gameOn){
        	 
			 comandoJogador = "";
			 esperaAdmin = false;
			 esperaJogador = false;
			 
			 /*try {
				Thread.sleep(1000 * 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // espera um pouco, para nao mandar toda hora a mesma coisa
             */
             if (server.getStatus() == 0) {
                 if (server.adminJogo() == idJogador && server.getQtdJogadores() > 1) { // se jogador for o admin
                     System.out.println(gameMaster());
                     System.out.print("Opcao: ");
                     comandoJogador= userInput();
                   
                     while (!comandoJogador.equals("/c") && gameOn) { // enquando admin n começar o jogo
                         if (comandoJogador.equals("/p")) {
                             System.out.println(server.getPlacarGeral());
                         } 
                         else if(comandoJogador.equals("/i")) {
                             System.out.println(server.getPlacarIndividual(idJogador));
                         }
                         	else if(comandoJogador.equals("/j")) {
                         		System.out.println(server.listarJogadores());
                         	}
                         		else if(comandoJogador.equals("/l")) {
                         			System.out.println(server.getRegras());
                         		}
                         			else if(comandoJogador.equals("/d")) {
                         				server.desistirJogo(idJogador);
                         				gameOn = false;
                         				System.out.println("Obrigado pela participacao");
                         			}
                         			
                         
                         			
                         				else{
                         					System.out.println("Opcao invalida");
                         				}
                         if(gameOn){
	                         System.out.println(gameMaster());
	                         System.out.print("Opcaoo: ");
	                         comandoJogador = userInput();
                         }
                     }
                     
                    
                     
                    
                   
                    
                     if(gameOn){
                    	 server.setStatus(1);
                    	 jogoIniciado = true;
	                     System.out.println("***********************************************************************");
	                     System.out.println("\tJogo Iniciado com Sucesso\n\tBoa sorte");
	                     System.out.println("***********************************************************************");
                     }
                   
                  
                     
                    	 
                     
             
                     }
                 else if (server.adminJogo() == idJogador && server.getQtdJogadores() == 1){
                	 System.out.println("Aguardando mais jogadores se conectarem ao jogo...");
                	 
                	 esperaJogador = true;
                	 while (esperaJogador){
                		 if (server.getStatus() !=0){
                			 esperaJogador = false;
                		 }
                		 if (server.getQtdJogadores() > 1){
                			 esperaJogador = false;
                		 }
                		
                		 if(!esperaJogador ){
                			 System.out.println("***********************************************************************");
                             System.out.println("\tJogo Iniciado com Sucesso\n\tBoa sorte");
                             System.out.println("***********************************************************************"); 
                	}
                 }
               }
                 
                 else {
                	 
                     System.out.println("Aguardando o administrador iniciar o jogo.");
                     
                 
                     
                     // esperar admin começar o jogo
                     esperaAdmin = true;
                     while (esperaAdmin){
                         if(server.getStatus() != 0){
                        	 esperaAdmin = false;
                         }
                         if(server.adminJogo() == idJogador){
                        	 esperaAdmin = false;
                         }
                         
                         if(!esperaAdmin ){
                        	 System.out.println("***********************************************************************");
                             System.out.println("\tJogo Iniciado com Sucesso\n\tBoa sorte");
                             System.out.println("***********************************************************************");
                         }
                         
                        
                         
                     }
                         
                 }
               
             }
                 
             
       
             
             if (server.getStatus() == 1) {
            	 server.setNewDesafioItaliano(idJogador); // lançar um novo desafio para o jogador
            	 
            	 acertouDesafio = false;
            	 
            	 while(!acertouDesafio && gameOn){
            		 
            		 // coletar resposta do jogador ou outra ação
            		 userInputModeOn = true;
            		 respostaJogador = "";
            		 responderDesafio = false;
            		 while(userInputModeOn){
            			 userInputModeOn = false;
	            		 System.out.println("\nDesafio Italiano: " + server.lancarDesafio(idJogador) );
	            		 System.out.println("***********************************************************************");
	            		 System.out.println("\t\tOpcoes\n");
	            		 System.out.println("***********************************************************************");
	            		 System.out.println(gamePlayer() + '\n' + "'/r' para responder desafio");
	            		 
	            		 System.out.print("Opcao: ");
	                     comandoJogador= userInput();
	                     
	            		 if (comandoJogador.equals("/p")) {
	                         System.out.println('\n'+server.getPlacarGeral());
	                     } 
	                     else if(comandoJogador.equals("/i")) {
	                         System.out.println('\n'+server.getPlacarIndividual(idJogador));
	                     }
	                     	else if(comandoJogador.equals("/j")) {
	                     		System.out.println('\n'+server.listarJogadores());
	                     	}
	                     		else if(comandoJogador.equals("/l")) {
	                     			System.out.println('\n'+server.getRegras());
	                     		}
	                     			else if(comandoJogador.equals("/d")) {
	                     				server.desistirJogo(idJogador);
	                     				gameOn = false;
	                     				if(server.getQtdJogadores() == 0){
	                     					server.setStatus(0);
	                     					server.resetAdmin();
	                     				}
	                     				System.out.println("Obrigado pela participacao");
	                     			}
	                     
	                     			else if(comandoJogador.equals("/r")){
	                     				System.out.println("\n***********************************************************************");
	                     				System.out.println('\t' +server.lancarDesafio(idJogador) + " Significa ? ");
	                     				System.out.println("***********************************************************************");
	                     				System.out.print("Resposta: ");
	                     				respostaJogador = userInput();
	                     				responderDesafio = true;
	                     				
	                     			}
	            		 
	                     			
	                     				else{
	                     					System.out.println("Opcao invalida");
	                     					userInputModeOn = true;
	                     				}
            		 }
            		 
            		 // checar resposta do usuario
            		 if(gameOn && responderDesafio){ // se jogador nao optou por desistir do jogo
            			 
            			 // acertou desafio
            			 // checar, apenas se a pontuacao do mesmo for maior q 0
            			 if( ( !server.jogadorEliminado(idJogador) && !server.gameOver())
                			     && server.checarResposta(respostaJogador, idJogador) == 1){
    	            				 
    	            				 System.out.println("\nParabéns, você acertou");
    	            				 System.out.println(server.getPlacarIndividual(idJogador));
    	            				 acertouDesafio = true;
    	            	    }
            			 
            			 // jogador ja eliminado
            			 else if( server.jogadorEliminado(idJogador)){
            				 
            				 System.out.println(server.getPlacarIndividual(idJogador));
            				 System.out.println("\nDesculpe, voce nao pode responder mais\nVoce foi eliminado do jogo!!!!");
        					 gameOn = false;
        					 server.removerJogador(idJogador);
            				 
            			 }
            			 
            			 // jogo ja encerrado
            			 else if( server.gameOver()){
            				 
            				 System.out.println(server.getPlacarIndividual(idJogador));
            				 System.out.println("\nDesculpe, voce nao pode responder mais\nJogo ja encerrado!!!!");
        					 gameOn = false;
            				 
            			 }
            			 
            			 // errou desafio
            			 else if(server.checarResposta(respostaJogador, idJogador) == 0){
            				 
            				 System.out.println("\nDesculpe, essa nao é a resposta correta!!!");
            				 System.out.println(server.getPlacarIndividual(idJogador));
            				 
            				 if(server.jogadorEliminado(idJogador)){
            					 System.out.println(server.getPlacarIndividual(idJogador));
            					 System.out.println("\nVoce foi eliminado do jogo!!!!");
            					 gameOn = false;
            					 server.removerJogador(idJogador);
            				 }
            			 }
            		 
            		 
            		 }
            		 
            		 // jogo acabou?
            		 if(server.gameOver()){
            			 
            			 System.out.println("\n***********************************************************************");
            			 System.out.println("\t\t|Fim de Jogo|");
            			 System.out.println("\tDados Jogador Vencedor");
            			 System.out.println("Nome\tPontuacao Final");
            			 System.out.println(server.getJogadorVencedor());
            			 System.out.println("\n***********************************************************************");
            			 gameOn = false;
            			 
            			 if(idJogador == server.getIdGanhador() && server.getIdGanhador() != -1){
            				 System.out.println("\n\tParabens, voce foi o ganhador");
            				 server.resetGame(); // apenas o ganhor pode resetar o jogo, para proximos jogadores
            			 }
            		 }
            	 }
                 
             }
		 }
		 
	 }
	 
	
	 public String gameMaster() {
		 String msg = "";
		 msg += "\n*******************************************************************\n";
	     msg += "\tVoce é o administrador do jogo\n";
	     
	    
	     msg += gamePlayer(); // a master game tbm é um game player
	     msg += "*******************************************************************\n";
	     msg +="\tOutras opcoes\n";
		 msg += "'/c' para comecar o jogo\n";
		     
	     
	     return msg;
	 }
	 
	 public String gamePlayer() {
		 
		 String msg = "";
	     msg += "Digite:\n'/p' para verificar o placar geral do jogo\n";
	     msg += "'/i' para verificar o seu placar individual do jogo\n";
	     msg += "'/j' para verificar os jogadores conectados ao jogo\n";
	     msg += "'/l' para verificar os regras do jogo\n";
	     msg += "'/d' para desistir do jogo";
	     return msg;
		 
	 }
	 
	 public static String userInput() {
		 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	     try {
	          return reader.readLine().trim();
	     } catch (IOException e) {
	          System.out.println("Erro ao entrar dados");
	          return "";
	     }
	}

}
