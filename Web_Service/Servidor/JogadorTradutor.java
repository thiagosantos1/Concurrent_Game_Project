package Servidor;

/**
 *
 * @author Thiago Santos
 */
public class JogadorTradutor {
	
	private String nome;
	private int pontuacao; // jogador sempre começa com 25 pontos(fizer 50, ganha o jogo, se zerar é eliminado
	private int id;
						// Cada cliente tem sua própria palavra, nao cabendo ao servidor essa informação, como no trabalho anterior
    private Gerar_Palavra geradorDesafioItaliano; // guarda os dados sobre o desafio atual
	    
    public JogadorTradutor() {
        this.nome= "Sem Registro";
        this.pontuacao = pontuacao;
        id = id;
        
        geradorDesafioItaliano = new Gerar_Palavra(); // primeira palavra ja é gerada para o cliente
    }
           
    public JogadorTradutor(String nome) {
        this.nome = nome;
        this.pontuacao = pontuacao;
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
    
    public Gerar_Palavra getDesafio(){
    	
    	return geradorDesafioItaliano;
    }
    
    public void setNewDesafio(){
    	
    	 geradorDesafioItaliano = new Gerar_Palavra();
    }
    
    @Override
    public String toString() {
        return nome + "\t" + getPontuacao();
    }
 
    
}
