package crea_json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Crea_json {

	public static void main(String[] args) {
		String pathLettura = "C:\\Users\\luca\\Desktop\\file prove\\classe_2.txt";
		String pathClasses = "C:\\Users\\luca\\Desktop\\file prove\\classi_json\\classes.json";
		String pathJson = "C:\\Users\\luca\\Desktop\\file prove\\classi_json\\classi\\";
		
		try {
			File fileClasses = new File(pathClasses);
			File fileClasses_data;
			
			//filewriter e bufferedwriter per il file contenente i nomi delle classi
			FileWriter fwClasses = new FileWriter(fileClasses);
			BufferedWriter bwClasses = new BufferedWriter(fwClasses);
			
			//filewriter e bufferedwriter per ogni json
			FileWriter fwClasses_data;
			BufferedWriter bwClasses_data = null;
						
			//inizializzo classes.json
			bwClasses.write("[\n");
			bwClasses.flush();
			
			File fileLettura = new File(pathLettura);
			FileReader reader = new FileReader(fileLettura);
			
			Scanner in = new Scanner(reader);
			
			boolean primaRigaMetodo = false;
			boolean primoAttributo = true;
			String rigaLetta;		
			
			byte[] ib = new byte[] { (byte) 0x42, (byte) 0xE4, (byte) 0x72 };
			String rigaModificata = new String(ib,"UTF-8");
			int i=0, x=0;
			while(in.hasNextLine()) {
				rigaLetta=in.nextLine();				
								
				if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("-") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";")) )))) {
					//attributo					
					String nome = "", tipo = "";
					int k=0;
					while((rigaLetta.charAt(k)!=':')&&(rigaLetta.charAt(k)!=';')) {
						if(rigaLetta.charAt(k)!='-') {
							nome=nome+rigaLetta.charAt(k);
						}							
						k++;
					}
					k++;
					while(rigaLetta.charAt(k)!='<') {
						if(rigaLetta.charAt(k)!=' ') {
							tipo=tipo+rigaLetta.charAt(k);
						}							
						k++;
					}
					
					if(primoAttributo){
						bwClasses_data.write("\t\t{\n");
					} else {
						bwClasses_data.write(",\n\t\t{\n");
					}
									
					bwClasses_data.write("\t\t\t\"name\": \""+nome+"\",\n");
					bwClasses_data.write("\t\t\t\"type\": \""+tipo+"\",\n");
					bwClasses_data.write("\t\t\t\"visibility\": \"private\",\n");
					bwClasses_data.write("\t\t\t\"scope\": \"class\"\n");
					
					if(rigaLetta.charAt(k)=='<') {
						primaRigaMetodo = true;
					}					
					
					bwClasses_data.write("\t\t}");
					bwClasses_data.flush();			
					primoAttributo = false;
					
				} else if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("+") && rigaLetta.contains("(") && rigaLetta.contains(")") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";") ))))){
						//metodo
						primoAttributo=true;
						if(primaRigaMetodo) {
							
							bwClasses_data.write("\n\t],\n");
							bwClasses_data.write("\t\"methods\": [\n");
							bwClasses_data.write("\t\t{\n");
							bwClasses_data.flush();
							primaRigaMetodo=false;
						}
						////////
					
					} else if((rigaLetta.contains("</text>")) && !(rigaLetta.contains("..") && !(rigaLetta.contains("1.*")))){
						//nome classe
						primoAttributo=true;
						rigaModificata= rigaLetta.replace("</text>", "");
						bwClasses.write("\t\""+rigaModificata+"\",\n");
						
						//creo nuovo file per ogni json
						fileClasses_data = new File(pathJson+rigaModificata+".json");
						fwClasses_data = new FileWriter(fileClasses_data);
						bwClasses_data = new BufferedWriter(fwClasses_data);
						//inserisco i primi dati nel json
						
						bwClasses_data.write("{\n");
						bwClasses_data.write("\t\"key\": \""+x+"\",\n");
						bwClasses_data.write("\t\"name\": \""+rigaModificata+"\",\n");
						bwClasses_data.write("\t\"description\": \"descrizione\",\n");
						bwClasses_data.write("\t\"stereotype\": \"\",\n");
						bwClasses_data.write("\t\"subname\": \"\",\n");
						bwClasses_data.write("\t\"isAbstract\": \"false\",\n");
						bwClasses_data.write("\t\"properties\": [\n");
						bwClasses_data.flush();
							
						x++;
					}
				i++;
			}
			bwClasses.write("]");
			bwClasses.flush();
			System.out.println("numeri di riga processati: "+i);
			x--;
			System.out.println("numero classi: "+x);
			
			//chiusura dei buffer
			in.close();
			reader.close();
			bwClasses.close();
			bwClasses_data.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
