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

    private String path = "data.xls";
    private String path2 = "output.txt";
    private String inputPath = "/Users/jiu/input.txt";
    private String outputPath = "/Users/jiu/output.txt";
    //private String path = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";

    public static void main(String[] args){

        System.out.println(System.getProperty("os.name")+System.getProperty("os.version"));
        new ExcelReader().init();

    }

    public void init(){


        File file=new File(path);

        if(!file.exists())
        {
            try {
                createExcel();
                writeExcel();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
        }else {
            try {
                writeExcel();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
        }
    }


    public void writeExcel() throws WriteException, IOException, BiffException {

        String data = "";



        FileReader fr = new FileReader(outputPath);
        BufferedReader br = new BufferedReader(fr);
        //int lastRow = copySheet.getRows();


        jxl.Workbook wb = jxl.Workbook.getWorkbook(new File(path));
        WritableWorkbook workbook = jxl.Workbook.createWorkbook(new File(path), wb);
        WritableSheet sheet = workbook.getSheet(0);
        while ((data = br.readLine()) != null) {



            String[] datos = data.split(";");
            Label cell = null;

            int lastRow = sheet.getRows();

            for(int i=0;i<datos.length;i++){
                //System.out.println("datos["+i+"]: "+datos[i]);
                cell =  new Label(i,lastRow,datos[i]);
                System.out.println("i: "+i);
                System.out.println("lastRow: "+lastRow);
                System.out.println(datos[i]);
                sheet.addCell(cell);

                //aCopySheet.addCell(anotherWritableCel2);



            }
        }
        workbook.write();
        workbook.close();





    }

    public void createExcel() throws IOException, WriteException {

        WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(path));
        writableWorkbook.createSheet("firstexcel.xls",0);

        writableWorkbook.write();
        writableWorkbook.close();
    }

}
