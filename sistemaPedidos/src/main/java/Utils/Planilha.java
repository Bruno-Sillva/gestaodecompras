/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author bssil
 */
public class Planilha {
    

    public Planilha(){}
    
   public void lerPlanilha(File arquivoPlanilha){
       try {
           
            FileInputStream fis = new FileInputStream(arquivoPlanilha);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
 
            XSSFSheet planilha = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = planilha.iterator(); 
            while(rowIterator.hasNext()){
                Row linha = rowIterator.next();
                Iterator<Cell> cellIterator = linha.iterator();
                while(cellIterator.hasNext()){
                Cell celula = cellIterator.next();
               
                switch(celula.getCellType()){
                    case STRING: 
                        System.out.println("Valor " + celula.getStringCellValue()); break;
                    case NUMERIC:
                         System.out.println("Valor " + celula.getNumericCellValue()); break;
                }
                }
            }
       } catch (Exception e) {
           System.out.println(e);
       }
      
   }
   
   
   public static FileOutputStream exportarParaPlanilha(JTable tabela, String caminhoDoArquivo) throws FileNotFoundException{
       XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream file = new FileOutputStream(caminhoDoArquivo.concat(".xlsx"));
        XSSFSheet sheet = workbook.createSheet("export");
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        sheet.createRow(0); 
       for(int coluna=0;coluna<model.getColumnCount();coluna++){
       sheet.getRow(0).createCell(coluna).setCellValue(model.getColumnName(coluna));
       }
       for(int linha=0;linha<model.getRowCount();linha++){
          sheet.createRow(linha+1);
           for(int coluna=0;coluna<model.getColumnCount();coluna++){
              if(model.getValueAt(linha, coluna) != null){
                sheet.getRow(linha+1).createCell(coluna).setCellValue(model.getValueAt(linha, coluna).toString());
              }else{
               sheet.getRow(linha+1).createCell(coluna).setCellValue("");
              }
          }
       }
       try {
           workbook.write(file);
           workbook.close();
           file.close();    
    
           
       } catch (Exception e) {
       }
       return file;
   }




}
