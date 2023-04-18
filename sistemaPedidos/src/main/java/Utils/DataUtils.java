/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author bssil
 */
public class DataUtils {
    
    
    public static Date dataDeHoje(){
        return  new Date();
    }
    
    public static java.sql.Date sqlDateHoje(){
        Date hoje = new Date();
        return new java.sql.Date(hoje.getTime());
    }
    
    public static int semanaAtual(){
              Calendar cal = new GregorianCalendar();
              cal.setTime(dataDeHoje());
              return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static java.sql.Date primeiroDiaDaSemana(){
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDia = hoje.with(previousOrSame(DayOfWeek.SUNDAY));
        return java.sql.Date.valueOf(primeiroDia);
    }
    
    public static java.sql.Date ultimaDiaDaSemana(){
        LocalDate hoje = LocalDate.now();
        LocalDate ultimoDia = hoje.with(nextOrSame(DayOfWeek.SATURDAY));
        return java.sql.Date.valueOf(ultimoDia);
    }
    
}
