package jxtreetable.stackoverflow;
 
class Series { 
    public String seriesInstanceUID; 
    public String patientName; 
    public String patientBirthDate; 
    public String securityToken; 
 
    public Series(String seriesInstanceUID, String patientName, String patientBirthDate, String securityToken) { 
        this.seriesInstanceUID = seriesInstanceUID; 
        this.patientName = patientName; 
        this.patientBirthDate = patientBirthDate; 
        this.securityToken = securityToken; 
    } 
}
