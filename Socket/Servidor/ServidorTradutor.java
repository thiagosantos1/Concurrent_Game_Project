package Servidor;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServidorTradutor {
   ArrayList<ServidorThread> servidores = 
         new ArrayList<ServidorThread>();
	int porta = 6789;
	int status = 0;
	ServerSocket conexaoServidor;
	Gerar_Palavra desafioItaliano;

   public ServidorTradutor(int porta) {
      try {
         this.conexaoServidor = new ServerSocket(porta);         
   		System.out.println("Esperando conexoes de jogadores na porta " + porta);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   ServerSocket getConexaoServidor() {
      return conexaoServidor;
   }

   ArrayList<ServidorThread> getServidores() {
      return servidores;
   }

   Gerar_Palavra getDesafio() {
      return desafioItaliano;
   }

   void lancarDesafio() {
	  
      for (ServidorThread servidor: getServidores()) {
         servidor.notificar("/desafio " + desafioItaliano.imprimirDesafio()+ "\n" );
      }
      
      //return getDesafio();
   }
   
   // criar um novo desafio( uma nova palavra em italiano)
   void setNovoDesafio(){
	   
	   desafioItaliano = new Gerar_Palavra();
   }
   
   int getStatus() {
      return status;
   }
    void setStatus(int status){
    	
    	this.status = status;
    }
   
   void iniciar() {
      status = 1; // jogo iniciado
      desafioItaliano = new Gerar_Palavra();
      for (ServidorThread servidor: servidores){ 
         servidor.notificar("/jogo_iniciado\n"); // cliente deve dar msg q jogo iniciou e  boa sorte aos jogadores
      }
   }
   
   void notificarJogadores(String message){
	   
	   for (ServidorThread servidor: servidores){ 
	         servidor.notificar("\n" + message +"\n\n");
	      }
   }

	public static void main(String argv[]) throws Exception  {   		
		ServidorTradutor servidorTrad = new ServidorTradutor(6789);
		while (true) {
         if ( servidorTrad.getStatus() == 0 ) {
   			Socket conexao = servidorTrad.getConexaoServidor().accept();
            ServidorThread st = new ServidorThread( conexao, servidorTrad );
            servidorTrad.getServidores().add(st);
            st.start();
         }
		}
	}
}

