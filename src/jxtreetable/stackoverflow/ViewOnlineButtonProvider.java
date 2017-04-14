package jxtreetable.stackoverflow;
 
import javax.swing.JButton; 
 
import org.jdesktop.swingx.renderer.CellContext; 
import org.jdesktop.swingx.renderer.ComponentProvider; 
 
class ViewOnlineButtonProvider extends ComponentProvider<JButton> { 
 
    public ViewOnlineButtonProvider() { 
        rendererComponent.setHorizontalAlignment(JButton.CENTER); 
    } 
 
    @Override 
    protected void format(CellContext context) { 
        rendererComponent.setText(getValueAsString(context)); 
    } 
 
    @Override 
    protected void configureState(CellContext context) { 
        rendererComponent.setHorizontalAlignment(getHorizontalAlignment()); 
    } 
 
    @Override 
    protected JButton createRendererComponent() { 
        return new JButton("View online"); 
    } 
}
