package cl.deloitte.wsib;

import java.io.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Created by jinostrozau on 2017-06-23.
 */
public class ExcelReader {

    public static void main(String[] args){

        String path = "C://JIU/WSIB/Reportes/AcesViewer Unnumbered DocsAnalysis.xlsx";

        try{
            Workbook wb = WorkbookFactory.create(new File(path));
            System.out.println(wb.getSheetAt(0).getRow(2).getCell(1));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(InvalidFormatException e){
            e.printStackTrace();
        }
    }
}
