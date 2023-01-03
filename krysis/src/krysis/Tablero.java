package krysis;

import java.io.*;
import java.util.*;

public class Tablero implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private final int NUM_LETRAS = 26;
	private int numFilas;
	private int numColumnas;
	private char[][] tablero;
	private char[][] tableroJuego;
	private String[][] horizontales;
	private String[][] verticales;
	private ArrayList<Character> letrasUtilizadas;
	private int[] letras;
	
	//Crea un nuevo tablero inicializando todos sus atributos
 	public Tablero(int filas, int columnas, String[][] horizontales)
	{
		this.numFilas = filas;
		this.numColumnas = columnas;
		this.setTablero(horizontales);
		this.setTableroJuego();
		this.setHorizontales();
		this.setVerticales();
		this.setListaLetras();
		
		//Iniciamos el vector letras;
		//contiene todas las letras del abecedario menos la Ñ en código ASCII
		//desde 'A'=65 hasta 'Z'=90 (26 letras)
		this.letras=new int[this.NUM_LETRAS];
 		int l=65;
 		for(int n=0;n<this.NUM_LETRAS;n++)
 		{
 			this.letras[n]=l;
 			l++;
 		}
	}
 	
 	/* Inicializa la matriz tablero con dimensión numFilasXnumColumnas y
 	 * rellena todas sus casillas con las palabras de la matriz pasada por parámetro.*/
 	private void setTablero(String[][] horizontales)
	{
 		this.tablero= new char[this.numFilas][this.numColumnas];
 		//indices en la matriz tablero
 		int ti=0;
 		int tj=0;
 		//indices en la matriz horizontales
 		int hi=0;
 		int hj=0;
 			
 		while(ti<this.numFilas)
 		{
 			while(tj<this.numColumnas)
 			{
 				char[] array = horizontales[hi][hj].toCharArray();
 				for(int a=0;a<array.length;a++)
 				{
 					this.tablero[ti][tj]=array[a];
 					tj++;
 				}
 				if(tj<this.numColumnas)
 				{
 					this.tablero[ti][tj]=' ';
 					tj++;
 					hj++;
 				}
 			}
 			ti++;
 			tj=0;
 			hi++;
 			hj=0;
 		}
	}
 	
 	/* Inicializa la matriz tableroJuego con dimensión numFilasXnumColumnas y
 	 * rellena cada hueco donde debe ir una letra con un espacio en blanco (' ') 
 	 * y con un asterisco ('*') donde no hay letras*/
 	private void setTableroJuego()
	{
 		this.tableroJuego= new char[this.numFilas][this.numColumnas];
 		for(int i=0; i<this.numFilas;i++) 
		{
			for(int j=0; j<this.numColumnas;j++) 
			{
				if(this.tablero[i][j]==' ')
				{
					this.tableroJuego[i][j]='*';
				}
				else
				{
					this.tableroJuego[i][j]=' ';
				}
			}
		}
	}
 	
 	/* Inicializa la matriz horizontales, que contiene por cada fila
 	 * todas las palabras de cada fila del tablero (palabras horizontales)*/
	private void setHorizontales()
	{
		this.horizontales= new String[this.numFilas][];
		LinkedList<String> list=new LinkedList<String>();
		String[] array;
		String s="";
		for(int i=0; i<this.numFilas;i++)
		{
			for(int j=0;j<this.numColumnas;j++)
			{
				if(this.tablero[i][j]==' ')
				{
					if(j!=this.numColumnas-1)
					{
						list.add(s);
						s="";
					}
				}
				else
				{
					s=s+String.valueOf(this.tablero[i][j]);
				}
			}
			list.add(s);
			s="";
			array=new String[list.size()];
			for(int h=0;h<list.size();h++)
			{
				array[h]=list.get(h);
			}
			this.horizontales[i]=array;
			list.clear();
		}
	}
	
	/* Inicializa la matriz horizontales, que contiene por cada fila
 	 * todas las palabras de cada columna del tablero (palabras verticales)*/
	private void setVerticales()
	{
		this.verticales= new String[this.numColumnas][];
		LinkedList<String> list=new LinkedList<String>();
		String s="";
		for(int j=0;j<this.numColumnas;j++)
		{
			for(int i=0;i<this.numFilas;i++)
			{
				if(this.tablero[i][j]==' ')
				{
					if(i!=this.numFilas-1)
					{
						list.add(s);
						s="";
					}
				}
				else
				{
					s=s+String.valueOf(this.tablero[i][j]);
				}
			}
			list.add(s);
			s="";
			String[] array=new String[list.size()];
			for(int v=0;v<list.size();v++)
			{
				array[v]=list.get(v);
			}
			this.verticales[j]=array;
			list.clear();
		}
	}
	
	/* Inicializa la lista letrasUtilizadas,
	 * que contiene todas las letras usadas en el tablero (puede haber repetidas)*/
	private void setListaLetras()
	{
		this.letrasUtilizadas=new ArrayList<Character>();
		for(int i=0;i<this.numFilas;i++)
		{
			for(int j=0;j<this.numColumnas;j++)
			{
				if(this.tablero[i][j]!=' ')
				{
					this.letrasUtilizadas.add(this.tablero[i][j]);
				}
			}
		}
	}
	
	/*Devuelve una letra aleatoria de la lista de letrasUtilizadas*/
	public char getLetra()
	{
		Random r= new Random();
		int n=r.nextInt(this.letrasUtilizadas.size());
		return this.letrasUtilizadas.get(n);
	}
	
	/* Devuelve true si ha podido colocar la letra en la fila y columna especificadas 
	 * como parámetros; devuelve false en caso contrario.
	 * Poner la letra supone que la fila y columna esten dentro del rango permitido,
	 * el espacio no este ocupado (sea un espacio en blanco) en el tablero de juego 
	 * y que la letra coincida con su correspondiente en el tablero.
	 * Cada vez que se coloque una nueva letra se elimina de la lista de letrasUtilizadas y
	 * se comprueba si se ha completado alguna nueva palabra*/
 	public boolean ponerLetra(int fila, int columna, char letra)
 	{
 		if(0<=fila && fila<this.numFilas && 0<=columna && columna<this.numColumnas)
 		{
 			if(this.tableroJuego[fila][columna]==' ' && this.tablero[fila][columna]==letra)
 			{
 				this.tableroJuego[fila][columna]=letra;
 				int n=this.letrasUtilizadas.indexOf(letra);
 				this.letrasUtilizadas.remove(n);
 				comprobarPalabrasCompletas(fila, columna);
 				return true;
 			}
 		}
 		return false;
 	}
 	
 	//Método suministrado
 	/*Verifica si  se han completado alguna palabra tanto horizontal como vertical*/
 	private void comprobarPalabrasCompletas(int fila, int columna)
 	{
 		comprobarPalabrasCompletasH(fila, columna);
 		comprobarPalabrasCompletasV(fila, columna);
 	}
 	
 	/* Confirma si se ha completado alguna palabra horizontal en el autodefinido.
 	 * Primero recorre desde su posición hasta llegar al inicio de la pablabra. 
 	 * Después comprueba cada letra de la palabra hasta al final. Vamos almacenando cada letra, 
 	 * formando asi la palabra. Si alguna letra del tablero de juego no coincide con el tablero
 	 * con la solución sale del bucle ya que aun la palabra no está completa.
 	 * En el caso de la palabra si esté completa la escribe por pantalla*/
 	private void comprobarPalabrasCompletasH(int fila, int columna)
 	{
 		int j=columna;
 		boolean b=true;
 		while(0<=j && this.tablero[fila][j]!=' ' && b)
 		{
 			if(this.tableroJuego[fila][j]!=this.tablero[fila][j])
 			{
 				b=false;
 			}
 			j--;
 		}
 		j++;
 		String s="";
 		while(j<this.numColumnas && this.tablero[fila][j]!=' ' && b)
 		{
 			if(this.tableroJuego[fila][j]!=this.tablero[fila][j])
 			{
 				b=false;
 			}
 			else
 			{
 				s=s+String.valueOf(this.tablero[fila][j]);
 			}
 			j++;
 		}
 		if(b)
 		{
 			System.out.println("Palabra encontrada: "+s);
 		}
 	}

 	/* Confirma si se ha completado alguna palabra vertical en el autodefinido.
 	 * Mismo razonamiento que al comprobar palabras en horizontal (ComprobarPalabrasCompletasH)*/
 	private void comprobarPalabrasCompletasV(int fila, int columna)
 	{
 		int i=fila;
 		boolean b=true;
 		while(0<=i && this.tablero[i][columna]!=' ' && b)
 		{
 			if(this.tableroJuego[i][columna]!=this.tablero[i][columna])
 			{
 				b=false;
 			}
 			i--;
 		}
 		i++;
 		String s="";
 		while(i<this.numFilas && this.tablero[i][columna]!=' ' && b)
 		{
 			if(this.tableroJuego[i][columna]!=this.tablero[i][columna])
 			{
 				b=false;
 			}
 			else
 			{
 				s=s+String.valueOf(this.tablero[i][columna]);
 			}
 			i++;
 		}
 		if(b)
 		{
 			System.out.println("Palabra encontrada: "+s);
 		}
 	}

 	/* Devuelve true si el tablero de juego está completo (no quedan letrasUtilizadas por poner);
 	 * false en caso contrario*/
 	public boolean estaCompleto()
 	{
 		return this.letrasUtilizadas.size()==0;
 	}
 	
 	/*Devuelve el numero de palabras horizontales en el tablero.*/
	public int numPalabrasHorizontales()
	{
		int n=0;
		for(int i=0; i<this.numFilas;i++)
		{
			n=n+this.horizontales[i].length;
		}
		return n;
	}
	
	/*Devuelve el numero de palabras verticales en el tablero*/
	public int numPalabrasVerticales()
	{
		int n=0;
		for(int j=0;j<this.numColumnas;j++)
		{
			n=n+this.verticales[j].length;
		}
		return n;
	}

	/*Devuelve la suma de palabras horizontales más las palabras verticales*/
	public int numPalabrasTotal()
	{
		return numPalabrasHorizontales()+numPalabrasVerticales();
	}

	/*Devuelve el número de letras totales del autodefinido*/
	public int numLetras()
	{
		return this.letrasUtilizadas.size();
	}
	
	/*Devuelve la primera letra más repetida en el autodefinido*/
	public char letraMasRepetida()
	{
		char c='A';
		int a=0;
		int k;
		//obtenemos las apariciones de la primera letra que aparezca en el autodefinido
		do
		{
			k=0;
			c=(char)this.letras[a];
			for(int y=0;y<this.letrasUtilizadas.size();y++)
			{
				if(this.letrasUtilizadas.get(y)=='A')
				{
					k++;
				}
			}
			a++;
		}while(k==0);
	
		char l=' ';
		int n=0;
		for(int i=a;i<this.letras.length;i++)
		{
			l=(char)this.letras[i];
			n=0;
			for(int y=0;y<this.letrasUtilizadas.size();y++)
			{
				if(this.letrasUtilizadas.get(y)==l)
				{
					n++;
				}
			}
			if(k<n)
			{
				k=n;
				c=l;
			}
		}	
		return c;
	}

	/*Devuelve la primera letra menos repetida en el autodefinido; minimo tiene que aparecer 1 vez*/
	public char letraMenosRepetida()
	{
		char c='A';
		int a=0;
		int k;
		//obtenemos las apariciones de la primera letra que aparezca en el autodefinido
		do
		{
			k=0;
			c=(char)this.letras[a];
			for(int y=0;y<this.letrasUtilizadas.size();y++)
			{
				if(this.letrasUtilizadas.get(y)=='A')
				{
					k++;
				}
			}
			a++;
		}while(k==0);
		char l=' ';
		int n=0;
		for(int i=a;i<this.letras.length;i++)
		{
			l=(char)this.letras[i];
			for(int y=0;y<this.letrasUtilizadas.size();y++)
			{
				if(this.letrasUtilizadas.get(y)==l)
				{
					n++;
				}
			}
			if(n!=0 && n<k)
			{
				k=n;
				c=l;
			}
			n=0;
		}	
		return c;
	}

	//Método suministrado
	/*Muestra por pantalla el tablero y el tablero de juego del autodefinido*/
	public void mostrar()
	{
		System.out.println("  |0 1 2 3 4 5 6\t  |0 1 2 3 4 5 6");
		System.out.println("--+-------------\t--+-------------");
		for (int fila = 0; fila < this.numFilas; fila++)
		{
			// Muestra las filas del tablero principal
			if (fila < 10)
				System.out.print(" " + fila + "|");
			else
				System.out.print(fila + "|");
			for (int columna = 0; columna < this.numColumnas; columna++)
			{
				System.out.print(this.tablero[fila][columna] + " ");
			}

			// Muestra las filas del tablero de juego desplazado
			System.out.print("\t");
			if (fila < 10)
				System.out.print(" " + fila + "|");
			else
				System.out.print(fila + "|");
			for (int columna = 0; columna < this.numColumnas; columna++)
			{
				System.out.print(this.tableroJuego[fila][columna] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/*Muestra por pantalla todas las palabras horizontales del tablero*/
 	public void mostrarHorizontales()
	{
 		String s="";
 		for(int i=0;i<this.horizontales.length;i++)
 		{
 			for(int j=0;j<this.horizontales[i].length;j++)
 			{
 				s =s+this.horizontales[i][j]+" ";
 			}
 		}
		System.out.println(s);
	}
	
 	/*Muestra por pantalla todas las palabras verticales del tablero*/
	public void mostrarVerticales()
	{	
		String s="";
 		for(int i=0;i<this.verticales.length;i++)
 		{
 			for(int j=0;j<this.verticales[i].length;j++)
 			{
 				s=s+this.verticales[i][j]+" ";
 			}
 		}
		System.out.println(s);
	}
	
	/*Muestra por pantalla todas las letras del abecedario con el numero de apariciones en el autodefinido*/
	public void mostrarLetras()
	{
		char c=' ';
		int k=0;
		String s="";
		for(int i=0;i<this.letras.length;i++)
		{
			c=(char)this.letras[i];
			for(int y=0;y<this.letrasUtilizadas.size();y++)
			{
				if(this.letrasUtilizadas.get(y)==c)
				{
					k++;
				}
			}
			s=s+String.valueOf(c)+": "+k+" ";
			k=0;
		}	
		System.out.println(s);
	}
	public int getNumFilas() {
		return this.numFilas;
	}
	public int getNumCol() {
		return this.numColumnas;
	}
}

