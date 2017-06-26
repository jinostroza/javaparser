package cl.deloitte.wsib;

import java.io.*;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by jinostrozau on 2017-06-23.
 */
public class ExcelReader {

    private String path = "AcesViewer.xls";
    //private String path = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";
    private String inputPath = "input.txt";
    private String outputPath = "output.txt";

    public static void main(String[] args){

        System.out.println(System.getProperty("os.name")+System.getProperty("os.version"));
        new ExcelReader().init();
    }

    public void init(){

        File file=new File(path);

        if(!file.exists()){
            createExcel();
            writeExcel();
        }else {
            writeExcel();
        }
    }


    public void writeExcel() {

        String data = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Workbook wb = null;
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;

        try {
            fileReader = new FileReader(outputPath);
            bufferedReader = new BufferedReader(fileReader);
            wb = jxl.Workbook.getWorkbook(new File(path));
            workbook = jxl.Workbook.createWorkbook(new File(path), wb);
            sheet = workbook.getSheet(0);

            while ((data = bufferedReader.readLine()) != null) {

                String[] datos = data.split(";");
                Label cell = null;

                int lastRow = sheet.getRows();

                for (int i = 0; i < datos.length; i++) {
                    cell = new Label(i, lastRow, datos[i]);
                    System.out.println("i: " + i);
                    System.out.println("lastRow: " + lastRow);
                    System.out.println(datos[i]);
                    sheet.addCell(cell);
                }
            }
            workbook.write();
            workbook.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch(WriteException e){
            e.printStackTrace();
        }catch(BiffException e){
            e.printStackTrace();
        }
    }

    public void createExcel(){

        try {
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(path));
            writableWorkbook.createSheet("AcesViewer.xls", 0);
            writableWorkbook.write();
            writableWorkbook.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch(WriteException e){
            e.printStackTrace();
        }
    }
}
