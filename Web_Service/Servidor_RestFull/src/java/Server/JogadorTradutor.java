/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author thiago
 */



public class JogadorTradutor {
	
	private String nome;
	private int pontuacao; // jogador sempre começa com 25 pontos(fizer 50, ganha o jogo, se zerar é eliminado
	private int id;
						// Cada cliente tem sua própria palavra, nao cabendo ao servidor essa informação, como no trabalho anterior
    private GerarPalavra geradorDesafioItaliano; // guarda os dados sobre o desafio atual
	    
    public JogadorTradutor() {
        this.nome= "Sem Registro";
        this.pontuacao = pontuacao;
        id = id;
        
        geradorDesafioItaliano = new GerarPalavra(); // primeira palavra ja é gerada para o cliente
    }
           
    public JogadorTradutor(String nome) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }
    
    public JogadorTradutor(String nome, int pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }
    
    public JogadorTradutor(String nome, int pontuacao, int id) {
        this.nome = nome;
        this.pontuacao = pontuacao;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }
    
    public void setPontuacao(int pontuacao) {
    	
    	if(pontuacao >=0)
    		this.pontuacao = pontuacao;
    }
    
    public void setIdJogador(int ID){
    	
    	this.id = ID;
    }
    public int getIdJogador(){
    	
    	return this.id;
    }
    
    public GerarPalavra getDesafio(){
    	
    	return geradorDesafioItaliano;
    }
    
    public void setNewDesafio(){
    	
    	 geradorDesafioItaliano = new GerarPalavra();
    }
    
    @Override
    public String toString() {
        return nome + " " + getPontuacao();
    }
 
    
}
