import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Hilo extends Thread{
	
	private Tarjeta tarjeta;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private Map<String, Boolean> mapaAlumnos;
	private File ficheroAsistencias;
	private String dniRecibido;
	
	public Hilo(Socket s, Map mapa, File fichero) {
		this.socket = s;
		this.mapaAlumnos = mapa;
		this.ficheroAsistencias = fichero;
		try {
			input= new DataInputStream(s.getInputStream());
			output= new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
	}

	@Override
	public void run() {
		System.out.println("SERVIDOR");
		Tarjeta tarjeta = new Tarjeta();
		tarjeta.mostrarCodigos();
		try {
			//Pedir DNI al cliente y comprobar que esté en la lista 
			// y no haya registrado asistencia previamente
			comprobarDNI();
			
			ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
			salida.writeObject(tarjeta);
			
			int fila = (int)(Math.random()*5 + 1);
			int columna = (int)(Math.random()*5 + 1);
			
			int intentos = 1;
			
			boolean flag = true;
			
			while(flag) {
				
				String pedirCodigo = "Intento " + intentos +" de 3 \n Introduce fila: " + fila + " - columna: " + columna;
				System.out.println("SERVIDOR: " + pedirCodigo);
				output.writeUTF(pedirCodigo);
				
				String codigoRecibido = input.readUTF();
				
				if(tarjeta.comprobarCodigo(fila-1, columna-1, Integer.parseInt(codigoRecibido))) {
					output.writeUTF("Codigo correcto");
					output.writeBoolean(false);
					//Registrar asistencia en fichero
					registrarAsistencia();
					flag = false;
				}
				else {
					if(intentos<3) {
						output.writeUTF("Codigo incorrecto");
						output.writeBoolean(true);
						intentos++;
					}
					else {
						output.writeUTF("Codigo incorrecto. Has consumido todos los intentos");
						output.writeBoolean(false);
						//Si se agotan los intentos, se vuelve a poner a false el Map
						cambiarEstadoDNIMap();
					}
					
				}
				
			}
			
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private synchronized void comprobarDNI() {
		
		boolean bandera = true;
		
		try {
			while(bandera) {
				output.writeUTF("Introduce DNI: ");
				
				dniRecibido = input.readUTF();
				
				if(mapaAlumnos.containsKey(dniRecibido)) {
					if(mapaAlumnos.get(dniRecibido)) {
						output.writeUTF("El DNI introducido ya ha registrado la asistencia");
						output.writeBoolean(true);
					}
					else {
						output.writeUTF("DNI Correcto, enviando codigos");
						//Cambiar valor en el Map
						mapaAlumnos.replace(dniRecibido, true);
						output.writeBoolean(false);
						bandera = false;
					}
				}
				else {
					output.writeUTF("El DNI introducido no existe, vuelve a probar: ");
					output.writeBoolean(true);
				}
			}
		}
		catch(ConcurrentModificationException cme) {
	         cme.printStackTrace();
	    }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private synchronized void cambiarEstadoDNIMap() {
		
		try { 
			mapaAlumnos.replace(dniRecibido, false);
	      } 
		catch(ConcurrentModificationException cme) {
	         cme.printStackTrace();
		}
		
	}
	
	private synchronized void registrarAsistencia() {
		
        try {
            FileWriter escribir = new FileWriter(ficheroAsistencias, true);
            
            String ipCliente = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
            
            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
            String cadenaFecha = formato.format(fecha);

            escribir.write(dniRecibido+";"+cadenaFecha+";"+"presente;" + ipCliente +"\r\n");
            
            escribir.close();
        } 
        catch (Exception e) {
            System.out.println("Error al escribir en fichero");
        }
		
	}
	

}
