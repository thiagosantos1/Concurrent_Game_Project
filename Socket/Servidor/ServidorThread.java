package Servidor;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServidorThread extends Thread {
   Socket conexao;
   String nome; // nome de cada jogador
   int pontuacao = 25; // pontuaçao para cada jogador
   static int  numJogadores = 0;
   static boolean palavraTraduzida = false; // false = palavra em jogo ainda nao traduzida por nenhum jogador
   											// true = palavra foi corretamente traduzida por algum jogador
   static String ganhador =null;	
   static String perdedor =null;
   ServidorTradutor servidorTradutor;
   BufferedReader receptorCliente;   

   DataOutputStream mandarParaCliente;

   public ServidorThread( Socket conexao, ServidorTradutor servidorTrad ) {
      try {
         this.conexao = conexao;
         this.servidorTradutor = servidorTrad;
         receptorCliente = new BufferedReader(
			                  new InputStreamReader(
                              conexao.getInputStream()
                           )
                     );
         mandarParaCliente = new DataOutputStream(
                              conexao.getOutputStream()
                       );
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void notificar(String mensagem) {
      try {
    	  mandarParaCliente.writeBytes(mensagem);
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }      
   }

   public void pontuacao() {
      try {
    	  
    	  
    	  mandarParaCliente.writeBytes("/pontuacao "+pontuacao +"\n");
    	  
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } 
   }
   
   public void listarJogadores(){
	  
	try{
		   ArrayList<ServidorThread> servidores 
	       = servidorTradutor.getServidores();
		   mandarParaCliente.writeBytes("/players\n"); // informa o cliente q sera enviado todos os nomes dos jogadores
		   for (ServidorThread servidorThread: servidores) {
			   mandarParaCliente.writeBytes("/jogadores " + servidorThread.nome+"\n");
		   }
    
	 } catch (IOException ioe) {
       ioe.printStackTrace();
	 } 
	
   }
   
   public void placarGeral(){
	   
	   try{
		   
		   mandarParaCliente.writeBytes("/placarGeral\n");
		   ArrayList<ServidorThread> servidores 
           = servidorTradutor.getServidores();
   			for (ServidorThread servidorThread: servidores) {
   				mandarParaCliente.writeBytes("/placar " + servidorThread.nome+" " + servidorThread.pontuacao + "\n");
   		}
   		
	   } catch (IOException ioe) {
	       ioe.printStackTrace();
		 } 
	   
   }
   

   public void run() {
      try {
    	  
    	  // antes de realizar uma conexao, deve se conferir se ela pode ser aceita
    	  if(servidorTradutor.getStatus() == 1) { // jogo ja iniciado
    		  
    		  mandarParaCliente.writeBytes("/recusado_iniciou\n"); // jogador desistiu
          	// cliente deve enviar msg q jogador n pode jogar pq jogo ja iniciou
    		  conexao.close();
    		  
    	  }
    	  else if(numJogadores >= 5){ // jogo ja esta com a capacidade maxima de jogadores
    		  
    		  mandarParaCliente.writeBytes("/recusado_full\n"); // jogador desistiu
            	// cliente deve enviar msg q jogador n pode jogar pq jogo ja tem a capacidade maxima d jogadores
      		  conexao.close();
      		  
    	  }
    	  
    	  // welcome message
    	  mandarParaCliente.writeBytes("/conectado\n"); // cliente pode dar uma msg d boas vindas
    	  
    	  mandarParaCliente.writeBytes("/nome\n"); // informando o cliente, que o servidor quer receber o nome do cliente
    	  try{
    		  
    		  nome = receptorCliente.readLine();
    	  }
    	  catch(Exception e){
    		  
    	  }
    	  
    	  
    	  
    	  numJogadores++;
    	  //mandarParaCliente.writeBytes("\nBem vindo " +nome); // cliente pode dar uma mensagem d boas vindas ao jogador
    	  
         while ( servidorTradutor.getStatus() == 0 ) { // jogo ainda nao inicializado
        	
        	 // menu de opcoes
        	 if(numJogadores <2){
        		 
        		 // mandar para cliente q o numero de jogadores necessarios ainda n foi atingido
        		 mandarParaCliente.writeBytes("/faltanto_jogadores\n");
        		 
        		 // cliente deve criar o menu com as opçoes do jogo tambem.
        		  /*
        		   * /players
        		   * /regras
        		   * /desistir
        		   */
        	 }
        	 else{
        		 
        		 // mandar para cliente que o numero de jogadores necessarios ja foi atingido e o jogo ja pode começar
        		 
        		 mandarParaCliente.writeBytes("/jogadores_ready\n");
        		 
        		// cliente deve criar o menu com as opçoes do jogo tambem.
        		 
        	   /*
       		   * /iniciar
       		   * /players
       		   * /regras
       		   * /desistir
       		   */
        		 
        	 }
        	
        	mandarParaCliente.writeBytes("/opcao\n"); // cliente deve comunicar o cliente q ele deve escolher uma opçaão
        	String comando = null;
	        try{
	      		  
	        	 comando = receptorCliente.readLine();
	      	  }
	      	  catch(Exception e){
	      		  
	      	  }
        	
            
            // listar jogadores
            if ( comando.toLowerCase().equals("/players") ) {
               
            	listarJogadores();
            }
            // listar regras do jogo
            else  if ( comando.toLowerCase().equals("/regras") ){
            	
            	mandarParaCliente.writeBytes("/regras\n");
            	
            	// cliente deve mandar msg com as regras do jogo
            
            }
            // iniciar jogo
            else  if ( comando.toLowerCase().equals("/iniciar") ){
            	servidorTradutor.iniciar();
            }
            
            else if (comando.toLowerCase().equals("/desistir")) {
            	
            	mandarParaCliente.writeBytes("/desistir\n"); // jogador desistiu
            	// cliente deve enviar msg q jogador desistiu
            	numJogadores--;
            	conexao.close();
            }
            else{
            	mandarParaCliente.writeBytes("/invalido\n"); // cliente deve dar msg q opção é invalida
            }
         }
         
         String resposta = null;
         
         // é mandado /desafio palavra
         // cliente deve entao separar o /desafio da palavra 
         // e formatar a forma como vai mostrar a palavra
         servidorTradutor.lancarDesafio();
         
         pontuacao();
         while ( servidorTradutor.getStatus() == 1 ) { // jogo foi inicializado
        	String opcao = null; // opcao de escolha cliente
        	
        	 if(palavraTraduzida){ // se palavra foi traduzida, hora de buscar uma nova e notificar a todos os jogadores
        		servidorTradutor.setNovoDesafio(); // lancar um novo desafio
        		servidorTradutor.lancarDesafio();
        		//desafioItaliano = servidorTradutor.lancarDesafio(); // buscar a palavra a ser descoberta
        		palavraTraduzida = false; // reseta a variavel
        	 }
        	
        	
        	// cliente deve entao mostrar o menu do jogo para o jogador
        	/*
        	 *  /responder
        	 *  /placar
        	 *  /desistir
        	 *  /players
        	 */
        	 
        	try{
	        	mandarParaCliente.writeBytes("/menuJogo\n");
	        	mandarParaCliente.writeBytes("/opcao\n");
	        	opcao = receptorCliente.readLine();
	        	opcao = opcao.toLowerCase();
        	}catch(Exception e){
        		
        	}
        	
        	// checkar qual das opcoes cliente escolheu
        	
        	if(opcao.equals("/responder")){
        		
        		// cliente deve informar entao que o jogador deve responder
        		mandarParaCliente.writeBytes("/responder\n");
        		
        		try{
  	      		  
        			resposta = receptorCliente.readLine();
	   	      	  }
	   	      	  catch(Exception e){
	   	      		  
	   	      	  }
				
				resposta = resposta.toUpperCase();
	            if ( resposta.equals( servidorTradutor.desafioItaliano.getTraducao().toUpperCase() )) {
	            	
	            	// cliente deve apresentar msg d acertou, para seu jogador
	            	mandarParaCliente.writeBytes("/acertou\n");
	            	pontuacao = pontuacao +1 + (numJogadores); // 1 ponto pelo acerto, mais 1 ponto d cada um dos outros jogadores 
	            						// Foi adicionado 2, pq 1 sera deduzido logo abaixo
	            	
	            	// deve entao deduzir 1 ponto d cada um dos demais jogadores q nao acertou
	            	ArrayList<ServidorThread> servidores 
	                   = servidorTradutor.getServidores();
	                for (ServidorThread servidorThread: servidores) {
	             	   if(servidorThread.pontuacao >0)
	                	servidorThread.pontuacao--;
	                }
	            	
	            	// informar todos os cliente que a rodada acabou, alguem acertou a palavra
	            	// cliente deve entao formar isso e comunicar a todos os jogadores q tal palavra foi traduzida
	            	servidorTradutor.notificarJogadores("/fim_rodada "+ servidorTradutor.desafioItaliano.imprimirDesafio() +"\n");
	            	
	            	palavraTraduzida = true; // palavra foi traduzida
	            	pontuacao();
	            }
	            else {
	            	
	            	// cliente deve apresentar msg d errou, para seu jogador
	            	mandarParaCliente.writeBytes("/errou\n"); 
	            	
	            	if(pontuacao >0)
	            		pontuacao--;
	            	
	               	pontuacao();  
	               	
	               	// cliente deve apresentar a palavra novamente na tela, para seu jogador q errou a palavra
	           		mandarParaCliente.writeBytes("/desafio " + servidorTradutor.desafioItaliano.imprimirDesafio()+"\n"  ); 
	           		// imprimir novamente a palavra
	        	
	            }
        	}
        	
        	else if ( opcao.toLowerCase().equals("/players") ) {
                
        		listarJogadores();
             }
        	
        	else if(opcao.toLowerCase().equals("/placar")){ // placar geral do jogo
        		
        		placarGeral();
        		
        	}
        	
        	else if(opcao.toLowerCase().equals("/desistir")){
        		
        		mandarParaCliente.writeBytes("/desistir\n"); // jogador desistiu
            	// cliente deve enviar msg q jogador desistiu
        		numJogadores--;
            	conexao.close();
        		
        	}
        	
        	else{
        		
        		mandarParaCliente.writeBytes("/invalido\n");
        	}
            
        	
        	/*
        	 *  Devemos entao verificar se algum jogador ganhou o jogo ou se algum zerou os pontos e foi eliminado
        	 */
        	
        	if(pontuacao >= 50 || numJogadores==1){ // jogador atingiu 50 pontos, os todos os demais foram eliminados
        		
        		// jogador ganhou o jogo
        		// cliente deve entao informar que o jogador ganhou
        		mandarParaCliente.writeBytes("/ganhou\n");
        		ganhador = nome;
        		
        		// informar a todos os clientes q o jogo acabou, alguem ganhou
        		ArrayList<ServidorThread> servidores 
                = servidorTradutor.getServidores();
        		for (ServidorThread servidorThread: servidores) {
        			// enviar para todos q o jogo terminou, e o ganhador foi o jogador "x"
        			mandarParaCliente.writeBytes("/game_over " + servidorThread.ganhador +"\n");
        		}
        		
        		servidorTradutor.setStatus(2); // fim de jogo
        		
        	}
        	
        	if(pontuacao <=0){
        		
        		// jogador zerou os pontos e foi eliminado
        		// cliente deve informar isso ao jogador
        		mandarParaCliente.writeBytes("/perdeu\n");
        		perdedor = nome;
        		
        		// informar a todos os clientes q determinado jogador perdeu e foi eliminado 
        		ArrayList<ServidorThread> servidores 
                = servidorTradutor.getServidores();
        		for (ServidorThread servidorThread: servidores) {
        			// enviar para todos q o jogador "x" foi eliminado
        			mandarParaCliente.writeBytes("/jogador_eliminado " + servidorThread.perdedor+"\n");
        		}
        		
        		numJogadores--; // um jogador a menos no jogo
        		conexao.close();
        	}
         }
         
         if(servidorTradutor.getStatus() == 2){ // jogo foi encerrado
        	 
        	 /*
        	  *  caso queira uma msg final, implementar aqui (apos fim do jogo)
        	  */
        	 
         }
         
		 conexao.close();
      } catch (Exception e) {
         e.printStackTrace();
    	  System.out.println("\nConexao Socket fechada para jogador\n");
      }
   }
}

