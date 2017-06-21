package cl.deloitte.wsib;

import java.io.*;


/**
 * Created by jinostrozau on 2016-07-18.
 */
public class JavaParser {

    final static String SEPARADORLINEA = "#@#@#";
    final static String SEPARADORLINEA2 = "\n";
    final static String SEPARADORCOLUMNA = "\t";
    final static String queuepathKofaxSYNC = "ESB.SERVICES.KOFAX.SYNC.ERROR";
    final static String queuepathKofaxTCM = "ESB.SERVICES.KOFAX.TO.TCM.ERROR";
    final static String queuepathTCMClaimRequest = "ESB.TCM.DOC.CLAIM.REQUEST.ERROR";
    final static String pathKofaxSYNC = "C://JIU/WSIB/Queues/"+queuepathKofaxSYNC + ".txt";
    final static String pathKofaxTCM = "C://JIU/WSIB/Queues/"+queuepathKofaxTCM +".txt";
    final static String pathTCMClaimRequest = "C://JIU/WSIB/Queues/"+queuepathTCMClaimRequest +".txt";
    final static String tags1[] = {"<Name>","</Name><Values>"};
    final static String tags2[] = {"<name>","</name><value>"};
    final static String tags3[] = {"<UserException>","</UserException>"};

    String path = "";
    String DocUnitID = "";
    String DocumentID = "";
    String KofaxExportImageFilePath = "";
    String KofaxExportXmlFilePath = "";
    String DocumentClassName = "";

    public static void main(String[] args) {

        JavaParser javaParser  = new JavaParser();
        javaParser.inicializaTags(3); //1: Kofax Sync Error |  2: Kofax TCM ERROR   | 3:  TCM DOC CLAIM REQUEST
        javaParser.reemplazaSeparador();
        javaParser.extraeData();
    }

    public  void inicializaTags(int indicador){
        String tags[] = null;

        if(indicador == 1) {
            tags = tags1;
            path = pathKofaxSYNC;
        }else if (indicador == 2){
            tags = tags2;
            path = pathKofaxTCM;
        }else if(indicador == 3) {
            tags = tags1;
            path = pathTCMClaimRequest;
        }

        this.DocUnitID = tags[0].concat("DocUnitID").concat(tags[1]);
        this.DocumentID = tags[0].concat("DocumentID").concat(tags[1]);
        this.KofaxExportImageFilePath = tags[0].concat("KofaxExportImageFilePath").concat(tags[1]);
        this.KofaxExportXmlFilePath = tags[0].concat("KofaxExportXmlFilePath").concat(tags[1]);
        this.DocumentClassName = tags[0].concat("DocumentClassName").concat(tags[1]);
    }

    public void reemplazaSeparador() {
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        String xmlOriginal = "";
        String xmlModified = "";
        boolean hasOldSeparator = false;

        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            while ((xmlOriginal = bufferedReader.readLine()) != null) {
                if(xmlOriginal.contains(SEPARADORLINEA)){
                    hasOldSeparator = true;
                    xmlModified = xmlOriginal.replace(SEPARADORLINEA, SEPARADORLINEA2);
                }
            }
            bufferedReader.close();
            fileReader.close();

            if (hasOldSeparator){
                fileWriter = new FileWriter(path); //writes and modifies the file with a breakline
                fileWriter.write(xmlModified);
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extraeData() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String xmlData = "";
        int cont = 0;

        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            while ((xmlData = bufferedReader.readLine()) != null) {

                String docunitId = xmlData.substring(xmlData.indexOf(DocUnitID) + DocUnitID.length(), xmlData.indexOf(DocUnitID) + DocUnitID.length() + 8);
                String documentID = xmlData.substring(xmlData.indexOf(DocumentID) + DocumentID.length(), xmlData.indexOf(DocumentID) + DocumentID.length() + 8);
                String kofaxExportImageFilePath = xmlData.substring(xmlData.indexOf(KofaxExportImageFilePath) + KofaxExportImageFilePath.length(), xmlData.indexOf(KofaxExportImageFilePath) + KofaxExportImageFilePath.length() + 54);
                String kofaxExportXmlFilePath = xmlData.substring(xmlData.indexOf(KofaxExportXmlFilePath) + KofaxExportXmlFilePath.length(), xmlData.indexOf(KofaxExportXmlFilePath) + KofaxExportXmlFilePath.length() + 48);
                String docClassName = xmlData.substring(xmlData.indexOf(DocumentClassName) + DocumentClassName.length(), xmlData.indexOf(DocumentClassName) + DocumentClassName.length() + 6);
                String extractedData = docunitId + SEPARADORCOLUMNA + documentID + SEPARADORCOLUMNA + kofaxExportImageFilePath + SEPARADORCOLUMNA + kofaxExportXmlFilePath + SEPARADORCOLUMNA + docClassName + SEPARADORCOLUMNA;
                cont++;
                System.out.println(extractedData);
            }
            System.out.println("Total of Messages: "+cont);
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}