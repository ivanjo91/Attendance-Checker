import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	static Scanner sc = new Scanner(System.in);
	   
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			// Obtenemos la direccion de sockets
			InetAddress ip = InetAddress.getByName("localhost");
			
			// Establecemos la comunicacion con el puerto 8080
			Socket s = new Socket(ip, 8080);
	        
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
	        
			//El alumno inserta DNI, el servidor comprueba que esté en la lista y
			// que no haya registrado la asistencia previamente
			boolean bandera = true;
			
			while(bandera) {
				System.out.println(input.readUTF());
				
				String dni = sc.nextLine();
				output.writeUTF(dni.toUpperCase());
				System.out.println(input.readUTF());
				bandera = input.readBoolean();
			}
			
			//Recibir tarjeta de codigos
			ObjectInputStream entrada = new ObjectInputStream(s.getInputStream());
			Tarjeta tarjeta = (Tarjeta)entrada.readObject();
			//Se muestran los codigos
			tarjeta.mostrarCodigos();
			
			boolean flag = true;
			
			while(flag) {
				System.out.println(input.readUTF());
				
				String codigoEnviar = sc.nextLine();
				output.writeUTF(codigoEnviar);
	           
				System.out.println(input.readUTF());
				flag = input.readBoolean();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        

	}

}
