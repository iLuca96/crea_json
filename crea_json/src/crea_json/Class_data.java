package crea_json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Class_data {

	public static void main(String[] args) {
		String pathLettura = "C:\\Users\\luca\\Desktop\\file prove\\classe_2.txt";
		String pathClasses = "C:\\Users\\luca\\Desktop\\file prove\\classi_json\\classes.json";
		String pathClasses_data = "C:\\Users\\luca\\Desktop\\file prove\\classi_json\\classes_data.json";
				
		try {
			File fileClasses = new File(pathClasses);
			File fileClasses_data_finale = new File(pathClasses_data);
			
			//filewriter e bufferedwriter per il file contenente i nomi delle classi
			FileWriter fwClasses = new FileWriter(fileClasses);
			BufferedWriter bwClasses = new BufferedWriter(fwClasses);
			
			FileWriter fwClasses_data_finale = new FileWriter(fileClasses_data_finale);
			BufferedWriter bwClasses_data_finale = new BufferedWriter(fwClasses_data_finale);
/////////////////////////////////
			bwClasses_data_finale.write("[\n");
			bwClasses_data_finale.flush();
/////////////////////////////////
						
			//inizializzo classes.json
			bwClasses.write("[\n");
			bwClasses.flush();
			
			File fileLettura = new File(pathLettura);
			FileReader reader = new FileReader(fileLettura);
			
			Scanner in = new Scanner(reader);
			
			boolean primaRigaMetodo = false;
			boolean primoAttributo = true;
			
			boolean noAttributo = false;
			boolean noMetodo = false;
			
			boolean ultimoMetodo = false;
			String rigaLetta;		
			
			byte[] ib = new byte[] { (byte) 0x42, (byte) 0xE4, (byte) 0x72 };
			String rigaModificata = new String(ib,"UTF-8");
			int i=0, x=0;
			while(in.hasNextLine()) {
				rigaLetta=in.nextLine();				
								
				if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("-") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";")) )))) {
					//attributo
					noAttributo=false;
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
						bwClasses_data_finale.write("\t\t{\n");
					} else {
						bwClasses_data_finale.write(",\n\t\t{\n");
					}
									
					bwClasses_data_finale.write("\t\t\t\"name\": \""+nome+"\",\n");
					bwClasses_data_finale.write("\t\t\t\"type\": \""+tipo+"\",\n");
					bwClasses_data_finale.write("\t\t\t\"visibility\": \"private\",\n");
					bwClasses_data_finale.write("\t\t\t\"scope\": \"class\"\n");
					
					if(rigaLetta.charAt(k)=='<') {
						primaRigaMetodo = true;
					}					
					
					bwClasses_data_finale.write("\t\t}");
					bwClasses_data_finale.flush();			
					primoAttributo = false;
					
				} else if((rigaLetta.contains("</text>"))&&(rigaLetta.contains("+") && rigaLetta.contains("(") && rigaLetta.contains(")") &&  ((rigaLetta.contains(":") || (rigaLetta.contains(";") ))))){
						//metodo
						noMetodo=false;
						primoAttributo=true;
						ultimoMetodo=true;
						if(primaRigaMetodo||noAttributo) {							
							bwClasses_data_finale.write("\n\t],\n");
							bwClasses_data_finale.write("\t\"methods\": [\n");
							
							bwClasses_data_finale.flush();
							primaRigaMetodo=false;
						}else {
							bwClasses_data_finale.write(",\n");
						}
						int k=0;
						String nomeMetodo = "";						
						while(rigaLetta.charAt(k)!='(') {
							if(rigaLetta.charAt(k)!='+') {
								nomeMetodo=nomeMetodo+rigaLetta.charAt(k);
							}							
							k++;							
						}
						bwClasses_data_finale.write("\t\t{\n");
						bwClasses_data_finale.write("\t\t\t\"name\": \""+nomeMetodo+"\",\n");
						String ritornoMetodo = "";
						while(rigaLetta.charAt(k)!='<') {
							if((rigaLetta.charAt(k)=='(')&&(rigaLetta.charAt(k+1)==')')) {
								//get
								k+=2;
								while(rigaLetta.charAt(k)!='<') {
									if((rigaLetta.charAt(k)!=' ')&&(rigaLetta.charAt(k)!=':')) {
										ritornoMetodo=ritornoMetodo+rigaLetta.charAt(k);										
									}
									k++;
								}								
								bwClasses_data_finale.write("\t\t\t\"type\": \""+ritornoMetodo+"\",\n");
								bwClasses_data_finale.write("\t\t\t\"visibility\": \"public\"\n");
								
								break;
							} else {
								//set
								bwClasses_data_finale.write("\t\t\t\"visibility\": \"public\",\n");
								bwClasses_data_finale.write("\t\t\t\"parameters\": [\n");
								while(rigaLetta.charAt(k)!=')') {
									bwClasses_data_finale.write("\t\t\t\t{\n");
									String nomeParametro = "";
									String tipoParametro = "";
									while(rigaLetta.charAt(k)!=':') {
										if(rigaLetta.charAt(k)!='('&&rigaLetta.charAt(k)!=','&&rigaLetta.charAt(k)!=' ') {
											nomeParametro = nomeParametro+rigaLetta.charAt(k);
										}										
										k++;
									}
									while(rigaLetta.charAt(k)!=','&&rigaLetta.charAt(k)!=')') {
										if(rigaLetta.charAt(k)!=':'&&rigaLetta.charAt(k)!=' ') {
											tipoParametro = tipoParametro+rigaLetta.charAt(k);
										}
										k++;
									}								
									
									bwClasses_data_finale.write("\t\t\t\t\t\"name\": \""+nomeParametro+"\",\n");
									bwClasses_data_finale.write("\t\t\t\t\t\"type\": \""+tipoParametro+"\"\n");
									if(rigaLetta.charAt(k)==')') {
										bwClasses_data_finale.write("\t\t\t\t}\n");
									}else{
										bwClasses_data_finale.write("\t\t\t\t},\n");
									}													
								}						
								bwClasses_data_finale.write("\t\t\t]\n");
								bwClasses_data_finale.flush();
								break;
							}
						}						
						bwClasses_data_finale.write("\t\t}");						
						bwClasses_data_finale.flush();			
					
					} else if((rigaLetta.contains("</text>")) && !(rigaLetta.contains("..") && !(rigaLetta.contains("1.*")))){
						//nome classe
						if(ultimoMetodo) {
							bwClasses_data_finale.write("\n\t]\n");
							bwClasses_data_finale.write("},\n");
							bwClasses_data_finale.flush();
							ultimoMetodo=false;
						}
						if(noAttributo&&noMetodo) {
							bwClasses_data_finale.write("\t],\n");
							bwClasses_data_finale.write("\t\"methods\": [\n");
							bwClasses_data_finale.write("\t]\n");
							bwClasses_data_finale.write("},\n");
							bwClasses_data_finale.flush();
						}
						
						primoAttributo=true;
						primaRigaMetodo=true;
						
						rigaModificata= rigaLetta.replace("</text>", "");
						bwClasses.write("\t\""+rigaModificata+"\",\n");
						
						//inserisco i primi dati nel json
						
						bwClasses_data_finale.write("{\n");
						bwClasses_data_finale.write("\t\"key\": \""+x+"\",\n");
						bwClasses_data_finale.write("\t\"name\": \""+rigaModificata+"\",\n");
						bwClasses_data_finale.write("\t\"description\": \"descrizione\",\n");
						bwClasses_data_finale.write("\t\"stereotype\": \"\",\n");
						bwClasses_data_finale.write("\t\"subname\": \"\",\n");
						bwClasses_data_finale.write("\t\"isAbstract\": \"false\",\n");
						bwClasses_data_finale.write("\t\"properties\": [\n");
						bwClasses_data_finale.flush();
						
						noAttributo=true;
						noMetodo=true;
							
						x++;
					}
				i++;
			}
			bwClasses_data_finale.write("\n\t]\n}");
			bwClasses_data_finale.flush();
			
			bwClasses_data_finale.write("]");
			bwClasses_data_finale.flush();
			bwClasses.write("]");
			bwClasses.flush();
			System.out.println("numeri di riga processati: "+i);
			x--;
			System.out.println("numero classi: "+x);
			
			//chiusura dei buffer
			in.close();
			reader.close();
			bwClasses.close();
			bwClasses_data_finale.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
