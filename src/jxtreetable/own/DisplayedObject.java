package jxtreetable.own;
 
import java.math.BigDecimal; 
import java.util.Arrays; 
import java.util.List; 
 
public class DisplayedObject { 
    private final String name; 
    private final List<BigDecimal> values; 
 
 
    public DisplayedObject(String name, BigDecimal... values) { 
        this.name = name; 
        this.values = Arrays.asList(values); 
    } 
 
    public String name() { 
        return name; 
    } 
 
    public BigDecimal sum() { 
        BigDecimal sum = BigDecimal.ZERO; 
        for (BigDecimal value : values) { 
            sum = sum.add(value); 
        } 
        return sum; 
    } 
 
    public BigDecimal valueAt(int index) { 
        return values.get(index); 
    } 
 
    public String hiddenTooltip() { 
        return "the hidden tooltip"; 
    } 
 
    @Override 
    public String toString() { 
        return "overridden for test"; 
    } 
}
