package cl.deloitte.wsib;

import java.io.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Created by jinostrozau on 2017-06-23.
 */
public class ExcelReader {

    private String path = "/JIU/AcesViewer.xlsx";
    //private String path = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";

    public static void main(String[] args){

        new ExcelReader().leeExcel();

    }

    public void leeExcel(){

        try{
            Workbook wb = WorkbookFactory.create(new File(path));
            System.out.println(wb.getSheetAt(0).getRow(0).getCell(0));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(InvalidFormatException e){
            e.printStackTrace();
        }
    }
}
