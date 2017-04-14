package jxtreetable.own;
 
import java.awt.Color; 
import java.awt.HeadlessException; 
import java.math.BigDecimal; 
 
import javax.swing.JFrame; 
import javax.swing.JScrollPane; 
import javax.swing.SwingUtilities; 
import javax.swing.WindowConstants; 
import javax.swing.table.TableColumnModel; 
 
import org.jdesktop.swingx.JXTreeTable; 
import org.jdesktop.swingx.decorator.ColorHighlighter; 
import org.jdesktop.swingx.renderer.DefaultTreeRenderer; 
import org.jdesktop.swingx.renderer.StringValue; 
import org.jdesktop.swingx.table.DefaultTableColumnModelExt; 
import org.jdesktop.swingx.table.TableColumnExt; 
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode; 
import org.jdesktop.swingx.treetable.DefaultTreeTableModel; 
 
public class TreeTableExample extends JFrame { 
 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable() { 
            public void run() { 
                TreeTableExample ex = new TreeTableExample(); 
                ex.setVisible(true); 
            } 
        }); 
    } 
 
    public TreeTableExample() throws HeadlessException { 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        setTitle("Simple example"); 
        setSize(600, 200); 
 
        AbstractMutableTreeTableNode root = new RootNode(); 
        for (int i = 0; i < 20; ++i) { 
            AbstractMutableTreeTableNode child = createNode(i); 
            child.setParent(root); 
        } 
 
        final JXTreeTable treeTable = new JXTreeTable(); 
        treeTable.setAutoCreateColumnsFromModel(false); 
        treeTable.setRootVisible(true); 
        TableColumnModel tableColumnModel = createTableColumnModel(); 
        treeTable.setColumnModel(tableColumnModel); 
        DefaultTreeTableModel model = new DefaultTreeTableModel(root) { 
            @Override 
            public Class<?> getColumnClass(int column) { 
                if (column == 2 || column == 1) { 
                    return BigDecimal.class; 
                } 
                return super.getColumnClass(column); 
            } 
        }; 
        treeTable.setTreeTableModel(model); 
        treeTable.setHighlighters(new NosyToolTipHighlighter()); 
        StringValue stringValue = new StringValue() { 
            @Override 
            public String getString(Object value) { 
                if(null == value) { 
                    return "ups"; 
                } 
                return ((DisplayedObject)value).name(); 
            } 
        }; 
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(stringValue)); 
        add(new JScrollPane(treeTable)); 
    } 
 
    private DefaultTableColumnModelExt createTableColumnModel() { 
        DefaultTableColumnModelExt columnModel = new DefaultTableColumnModelExt(); 
        TableColumnExt name = new TableColumnExt(); 
        name.setHeaderValue("Name"); 
        name.setModelIndex(0); 
        name.setIdentifier("identifier.name"); 
        name.setHighlighters(new ColorHighlighter(Color.DARK_GRAY, Color.GREEN)); 
 
        TableColumnExt sum = new TableColumnExt(); 
        sum.setHeaderValue("Sum"); 
        sum.setModelIndex(1); 
        sum.setIdentifier("identifier.sum"); 
 
        TableColumnExt value_01 = new TableColumnExt(); 
        value_01.setHeaderValue("1st"); 
        value_01.setModelIndex(2); 
        value_01.setIdentifier("identifier.value.1"); 
 
        TableColumnExt value_02 = new TableColumnExt(); 
        value_02.setHeaderValue("2nd"); 
        value_02.setModelIndex(3); 
        value_02.setIdentifier("identier.value.2"); 
 
        TableColumnExt value_03 = new TableColumnExt(); 
        value_03.setHeaderValue("3rd"); 
        value_03.setModelIndex(4); 
        value_03.setIdentifier("identifier.value.3"); 
 
 
        columnModel.addColumn(name); 
        columnModel.addColumn(sum); 
        columnModel.addColumn(value_01); 
        columnModel.addColumn(value_02); 
        columnModel.addColumn(value_03); 
        return columnModel; 
    } 
 
    private AbstractMutableTreeTableNode createNode(int startValue) { 
        BigDecimal start = BigDecimal.valueOf(startValue); 
        BigDecimal squareOfStart = start.pow(2); 
        final DisplayedObject displayedObject = new DisplayedObject("#" + startValue, start, squareOfStart); 
        return new DisplayObjectNode(displayedObject); 
    } 
 
}