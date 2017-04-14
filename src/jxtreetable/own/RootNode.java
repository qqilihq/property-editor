package jxtreetable.own;
 
import java.math.BigDecimal; 
 
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode; 
 
class RootNode extends AbstractMutableTreeTableNode { 
 
    public RootNode() { 
        super(new DisplayedObject("Root Node", BigDecimal.ONE)); 
    } 
 
    @Override 
    public Object getValueAt(int column) { 
        switch (column){ 
            case 0: 
                return getUserObject(); 
            default: 
                return null; 
        } 
    } 
 
    @Override 
    public int getColumnCount() { 
        return 5; 
    } 
 
}
