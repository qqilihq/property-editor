package jxtreetable.stackoverflow;
 
import java.awt.Dimension; 
 
import javax.swing.JFrame; 
import javax.swing.JPanel; 
 
import org.jdesktop.swingx.JXTreeTable; 
import org.jdesktop.swingx.decorator.HighlighterFactory; 
import org.jdesktop.swingx.renderer.DefaultTableRenderer; 
 
/**
 * http://stackoverflow.com/questions/9190737/jxtreetable-how-to-use-componentprovider-to-set-the-renderer-for-one-column 
 */ 
public class PatientStudy extends JFrame { 
 
    public static void main(String[] args) { 
        new PatientStudy().setVisible(true); 
    } 
 
    private final JXTreeTable table = new JXTreeTable(); 
    private final JPanel panel = new JPanel(); 
 
    public PatientStudy() { 
        setMinimumSize(new Dimension(400, 400)); 
        table.setEditable(false); 
        table.setDragEnabled(false); 
        table.setColumnSelectionAllowed(false); 
        table.setHighlighters(HighlighterFactory.createAlternateStriping()); 
        table.setRowHeight(20); 
        table.setMinimumSize(new Dimension(200, 200)); 
        table.setTreeTableModel(new PatientStudyModel()); 
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableRenderer(new ViewOnlineButtonProvider())); 
        panel.add(table); 
        this.setContentPane(panel); 
    } 
 
}
