package cl.deloitte.wsib;

import java.io.*;

import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by jinostrozau on 2017-06-23.
 */
public class ExcelReader {

    private String path = "/JIU/Test.xls";
    //private String path = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";

    public static void main(String[] args){

        try {
            new ExcelReader().writeExcel();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }


    public void writeExcel() throws WriteException, IOException, BiffException {
        jxl.Workbook wb = jxl.Workbook.getWorkbook(new File(path));
        WritableWorkbook copy = jxl.Workbook.createWorkbook(new File(path), wb);

        WritableSheet copySheet = copy.getSheet(0);


        int lastRow = copySheet.getRows();
        System.out.println("Last row: "+copySheet.getRows());

        Label anotherWritableCell =  new Label(0,lastRow,"125");


        copySheet.addCell(anotherWritableCell);

        //aCopySheet.addCell(anotherWritableCel2);

        copy.write();
        copy.close();

    }

}
