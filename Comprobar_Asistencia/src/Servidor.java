import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String, Boolean> alumnos = new ConcurrentHashMap<String, Boolean>();
		
		File ficheroAsistencias = new File("asistencia.txt");
		
		alumnos.put("12345678A", false);
		alumnos.put("11111111A", false);
		alumnos.put("22222222B", false);
		
		try {
			ServerSocket servidor = new ServerSocket(8080);
			Socket socket = null;
			while(true) {
				//Abrir socket
				socket = servidor.accept();
				//Ejecutar hilo
				Hilo hilo = new Hilo(socket, alumnos, ficheroAsistencias);
				hilo.start();
				//Cerrar socket
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
