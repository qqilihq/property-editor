package jxtreetable.own;
 
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode; 
 
class DisplayObjectNode extends AbstractMutableTreeTableNode { 
    private final DisplayedObject displayedObject; 
 
    public DisplayObjectNode(DisplayedObject displayedObject) { 
        super(displayedObject); 
        this.displayedObject = displayedObject; 
    } 
 
    @Override 
    public Object getValueAt(int column) { 
        switch (column) { 
            case 0: 
                return displayedObject; 
            case 1: 
                return displayedObject.sum(); 
            case 2: 
            case 3: 
                return displayedObject.valueAt(column - 2); 
            case 4: 
                return null; 
            default: 
                throw new RuntimeException("not handled column " + column); 
        } 
    } 
 
    @Override 
    public int getColumnCount() { 
        return 5; 
    } 
}
