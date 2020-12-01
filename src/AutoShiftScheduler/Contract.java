
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
public class Contract {
    public SimpleStringProperty contractID;
    public SimpleStringProperty employee;
    public SimpleStringProperty contractStartDate;
    public SimpleStringProperty contractedHours;
    public SimpleStringProperty contractEndDate;
    public SimpleStringProperty hourlyRate;
    public SimpleStringProperty minHoursBetweenShifts;
    public SimpleStringProperty maxConsWorkDays;        
    
    public Contract(){
        
        this.contractID = new SimpleStringProperty();
        this.employee = new SimpleStringProperty();
        this.contractStartDate = new SimpleStringProperty();
        this.contractedHours = new SimpleStringProperty();
        this.contractEndDate = new SimpleStringProperty();
        this.hourlyRate = new SimpleStringProperty();
        this.minHoursBetweenShifts = new SimpleStringProperty();
        this.maxConsWorkDays = new SimpleStringProperty();
    }
    
    public StringProperty contractIDProperty(){
        return contractID;
        }
    
    public StringProperty employeeProperty(){
        return employee;
        }
    
    public StringProperty contractStartDateProperty(){
        return contractStartDate;
        }
    
    public StringProperty contractedHoursProperty(){
        return contractedHours;
        }
    
    public StringProperty contractEndDateProperty(){
        return contractEndDate;
        }
    public StringProperty hourlyRateProperty(){
        return hourlyRate;
        }
    public StringProperty minHoursBetweenShiftsProperty(){
        return minHoursBetweenShifts;
        }
    public StringProperty maxConsWorkDaysProperty(){
        return maxConsWorkDays;
        }

  
     /**
     * @return the contractID
     */
    public String getContractID() {
        return contractID.get();
    }

    /**
     * @param contractID the contractID to set
     */
    public void setContractID(String contractID) {
        this.contractID.set(contractID);
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
     * @return the contractStartDate
     */
    public String getContractStartDate() {
        return contractStartDate.get();
    }

    /**
     * @param contractStartDate the contractStartDate to set
     */
    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate.set(contractStartDate);
    }
    
     /**
     * @return the contractedHours
     */
    public String getContractedHours() {
        return contractedHours.get();
    }

    /**
     * @param contractedHours the contractedHours to set
     */
    public void setContractedHours(String contractedHours) {
        this.contractedHours.set(contractedHours);
    }
    
     /**
     * @return the contractEndDate
     */
    public String getContractEndDate() {
        return contractEndDate.get();
    }

    /**
     * @param contractEndDate the contractEndDate to set
     */
    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate.set(contractEndDate);
    }
    
         /**
     * @return the hourlyRate
     */
    public String getHourlyRate() {
        return hourlyRate.get();
    }

    /**
     * @param hourlyRate the hourlyRate to set
     */
    public void setHourlyRate (String hourlyRate) {
        this.hourlyRate.set(hourlyRate);
    }
    
         /**
     * @return the minHoursBetweenShifts
     */
    public String getMinHoursBetweenShifts() {
        return minHoursBetweenShifts.get();
    }

    /**
     * @param minHoursBetweenShifts the minHoursBetweenShifts to set
     */
    public void setMinHoursBetweenShifts(String minHoursBetweenShifts) {
        this.minHoursBetweenShifts.set(minHoursBetweenShifts);
    }
    
         /**
     * @return the maxConsWorkDays
     */
    public String getMaxConsWorkDays() {
        return maxConsWorkDays.get();
    }

    /**
     * @param maxConsWorkDays the maxConsWorkDays to set
     */
    public void setMaxConsWorkDays(String maxConsWorkDays) {
        this.maxConsWorkDays.set(maxConsWorkDays);
    }
}
