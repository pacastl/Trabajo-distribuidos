package krysis;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente 
{
	public static void main(String[] args) 
	{
		
		//Con sockets seguros sería así
//		SSLSocketFactory sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
//		SSLSocket s= (SSLSocket) sslFact.createSocket("localhost",6666)
		try {
			Socket s=new Socket("localhost",6666);
			
			try(DataOutputStream out=new DataOutputStream(s.getOutputStream());
				ObjectInputStream in=new ObjectInputStream(s.getInputStream());
				DataInputStream in2=new DataInputStream(s.getInputStream()))
			{
				Tablero tablero=(Tablero) in.readObject();//Leemos el tablero que pasa el servidor
				tablero.mostrar();
				
				while (!tablero.estaCompleto())
				{
					int fila, columna;
					char letra;
					letra = tablero.getLetra();
					System.out.println("Coloca la letra " + letra);
					System.out.print("Fila: "); 
					fila = leerInt();
					System.out.print("Columna: ");
					columna = leerInt();
					boolean correcto=false;
					
					while(!correcto) {
						if(0<=fila && fila<tablero.getNumFilas() && 0<=columna && columna<tablero.getNumCol())
				 		{
				 			String linea=fila + " "+ columna+ " "+ letra+ " \r\n";
				 			out.writeBytes(linea);
				 			out.flush();
				 			correcto=true;
				 		}else {
				 			System.out.println("DATOS NO VÁLIDOS");
				 			System.out.print("Fila: "); 
							fila = leerInt();
							System.out.print("Columna: ");
							columna = leerInt();
				 		}
					}
					System.out.println("");
					
					String msg=in2.readLine();
					
					System.out.println(msg);
					System.out.println("");
					
					if(msg.equals("Correcto")) { //Si el servidor devuelve correcto añade la letra
						tablero.ponerLetra(fila, columna,letra );
					}
					tablero.mostrar();//mostramos el tablero actualizado con la letra colocada
				}
				System.out.println("¡¡¡ENHORABUENA!!! Lo has completado correctamente.");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String leer()
	{
		try
		{
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String linea = br.readLine();
			return linea;
		}
		catch (IOException exc)
		{
			return null;
		}		
	}
	
	public static void escribir(String cadena)
	{
		System.out.println(cadena);
	}
	
	public static int leerInt()
	{
		String cadena = leer();
		try {
			int valor = Integer.parseInt(cadena);
			return valor;
		}catch(NumberFormatException e)
		{
			return -1;
		}
	}
}
