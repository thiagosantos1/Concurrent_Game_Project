/**
 *
 * @author Thiago Santos
 */

package Servidor;

import javax.xml.ws.Endpoint;


public class ServidorTradutor {
    public static void main(String[] args) {
    	
    					// "http://192.168.2.19:8081/WebServices/jogo
        Endpoint.publish("http://localhost:8081/WebServices/jogo", new JogoTradutorItaliano());
    }
}
