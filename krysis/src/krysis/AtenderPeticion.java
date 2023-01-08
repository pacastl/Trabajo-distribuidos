package krysis;

import java.io.*;
import java.net.*;

public class AtenderPeticion implements Runnable {

	private Socket socket;

	
	private static final String[][][] TABLEROS = { 
			{ {"MONEDAS"},{"ASESORA"},{"NI","PLAN"},{"TAPIOCA"},{"ENEA","ER"},{"Q","GRAL"},{"USA","DIA"},{"IERRA","S"},{"LR","ALAN"},{"LIGNITO"},{"AOVADOS"} },
			{ {"CARRACA"},{"OCEANOS"},{"RAD","ORI"},{"TU","MASA"},{"ALMA","AN"},{"PEAJERO"},{"L","SESI"},{"US","STOP"},{"MANTO","E"},{"APIA","CL"},{"SOLDADO"} },
			{ {"PEPINOS"},{"ERI","AJO"},{"RR","ROES"},{"TORO","AT"},{"U","ONU","E"},{"RAS","RIN"},{"BLASON"},{"ANSA","CI"},{"DA","LAOS"},{"ODA","MAL"},{"SONRISA"} },
			{ {"PARLERA"},{"ISO","SEM"},{"NULA","TO"},{"TA","TOAD"},{"ANZUELO"},{"L","ASA","R"},{"ARIA","AR"},{"BARRIGA"},{"ICE","NUS"},{"OH","ARDE"},{"SANCION"}},
	};
	
	public AtenderPeticion(Socket s) {
		this.socket=s;

	}
	
	@Override
	public void run() {
		
		try(ObjectOutputStream out=new ObjectOutputStream(this.socket.getOutputStream());
			DataInputStream in=new DataInputStream(this.socket.getInputStream());
			DataOutputStream out2=new DataOutputStream(this.socket.getOutputStream());)
		{
			int n = (int)Math.floor(Math.random()*4);
			Tablero tablero = new Tablero(11,7,TABLEROS[n]);
			out.writeObject(tablero);//Envía el tablero al cliente
			out.flush();
			
			while(!tablero.estaCompleto())
			{
				String leido=in.readLine();//Se comprueba en el cliente que pasa todos los parámetros bien
				while(leido==null)
				{
					leido=in.readLine();
				}
				String trozos[]=leido.split(" ");
				
				int fila=Integer.parseInt(trozos[0]);
				int col=Integer.parseInt(trozos[1]);
				char letra=trozos[2].charAt(0);
				
				String msg="";
				//Usamos los datos que nos pasa el cliente
				if(tablero.ponerLetra(fila, col,letra )) { 
					msg="Correcto";
				}else {
					msg="Error";	
				}
				//tablero.mostrar();
				out2.writeBytes(msg+"\r\n");
				out2.flush();
			}
			
//			
//			tablero.mostrarHorizontales(); 
//			System.out.println("Número de palabras Horizontales: " + tablero.numPalabrasHorizontales());
//			tablero.mostrarVerticales();
//			System.out.println("Número de palabras Verticales: " + tablero.numPalabrasVerticales());
//			System.out.println("Número de palabras Total: " + tablero.numPalabrasTotal());
//			System.out.println("Número de letras Total: " + tablero.numLetras());
//			tablero.mostrarLetras();
//			System.out.println("Letra más repetida: " + tablero.letraMasRepetida());
//			System.out.println("Letra menos repetida: " + tablero.letraMenosRepetida());
//			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(this.socket!=null) {
				try {
					this.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
