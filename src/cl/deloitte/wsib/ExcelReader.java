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
    //private String excelPath = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";
    private String excelPath = "AcesViewer.xls";
    private String inputPath = "input.txt";
    private String outputPath = "output.txt";
    private String data = "";
    private FileReader fileReader = null;
    private BufferedReader bufferedReader = null;
    private Workbook wb = null;
    private WritableWorkbook workbook = null;
    private WritableSheet sheet = null;
    private Label cell = null;

    public static void main(String[] args){
        //System.out.println(System.getProperty("os.name")+System.getProperty("os.version"));
        new ExcelReader().init();
    }

    public void init(){
        File file=new File(excelPath);

        if(!file.exists()){
            createExcel();
            writeExcel();
        }else {
            writeExcel();
        }
    }

    public void createExcel(){

        try {
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(excelPath));
            writableWorkbook.createSheet("AcesViewer.xls", 0);
            writableWorkbook.write();
            writableWorkbook.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch(WriteException e){
            e.printStackTrace();
        }
    }

    public void writeExcel() {

        try {
            fileReader = new FileReader(outputPath);
            bufferedReader = new BufferedReader(fileReader);
            wb = jxl.Workbook.getWorkbook(new File(excelPath));
            workbook = jxl.Workbook.createWorkbook(new File(excelPath), wb);
            sheet = workbook.getSheet(0);

            while ((data = bufferedReader.readLine()) != null) {
                String[] datos = data.split(";");
                int lastRow = sheet.getRows();

                for (int i = 0; i < datos.length; i++) {
                    cell = new Label(i, lastRow, datos[i]);
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
}