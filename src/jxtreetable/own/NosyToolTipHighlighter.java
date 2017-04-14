package jxtreetable.own;
 
import java.awt.Component; 
import java.math.BigDecimal; 
 
import javax.swing.JComponent; 
 
import org.jdesktop.swingx.decorator.AbstractHighlighter; 
import org.jdesktop.swingx.decorator.ComponentAdapter; 
import org.jdesktop.swingx.decorator.HighlightPredicate; 
 
public class NosyToolTipHighlighter extends AbstractHighlighter { 
 
    public NosyToolTipHighlighter() { 
        super(HighlightPredicate.ALWAYS); 
    } 
 
    @Override 
    protected Component doHighlight(Component component, ComponentAdapter adapter) { 
        String tooltip; 
        if (0 == adapter.column) { 
            DisplayedObject value = (DisplayedObject) adapter.getValue(); 
            tooltip = value.hiddenTooltip(); 
        } else { 
            BigDecimal value = (BigDecimal) adapter.getValue(); 
            if( null == value){ 
                tooltip = "no tooltip for you"; 
            }else { 
                tooltip = "bd: " + value.toPlainString(); 
            } 
        } 
        ((JComponent) component).setToolTipText(tooltip); 
        return component; 
    } 
}
