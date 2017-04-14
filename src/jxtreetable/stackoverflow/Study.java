package jxtreetable.stackoverflow;
 
import java.util.ArrayList; 
import java.util.List; 
 
class Study { 
    public static Study One() { 
        Study study = new Study(); 
        study.series.add(new Series("Series 1.1", "Mr. X", "1988-10-23", "sec-xx-1")); 
        study.series.add(new Series("Series 1.2", "Mr. X", "1988-10-23", "sec-xx-2")); 
        study.series.add(new Series("Series 1.3", "Mr. X", "1988-10-23", "sec-xx-3")); 
        return study; 
    } 
 
    public static Study Two() { 
        Study study = new Study(); 
        study.series.add(new Series("Series 2.1", "Mrs. Y", "1960-02-11", "sec-yy-1")); 
        return study; 
    } 
 
    public static Study Three() { 
        Study study = new Study(); 
        study.series.add(new Series("Series 3.1", "HAL 9000", "1975-04-21", "sec-zz-1")); 
        study.series.add(new Series("Series 3.2", "HAL 9000", "1975-04-21", "sec-zz-2")); 
        return study; 
    } 
 
    public List<Series> series = new ArrayList<>(); 
}
