/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author thiago
 */
@Path("tradutor_italiano")
public class TradutorItaliano {

    @Context
    private UriInfo context;
    List<JogadorTradutor>  jogadores = new ArrayList();
    private String desafioItaliano;
    private String respostaDesafio;
    private int statusJogo;
    private int IdsJogadores;
    private int idAdmin;
    private int idJogadorGanhador; // salvar o Id do jogador vencedor
    private boolean gameOver; // salvar que o jogo acabou(se true)
    private int pontuacaoGameOver;

    /**
     * Creates a new instance of TradutorItaliano
     */
    public TradutorItaliano() {
        
        statusJogo = 0; // 0 = jogo não iniciado; 1 = jogo ja iniciado
    	idJogadorGanhador = -1;
    	gameOver = false;
        IdsJogadores = 0; // quantos jogadores estão conectados
        idAdmin = 1; // Admin vai ser aquele jogador que se conectar primeiro
        pontuacaoGameOver = 50; // jogador precisa ter x pontos para ganhar jogo
    }

    /**
     * Retrieves representation of an instance of Server.TradutorItaliano
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of TradutorItaliano
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jogadores/get")
    public String listarJogadores(){
        
        /* Teste
        JogadorTradutor jr = new JogadorTradutor("Carlos");
        JogadorTradutor jr2 = new JogadorTradutor("Thiago");
        JogadorTradutor jr3 = new JogadorTradutor("Magno");
        
        jogadores.add(jr);
        jogadores.add(jr2);
        jogadores.add(jr3);
        */
        String jogadoresConectados[] = new String[jogadores.size()];
        int aux =0;
        for (JogadorTradutor jogador : jogadores) {
        	
        	jogadoresConectados[aux] = jogador.getNome();
                aux++;
        	
        }
        
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(jogadoresConectados);
    }
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("resetAdmin/put")
     public void resetAdmin(){
    	
    	idAdmin = 1;
    	IdsJogadores = 0;
    	idJogadorGanhador = -1;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("placarGeral/get")
    public String getPlacarGeral() {
    	
        /* teste
        JogadorTradutor jr = new JogadorTradutor("Carlos",20);
        JogadorTradutor jr2 = new JogadorTradutor("Thiago",30);
        JogadorTradutor jr3 = new JogadorTradutor("Magno",40);
        
        jogadores.add(jr);
        jogadores.add(jr2);
        jogadores.add(jr3); 
       */
        
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(jogadores); // retorna o ID tbm - dai teria q tratar isso no cliente
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("placarIndividual/get/{idJogador}")
    // placar individual do jogador
    public String getPlacarIndividual(@PathParam("idJogador") int idJogador){
    	
        /* Teste
        JogadorTradutor jr = new JogadorTradutor("Carlos",20,1);
        JogadorTradutor jr2 = new JogadorTradutor("Thiago",30,2);
        JogadorTradutor jr3 = new JogadorTradutor("Magno",40,3);
        
        jogadores.add(jr);
        jogadores.add(jr2);
        jogadores.add(jr3);
        */
        
    	for (JogadorTradutor jogador : jogadores) {
            if (idJogador == jogador.getIdJogador()) { // achou o jogador na lista, que fez a requisição       	
            	
                Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
                return g.toJson(jogador); // retorna o ID tbm - dai teria q tratar isso no cliente
            }
    	}
    	
    	return "Faild";
     	  	
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("regras/get")
    public String getRegras(){
    	
    	String regras[] = new String[6];
        
        regras[0] = "**********************************************************************************\n"; 
    	regras[1] ="O jogo não tem um limite de tempo, e voce começa com 25 pontos.\n"; 
    	regras[2] ="Para cada palavra acertada, voce ganha 1 ponto, e mais 1 ponto de cada um dos outros\n";
    	regras[3] ="jogadores. A cada erro, voce perde 1 ponto. Caso sua pontuação chege a 0, voce\n";
    	regras[4] ="é eliminado do jogo. Quem fizer 50 pontos primeiro, ganha o jogo\n";
    	regras[5] ="**********************************************************************************\n";
    	
        
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(regras);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("setNewDasio/put/{idJogador}")
    // lançar um novo desafio para aquele cliente que acertou
    public void setNewDesafioItaliano(@PathParam("idJogador") int idJogador){
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	jogador.setNewDesafio();
            }
        }
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("desafioItaliano/get")
    public String getDesafioItaliano() {
   
        
        String desafio[] = new String[1];
        desafio[0] = desafioItaliano;
        
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(desafio);
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("lancarDesafio/get/{idJogador}")
    public String lancarDesafio(@PathParam("idJogador") int idJogador) {
    	
    	String desafio[] = new String[1];
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	desafio[0] = jogador.getDesafio().imprimirDesafio();
            }
        }
        
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(desafio);
     }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("qtdJogadores/get")
    public String getQtdJogadores() {
        
        int qtdJogadoresConectados[] = new int[1];
    	
        qtdJogadoresConectados[0] =  jogadores.size();
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(qtdJogadoresConectados);
        
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("adminJogo/get")
    // verificar se jogador é o admin do jogo - so ele pode inicar o jogo
    public String adminJogo() {
        int admin[] = new int[1];
    	
        admin[0] =  idAdmin;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(admin);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("monitoraAdmin/put")
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registrarJogador/put/{jogador}")
    public void registrar(@PathParam("jogador") JogadorTradutor jogador) {
    	jogador.setPontuacao(pontuacaoGameOver/2);
    	jogadores.add(jogador);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("status/get")
    public String getStatus() {
        int status[] = new int[1];
    	
        status[0] =  statusJogo;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(status);
    }
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("setStatus/put/{status}")
    public void setStatus(@PathParam("status") int status) {
        this.statusJogo = status;
        if(status == 0){
            for(JogadorTradutor jogador: jogadores){
            	jogador.setPontuacao(25); // jogador começa com 25 pontos
            	jogador.setNewDesafio();
            }
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("removerJogador/put/{idJogador}")
    public void removerJogador(@PathParam("idJogador") int idJogador){
    	try {
    		
    		for (JogadorTradutor jogador : jogadores) {
                if (jogador.getIdJogador() == idJogador) {
                	jogadores.remove(jogador);
                }
            }
    		
        } catch (Exception e) {
        	}
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checarResposta/get/{resposta}/{idJogador}")
    public String checarResposta(@PathParam("resposta") String resposta, @PathParam("idJogador") int idJogador){
    	
    	resposta = resposta.toUpperCase();
    	int resultado = 0;
        
        int resultadoJson[] = new int[1];
        
    	
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	
            	if ( resposta.equals( jogador.getDesafio().getTraducao().toUpperCase() )) {
                	
                	// cliente deve apresentar msg d acertou, para seu jogador
                	resultado = 1;
                	
                	jogador.setPontuacao(jogador.getPontuacao() +1 +  jogadores.size());
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
    	
        
        resultadoJson[0]= resultado;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(resultadoJson);// 0 =  errou / 1 = acertou
  
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("isJogadorEliminado/get/{idJogador}")
    // verifica se jogador foi eliminado do jogo(Possui pontuação zero)
    public String jogadorEliminado(@PathParam("idJogador") int idJogador){
    	
    	boolean eliminado = false;
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogador) {
            	
            	if(jogador.getPontuacao() <=0)
            	eliminado = true;
            }
    	}
    	
        
        boolean jogEliminado[] = new boolean[1];
    	
        jogEliminado[0] =  eliminado;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(jogEliminado);       
    	
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ultimoId/get")
    public String ultimoId() {
        
        int ultId[] = new int[1];
    	
        ultId[0] =  IdsJogadores;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(ultId);
       
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("setIdJogador/get")
    public String setIdJogador() {
    	IdsJogadores++;
    	
         // ID é incremental, ou seja, sempre que requisitar um setID, ele é incrementado e
    	// retornado entao aquele novo valor para o cliente
    
        int idjogadores[] = new int[1];
    	
        idjogadores[0] =  IdsJogadores;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(idjogadores);                                       
    }
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("desistirJogo/put/{idJogador}")
    // Quem desenvolver o cliente, deve então dar uma mensagem de fim de jogo, obrigado pela participação, ou algo do tipo
    public void desistirJogo(@PathParam("idJogador") int idJogador){ 
    	
    	removerJogador(idJogador);
   	 
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("isGameOver/get")
    public String gameOver(){
          
        boolean gameDone[] = new boolean[1];
    	
        gameDone[0] =  gameOver;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(gameDone);   
    	
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("idGanhadorJogo/get")
    public String getIdGanhador(){
  
        
        int idGanhador[] = new int[1];
    	
        idGanhador[0] =  idJogadorGanhador;
        Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
        return g.toJson(idGanhador);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("dadosJogadorVencedor/get")
    public String getJogadorVencedor(){
    	
    	for (JogadorTradutor jogador : jogadores) {
            if (jogador.getIdJogador() == idJogadorGanhador) {
               
                Gson g = new Gson(); // criar uma variavel Gson q transforma qlq coisa em uma saida Json 
        
                return g.toJson(jogador);
            }
        }
    	
    	return "faild";
    	
    }
   
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("resetGame/put")
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
