package jxtreetable.stackoverflow;
 
import java.util.ArrayList; 
import java.util.List; 
 
class Root { 
    public List<Study> studies = new ArrayList<>(); 
 
    public Root() { 
        studies.add(Study.One()); 
        studies.add(Study.Two()); 
        studies.add(Study.Three()); 
    } 
}
