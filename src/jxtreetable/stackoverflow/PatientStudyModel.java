package jxtreetable.stackoverflow;
 
import org.jdesktop.swingx.treetable.AbstractTreeTableModel; 
 
class PatientStudyModel extends AbstractTreeTableModel { 
    private final Root root = new Root(); 
 
    @Override 
    public String getColumnName(int columnIndex) { 
 
        switch (columnIndex) { 
            case 0: 
                return "Series Instance UID"; 
 
            case 1: 
                return "Patient Name"; 
 
            case 2: 
                return "Patient Birth Date"; 
 
            case 3: 
                return "View online"; 
 
            default: 
                return ""; 
        } 
    } 
 
    @Override 
    public int getIndexOfChild(Object parent, Object child) { 
        if (parent == root) { 
            return root.studies.indexOf(child); 
        } 
 
        if (parent instanceof Study) { 
            Study study = (Study) parent; 
 
            return study.series.indexOf(child); 
        } 
 
        return -1; 
    } 
 
    @Override 
    public int getChildCount(Object parent) { 
        if (parent == root) { 
            return root.studies.size(); 
        } 
 
        if (parent instanceof Study) { 
            Study study = (Study) parent; 
 
            return study.series.size(); 
        } 
 
        return 0; 
    } 
 
    @Override 
    public Object getChild(Object parent, int index) { 
        if (parent == root) { 
            return root.studies.get(index); 
        } 
 
        if (parent instanceof Study) { 
            Study study = (Study) parent; 
 
            return study.series.get(index); 
        } 
 
        return null; 
    } 
 
    @Override 
    public Object getValueAt(Object node, int columnIndex) { 
        if (!(node instanceof Series) && !(node instanceof Study)) 
            return null; 
 
        if (columnIndex < 0 || columnIndex >= getColumnCount()) 
            return null; 
 
        if (root == null) 
            return null; 
 
        if (node instanceof Series) { 
            Series series = (Series) node; 
 
            if (columnIndex == 0) 
                return series.seriesInstanceUID; 
            else if (columnIndex == 1) 
                return series.patientName; 
            else if (columnIndex == 2) 
                return series.patientBirthDate; 
            else if (columnIndex == 3) 
                return series.securityToken; 
        } else if (node instanceof Study) { 
            // Empty for now 
        } 
 
        return null; 
    } 
 
    @Override 
    public int getColumnCount() { 
        return 4; 
    } 
 
    @Override 
    public Object getRoot() { 
        return root; 
    } 
 
    public void update() { 
        modelSupport.fireNewRoot(); 
    } 
}
