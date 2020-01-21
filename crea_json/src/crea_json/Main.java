package crea_json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String pathLettura = "C:\\Users\\luca\\Desktop\\file prove\\classe_2.txt";
		String pathScrittura = "C:\\Users\\luca\\Desktop\\file prove\\classi_json\\prova_json.json";
		try {
			File fileScrittura = new File(pathScrittura);
			FileWriter fw = new FileWriter(fileScrittura);
			BufferedWriter bw = new BufferedWriter(fw);
			
			File fileLettura = new File(pathLettura);
			FileReader reader = new FileReader(fileLettura);
			Scanner in = new Scanner(reader);
			
			String rigaLetta;
			int i=0, x=0;
			while(in.hasNextLine()) {
				rigaLetta=in.nextLine();
				if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("-") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";")) )))) {
					//attributo
					bw.write(rigaLetta+"\n");
					bw.flush();
				} else if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("+") && rigaLetta.contains("(") && rigaLetta.contains(")") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";") ))))){
						//metodo
						bw.write(rigaLetta+"\n");
						bw.flush();
						} else if((rigaLetta.contains("</text>")) && !(rigaLetta.contains("..") && !(rigaLetta.contains("1.*")))){
							//nome classe
							x++;
							System.out.println(x+". nome classe: "+rigaLetta);
							bw.write("\n\n"+rigaLetta+"\n");
							bw.flush();
						}
				i++;
			}
			System.out.println("numeri di riga processati: "+i);
			System.out.println("numero classi: "+x);
			in.close();
			reader.close();
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
