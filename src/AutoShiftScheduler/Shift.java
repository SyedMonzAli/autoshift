
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
public class Shift {
    public SimpleStringProperty shiftID;
    public SimpleStringProperty employee;
    public SimpleStringProperty shiftDate;
    public SimpleStringProperty shiftStart;
    public SimpleStringProperty shiftEnd;
    public SimpleStringProperty shiftPattern;
    public SimpleStringProperty shiftType;
    public SimpleStringProperty shiftCost;
    
    
    
    public Shift(){
        
        this.shiftID = new SimpleStringProperty();
        this.employee = new SimpleStringProperty();
        this.shiftDate = new SimpleStringProperty();
        this.shiftStart = new SimpleStringProperty();
        this.shiftEnd = new SimpleStringProperty();
        this.shiftPattern = new SimpleStringProperty();
        this.shiftType = new SimpleStringProperty();
        this.shiftCost = new SimpleStringProperty();
    }
    
    public StringProperty shiftIDProperty(){
        return shiftID;
        }
    
    public StringProperty employeeProperty(){
        return employee;
        }
    
    public StringProperty shiftDateProperty(){
        return shiftDate;
        }
    
    public StringProperty shiftStartProperty(){
        return shiftStart;
        }
    
    public StringProperty shiftEndProperty(){
        return shiftEnd;
        }
    public StringProperty shiftPatternProperty(){
        return shiftPattern;
        }
    public StringProperty shiftTypeProperty(){
        return shiftType;
        }
    public StringProperty shiftCostProperty(){
        return shiftCost;
        }
    
  
     /**
     * @return the shiftID
     */
    public String getShiftID() {
        return shiftID.get();
    }

    /**
     * @param shiftID the shiftID to set
     */
    public void setshiftID(String shiftID) {
        this.shiftID.set(shiftID);
    }
    
     /**
     * @return the employee
     */
    public String getEmployee() {
        return employee.get();
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(String employee) {
        this.employee.set(employee);
    }
    
     /**
     * @return the shiftDate
     */
    public String getShiftDate() {
        return shiftDate.get();
    }

    /**
     * @param shiftDate the shiftDate to set
     */
    public void setShiftDate(String shiftDate) {
        this.shiftDate.set(shiftDate);
    }
    
     /**
     * @return the shiftStart
     */
    public String getShiftStart() {
        return shiftStart.get();
    }

    /**
     * @param shiftStart the shiftStart to set
     */
    public void setShiftStart(String shiftStart) {
        this.shiftStart.set(shiftStart);
    }
    
     /**
     * @return the shiftEnd
     */
    public String getShiftEnd() {
        return shiftEnd.get();
    }

    /**
     * @param shiftEnd the shiftEnd to set
     */
    public void setShiftEnd(String shiftEnd) {
        this.shiftEnd.set(shiftEnd);
    }
    
         /**
     * @return the shiftPattern
     */
    public String getShiftPattern() {
        return shiftPattern.get();
    }

    /**
     * @param shiftPattern the shiftPattern to set
     */
    public void setShiftPattern (String shiftPattern) {
        this.shiftPattern.set(shiftPattern);
    }
    
         /**
     * @return the shiftType
     */
    public String getShiftType() {
        return shiftType.get();
    }

    /**
     * @param shiftType the shiftType to set
     */
    public void setShiftType(String shiftType) {
        this.shiftType.set(shiftType);
    }
    
         /**
     * @return the shiftCost
     */
    public String getShiftCost() {
        return shiftCost.get();
    }

    /**
     * @param shiftCost the shiftCost to set
     */
    public void setShiftCost(String shiftCost) {
        this.shiftCost.set(shiftCost);
    }
}

