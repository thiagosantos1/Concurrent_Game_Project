package Servidor;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;




/**
 *
 * @author Thiago Santos
 */

/*
 *  A regra do jogo mudou. Antes, era uma palavra apenas por jogador, quem acertasse primeiro ganhava a radoda.
 *  Agora, cada jogador possui seu próprio desafio, e quem fazer 50 pontos primeiro ganha. Ou seja, um jogador pode ter mais palavras que outro
 */

@WebService
public class JogoTradutorItaliano {
    List<JogadorTradutor>  jogadores = new ArrayList();
    private String desafioItaliano;
    private String respostaDesafio;
    private int statusJogo;
    private int IdsJogadores;
    private int idAdmin;
    private int idJogadorGanhador; // salvar o Id do jogador vencedor
    private boolean gameOver; // salvar que o jogo acabou(se true)
    private int pontuacaoGameOver;
    
    public JogoTradutorItaliano() {
    	statusJogo = 0; // 0 = jogo não iniciado; 1 = jogo ja iniciado
    	idJogadorGanhador = -1;
    	gameOver = false;
        IdsJogadores = 0; // quantos jogadores estão conectados
        idAdmin = 1; // Admin vai ser aquele jogador que se conectar primeiro
        pontuacaoGameOver = 50; // jogador precisa ter x pontos para ganhar jogo
    }
    
    public void resetAdmin(){
    	
    	idAdmin = 1;
    	IdsJogadores = 0;
    	idJogadorGanhador = -1;
    }
    
    public String getPlacarGeral() {
    	String msgPlacar = "";
    	msgPlacar += "******** PLACAR Geral ********\n";
        for (JogadorTradutor jogador : jogadores) {
        	
        	msgPlacar += jogador.toString() + "\n"; // pega informação de msg de todos os clientes
        }
        msgPlacar += "*******************************\n";
        return msgPlacar;
    }
    
    // placar individual do jogador
    public String getPlacarIndividual(int idJogador){
    	
    	String msgPlacar = "";
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (idJogador == jogador.getIdJogador()) { // achou o jogador na lista, que fez a requisição       	
            	msgPlacar += "****** PLACAR Individual ******\n";
            	msgPlacar += jogador.toString() + "\n";
            	msgPlacar += "*******************************\n";
            }
    	}
    	
    	return msgPlacar;
     	  	
    }
    
    public String getRegras(){
    	
    	String regras = "\n**********************************************************************************\n" 
    					+ "O jogo não tem um limite de tempo, e voce começa com 25 pontos.\n" 
    					+ "Para cada palavra acertada, voce ganha 1 ponto, e mais 1 ponto de cada um dos outros\n"
    					+ "jogadores. A cada erro, voce perde 1 ponto. Caso sua pontuação chege a 0, voce\n"
    					+ "é eliminado do jogo. Quem fizer 50 pontos primeiro, ganha o jogo\n"
    					+ "**********************************************************************************\n";
    			
    	return regras;
    }
    
    // lançar um novo desafio para aquele cliente que acertou
    public void setNewDesafioItaliano(int idJogador){
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	jogador.setNewDesafio();
            }
        }
    }
    
    public String getDesafioItaliano() {
        return this.desafioItaliano;
    }
    
    public String lancarDesafio(int idJogador) {
    	
    	String desafio ="";
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	desafio = jogador.getDesafio().imprimirDesafio();
            }
        }
  
       return desafio;
     }
    
    public int getQtdJogadores() {
        return jogadores.size();
    }
    
    // verificar se jogador é o admin do jogo - so ele pode inicar o jogo
    public int adminJogo() {
        return idAdmin;
    }
    
    // monitora caso admin saiu do jogo. Admin passaria pra um jogador aleatorio
    public void monitorAdmin(){
    	boolean adminSaiu = true;
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idAdmin) {
            	adminSaiu = false;
            	break;
            }
        }
    	
    	// se admin saiu do jogo, mudar o admin então
    	if(adminSaiu){
    		
    		for (JogadorTradutor jogador : jogadores) {
                
    			idAdmin = jogador.getIdJogador();
    			break;
                
            }
    		
    	}
    	
    }

    public void registrar(JogadorTradutor jogador) {
    	// checar caso admin saiu do jogo, entao novo jogador seria 
    	jogador.setPontuacao(pontuacaoGameOver/2);
    	jogadores.add(jogador);
    }

    public int getStatus() {
        return statusJogo; 
    }
    
    // reset
    public void setStatus(int status) {
        this.statusJogo = status;
        if(status == 0){
            for(JogadorTradutor jogador: jogadores){
            	jogador.setPontuacao(25); // jogador começa com 25 pontos
            	jogador.setNewDesafio();
            }
        }
    }
    
    public void removerJogador(int idJogador){
    	try {
    		
    		for (JogadorTradutor jogador : jogadores) {
                if (jogador.getIdJogador() == idJogador) {
                	jogadores.remove(jogador);
                }
            }
    		
        } catch (Exception e) {
        	}
    }
    
    public int checarResposta(String resposta, int idJogador){
    	
    	resposta = resposta.toUpperCase();
    	int resultado = 0;
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	
            	if ( resposta.equals( jogador.getDesafio().getTraducao().toUpperCase() )) {
                	
                	// cliente deve apresentar msg d acertou, para seu jogador
                	resultado = 1;
                	
                	jogador.setPontuacao(jogador.getPontuacao() +1 + getQtdJogadores());
                	// 1 ponto pelo acerto, mais 1 ponto d cada um dos outros jogadores 
                	// Foi adicionado 2, pq 1 sera deduzido logo abaixo
                	
                	
                	// deve entao deduzir 1 ponto d cada um dos demais jogadores q nao acertou
                	for (JogadorTradutor jogadorLoser : jogadores) {
                		
                		jogadorLoser.setPontuacao(jogadorLoser.getPontuacao() -1);
                		
                	}
                	
                	// caso jogador atingiu a pontução minima para ser o vencedor
                	if(jogador.getPontuacao() >= pontuacaoGameOver){
                		
                		idJogadorGanhador = idJogador;
                		gameOver = true;
                		
                	}
                	
                 }
                else {
                	
                	// cliente deve apresentar msg d errou, para seu jogador
                	resultado = 0;
                	
                	if(jogador.getPontuacao() >0){
                		jogador.setPontuacao(jogador.getPontuacao() -1);
                	}
                	
                   	
            	
                }
            }
        }   
    	
    	return resultado; // 0 =  errou / 1 = acertou
    }
    
    
    // verifica se jogador foi eliminado do jogo(Possui pontuação zero)
    public boolean jogadorEliminado(int idJogador){
    	
    	boolean eliminado = false;
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	
            	if(jogador.getPontuacao() <=0)
            	eliminado = true;
            }
    	}
    	
    	return eliminado;
    	
    }
    
    public int ultimoId() {
        return IdsJogadores;
    }

    public int setIdJogador() {
    	IdsJogadores++;
    	
    	return IdsJogadores; // ID é incremental, ou seja, sempre que requisitar um setID, ele é incrementado e
    						 // retornado entao aquele novo valor para o cliente
    }
    
    
    public String listarJogadores(){
    	
    	String jogadoresConectados = "";
    	jogadoresConectados = "******** Jogadores Conectados ********\n";
        for (JogadorTradutor jogador : jogadores) {
        	
        	jogadoresConectados += jogador.getNome() + "\n";
        	
        }
        
        return jogadoresConectados;
    		
    }
    
    // Quem desenvolver o cliente, deve então dar uma mensagem de fim de jogo, obrigado pela participação, ou algo do tipo
    public void desistirJogo(int idJogador){ 
    	
    	removerJogador(idJogador);
   	 
    }
    
    public boolean gameOver(){
    	
    	return gameOver;
    	
    }
    
    public int getIdGanhador(){
    	
    	return idJogadorGanhador;
    }
    
    public String getJogadorVencedor(){
    	
    	String dadosJogador = "";
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogadorGanhador) {
            	dadosJogador = jogador.getNome() + "\t" + jogador.getPontuacao();
            }
        }
    	
    	return dadosJogador;
    	
    }
   
    public void resetGame(){
    	
    	if(gameOver){
    		
    		try {
        		
        		for (JogadorTradutor jogador : jogadores) {
                    jogadores.remove(jogador);
                }
        		
            } catch (Exception e) {
            	}
    		
    		resetAdmin();
    		gameOver = false;
    		statusJogo = 0;
    	}
    }


  

  
}
