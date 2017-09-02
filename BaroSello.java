import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.fusesource.jansi.AnsiConsole;

//import org.fusesource.jansi.AnsiConsole;
//import static org.fusesource.jansi.Ansi.*;
//import static org.fusesource.jansi.Ansi.Color.*;

public class BaroSello {

	public static void main(String[] args) {
		
		try {
			String[] inputValue = null;
			String s = "0";
			String param = "";
			
			// Controllo il numero dei parametri passati in input
			if(args.length > 4) {
				System.out.println("Sintassi non corretta!");
				messaggioIstruzioni();
				return;
			}
			
			// Controllo il primo parametro passato in input
			if(!isArrayOrIntSequence(args[0], 0, 100)) {
				System.out.println("Sintassi non corretta!");
				messaggioIstruzioni();
			}
			
			boolean justMultipleOfThreeFive = false;
			boolean printedColored = false;
			boolean addMultipleOfSeven = false;
			boolean splitOutput = false;
			HashMap<String, String> outputPrint = null;
			
			// Controllo i parametri successivi passati in input
			for(int i=1; i<args.length; i++) {
				if(!isValidParam(args[i])) {
					System.out.println("Sintassi non corretta!");
					messaggioIstruzioni();
					return;
				} else {
					param = args[i];
				}
				if("-m35".equals(param)) {
					justMultipleOfThreeFive = true;
				}
				if("-m357".equals(param)) {
					addMultipleOfSeven = true;
				}
				if("-color".equals(param)) {
					printedColored = true;
				}
				if("-splito".equals(param)) {
					splitOutput = true;
					outputPrint = new HashMap<String, String>();
					outputPrint.put("Baro", "console");
					outputPrint.put("BaroSello", "file");
					outputPrint.put("Sello", "console,file");
				}
			}
			
			int[] numbers = null;
			
			try {
				inputValue = args[0].split(",");
				numbers = new int[inputValue.length];
				for(int i=0; i<inputValue.length; i++) {
					numbers[i] = Integer.parseInt(inputValue[i]);
				}
			} catch (Exception e) {
				if("array".equals(inputValue[0])) {
					numbers = new int[101];
					
					for (int i=0; i<numbers.length; i++) {
						numbers[i] = i;
					}					
				}
			}
			
			if(splitOutput) {
				print(numbers, outputPrint);
			} else {
				for (int i=0; i<numbers.length; i++) {
					
					if(numbers[i]!=0) {
						if(justMultipleOfThreeFive) {
							s = builtStringBaroSello(numbers[i]);
						} else {
							s = builtString(numbers[i], addMultipleOfSeven);
						}
					}					
					
					if(printedColored) {
						printColored(s);
					} else {
						System.out.print(s);
					}
					
					if(i<numbers.length-1) {
						System.out.print(",");
					} else {
						System.out.print(" ");
					}
				}
			}			
		} catch (ArrayIndexOutOfBoundsException aex) {
			System.out.println("Sintassi non corretta! Inserire almeno un parametro!");
			messaggioIstruzioni();
		}
	}
	
	/**
	 * Funzione per splittare la stampa su piattaforme diverse
	 * 
	 * @param numbers Array di interi passata in input
	 * @param hashMap Coppia chiave-valore contenente la mappatura del multiplo e della piattaforma di stampa
	 */
	public static void print(int[] numbers, HashMap<String, String> hashMap) {
		String stringBuilt = "0";
		String[] key = new String[hashMap.size()];
		String[] value = new String[hashMap.size()];
		
		Iterator<Entry<String, String>> it = hashMap.entrySet().iterator();
		
		int index=0;
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			value[index] = entry.getValue().toString();			
			key[index] = entry.getKey().toString();
			index++;
		}
		
		try {
			FileWriter w = new FileWriter("output.txt");
			for (int i=0; i<numbers.length; i++) {
				if(numbers[i]!=0) {
					stringBuilt = builtString(numbers[i]);
				}
				
				boolean printed = false;
				
				for(int j=0; j<key.length; j++) {					
					if(stringBuilt.equals(key[j])) {
						printed = true;
						String[] output = value[j].split(",");
						if(i<numbers.length-1) {
							stringBuilt += ",";
						} else {
							stringBuilt += " ";
						}
						for(int k=0; k<output.length; k++) {
							if("file".equals(output[k]) && output.length==1) {
								w.write(stringBuilt);
								if(i<numbers.length-1) {
									System.out.print(numbers[i]+",");
								} else {
									System.out.print(numbers[i]+" ");
								}
							}
							if("console".equals(output[k]) && output.length==1) {
								if(i<numbers.length-1) {
									w.write(numbers[i]+",");
								} else {
									w.write(numbers[i]+" ");
								}
								System.out.print(stringBuilt);
							}
						}
						if(output.length>1) {
							w.write(stringBuilt);
							System.out.print(stringBuilt);
						}
					}
				}
				
				if(!printed) {
					if(i<numbers.length-1) {
						stringBuilt += ",";
					} else {
						stringBuilt += " ";
					}
					w.write(stringBuilt);
					System.out.print(stringBuilt);
				}
			}
			w.flush();
			w.close();
		} catch (IOException ex) {
			System.out.println("Errore nella scrittura su file");
		}
	}
	
	/**
	 * Funzione che consente di stampare le stringhe colorate utilizzando la console ANSI
	 * 
	 * @param inputString Stringa passata in input
	 */
	public static void printColored(String inputString) {
		AnsiConsole.systemInstall();
		
		if(inputString.equals("Baro")) {
			System.out.print(ansi().fg(GREEN).a(inputString).reset());
		} else if(inputString.equals("Sello")) {
			System.out.print(ansi().fg(RED).a(inputString).reset());
		} else if(inputString.equals("BaroSello")) {
			System.out.print(ansi().fg(BLUE).a(inputString).reset());
		} else if(inputString.equals("Nardo")) {
			System.out.print(ansi().fg(YELLOW).a(inputString).reset());
		} else {
		    System.out.print(inputString);
		}
		
		AnsiConsole.systemUninstall();
	}
	
	/**
	 * Funzione indica se il numero passato in input è multiplo di 3
	 * 
	 * @param i Numero intero da valutare
	 * @return true se il numero è multiplo di 3, false altrimenti
	 */
	public static boolean isMultipleOfThree(int i) {

		return (i % 3 == 0);
	}

	/**
	 * Funzione indica se il numero passato in input è multiplo di 5
	 * 
	 * @param i Numero intero da valutare
	 * @return true se il numero è multiplo di 5, false altrimenti
	 */
	public static boolean isMultipleOfFive(int i) {

		return (i % 5 == 0);
	}

	/**
	 * Funzione indica se il numero passato in input è multiplo di 7
	 * 
	 * @param i Numero intero da valutare
	 * @return true se il numero è multiplo di 7, false altrimenti
	 */
	public static boolean isMultipleOfSeven(int i) {

		return (i % 7 == 0);
	}

	/**
	 * Funzione che verifica se il numero passato in input è multiplo di 3, 5 o 7 a seconda del parametro passato in input.
	 * In caso positivo restituisce la stringa corrispondente.
	 * 
	 * @param i Numero intero da valutare
	 * @param param Parametro passato in input da console
	 * @return Output stringa costruita
	 */
	public static String builtString(int i, boolean addMultipleOfSeven) {

		String ret = "";

		if (isMultipleOfThree(i)) {
			ret = "Baro";
		}
		if (isMultipleOfFive(i)) {
			ret = "Sello";
		}
		if (addMultipleOfSeven && isMultipleOfSeven(i)) {
			ret = "Nardo";
		}
		if (isMultipleOfThree(i) && isMultipleOfFive(i)) {
			ret = "BaroSello";
		}
		if (ret.isEmpty()) {
			ret += i;
		}

		return ret;
	}
	
	/**
	 * Funzione che verifica se il numero passato in input è multiplo di 3 e 5 a seconda del parametro passato in input.
	 * In caso positivo restituisce la stringa corrispondente.
	 * 
	 * @param i Numero intero da valutare
	 * @return Output stringa costruita
	 */
	public static String builtString(int i) {

		String ret = "";

		if (isMultipleOfThree(i)) {
			ret += "Baro";
		}
		if (isMultipleOfFive(i)) {
			ret += "Sello";
		}
		if (ret.isEmpty()) {
			ret += i;
		}

		return ret;
	}
	
	/**
	 * Funzione che verifica se il numero passato in input è multiplo di 3 e 5.
	 * In caso positivo restituisce la stringa BaroSello.
	 * 
	 * @param i Numero intero da valutare
	 * @return Output stringa costruita
	 */
	public static String builtStringBaroSello(int i) {

		String ret = "";

		if (isMultipleOfThree(i) && isMultipleOfFive(i)) {
			ret += "BaroSello";
		}
		if (ret.isEmpty()) {
			ret += i;
		}

		return ret;
	}
	
	/**
	 * Funzione che verifica se il numero passato in input è multiplo di 3.
	 * In caso positivo restituisce la stringa Baro.
	 * 
	 * @param i Numero intero da valutare
	 * @return Output stringa costruita
	 */
	public static String builtStringBaro(int i) {

		String ret = "";

		if (isMultipleOfThree(i)) {
			ret += "BaroSello";
		}
		if (ret.isEmpty()) {
			ret += i;
		}

		return ret;
	}
	
	/**
	 * Funzione che controlla se il primo parametro in input è la stringa "array" oppure una sequenza di
	 * numeri interi separati da "," compresi nel range impostato
	 * 
	 * @param args Stringa che corrisponde ad arg[0]
	 * @param rangeMin Valore del range minimo
	 * @param rangeMax Valore del range massimo
	 * @return true se ok, false altrimenti
	 */
	public static boolean isArrayOrIntSequence(String args, int rangeMin, int rangeMax) {
		boolean ret = false;
		int numInt=-1;
		String[] value = args.split(",");
		
		for(int i=0; i<value.length; i++) {
			try {
				numInt = Integer.parseInt(value[i]);
				if(numInt>=rangeMin && numInt<=rangeMax) {
					ret=true;
				}
			} catch (Exception e) {
				if("array".equals(value[i])) {
					ret = true;
				} else {
					ret = false;
					return ret;
				}
			}
		}		
		return ret;		
	}
	
	/**
	 * Controllo se i parametri passati in input sono validi
	 * 
	 * @param inputParam Stringa che rappresenta il parametro in ingresso
	 * @return true se il parametro in input è valido, false altrimenti
	 */
	public static boolean isValidParam(String inputParam) {
		boolean ret = false;
		if("-m357".equals(inputParam)) {
			ret=true;
		} else if("-m35".equals(inputParam)) {
			ret=true;
		} else if("-color".equals(inputParam)) {
			ret=true;
		} else if("-splito".equals(inputParam)) {
			ret=true;
		}
		return ret;
	}
	
	/**
	 * Funzione che stampa le istruzioni per guida l'esecuzione
	 */
	public static void messaggioIstruzioni() {
		System.out.println("Inserire uno o più numeri interi compresi tra 0 e 100 (separati da virgola e senza ulteriori spazi) oppure digitare \"array\" per visualizzare l'intera sequenza.");
		System.out.println("Sintassi: java -cp jansi-1.16.jar;. BaroSello <num1>,<num2> oppure java -cp jansi-1.16.jar;. BaroSello array");
		System.out.println("[-m357] restituisce \"Nardo\" se l'intero è multiplo di 7");
		System.out.println("[-m35] stampo solo i \"BaroSello\" multipli di 3 e 5");
		System.out.print("[-color] stampo i dati con colori diversi");
		System.out.print("[-splito] stampo i dati su output differenti");
	}
}
