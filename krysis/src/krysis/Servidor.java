package krysis;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Servidor {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Con sockets seguros sería así
//		SSLServerSocket s;
//		SSLServerSocketFactory sslSrvFact=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//		s =(SSLServerSocket sslSrvFact.createServerSocket (6666);
//		SSLSocket c = (SSLSocket) s.accept();
		
		ExecutorService pool=Executors.newCachedThreadPool();
		try(ServerSocket ss=new ServerSocket(6666);) {
			
			while(true) 
			{
				try {
					Socket s=ss.accept();
					
					AtenderPeticion ap=new AtenderPeticion(s);
					
					pool.execute(ap);
					
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.shutdown();

	}

}
