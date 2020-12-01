package Database;

import AutoShiftScheduler.Contract;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** 
 * Undergraduate final year individual computing project - KV6003
 * Northumbria University - 2019/20
 * 
 * @author Syed Ali - w17023496
 * 
 * Database access class fo retrieving data from database
 */
public class DAO {
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static LocalDate today = LocalDate.now();
    
    public static AutoShiftScheduler.Employee getEmployee(String id) throws ClassNotFoundException{
        
        AutoShiftScheduler.Employee employee = new AutoShiftScheduler.Employee();
        
        try (Connection conn = DBConnection.dbConnect()) {
            
            String sql = "SELECT * FROM Employee WHERE employee_id=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
         
           if (rs.next()){
            employee.setEmployeeID(rs.getString("employee_id"));
            employee.setEmployeeName(rs.getString("employee_name"));
            employee.setEmployeeContract(rs.getString("employee_contract"));
            employee.setPreferredDayOff1(rs.getString("preferred_day_off_1"));
            employee.setPreferredDayOff2(rs.getString("preferred_day_off_2"));
           }            
        }
        catch(SQLException e){ 
           
        }
        return employee;
    }
      
    
    public static ObservableList<AutoShiftScheduler.Employee> getEmployees() throws ClassNotFoundException, SQLException{
        
        Connection conn = DBConnection.dbConnect();
        ObservableList<AutoShiftScheduler.Employee> employeeList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM Employee;";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()){
                                
                AutoShiftScheduler.Employee employee = new AutoShiftScheduler.Employee();
                
                employee.setEmployeeID(rs.getString("employee_id"));
                employee.setEmployeeName(rs.getString("employee_name"));
                employee.setEmployeeContract(rs.getString("employee_contract"));
                employee.setPreferredDayOff1(rs.getString("preferred_day_off_1"));
                employee.setPreferredDayOff2(rs.getString("preferred_day_off_2"));
                     
                employeeList.add(employee);               
            }             
        } catch (SQLException e){
            System.out.print(e);
        }
        finally{
            conn.close();
        }
        return employeeList;                             
    }

    
    public static void addEmployee(String employeeName, int preferredDayOff1, int preferredDayOff2) throws ClassNotFoundException, SQLException{
        
        try (Connection conn = DBConnection.dbConnect()) {
                       
            String sql = "INSERT INTO Employee(employee_name, preferred_day_off_1, preferred_day_off_2) VALUES (?,?,?);"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, employeeName);
            ps.setInt(2, preferredDayOff1);
            ps.setInt(3, preferredDayOff2);
            
            ps.executeUpdate(); 
         
            conn.close();

        } catch (SQLException e){ 
            System.out.println(e);            
        }        
    }
    
    public static int getEmployeeID() throws ClassNotFoundException, SQLException{
        
        int empID = 0;
               
        try (Connection conn = DBConnection.dbConnect()) {
                String sql = "SELECT MAX (employee_id) FROM Employee;";
                ResultSet rs = conn.createStatement().executeQuery(sql);
     
                empID = rs.getInt(1);
                conn.close();
                
            } catch (SQLException e){ 
                System.out.print(e);
        }
        return empID;
    }

   
    
    public static void addContract (int empID, String startDate, String hours, String endDate, String hrlyRate, String minHrs, String maxDays) throws ClassNotFoundException{
        
        try (Connection conn = DBConnection.dbConnect()) {
                       
            String sql = "INSERT INTO Contract(employee, contract_start_date, contracted_hours, contract_end_date, hourly_rate, min_hours_between_shifts, max_cons_work_days) VALUES (?,?,?,?,?,?,?);"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setInt(1, empID);
            ps.setString(2, startDate);
            ps.setString(3, hours);
            ps.setString(4, endDate);
            ps.setString(5, hrlyRate);
            ps.setString(6, minHrs);
            ps.setString(7, maxDays);
            
            ps.executeUpdate(); 
         
            conn.close();

        } catch (SQLException e){ 
            System.out.println(e);
            
        }              
    }
  
    public static int getContractID() throws ClassNotFoundException, SQLException{
        
        int contractID = 0;
               
        try (Connection conn = DBConnection.dbConnect()) {
                String sql = "SELECT MAX (contract_id) FROM Contract;";
                ResultSet rs = conn.createStatement().executeQuery(sql);
                
                
                
                contractID = rs.getInt(1);
                
                conn.close();
            } catch (SQLException e){ 
                System.out.println(e);
        }
        return contractID;
    }
    
    public static int getLastEmployeeID() throws ClassNotFoundException, SQLException{
        
        int lastEmployee = 0;
               
        try (Connection conn = DBConnection.dbConnect()) {
                String sql = "SELECT MAX (employee_id) FROM Employee;";
                ResultSet rs = conn.createStatement().executeQuery(sql);
     
                lastEmployee = rs.getInt(1);
                
                conn.close();
            } catch (SQLException e){ 
                System.out.println(e);
        }
        return lastEmployee;
    }
    
    
    public static AutoShiftScheduler.Employee getLastEmployee() throws ClassNotFoundException, SQLException{
            
        AutoShiftScheduler.Employee employee = new AutoShiftScheduler.Employee();
        try (Connection conn = DBConnection.dbConnect()){
            
            String sql = "SELECT * FROM Employee WHERE employee_id='" + getLastEmployeeID() + "';";
            ResultSet rs = conn.createStatement().executeQuery(sql);    
                
                employee.setEmployeeName(rs.getString("employee_name"));
                employee.setEmployeeID(rs.getString("employee_id"));
                      
            }             
         catch (SQLException e){
            System.out.print(e);
        }  
        return employee;                             
    }
    
   public static void addContractToEmployee() throws ClassNotFoundException{
       
       try (Connection conn = DBConnection.dbConnect()) {
                       
            String sql = "UPDATE Employee SET employee_contract='" + getContractID() + "' WHERE employee_id='" + getEmployeeID() + "';"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
                        
            ps.executeUpdate(); 
         
            conn.close();

        } catch (SQLException e){ 
            System.out.println(e);
            
        }                  
   }
   
   public static float getHourlyRate(String id) throws ClassNotFoundException{
       
       float hrlyRate = 0;
       
       try (Connection conn = DBConnection.dbConnect()){
           
           String sql = "SELECT hourly_rate FROM Contract INNER JOIN Employee ON Employee.employee_id = Contract.employee WHERE Employee.employee_id='" + Integer.parseInt(id) + "';";
           ResultSet rs = conn.createStatement().executeQuery(sql);
           
           hrlyRate = rs.getFloat(1);
           conn.close();
           
       } catch (SQLException e){
           System.out.print(e);
       }
       
       return hrlyRate;
       
   }
    
   
   public static void createShift(String id, String shiftDate, LocalTime startTime, LocalTime finishTime, int shiftPattern, int shiftType, double shiftCost) throws SQLException, ClassNotFoundException {
              
           try(Connection conn = DBConnection.dbConnect()){             
                           
             String sql = "INSERT INTO Shift(employee_assigned, shift_date, shift_start_time, shift_end_time, shift_pattern, shift_type, shift_cost) VALUES (?,?,?,?,?,?,?);"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, shiftDate);
            ps.setString(3, startTime.toString());
            ps.setString(4, finishTime.toString());
            ps.setInt(5, shiftPattern);
            ps.setInt(6, shiftType);
            ps.setDouble(7, shiftCost);
            
            ps.executeUpdate(); 
         
            conn.close();               
               
           } catch (SQLException e){
               System.out.print(e);
           }        
   }
   
   public static void editShift(LocalTime startTime, LocalTime endTime,  int shiftPattern, int shiftType, double shiftCost, String id, String shiftDate) throws SQLException, ClassNotFoundException {
       
       try(Connection conn = DBConnection.dbConnect()){
                                          
            String sql = "UPDATE Shift SET shift_start_time=?, shift_end_time=?, shift_pattern=?, shift_type=?, shift_cost=? WHERE employee_assigned=? AND shift_date=?;"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, startTime.toString());
            ps.setString(2, endTime.toString());
            ps.setInt(3, shiftPattern);
            ps.setInt(4, shiftType);
            ps.setDouble(5, shiftCost);
            ps.setString(6, id);
            ps.setString(7, shiftDate);
            
            ps.executeUpdate(); 
         
            conn.close();               
               
           } catch (SQLException e){
               System.out.println(e);
           }          
   }
   
   
   
   public static boolean checkEmployeeShift(String empID, String date) throws ClassNotFoundException{
              
   try (Connection conn = DBConnection.dbConnect()){
           
           //String sql = "SELECT shift_date FROM Shift INNER JOIN Employee ON employee_id = employee_assigned WHERE Employee.employee_id =? AND Shift.shift_date=?;";
           String sql = "SELECT 1 FROM Shift WHERE employee_assigned=? AND shift_date=?;";
           
           PreparedStatement ps = conn.prepareStatement(sql);
           
           ps.setString(1, empID);
           ps.setString(2, date);
           
           ResultSet rs = ps.executeQuery();
           
           if (rs.next()){
               
           return true;
           
           } 
           conn.close();
                     
       } catch (SQLException e){
           
       }      
       return false;      
   }
   
   
   //get employee shift
   public static AutoShiftScheduler.Shift getEmployeeShift(String empID, String date) throws ClassNotFoundException{
        
       AutoShiftScheduler.Shift shift = new AutoShiftScheduler.Shift();
       try (Connection conn = DBConnection.dbConnect()){
           
           String sql = "SELECT shift_id, employee_assigned, shift_date, shift_start_time, shift_end_time, shift_pattern, shift_type, shift_cost FROM Shift INNER JOIN Employee ON employee_id = employee_assigned WHERE Employee.employee_id =? AND Shift.shift_date=?;";
           PreparedStatement ps = conn.prepareStatement(sql);
           
           ps.setString(1, empID);
           ps.setString(2, date);
           
           ResultSet rs = ps.executeQuery();
                                
           if (rs.next()){
               
            shift.setshiftID(rs.getString("shift_id"));
            shift.setEmployee(rs.getString("employee_assigned"));
            shift.setShiftDate(rs.getString("shift_date"));
            shift.setShiftStart(rs.getString("shift_start_time"));
            shift.setShiftEnd(rs.getString("shift_end_time"));
            shift.setShiftPattern(rs.getString("shift_pattern"));
            shift.setShiftType(rs.getString("shift_type"));
            shift.setShiftCost(rs.getString("shift_cost"));
           
            conn.close();
           }
           else{               
               return null;
               
           }
           
             
       } catch (SQLException e){
          
       }      
       return shift;
   }
    
   public static void removeShift(String empID, String date) throws ClassNotFoundException{
       
        try (Connection conn = DBConnection.dbConnect()){
       String sql = "DELETE FROM Shift WHERE employee_assigned=? AND shift_date=?;"; 
            
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, empID);
            ps.setString(2, date);
          
            ps.executeUpdate(); 
         
            conn.close();
            
        }
        catch(SQLException e){
            System.out.print(e);
        }
       
       
   }
   
   public static AutoShiftScheduler.Contract getContract(String id) throws ClassNotFoundException{
       
       AutoShiftScheduler.Contract contract = new Contract();
       
       try (Connection conn = DBConnection.dbConnect()){
           
            String sql = "SELECT * FROM Contract WHERE contract_id=?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
         
           if (rs.next()){
               
                contract.setContractID(rs.getString("contract_id"));
                contract.setEmployee(rs.getString("employee"));
                contract.setContractStartDate(rs.getString("contract_start_date"));
                contract.setContractedHours(rs.getString("contracted_hours"));
                contract.setContractEndDate(rs.getString("contract_end_date"));
                contract.setHourlyRate(String.valueOf(rs.getString("hourly_rate")));
                contract.setMinHoursBetweenShifts(rs.getString("min_hours_between_shifts"));
                contract.setMaxConsWorkDays(rs.getString("max_cons_work_days"));

              conn.close();
           } 
                              
       } catch (SQLException e){
           System.out.print(e);
       }      
       return contract;
   }
   
   
   public static void editEmployee(String ID, String contractID, String employeeName, String preferredDayOff1, String preferredDayOff2, String startDate, String hours, String endDate, String hrlyRate, String minHrs, String maxDays) throws ClassNotFoundException{
       
         try (Connection conn = DBConnection.dbConnect()){
            
            String sql1 = "UPDATE Employee SET employee_name=?, preferred_day_off_1=?, preferred_day_off_2=? WHERE employee_id=?;"; 
            String sql2 = "UPDATE Contract SET contract_start_date=?, contracted_hours=?, contract_end_date=?, hourly_rate=?, min_hours_between_shifts=?, max_cons_work_days=? WHERE contract_id=?;"; 
                        
            PreparedStatement ps1 = conn.prepareStatement(sql1); 
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            
            ps1.setString(1, employeeName);
            ps1.setString(2, preferredDayOff1);
            ps1.setString(3, preferredDayOff2);
            ps1.setString(4, ID);
          
            ps2.setString(1, startDate);
            ps2.setString(2, hours);
            ps2.setString(3, endDate);
            ps2.setString(4, hrlyRate);
            ps2.setString(5, minHrs);
            ps2.setString(6, maxDays);
            ps2.setString(7, contractID);
            
            ps1.executeUpdate(); 
            ps2.executeUpdate();            
         
            conn.close();
            System.out.println("Employee and contract updated successfully");
        }
        catch(SQLException e){
            System.out.println(e);
        }
       
   }
   
   //This originally removed the shifts, contract and then employee from the database but now only sets the contract end date to 1 day before 
   //today so that the employee is no longer in the calendar however all of their shifts, details and contract remains in the database 
   //for business/HR purposes.
   public static void removeEmployee(String empID) throws ClassNotFoundException{
       
        try (Connection conn = DBConnection.dbConnect()){
        
            String sql = "UPDATE Contract SET contract_end_date=? WHERE employee=?;";
      
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, String.valueOf(today));
            ps.setString(2, empID);
            
            ps.executeUpdate(); 
         
            conn.close();           
        }
        catch(SQLException e){
            System.out.print(e);
        }     
   }
  
   
   public static int getNumOfShiftsOnDay(String date) throws ClassNotFoundException{
       
       int noOfShifts = 0;
       
       try(Connection conn = DBConnection.dbConnect()){
           
           String sql = "SELECT COUNT(*) FROM Shift WHERE shift_type=1 AND shift_date=?;";
           
           PreparedStatement ps = conn.prepareStatement(sql); 
                      
           ps.setString(1, date);
           ResultSet rs = ps.executeQuery();
           
           if(rs.next()){               
              noOfShifts = rs.getInt(1);
           }
           else{
               noOfShifts = 0;
           }
           
           conn.close();
       }
       catch(SQLException e){
           System.out.print(e);
       }
       return noOfShifts;
   }
   
   
   public static double getDailyCost(String date) throws ClassNotFoundException{
       
       double cost = 0.00;
       
       try(Connection conn = DBConnection.dbConnect()){
           
           String sql = "SELECT SUM(shift_cost) FROM Shift WHERE shift_type=1 AND shift_date=?;";
           
           PreparedStatement ps = conn.prepareStatement(sql); 
                      
           ps.setString(1, date);
           ResultSet rs = ps.executeQuery();
           
           if(rs.next()){
               
              cost = rs.getDouble(1);
           }
           else{
               cost = 0.00;
           }
           
           conn.close();
       }
       catch(SQLException e){
           System.out.print(e);
       }
       return cost;
   }
   
   
   
   public static double getWeeklyBudget() throws ClassNotFoundException, SQLException{
        
            double weeklyBudget = 0;
       
        try (Connection conn = DBConnection.dbConnect()){
            
            String sql = "SELECT budget_amount FROM Budget WHERE budget_id=2;";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
               
              weeklyBudget = rs.getDouble(1);
           }       
        }
        catch(SQLException | ClassNotFoundException c){
            System.out.print(c);
        }
        return weeklyBudget;
       
   }
   
   public static void setWeeklyBudget(double weeklyBudget) throws ClassNotFoundException{
       
       try (Connection conn = DBConnection.dbConnect()){
        
            String sql = "UPDATE Budget SET budget_amount=? WHERE budget_id=2;";
      
            PreparedStatement ps = conn.prepareStatement(sql); 

            
            ps.setDouble(1, weeklyBudget);           
            
            ps.executeUpdate(); 
         
            conn.close();
           
        }
        catch(SQLException e){
            System.out.print(e);
        }     
       
   }
   
   
   public static double getContractedHours(int empID) throws ClassNotFoundException, SQLException{
       
       double hours = 0;
       
        try (Connection conn = DBConnection.dbConnect()){
            
            String sql = "SELECT contracted_hours FROM Contract WHERE employee=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, empID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){             
              hours = rs.getDouble(1);
           }       
        }
        catch(SQLException | ClassNotFoundException e){
            System.out.print(e);
        }
        return hours; 
   }
        
}
