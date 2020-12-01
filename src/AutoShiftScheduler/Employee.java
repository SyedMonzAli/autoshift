
package AutoShiftScheduler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Undergraduate final year individual computing project - KV6003
 * Northumbria University - 2019/20
 * 
 * @author Syed Ali - w17023496
 *
 * FXML Controller class for home screen
 */
public class Employee {
     
    public SimpleStringProperty employeeID;
    public SimpleStringProperty employeeName;
    public SimpleStringProperty employeeContract;
    public SimpleStringProperty preferredDayOff1;
    public SimpleStringProperty preferredDayOff2;
        
    public Employee(){
        
        this.employeeID = new SimpleStringProperty();
        this.employeeName = new SimpleStringProperty();
        this.employeeContract = new SimpleStringProperty();
        this.preferredDayOff1 = new SimpleStringProperty();
        this.preferredDayOff2 = new SimpleStringProperty();
    }
    
    public StringProperty employeeIDProperty(){
        return employeeID;
        }
    
    public StringProperty employeeNameProperty(){
        return employeeName;
        }
    
    public StringProperty employeeContractProperty(){
        return employeeContract;
        }
    
    public StringProperty preferredDayOff1Property(){
        return preferredDayOff1;
        }
    
    public StringProperty preferredDayOff2Property(){
        return preferredDayOff2;
        }
    
     /**
     * @return the employeeID
     */
    public String getEmployeeID() {
        return employeeID.get();
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(String employeeID) {
        this.employeeID.set(employeeID);
    }
    
     /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName.get();
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }
    
     /**
     * @return the employeeContract
     */
    public String getEmployeeContract() {
        return employeeContract.get();
    }

    /**
     * @param employeeContract the employeeContract to set
     */
    public void setEmployeeContract(String employeeContract) {
        this.employeeContract.set(employeeContract);
    }
    
     /**
     * @return the preferredDayOff1
     */
    public String getPreferredDayOff1() {
        return preferredDayOff1.get();
    }

    /**
     * @param preferredDayOff1 the preferredDayOff1 to set
     */
    public void setPreferredDayOff1(String preferredDayOff1) {
        this.preferredDayOff1.set(preferredDayOff1);
    }
    
     /**
     * @return the preferredDayOff2
     */
    public String getPreferredDayOff2() {
        return preferredDayOff2.get();
    }

    /**
     * @param preferredDayOff2 the preferredDayOff2 to set
     */
    public void setPreferredDayOff2(String preferredDayOff2) {
        this.preferredDayOff2.set(preferredDayOff2);
    }
}
