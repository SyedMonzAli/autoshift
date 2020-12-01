
package AutoShiftScheduler;

import Database.DAO;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Undergraduate final year individual computing project - KV6003
 * Northumbria University - 2019/20
 * 
 * @author Syed Ali - w17023496
 *
 * FXML Controller class for home screen
 */
public class AutomatedScheduleFXMLController implements Initializable {
    
    private ObservableList<String> shiftTypes = FXCollections.observableArrayList("Working", "Day Off", "Off Sick", "Emergency Leave", "Annual Leave");
    private ObservableList<String> hours = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    private ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
    private ObservableList<String> patterns = FXCollections.observableArrayList("No Pattern","Days", "Evenings");
    private ObservableList<Employee> employeesSelected =  FXCollections.observableArrayList();
    private ObservableList<Employee> employeesSelected2 =  FXCollections.observableArrayList();
    private ObservableList<Employee> employeesAvailable =  FXCollections.observableArrayList(); 
    
    int monOldValue;
    int tueOldValue;
    int wedOldValue;
    int thuOldValue;
    int friOldValue;
    int satOldValue;
    int sunOldValue;
    
    protected GridPane calendar3;
    protected Label mondayDateLabel;
    protected Label tuesdayDateLabel;
    protected Label wednesdayDateLabel;
    protected Label thursdayDateLabel;
    protected Label fridayDateLabel;
    protected Label saturdayDateLabel;
    protected Label sundayDateLabel;
    protected Label monthYearLabel;
    protected TextField labourCostTextField;
    protected TextField weeklyBudgetTextField;
    protected TextField prefDaysOff;
    protected TextField consDays;
    protected LocalDate today;
    protected TextField monTotal;
    protected TextField tueTotal;   
    protected TextField wedTotal;   
    protected TextField thuTotal;   
    protected TextField friTotal;  
    protected TextField satTotal;  
    protected TextField sunTotal;  
    protected TextField shiftTotal;   
    protected TextField monCost;    
    protected TextField tueCost;    
    protected TextField wedCost;    
    protected TextField thuCost;    
    protected TextField friCost;    
    protected TextField satCost;   
    protected TextField sunCost;
    
    ArrayList<Label> dayLabels = new ArrayList<>();
    RowConstraints row = new RowConstraints(55);
    ArrayList<RowConstraints> rows = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> employeeLabels = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat ("#.##");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    
    @FXML
    private DatePicker scheduleFromDate;
    @FXML
    private DatePicker scheduleToDate;
    @FXML
    private Button autoShiftCancelButton;
    @FXML
    private Button allocateShiftsButton;
    @FXML
    private ComboBox<String> shiftTypeComboBox;
    @FXML
    private ComboBox<String> shiftPatternComboBox;
    @FXML
    private ComboBox<String> startTimeHours;
    @FXML
    private ComboBox<String> startTimeMinutes;
    @FXML
    private ComboBox<String> finishTimeHours;
    @FXML
    private ComboBox<String> finishTimeMinutes;
    @FXML
    private TableView<Employee> selectedEmployeesTable;
    @FXML
    private TableColumn<Employee, String> selectedID;
    @FXML
    private TableColumn<Employee, String> selectedName;
    @FXML
    private TableView<Employee> availableEmployeesTable;
    @FXML
    private TableColumn<Employee, String> availableID;
    @FXML
    private TableColumn<Employee, String> availableName;
    @FXML
    private Button selectButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button selectAllButton;
    @FXML
    private Button removeAllButton;
    @FXML
    private CheckBox allDaysTick;
    @FXML
    private CheckBox monTick;
    @FXML
    private CheckBox tueTick;
    @FXML
    private CheckBox wedTick;
    @FXML
    private CheckBox thuTick;
    @FXML
    private CheckBox friTick;
    @FXML
    private CheckBox satTick;
    @FXML
    private CheckBox sunTick;
    @FXML
    private TextField allDays;
    @FXML
    private TextField mon;
    @FXML
    private TextField tue;
    @FXML
    private TextField wed;
    @FXML
    private TextField thu;
    @FXML
    private TextField fri;
    @FXML
    private TextField sat;
    @FXML
    private TextField sun;       
    
    /**
     * Undergraduate final year individual computing project - KV6003
     * Northumbria University - 2019/20
     * 
     * @author Syed Ali - w17023496
     *
     * FXML Controller class for automated shifts screen
     * 
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        df.setRoundingMode(RoundingMode.UP);             
        scheduleFromDate.setValue(LocalDate.now());
        scheduleToDate.setValue(LocalDate.now());
        setShiftType();
        setShiftPattern();
        
        setStartHours();
        setStartMinutes();
        setFinishHours();
        setFinishMinutes();
        
        setComboBoxes();
        
        monOldValue = 0;
        tueOldValue = 0;
        wedOldValue = 0;
        thuOldValue = 0;
        friOldValue = 0;
        satOldValue = 0;
        sunOldValue = 0;
        
        try {
            loadAvailableEmployees();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AutomatedScheduleFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AutomatedScheduleFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       availableID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
       availableName.setCellValueFactory(new PropertyValueFactory<>("employeeName")); 
       selectedID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
       selectedName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
       employeesSelected.clear();             
    }    
    
    private void setShiftType(){
  
        shiftTypeComboBox.setItems(shiftTypes);   
        shiftTypeComboBox.getSelectionModel().selectFirst();                      
    }
    
    private void setShiftPattern(){
        
        shiftPatternComboBox.setItems(patterns);
        shiftPatternComboBox.getSelectionModel().select(1);                 
    }
    
    private void setComboBoxes(){
        
        startTimeHours.setDisable(true);
        startTimeMinutes.setDisable(true);
        finishTimeHours.setDisable(true);
        finishTimeMinutes.setDisable(true); 
        startTimeHours.setStyle("-fx-opacity: 1");
        startTimeMinutes.setStyle("-fx-opacity: 1");
        finishTimeHours.setStyle("-fx-opacity: 1");
        finishTimeMinutes.setStyle("-fx-opacity: 1");        
    }
    
    @FXML
    private void updateShiftType(ActionEvent e){        
                    
        if (shiftTypeComboBox.getSelectionModel().isSelected(0)){
            startTimeHours.setDisable(false);
            startTimeMinutes.setDisable(false);
            finishTimeHours.setDisable(false);
            finishTimeMinutes.setDisable(false); 
            shiftPatternComboBox.setDisable(false);          
        }
        else if(shiftTypeComboBox.getSelectionModel().getSelectedIndex()>0){
            
            mon.clear();
            tue.clear();
            wed.clear();
            thu.clear();
            fri.clear();
            sat.clear();
            sun.clear();
            allDays.clear();
            
            allDaysTick.setSelected(false);
            monTick.setSelected(false);
            tueTick.setSelected(false);
            wedTick.setSelected(false);
            thuTick.setSelected(false);
            friTick.setSelected(false);
            satTick.setSelected(false);
            sunTick.setSelected(false);
                       
            mon.setDisable(true);
            tue.setDisable(true);
            wed.setDisable(true);
            thu.setDisable(true);
            fri.setDisable(true);
            sat.setDisable(true);
            sun.setDisable(true);
            allDays.setDisable(true);
            
            shiftPatternComboBox.getSelectionModel().select(0);
            shiftPatternComboBox.setDisable(true);
            startTimeHours.setValue("00");
            startTimeMinutes.setValue("00");
            finishTimeHours.setValue("00");
            finishTimeMinutes.setValue("00");
            startTimeHours.setDisable(true);
            startTimeMinutes.setDisable(true);
            finishTimeHours.setDisable(true);
            finishTimeMinutes.setDisable(true); 
            
        }
        else{
            startTimeHours.setValue("00");
            startTimeMinutes.setValue("00");
            finishTimeHours.setValue("00");
            finishTimeMinutes.setValue("00");
            startTimeHours.setDisable(true);
            startTimeMinutes.setDisable(true);
            finishTimeHours.setDisable(true);
            finishTimeMinutes.setDisable(true); 
            shiftPatternComboBox.getSelectionModel().select(0);
            shiftPatternComboBox.setDisable(true); 
            startTimeHours.setStyle("-fx-opacity: 1");
            startTimeMinutes.setStyle("-fx-opacity: 1");
            finishTimeHours.setStyle("-fx-opacity: 1");
            finishTimeMinutes.setStyle("-fx-opacity: 1");                           
        }            
    }
    
    @FXML
    private void updatePatternHours(){        
        
        if (shiftPatternComboBox.getSelectionModel().getSelectedItem().equals(patterns.get(0))){
            
            startTimeHours.getSelectionModel().select("00");
            startTimeMinutes.getSelectionModel().select("00");
            finishTimeHours.getSelectionModel().select("00");
            finishTimeMinutes.getSelectionModel().select("00");
            startTimeHours.setDisable(false); 
            startTimeMinutes.setDisable(false); 
            finishTimeHours.setDisable(false); 
            finishTimeMinutes.setDisable(false); 
            startTimeHours.setStyle("-fx-opacity: 1");
            startTimeMinutes.setStyle("-fx-opacity: 1");
            finishTimeHours.setStyle("-fx-opacity: 1");
            finishTimeMinutes.setStyle("-fx-opacity: 1");
        }
     
        else if (shiftPatternComboBox.getSelectionModel().getSelectedItem().equals(patterns.get(1))){
            
            startTimeHours.getSelectionModel().select("09");
            startTimeMinutes.getSelectionModel().select("00");
            finishTimeHours.getSelectionModel().select("17");
            finishTimeMinutes.getSelectionModel().select("00");
            startTimeHours.setDisable(true); 
            startTimeMinutes.setDisable(true); 
            finishTimeHours.setDisable(true); 
            finishTimeMinutes.setDisable(true); 
            startTimeHours.setStyle("-fx-opacity: 1");
            startTimeMinutes.setStyle("-fx-opacity: 1");
            finishTimeHours.setStyle("-fx-opacity: 1");
            finishTimeMinutes.setStyle("-fx-opacity: 1");
            
            }
        else if(shiftPatternComboBox.getSelectionModel().getSelectedItem().equals(patterns.get(2))){

            startTimeHours.getSelectionModel().select("15");
            startTimeMinutes.getSelectionModel().select("00");
            finishTimeHours.getSelectionModel().select("23");
            finishTimeMinutes.getSelectionModel().select("00"); 
            startTimeHours.setDisable(true);
            startTimeMinutes.setDisable(true);
            finishTimeHours.setDisable(true);
            finishTimeMinutes.setDisable(true);
            startTimeHours.setStyle("-fx-opacity: 1");
            startTimeMinutes.setStyle("-fx-opacity: 1");
            finishTimeHours.setStyle("-fx-opacity: 1");
            finishTimeMinutes.setStyle("-fx-opacity: 1");
           
            }        
    }
    
    private void setStartHours(){
  
        startTimeHours.setItems(hours);
        startTimeHours.getSelectionModel().select("09");
    }
    
    private void setStartMinutes(){
  
        startTimeMinutes.setItems(minutes);
        startTimeMinutes.getSelectionModel().selectFirst();
    }
    
    private void setFinishHours(){
  
        finishTimeHours.setItems(hours);
        finishTimeHours.getSelectionModel().select("17");
    }
    
    private void setFinishMinutes(){
  
        finishTimeMinutes.setItems(minutes);
        finishTimeMinutes.getSelectionModel().selectFirst();
    }
    
    
   private void loadAvailableEmployees() throws ClassNotFoundException, SQLException{
       
       employeesAvailable = DAO.getEmployees();
       
       ObservableList<Employee> availablEmployees = FXCollections.observableArrayList();
       
       for (Employee employee : employeesAvailable){
           
           Contract contract = DAO.getContract(employee.getEmployeeContract());
           
           if(LocalDate.parse(contract.getContractEndDate()).isAfter(LocalDate.now()) && (!(LocalDate.parse(contract.getContractEndDate()).isEqual(LocalDate.now())))){
                             
               availablEmployees.add(employee);
           }    
       }                     
       availableEmployeesTable.setItems(availablEmployees);
   }
    
   @FXML
   private void selectedEmployee(ActionEvent e){
       
       if (availableEmployeesTable.getItems().isEmpty()){
           
            Alert noAvailableEmployees = new Alert(Alert.AlertType.ERROR);
            noAvailableEmployees.setHeaderText("There are no available employees to add");
            noAvailableEmployees.showAndWait();
       }
       else if (availableEmployeesTable.getSelectionModel().getSelectedIndex()==-1){
            
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please select an employee from the available employees");
            noSelection.showAndWait();           
       }
       else{             

            selectedEmployeesTable.getItems().add(availableEmployeesTable.getSelectionModel().getSelectedItem());
            availableEmployeesTable.getItems().remove(availableEmployeesTable.getSelectionModel().getSelectedItem());
       }
   }

   
   @FXML
   private void selectAllEmployees(ActionEvent e){
       
       if (availableEmployeesTable.getItems().isEmpty()){
           
            Alert noAvailableEmployees = new Alert(Alert.AlertType.ERROR);
            noAvailableEmployees.setHeaderText("There are no remaining available employees to add");
            noAvailableEmployees.showAndWait();
       }
       else{              
         selectedEmployeesTable.getItems().addAll(availableEmployeesTable.getItems());
         availableEmployeesTable.getItems().clear();
     }       
   }
   
   
   @FXML
   private void removeEmployee(ActionEvent e){
       
       if (selectedEmployeesTable.getItems().isEmpty()){
           
            Alert noSelectedEmployees = new Alert(Alert.AlertType.ERROR);
            noSelectedEmployees.setHeaderText("There are no selected employees to remove");
            noSelectedEmployees.showAndWait();
       }
       else if (selectedEmployeesTable.getSelectionModel().getSelectedIndex()==-1){
            
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please select an employee from the selected employees");
            noSelection.showAndWait();           
       }
       else{             
            
            availableEmployeesTable.getItems().add(selectedEmployeesTable.getSelectionModel().getSelectedItem());
            selectedEmployeesTable.getItems().remove(selectedEmployeesTable.getSelectionModel().getSelectedItem());
       }
       
   }
    
   @FXML
   private void removeAllEmployees(){
       
       if (selectedEmployeesTable.getItems().isEmpty()){
           
            Alert noSelectedEmployees = new Alert(Alert.AlertType.ERROR);
            noSelectedEmployees.setHeaderText("There are no remaining selected employees to remove");
            noSelectedEmployees.showAndWait();
       }
       else{           
         
         availableEmployeesTable.getItems().addAll(selectedEmployeesTable.getItems());
         selectedEmployeesTable.getItems().clear();
     } 
   }
    
    @FXML
    private void checkFromAndToDate(){
        
        if(scheduleFromDate.getValue().isBefore(LocalDate.now())){
            
            Alert dateBeforeToday = new Alert(Alert.AlertType.ERROR);
            dateBeforeToday.setHeaderText("Schedule start date must be from today or after today");
            dateBeforeToday.showAndWait();
            
            scheduleFromDate.setValue(LocalDate.now());            
        }
        else if(scheduleToDate.getValue().isBefore(scheduleFromDate.getValue())){
                          
            scheduleToDate.setValue(scheduleFromDate.getValue());            
        }
    }
    
    //Under optional Settings
    @FXML
    private void setAllDaysTickBoxes(){
        
        if(allDaysTick.isSelected() && !(shiftTypeComboBox.getSelectionModel().getSelectedIndex()>0)){
            
            allDays.setDisable(false);
            
            monTick.setSelected(false);
            tueTick.setSelected(false);
            wedTick.setSelected(false);
            thuTick.setSelected(false);
            friTick.setSelected(false);
            satTick.setSelected(false);
            sunTick.setSelected(false);
            
            mon.clear();
            tue.clear();
            wed.clear();
            thu.clear();
            fri.clear();
            sat.clear();
            sun.clear();
            
            monTick.setDisable(true);
            tueTick.setDisable(true);
            wedTick.setDisable(true);
            thuTick.setDisable(true);
            friTick.setDisable(true);
            satTick.setDisable(true);
            sunTick.setDisable(true);
                       
            mon.setDisable(true);
            tue.setDisable(true);
            wed.setDisable(true);
            thu.setDisable(true);
            fri.setDisable(true);
            sat.setDisable(true);
            sun.setDisable(true);                   
        }
        else if (!(allDaysTick.isSelected())){
            
            allDays.clear();
            allDays.setDisable(true);
            
            monTick.setDisable(false);
            tueTick.setDisable(false);
            wedTick.setDisable(false);
            thuTick.setDisable(false);
            friTick.setDisable(false);
            satTick.setDisable(false);
            sunTick.setDisable(false);            
        }
        else if(shiftTypeComboBox.getSelectionModel().getSelectedIndex()>0){
            
            if(allDaysTick.isSelected()){
            
            monTick.setDisable(true);
            tueTick.setDisable(true);
            wedTick.setDisable(true);
            thuTick.setDisable(true);
            friTick.setDisable(true);
            satTick.setDisable(true);
            sunTick.setDisable(true);
            
            monTick.setSelected(false);
            tueTick.setSelected(false);
            wedTick.setSelected(false);
            thuTick.setSelected(false);
            friTick.setSelected(false);
            satTick.setSelected(false);
            sunTick.setSelected(false);
            
            mon.clear();
            tue.clear();
            wed.clear();
            thu.clear();
            fri.clear();
            sat.clear();
            sun.clear();
                       
            mon.setDisable(true);
            tue.setDisable(true);
            wed.setDisable(true);
            thu.setDisable(true);
            fri.setDisable(true);
            sat.setDisable(true);
            sun.setDisable(true);
            }
            else if(allDaysTick.isSelected()==false){
            allDays.clear();
            allDays.setDisable(true);
            
            monTick.setDisable(false);
            tueTick.setDisable(false);
            wedTick.setDisable(false);
            thuTick.setDisable(false);
            friTick.setDisable(false);
            satTick.setDisable(false);
            sunTick.setDisable(false);
            }
        }
       
    }
    
    //Under optional Settings
    @FXML
    private void handleDayTickBoxes(){
        
      if(!(shiftTypeComboBox.getSelectionModel().getSelectedIndex()>0)){
          
        if(monTick.isSelected()){
            mon.setDisable(false);
        }
        else if (!(monTick.isSelected())){
            
            mon.clear();
            mon.setDisable(true);
        }
        
        if(tueTick.isSelected()){
            tue.setDisable(false);
        }
        else if (!(tueTick.isSelected())){
            
            tue.clear();
            tue.setDisable(true);
        }
        
        if(wedTick.isSelected()){
            wed.setDisable(false);
        }
        else if (!(wedTick.isSelected())){
            
            wed.clear();
            wed.setDisable(true);
        }
        
        if(thuTick.isSelected()){
            thu.setDisable(false);
        }
        else if (!(thuTick.isSelected())){
            
            thu.clear();
            thu.setDisable(true);
        }
        
        if(friTick.isSelected()){
            fri.setDisable(false);
        }
        else if (!(friTick.isSelected())){
            
            fri.clear();
            fri.setDisable(true);
        }
        
        if(satTick.isSelected()){
            sat.setDisable(false);
        }
        else if (!(satTick.isSelected())){
            
            sat.clear();
            sat.setDisable(true);
        }
        
        if(sunTick.isSelected()){
            sun.setDisable(false);
        }
        else if (!(sunTick.isSelected())){
            
            sun.clear();
            sun.setDisable(true);
        }
      }
    }
          
    
    /*
    *Same as 1, 2, and 3, however after hard constraints are considered, system prioritises any specified 
    *numbers of employees required on any day entered under optional settings. So if on Monday, 7 staff are 
    *required then this will take precedence over prefered days off and consecutive working days (or soft constraints).
    *If no employees are set on a particular day under optional settings then the system will prioritising soft constraints while 
    *attempting to assign an even number of shifts across the week/period.
    *
    *Random dates and random employees are chosen now for each allocation of shift to ensure a fair allocation when allocation takes place.
    *Shifts are now evenly placed once per iteration proportionally and incrementally over the period rather than all required shifts per day 
    *being placed on that iteration. This minimises shifts running out before the end of the randomised loops.
    *
    *Current system compared with a dataset benchmarked optimized system and acheives comparable results with less penalty violations.
    */
    @FXML
    private void automatedShifts6() throws ClassNotFoundException, SQLException, ParseException{
        
        //variables for the start and end dates of chosen schedule period
        LocalDate startDate = scheduleFromDate.getValue();
        LocalDate endDate = scheduleToDate.getValue(); 
        
        //variable for the selected shift pattern
        int shiftPattern = shiftPatternComboBox.getSelectionModel().getSelectedIndex()+1;
        
        //variable for the selected shift type
        int shiftType =  shiftTypeComboBox.getSelectionModel().getSelectedIndex()+1;
        
        //start and end times for chosen shift period
        LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(startTimeMinutes.getSelectionModel().getSelectedItem()));
        LocalTime finishTime = LocalTime.of(Integer.parseInt(finishTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(finishTimeMinutes.getSelectionModel().getSelectedItem()));
        
        //variable for days between start and end dates for the selected schedule period
        long schedulePeriod = ChronoUnit.DAYS.between(startDate, endDate)+1;
        
        int totalHours =0;
        double totalMinutes =0;
                
        //variable for total number of hours in a assigned/selected shift
        totalHours = Duration.ofHours(24).plus(Duration.between(startTime, finishTime)).toHoursPart();           
        //variable for the minutes part of a assigned/selected shift. For example 8 hours and 15 minutes. This value would be 15 minutes.
        totalMinutes = Double.parseDouble(df.format((Duration.between(startTime, finishTime).toMinutesPart())*0.0166));        
        
        //variable for total duration of a assigned shift
        double totalDuration = totalHours + totalMinutes;
        
        //variable shift cost value
        double shiftCost;
        
        //variable for holding the number of hours assigned to an employee in a week to evaluate for not exceeding the weekly contracted hours.
        double assignedHours1;
        
        double hoursRequired = 0; //required hours by shift planner
        double maxAvailableHours = 0.0; //max available hours based on selected staff and their contracted hours
        double averageEmployeesPossible = 0; //average number of employees possible per day in a week based on available hours
        double remainingEmployees = 0;//remainder from the average result to add onto random days
        int weekdays = 7;
        
       try{
        //validation check the user has selected at least one employee    
        if(selectedEmployeesTable.getItems().isEmpty()){
            
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please choose an employee or employees to allocate shifts");
            noSelection.showAndWait();
        }
        //validation check for ensuring only numbers are entered into the staff required per day under optional settings
        else if((allDaysTick.isSelected() && !allDays.getText().matches("\\d*")) || (monTick.isSelected() && !mon.getText().matches("\\d*")) || (tueTick.isSelected() && !tue.getText().matches("\\d*"))
                || (wedTick.isSelected() && !wed.getText().matches("\\d*")) || (thuTick.isSelected() && !thu.getText().matches("\\d*")) || (friTick.isSelected() && !fri.getText().matches("\\d*"))
                || (satTick.isSelected() && !sat.getText().matches("\\d*")) || (sunTick.isSelected() && !sun.getText().matches("\\d*"))){
            
            Alert numbersOnly = new Alert(Alert.AlertType.ERROR);
            numbersOnly.setHeaderText("Please use whole numbers only for employees required per day under Optional Settings.");
            numbersOnly.showAndWait();
            
        }
        //validation check for ensuring number of staff required per day under optional setiings, is not blank/empty.
        else if(shiftType==1 && ((allDaysTick.isSelected() && allDays.getText().isBlank()) || (monTick.isSelected() && mon.getText().isBlank()) ||
                    (tueTick.isSelected() && tue.getText().isBlank()) || (wedTick.isSelected() && wed.getText().isBlank()) || 
                    (thuTick.isSelected() && thu.getText().isBlank()) || (friTick.isSelected() && fri.getText().isBlank()) ||
                    (satTick.isSelected() && sat.getText().isBlank()) || (sunTick.isSelected() && sun.getText().isBlank()))){
            
            Alert noEmployees = new Alert(Alert.AlertType.ERROR);
            noEmployees.setHeaderText("Please add the number of employees required per day under Optional Settings or untick the selected option.");
            noEmployees.showAndWait();        
        }
        else if(shiftType==1 && ((allDaysTick.isSelected() && Integer.parseInt(allDays.getText())>selectedEmployeesTable.getItems().size()) || 
                (monTick.isSelected() && Integer.parseInt(mon.getText())>selectedEmployeesTable.getItems().size()) || 
                (tueTick.isSelected() && Integer.parseInt(tue.getText())>selectedEmployeesTable.getItems().size()) ||
                (wedTick.isSelected() && Integer.parseInt(wed.getText())>selectedEmployeesTable.getItems().size()) || 
                (thuTick.isSelected() && Integer.parseInt(thu.getText())>selectedEmployeesTable.getItems().size()) || 
                (friTick.isSelected() && Integer.parseInt(fri.getText())>selectedEmployeesTable.getItems().size()) ||
                (satTick.isSelected() && Integer.parseInt(sat.getText())>selectedEmployeesTable.getItems().size()) || 
                (sunTick.isSelected() && Integer.parseInt(sun.getText())>selectedEmployeesTable.getItems().size()))){
            
            Alert tooManyEmployees = new Alert(Alert.AlertType.ERROR);
            tooManyEmployees.setHeaderText("The required number of employees per day must be lower than the number of employees selected/available ("+selectedEmployeesTable.getItems().size()+")");
            tooManyEmployees.showAndWait();   
            
        } 
        else if(shiftType==1 && allDaysTick.isSelected() && Integer.parseInt(allDays.getText())==0){
            
            Alert tooManyEmployees = new Alert(Alert.AlertType.ERROR);
            tooManyEmployees.setHeaderText("This will create 0 shifts for all days. Please add at least 1 employee or untick the All Days tick box.");
            tooManyEmployees.showAndWait();
        }
        else{                              
            Alert createShiftConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            createShiftConfirmation.setHeaderText("Are you sure you want to create shifts automatically for the selected period, employees and settings?");
            createShiftConfirmation.showAndWait();

            if(createShiftConfirmation.getResult().getText().equalsIgnoreCase("OK")){
                                        
                    LocalDate scheduleDate = scheduleFromDate.getValue(); //set date variable as the first date in the selected shedule period                
                    ArrayList<LocalDate> dates = new ArrayList<>();//these are priority dates if selected by the shift planner under optional settings unless all are selected
                    ArrayList<LocalDate> dates2 = new ArrayList<>();//these will be randomised and less prioritised                                                               
                    
                    employeesSelected.addAll(selectedEmployeesTable.getItems()); //get the employees and add to an ArrayList                 
                    Collections.shuffle(employeesSelected);//randomise order of employees so a fair distribution of shifts can be acheived                 
                    employeesSelected2.addAll(selectedEmployeesTable.getItems()); //a copy of the list to be shuffled later and used in other search loops  

                    //retreive the maximum available hours for a week from the staff selected and their contracted hours
                    for (Employee employee : employeesSelected){                             
                        
                        maxAvailableHours = maxAvailableHours + DAO.getContractedHours(Integer.parseInt(employee.getEmployeeID()));
                          
                       }
                    
                    //get the average number of employees available per day in a week from the staff available and using the duration of each shift required
                    averageEmployeesPossible = Math.floor((maxAvailableHours/7)/totalDuration);                            
                    //retreive total of lost remainders during rounding numbers which may total more than one employee, which will need to be added onto a random day
                    remainingEmployees = Math.round((maxAvailableHours-((averageEmployeesPossible*7)*totalDuration))/totalDuration);
                    

                    ObservableList<CheckBox> daysTickBoxes = FXCollections.observableArrayList(monTick, tueTick, wedTick, thuTick, friTick, satTick, sunTick);//to store tickboxes
                    ObservableList<TextField> dayTextFields = FXCollections.observableArrayList(mon, tue, wed, thu, fri, sat, sun);//to store tickboxes
                    ObservableList<TextField> dayTextFields1 = FXCollections.observableArrayList(mon, tue, wed, thu, fri, sat, sun);//to iterate textfields
                    ObservableList<TextField> priorityDayTextFields = FXCollections.observableArrayList();//to record priority days
                                        
                    int employees = (int) averageEmployeesPossible;//converted rounded double values to integers to wok better with parsing textfields later                    
                    int intRemainder1 = 0;
     
                //if no optional days (including all days tickbox) are selected (ticked), then set all days in the selected schedule to be allocated to the closest as possible, 
                //an average number (whole integer) of employees per day. Any remaining employees (from rounding averages) will be addedd randomly to random days.          
                if(shiftType==1 && (!allDaysTick.isSelected() && !monTick.isSelected() && !tueTick.isSelected() && !wedTick.isSelected() && !thuTick.isSelected() &&
                         !friTick.isSelected() && !satTick.isSelected() && !sunTick.isSelected())){                                        

                    for (CheckBox dayTick: daysTickBoxes){                        
                        dayTick.setSelected(true);
                    }                                        
                                        
                    for (TextField day : dayTextFields){                        
                           day.setText(String.valueOf(employees));//else assign average employees on remaining random days in the week
                    }     
                                                                             
                }
                
                //if all days is selected then check if the hours required are over the hours available and if so then average the hours between all days
                else if(shiftType==1 && allDaysTick.isSelected()){  
                                                 
                //get hours requested
                hoursRequired = (Double.parseDouble(allDays.getText())*7)*totalDuration;                                                                         
                    
                  if(hoursRequired>=maxAvailableHours){
                        
                        allDaysTick.setSelected(false);
                        allDaysTick.setDisable(true);
                        allDays.clear();
                        allDays.setDisable(true);
                        
                        //will use the days to average the shift allocations 
                        for (CheckBox dayTick: daysTickBoxes){                        
                        dayTick.setSelected(true);
                        }                                                                                                         
                                                
                        for (TextField day : dayTextFields){                        
                            day.setText(String.valueOf(employees));//else assign average employees on remaining random days in the week                                        
                        }                            
                    }                   
                    //if under the available hours are required - evenly ditributes shifts using an average                 
                    else{                                                                                                
                        //will use the days to average the shift allocations 
                        for (CheckBox dayTick: daysTickBoxes){                        
                        dayTick.setSelected(true);
                        }                                                                                                         
                                                
                        for (TextField day : dayTextFields){                        
                            day.setText(String.valueOf(allDays.getText()));//else assign average employees on remaining random days in the week                                        
                        }  
                        
                        allDaysTick.setSelected(false);
                        allDaysTick.setDisable(true);
                        allDays.clear();
                        allDays.setDisable(true);                                                    
                    }
                   
                 }
                       
                int employeesRequired = 0;//will be used later to calculate a multiplier
                hoursRequired = 0;
                
                 if(shiftType==1 && (monTick.isSelected() || tueTick.isSelected() || wedTick.isSelected() || thuTick.isSelected() ||
                     friTick.isSelected() || satTick.isSelected() || sunTick.isSelected())){  

                    
                    int sublist = 0;//index counter for randomising (later) dates which are not selected under optional settings 
                    
                    //this loop will ensure that any days selected under optional settings by the shift planner will be prioritised for allocating shifts against non-selected days                                           
                                        
                    if (monTick.isSelected() && Integer.parseInt(mon.getText())>0){                        
                             hoursRequired = hoursRequired + Integer.parseInt(mon.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(mon.getText());
                    }
                    if (tueTick.isSelected() && Integer.parseInt(tue.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(tue.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(tue.getText());
                    }
                    if (wedTick.isSelected() && Integer.parseInt(wed.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(wed.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(wed.getText());
                    }
                    if (thuTick.isSelected() && Integer.parseInt(thu.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(thu.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(thu.getText());
                    }
                    if (friTick.isSelected() && Integer.parseInt(fri.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(fri.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(fri.getText());
                    }
                    if (satTick.isSelected() && Integer.parseInt(sat.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(sat.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(sat.getText());
                    }
                    if (sunTick.isSelected() && Integer.parseInt(sun.getText())>0){
                        
                             hoursRequired = hoursRequired + Integer.parseInt(sun.getText())*totalDuration;
                                employeesRequired = employeesRequired + Integer.parseInt(sun.getText());
                    }                                        
                  
                    for (int i=0; i<schedulePeriod; i++){
                      
                        if (scheduleDate.getDayOfWeek().getValue()==1 && (monTick.isSelected() && Integer.parseInt(mon.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1;                                                        
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==2 && (tueTick.isSelected() && Integer.parseInt(tue.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }  
                        else if(scheduleDate.getDayOfWeek().getValue()==3 && (wedTick.isSelected() && Integer.parseInt(wed.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==4 && (thuTick.isSelected() && Integer.parseInt(thu.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==5 && (friTick.isSelected() && Integer.parseInt(fri.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==6 && (satTick.isSelected() && Integer.parseInt(sat.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==7 && (sunTick.isSelected() && Integer.parseInt(sun.getText())>0)){
                                                        
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else{
                            dates2.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                        }                        
                    }                                      
                    Collections.shuffle(dates);//randomise these priority dates first 
                    
                    if(dates2.isEmpty()==false){
                        dates.addAll(dates2); //then add the lesser priority dates                                                                 
                        Collections.shuffle(dates.subList(sublist, dates.size()));//finally randomise the lesser priority dates from index sublist
                    }
                    
                    if(monTick.isSelected()==false || tueTick.isSelected()==false || wedTick.isSelected()==false || thuTick.isSelected()==false ||
                            friTick.isSelected()==false || satTick.isSelected()==false || sunTick.isSelected()==false){

                        double nonTickedDays = Math.round(((maxAvailableHours-hoursRequired)/totalDuration)/(7-sublist));
                        int intNonTickedAllocation = (int) nonTickedDays;

                        if (monTick.isSelected()==false){
                            mon.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(mon.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(mon.getText());
                        }
                        if (tueTick.isSelected()==false){
                            tue.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(tue.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(tue.getText());
                        }
                        if (wedTick.isSelected()==false){
                            wed.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(wed.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(wed.getText());
                        }
                        if (thuTick.isSelected()==false){
                            thu.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(thu.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(thu.getText());
                        }
                        if (friTick.isSelected()==false){
                            fri.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(fri.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(fri.getText());
                        }
                        if (satTick.isSelected()==false){
                            sat.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(sat.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(sat.getText());
                        }
                        if (sunTick.isSelected()==false){
                            sun.setText(String.valueOf(intNonTickedAllocation));
                            hoursRequired = hoursRequired + Integer.parseInt(sun.getText())*totalDuration;
                            employeesRequired = employeesRequired + Integer.parseInt(sun.getText());
                        }
                    }
                    
                 }
                    
                    //if hours required are higher than those available then first, we need to adjust (by reduction) all 
                    //of the required employees set by the shift planner, proportionally down to below the available hours so they can be allocated approrpiately.  
                 if(hoursRequired>=maxAvailableHours){  
                   
                    double multiplier = maxAvailableHours/employeesRequired;
                    double newValue = 0;                       
                    int intNewValue = 0; 
                    int totalAllocated = 0;                                           
                    
                    for(TextField day : dayTextFields1){ 

                        if (day.isDisabled()==false && Integer.parseInt(day.getText())>0){

                            if(day.equals(mon)){
                                monOldValue = Integer.parseInt(mon.getText());
                                newValue = (Integer.parseInt(mon.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                mon.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(mon);

                            }
                            else if(day.equals(tue)){
                                tueOldValue = Integer.parseInt(tue.getText());
                                newValue = (Integer.parseInt(tue.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                tue.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(tue);

                            }
                            else if(day.equals(wed)){
                               wedOldValue = Integer.parseInt(wed.getText());
                               newValue = (Integer.parseInt(wed.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                wed.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(wed);

                            }
                            else if(day.equals(thu)){
                                thuOldValue = Integer.parseInt(thu.getText());
                                newValue = (Integer.parseInt(thu.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                thu.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(thu);

                            }
                            else if(day.equals(fri)){
                                friOldValue = Integer.parseInt(fri.getText());
                                newValue = (Integer.parseInt(fri.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                fri.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(fri);

                            }
                            else if(day.equals(sat)){
                                satOldValue = Integer.parseInt(sat.getText());
                                newValue = (Integer.parseInt(sat.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                sat.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(sat);

                            }
                            else if(day.equals(sun)){
                                sunOldValue = Integer.parseInt(sun.getText());
                                newValue = (Integer.parseInt(sun.getText())*multiplier)/totalDuration;
                                intNewValue = (int) newValue;
                                sun.setText(String.valueOf(intNewValue));
                                totalAllocated = totalAllocated + intNewValue;
                                priorityDayTextFields.add(sun);

                            }                                                                                                                                                                                                                       

                        } 
                   }
                    //important - this is for allocating (later) any remaining days from teh rounding down of whole integer shifts
                     double remainder = 0;
                     int intRemainder = 0;
                     
                     remainder = Math.round((maxAvailableHours-(totalAllocated*totalDuration))/totalDuration);
                     intRemainder = (int) remainder;
                     intRemainder1 = intRemainder; //global variable to be used by an outer method                          
                }                   
                 
                if(shiftType>1){
                    for (int i=0; i < schedulePeriod; i++){                        
                        dates.add(scheduleDate);
                        scheduleDate = scheduleDate.plusDays(1);
                    }
                } 
                 
                //for each day in the schedule period
                //for (int i=0; i < schedulePeriod; i++){
                for (LocalDate date : dates){     
                    Collections.shuffle(employeesSelected);//randomise employees per day
                                                                
                      //for each employee selected for the projected schedule period  
                      for (Employee employee : employeesSelected){
                          
                        if(shiftType>1){

                            if(allDaysTick.isSelected() || (!monTick.isSelected() && !tueTick.isSelected() && !wedTick.isSelected() && !thuTick.isSelected()
                                    && !friTick.isSelected() && !satTick.isSelected() && !sunTick.isSelected()) ){

                             DAO.createShift(employee.getEmployeeID(), date.format(formatter), LocalTime.of(00, 00), LocalTime.of(00, 00), 1, shiftType, 0);
                             continue;
                            }
                            else if(date.getDayOfWeek().getValue()==1 && monTick.isSelected() || date.getDayOfWeek().getValue()==2 && tueTick.isSelected() ||
                                    date.getDayOfWeek().getValue()==3 && wedTick.isSelected() || date.getDayOfWeek().getValue()==4 && thuTick.isSelected() ||
                                    date.getDayOfWeek().getValue()==5 && friTick.isSelected() || date.getDayOfWeek().getValue()==6 && satTick.isSelected() ||
                                    date.getDayOfWeek().getValue()==7 && sunTick.isSelected()){

                                DAO.createShift(employee.getEmployeeID(), date.format(formatter), LocalTime.of(00, 00), LocalTime.of(00, 00), 1, shiftType, 0);
                                continue;
                            }
                            else{
                                continue;
                            }
                         }
                          
                        //get number of working shifts currently assigned on this day
                        int numOfShiftsOnDay = DAO.getNumOfShiftsOnDay(date.format(formatter)); 
                        int numOfShiftsOnDayAdded = 0;
                          
                        //set weekly assigned/allocated hours counter to 0  
                        assignedHours1=0;                                               
                        
                        //get and store employee contract from database as we will need this for contract constraint checks
                        Contract contract = DAO.getContract(employee.getEmployeeContract());
                        
                        //store shift cost for each shift that will be created, based on the hourly rate from the employee contract multiplied by the 
                        //duration of hours for the selected shift. This figure will be used at the point of assigning a shift.
                        shiftCost = Double.parseDouble(df.format((DAO.getHourlyRate(employee.getEmployeeID())) * totalDuration));                                   
                           
                        //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                        if(!(date.isAfter(LocalDate.parse(contract.getContractEndDate())))){
                                                                    
                        /*The following block of code then gets current number of working hours already assigned for the current employee, for the current week of current day
                        *of the projected schedule period.
                        */
                        //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
                        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
                        //sets period of days to iterate through to check for existing shifts
                        long weekPeriod = startOfWeek.until(endOfWeek, DAYS)+1;

                        LocalDate day = startOfWeek;

                            //for each day of the first week in the projected schedule period
                            for (int a=0; a < weekPeriod; a++){                            

                                //check if there are any existing shifts assigned
                                if(DAO.checkEmployeeShift(employee.getEmployeeID(), day.format(formatter))==true){                                                                              

                                    //get the shift if it exists
                                    Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), day.format(formatter));

                                    //then check that this is a working shift using the shift type
                                    if(shift.getShiftType().equalsIgnoreCase("1")){

                                    //if working shift then get the duration of hours and minutes for that shift    
                                    LocalTime shiftStart = LocalTime.parse(shift.getShiftStart());
                                    LocalTime shiftEnd = LocalTime.parse(shift.getShiftEnd());
                                    int hours1 = Duration.ofHours(24).plus(Duration.between(shiftStart, shiftEnd)).toHoursPart();
                                    double minutes1 = Double.parseDouble(df.format((Duration.between(shiftStart, shiftEnd).toMinutesPart())*0.0166)); 
                                    double duration1 = hours1 + minutes1;
                                    //set a variable for the day to 0 to store this duration
                                    double assignedHours2 = 0;

                                    //adds the duration to the day variable 
                                    assignedHours2 = Double.sum(assignedHours2, duration1);

                                    //passes the duration to a running total for the week
                                    assignedHours1 = Double.sum(assignedHours1, assignedHours2);  
                                    }
                                 }                                                                                                                                               
                                //increment a day and continue the loop to get all assigned hours for that week 
                                day = day.plusDays(1);
                            }//end of calculating working hours for the week                                      
                                                      
                            //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                            if (!(Double.sum(assignedHours1, totalDuration) > Double.parseDouble(contract.getContractedHours()))){                                
                                                                                             
                                /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                * the current date of the schedule period and if does, then to skip this day. 
                                */
                                if(!(DAO.checkEmployeeShift(employee.getEmployeeID(), date.format(formatter))==true)){
                                                                         
                                     /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                     * for minimum hours between shifts has not been exceeded                        
                                     */
                                     
                                     //check if a shift exists the day before this day
                                     if(DAO.checkEmployeeShift(employee.getEmployeeID(), date.minusDays(1).format(formatter))==true){
                                         
                                        //if the above is true, then get the shift
                                        Shift shift2 = DAO.getEmployeeShift(employee.getEmployeeID(), date.minusDays(1).format(formatter));
                                       
                                        //now check if the shift is a working shift  
                                        if(shift2.getShiftType().equalsIgnoreCase("1")){
                                           
                                         //if working shift - get the duration between the end of this shift and the beginning of the proposed shift as none working shifts would have zero duration                                          
                                         LocalTime shiftEnd = LocalTime.parse(shift2.getShiftEnd());
                                         LocalTime shiftStart = startTime;
                                         int hours2 = Duration.ofHours(24).plus(Duration.between(shiftEnd, shiftStart)).toHoursPart();
                                         double minutes2 = Double.parseDouble(df.format((Duration.between(shiftEnd, shiftStart).toMinutesPart())*0.0166));
                                         double duration2 = hours2 + minutes2;
                                                                                  
                                                                     
                                            //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                            if(duration2 >= Double.parseDouble(contract.getMinHoursBetweenShifts())){                                               

                                                //Now hard constraints checks finished consider any demands set by shift planner (staff required per day) and all soft constraints                                                                                               
                                                  
                                                //check that the number of currently assigned shifts have not exceeded the limit set under optional settings for the current day - if optinal settings are set
                                                if( ((date.getDayOfWeek().getValue()==1) && (Integer.parseInt(mon.getText())>numOfShiftsOnDay)) || 
                                                    ((date.getDayOfWeek().getValue()==2) && (Integer.parseInt(tue.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==3) && (Integer.parseInt(wed.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==4) && (Integer.parseInt(thu.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==5) && (Integer.parseInt(fri.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==6) && (Integer.parseInt(sat.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==7) && (Integer.parseInt(sun.getText())>numOfShiftsOnDay)) ){                                                       
                                                    
                                                 
         //////////////////////                        //get consecutive days/shifts count for current employee (soft constraint check)            
                                                       int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                                       LocalDate startConsDays1 = date.minusDays(consDays1); 
                                                       int consShifts1 = 0;
                                                       
                                                       for (int b=0; b < consDays1; b++){
                                                        try{
                                                                Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                                if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                                    consShifts1 = consShifts1 + 1;
                                                                }                                                           
                                                            }
                                                        catch(NullPointerException s){
                                                                }
                                                        startConsDays1 = startConsDays1.plusDays(1);
                                                       }                                                                                                  
                                                    
                                                    /*
                                                    * Soft constraint checks
                                                    * if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                    * then look for another employee who would better suit this day
                                                    */
                                                    if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                        Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                        consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){                                                                                                          
                                      
                                                        
                                                        Collections.shuffle(employeesSelected2);
                                                        //Now we will check if any other employees are available instead - will aim to find the next available only
                                                        for (Employee employee1 : employeesSelected2){
                                                            
                                                            //set assigned/allocated hours count to 0  
                                                            double assignedHours3=0;

                                                            //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                            Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                            //store shift cost for the shift that will be created
                                                            double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                            //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                            if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                            //The following block of code then gets current number of working hours already assigned for the current employee
                                                            
                                                                //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                                LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                                LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                                //sets period of days to iterate through to check for existing shifts
                                                                long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                                LocalDate day1 = startOfWeek1;

                                                                    //for each day of the week in the projected schedule period
                                                                    for (int b=0; b < weekPeriod1; b++){                            

                                                                        //check if there are any existing shifts assigned
                                                                        if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                            //get the shift if it exists
                                                                            Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                            //then check that this is a working shift using the shift type
                                                                            if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                                //if working shift then get the duration of hours and minutes for that shift    
                                                                                LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                                LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                                int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                                double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                                double duration3 = hours3 + minutes3;
                                                                                //set a variable for the day to 0 to store this duration
                                                                                double assignedHours4 = 0;

                                                                                //adds the duration to the day variable 
                                                                                assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                                //passes the duration to a running total for the week
                                                                                assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                            }
                                                                         }                                                                                                                                               
                                                                        //increment a day and continue the loop to get all assigned hours for that week 
                                                                        day1 = day1.plusDays(1);
                                                                    }//end of calculating working hours for the week                                      


                                                                    //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                                    if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                                        /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                                        * the current date of the schedule period and if does, then to skip this day. 
                                                                        */
                                                                        if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                                             /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                                             * for minimum hours between shifts has not been exceeded                        
                                                                             */

                                                                         //check if a shift exists the day before this day
                                                                         if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                             //if the above is true, then get the shift
                                                                             Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                            //now check if the shift is a working shift  
                                                                            if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                             //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                             LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                             LocalTime shiftStart1 = startTime;
                                                                             int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                             double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                             double duration3 = hours3 + minutes3;


                                                                                //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                                if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){
                                                                                    
                                                                                    //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                                           int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                           LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                           int consShifts2 = 0;
                                                                                           for (int c=0; c < consDays2; c++){
                                                                                            try{
                                                                                                    Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                                    if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                        consShifts2 = consShifts2 + 1;
                                                                                                    }                                                           
                                                                                                }
                                                                                            catch(NullPointerException s){
                                                                                                    }
                                                                                            startConsDays2 = startConsDays2.plusDays(1);
                                                                                           }
                                                                                    //System.out.println("employee1 at line: 1785 "+employee1.getEmployeeName());
                                                                                    if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                            Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                            consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                                //assign the working shift
                                                                                                DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
         
                                                                                                //get updated number of working shifts currently assigned on this day
                                                                                                numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                                break;                                                                                                                                                                                                                                                                                                
                                                                                      }                                                                                    
                                                                                 }                                                        
                                                                              }
                                                                            else{
                                                                                //System.out.println("employee1 at line: 1803 "+employee1.getEmployeeName());
                                                                                //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                               int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                               LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                               int consShifts2 = 0;
                                                                               for (int c=0; c < consDays2; c++){
                                                                                try{
                                                                                        Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                        if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                            consShifts2 = consShifts2 + 1;
                                                                                        }                                                           
                                                                                    }
                                                                                catch(NullPointerException s){
                                                                                        }
                                                                                startConsDays2 = startConsDays2.plusDays(1);
                                                                               }
                                                                               
                                                                               if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                    Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                    consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                        //assign the working shift
                                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                                        numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                        break;                                                                                                                                                                                                                                                                           
                                                                                }
                                                                            }
                                                                         }
                                                                         else{
                                                                                //System.out.println("employee1 at line: 1838 "+employee1.getEmployeeName());
                                                                                //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                               int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                               LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                               int consShifts2 = 0;
                                                                               for (int c=0; c < consDays2; c++){
                                                                                try{
                                                                                        Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                        if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                            consShifts2 = consShifts2 + 1;
                                                                                        }                                                           
                                                                                    }
                                                                                catch(NullPointerException s){
                                                                                        }
                                                                                startConsDays2 = startConsDays2.plusDays(1);
                                                                               }
                                                                               
                                                                               if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                    Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                    consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                        //assign the working shift
                                                                                         DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                                                                                                                                                               
                                                                                         
                                                                                         //get updated number of working shifts currently assigned on this day
                                                                                         numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                         break;                                                                                              
                                                                                }
                                                                            }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(numOfShiftsOnDayAdded>0){
                                                            break;//move to next day as we want to spread allocation evenly
                                                        }                                                        
                                                    }
                                                    else{
                                                         //assign the working shift
                                                         DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                                         //System.out.println("Working shift assigned at line 1885 for "+employee.getEmployeeName()+" "+date);
                                                    }                                                
                                                }                                                                                                                                                                                                                                                                   
                                            }                                           
                                        }
                                        else{
                                                //check that the number of currently assigned shifts have not exceeded the limit set under optional settings for the current day
                                                if( ((date.getDayOfWeek().getValue()==1) && (Integer.parseInt(mon.getText())>numOfShiftsOnDay)) || 
                                                    ((date.getDayOfWeek().getValue()==2) && (Integer.parseInt(tue.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==3) && (Integer.parseInt(wed.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==4) && (Integer.parseInt(thu.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==5) && (Integer.parseInt(fri.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==6) && (Integer.parseInt(sat.getText())>numOfShiftsOnDay)) ||
                                                    ((date.getDayOfWeek().getValue()==7) && (Integer.parseInt(sun.getText())>numOfShiftsOnDay)) ){ 
                                                                                                          
         //////////////////////                                           
                                                       int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                                       LocalDate startConsDays1 = date.minusDays(consDays1); 
                                                       int consShifts1 = 0;
                                                       
                                                       for (int b=0; b < consDays1; b++){
                                                        try{
                                                                Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                                if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                                    consShifts1 = consShifts1 + 1;
                                                                }                                                           
                                                            }
                                                        catch(NullPointerException s){
                                                                }
                                                        startConsDays1 = startConsDays1.plusDays(1);
                                                       }                                                                                                  
                                                    
                                                    /*if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                    * then look for another employee who may not prefer to be off on this day
                                                    */
                                                    if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                            Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                            consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){                                                                                                          
                                                        
                                                        Collections.shuffle(employeesSelected2);
                                                        //Now we will check if any other employees are available instead - will find the next available only
                                                        for (Employee employee1 : employeesSelected2){
                                                            
                                                            //set assigned/allocated hours count to 0  
                                                            double assignedHours3=0;

                                                            //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                            Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                            //store shift cost for the shift that will be created
                                                            double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                            //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                            if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                            //The following block of code then gets current number of working hours already assigned for the current employee
                                                            
                                                                //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                                LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                                LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                                //sets period of days to iterate through to check for existing shifts
                                                                long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                                LocalDate day1 = startOfWeek1;

                                                                    //for each day of the first week in the projected schedule period
                                                                    for (int b=0; b < weekPeriod1; b++){                            

                                                                        //check if there are any existing shifts assigned
                                                                        if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                            //get the shift if it exists
                                                                            Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                            //then check that this is a working shift using the shift type
                                                                            if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                                //if working shift then get the duration of hours and minutes for that shift    
                                                                                LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                                LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                                int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                                double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                                double duration3 = hours3 + minutes3;
                                                                                //set a variable for the day to 0 to store this duration
                                                                                double assignedHours4 = 0;

                                                                                //adds the duration to the day variable 
                                                                                assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                                //passes the duration to a running total for the week
                                                                                assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                            }
                                                                         }                                                                                                                                               
                                                                        //increment a day and continue the loop to get all assigned hours for that week 
                                                                        day1 = day1.plusDays(1);
                                                                    }//end of calculating working hours for the week                                      


                                                                    //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                                    if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                                        /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                                        * the current date of the schedule period and if does, then to skip this day. 
                                                                        */
                                                                        if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                                             /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                                             * for minimum hours between shifts has not been exceeded                        
                                                                             */

                                                                         //check if a shift exists the day before this day
                                                                         if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                             //if the above is true, then get the shift
                                                                             Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                            //now check if the shift is a working shift  
                                                                            if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                             //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                             LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                             LocalTime shiftStart1 = startTime;
                                                                             int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                             double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                             double duration3 = hours3 + minutes3;


                                                                                //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                                if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){
                                                                                    
                                                                                    //before assigning the shift check for consecutive working days (soft constraint) before this date   
                                                                               
                                                                                           int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                           LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                           int consShifts2 = 0;
                                                                                           for (int c=0; c < consDays2; c++){
                                                                                            try{
                                                                                                    Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                                    if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                        consShifts2 = consShifts2 + 1;
                                                                                                    }                                                           
                                                                                                }
                                                                                            catch(NullPointerException s){
                                                                                                    }
                                                                                            startConsDays2 = startConsDays2.plusDays(1);
                                                                                           }
                                                                                    
                                                                                            if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                                Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                                consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                                //assign the working shift
                                                                                                DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                                                                                                                                                                                             
                                                                                                //update number of working shifts currently assigned on this day
                                                                                                numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                                break;                                                                                                
                                                                                    }                                                                                    
                                                                                 }                                                                               
                                                                              }
                                                                            else{
                                                                                 //before assigning the shift check for consecutive working days (soft constraint) before this date   
                                                                               
                                                                                           int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                           LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                           int consShifts2 = 0;
                                                                                           for (int c=0; c < consDays2; c++){
                                                                                            try{
                                                                                                    Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                                    if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                        consShifts2 = consShifts2 + 1;
                                                                                                    }                                                           
                                                                                                }
                                                                                            catch(NullPointerException s){
                                                                                                    }
                                                                                            startConsDays2 = startConsDays2.plusDays(1);
                                                                                           }
                                                                                    
                                                                                            if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                                Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                                consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                                //assign the working shift
                                                                                                DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                  
                                                                                                //get updated number of working shifts currently assigned on this day
                                                                                                numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                                break;
                                                                                            }
                                                                            }
                                                                         }
                                                                         else{
                                                                              //before assigning the shift check for consecutive working days (soft constraint) before this date   

                                                                            int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                            LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                            int consShifts2 = 0;
                                                                            for (int c=0; c < consDays2; c++){
                                                                             try{
                                                                                     Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                     if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                         consShifts2 = consShifts2 + 1;
                                                                                     }                                                           
                                                                                 }
                                                                             catch(NullPointerException s){
                                                                                     }
                                                                             startConsDays2 = startConsDays2.plusDays(1);
                                                                            }

                                                                             if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                 Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                 consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                 //assign the working shift
                                                                                DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);           
                                                                                //get updated number of working shifts currently assigned on this day
                                                                                numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                break;
                                                                            }
                                                                         }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(numOfShiftsOnDayAdded>0){
                                                            break;//move to next day
                                                        }
                                                    }
                                                    else{
                                                         //assign the working shift
                                                         DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                                    }
                                                }                                                
                                        }
                                     }
                                     else{
                                         //check that the number of currently assigned shifts have not exceeded the limit set under optional settings for the current day
                                        if( ((date.getDayOfWeek().getValue()==1) && (Integer.parseInt(mon.getText())>numOfShiftsOnDay)) || 
                                            ((date.getDayOfWeek().getValue()==2) && (Integer.parseInt(tue.getText())>numOfShiftsOnDay)) ||
                                            ((date.getDayOfWeek().getValue()==3) && (Integer.parseInt(wed.getText())>numOfShiftsOnDay)) ||
                                            ((date.getDayOfWeek().getValue()==4) && (Integer.parseInt(thu.getText())>numOfShiftsOnDay)) ||
                                            ((date.getDayOfWeek().getValue()==5) && (Integer.parseInt(fri.getText())>numOfShiftsOnDay)) ||
                                            ((date.getDayOfWeek().getValue()==6) && (Integer.parseInt(sat.getText())>numOfShiftsOnDay)) ||
                                            ((date.getDayOfWeek().getValue()==7) && (Integer.parseInt(sun.getText())>numOfShiftsOnDay)) ){ 
                                                                                                          
        /////////////////                        //calculate consecutive days                   
                                                  int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                                  LocalDate startConsDays1 = date.minusDays(consDays1); 
                                                  int consShifts1 = 0;

                                                  for (int b=0; b < consDays1; b++){
                                                   try{
                                                           Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                           if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                               consShifts1 = consShifts1 + 1;
                                                           }                                                           
                                                       }
                                                   catch(NullPointerException s){
                                                           }
                                                   startConsDays1 = startConsDays1.plusDays(1);
                                                  }                                                                                                  
                                                    
                                                    /*if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                    * then look for another employee who may not prefer to be off on this day
                                                    */
                                                    if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                        Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                        consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){                                                                                                          
                                      
                                                        Collections.shuffle(employeesSelected2);
                                                        //Now we will check if any other employees are available instead - will find the next available only
                                                        for (Employee employee1 : employeesSelected2){
                                                            
                                                            //set assigned/allocated hours count to 0  
                                                            double assignedHours3=0;

                                                            //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                            Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                            //store shift cost for the shift that will be created
                                                            double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                            //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                            if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                            //The following block of code then gets current number of working hours already assigned for the current employee

                                                            //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                            LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                            LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                            //sets period of days to iterate through to check for existing shifts
                                                            long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                            LocalDate day1 = startOfWeek1;

                                                                //for each day of the first week in the projected schedule period
                                                                for (int b=0; b < weekPeriod1; b++){                            

                                                                    //check if there are any existing shifts assigned
                                                                    if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                        //get the shift if it exists
                                                                        Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                        //then check that this is a working shift using the shift type
                                                                        if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                            //if working shift then get the duration of hours and minutes for that shift    
                                                                            LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                            LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                            int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                            double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                            double duration3 = hours3 + minutes3;
                                                                            //set a variable for the day to 0 to store this duration
                                                                            double assignedHours4 = 0;

                                                                            //adds the duration to the day variable 
                                                                            assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                            //passes the duration to a running total for the week
                                                                            assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                            }
                                                                         }                                                                                                                                               
                                                                    //increment a day and continue the loop to get all assigned hours for that week 
                                                                    day1 = day1.plusDays(1);
                                                                }//end of calculating working hours for the week                                      


                                                                //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                                if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                                    /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                                    * the current date of the schedule period and if does, then to skip this day. 
                                                                    */
                                                                    if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                                    /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                                    * for minimum hours between shifts has not been exceeded                        
                                                                    */

                                                                        //check if a shift exists the day before this day
                                                                        if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                        //if the above is true, then get the shift
                                                                        Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                            //now check if the shift is a working shift  
                                                                            if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                            //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                            LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                            LocalTime shiftStart1 = startTime;
                                                                            int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                            double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                            double duration3 = hours3 + minutes3;

                                                                                //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                                if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){
                                                                                    
                                                                                    //before assigning the shift check for consecutive working days (soft constraint) before this date   
        
                                                                                    int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                    LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                    int consShifts2 = 0;
                                                                                    for (int c=0; c < consDays2; c++){
                                                                                     try{
                                                                                             Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                             if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                 consShifts2 = consShifts2 + 1;
                                                                                             }                                                           
                                                                                         }
                                                                                     catch(NullPointerException s){
                                                                                             }
                                                                                     startConsDays2 = startConsDays2.plusDays(1);
                                                                                    }
                                                                                    
                                                                                    if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                        Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                        consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                        //assign the working shift
                                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                                        //get updated number of working shifts currently assigned on this day
                                                                                         numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                         break;
                                                                                    }
                                                                                    
                                                                                 }                                                        
                                                                              }
                                                                            else{
                                                                                int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                int consShifts2 = 0;
                                                                                for (int c=0; c < consDays2; c++){
                                                                                 try{
                                                                                         Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                         if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                             consShifts2 = consShifts2 + 1;
                                                                                         }                                                           
                                                                                     }
                                                                                 catch(NullPointerException s){
                                                                                         }
                                                                                 startConsDays2 = startConsDays2.plusDays(1);
                                                                                }

                                                                                if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                        Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                        consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                        //assign the working shift
                                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                                        //get updated number of working shifts currently assigned on this day
                                                                                         numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                         break;
                                                                                }
                                                                            }
                                                                         }
                                                                        else{
                                                                            int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                            LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                            int consShifts2 = 0;
                                                                            for (int c=0; c < consDays2; c++){
                                                                             try{
                                                                                     Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                     if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                         consShifts2 = consShifts2 + 1;
                                                                                     }                                                           
                                                                                 }
                                                                             catch(NullPointerException s){
                                                                                     }
                                                                             startConsDays2 = startConsDays2.plusDays(1);
                                                                            }

                                                                            if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                    Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                    consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                    //assign the working shift
                                                                                    DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                                    //get updated number of working shifts currently assigned on this day
                                                                                    numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                    break;
                                                                            }
                                                                        }
                                                                    }                                                                                                                                  
                                                                }
                                                            }
                                                        }
                                                        if(numOfShiftsOnDayAdded>0){
                                                            break;
                                                        }
                                                    }
                                                    else{
                                                         //assign the working shift
                                                         DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                                         //System.out.println("Working shift assigned at line 2496 for "+employee.getEmployeeName()+" "+date);
                                                    }
                                                }                                                                                                                                                                                     
                                            }
                                     }
                                }
                            }                                                                       
                     }           
                }
                //Distribute remaining shifts required 
                try{
                    if(intRemainder1>0){
                        System.out.println(intRemainder1);
                        Collections.shuffle(priorityDayTextFields);
                                                    
                            for (TextField day : priorityDayTextFields){                                      
                                
                                if(day.equals(mon) && Integer.parseInt(mon.getText()) < monOldValue){                                    
                                    if(intRemainder1>0){
                                        mon.setText(String.valueOf(Integer.valueOf(1)));
                                      intRemainder1 = intRemainder1 -1;
                                    }
                                    else{
                                        mon.setText(String.valueOf(0));
                                    }
                                } 
                                if(day.equals(mon) && Integer.parseInt(mon.getText()) >= monOldValue){
                                    mon.setText(String.valueOf(0));
                                }
                                if(day.equals(tue) && Integer.parseInt(tue.getText()) < tueOldValue){
                                    if(intRemainder1>0){
                                        tue.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                    else{
                                        tue.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(tue) && Integer.parseInt(tue.getText()) >= tueOldValue){
                                    tue.setText(String.valueOf(0));
                                }
                                if(day.equals(wed) && Integer.parseInt(wed.getText()) < wedOldValue){
                                    if(intRemainder1>0){
                                        wed.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                    else{
                                        wed.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(wed) && Integer.parseInt(wed.getText()) >= wedOldValue){
                                    wed.setText(String.valueOf(0));
                                }
                                if(day.equals(thu) && Integer.parseInt(thu.getText()) < thuOldValue){

                                    if(intRemainder1>0){
                                        thu.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                    else{
                                        thu.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(thu) && Integer.parseInt(thu.getText()) >= thuOldValue){
                                    thu.setText(String.valueOf(0));
                                }
                                if(day.equals(fri) && Integer.parseInt(fri.getText()) < friOldValue){

                                    if(intRemainder1>0){
                                        fri.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                    else{
                                        fri.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(fri) && Integer.parseInt(fri.getText()) >= friOldValue){
                                    fri.setText(String.valueOf(0));
                                }
                                if(day.equals(sat) && Integer.parseInt(sat.getText()) < satOldValue){

                                    if(intRemainder1>0){
                                        sat.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                    else{
                                        sat.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(sat) && Integer.parseInt(sat.getText()) >= satOldValue){
                                    sat.setText(String.valueOf(0));
                                }
                                if(day.equals(sun) && Integer.parseInt(sun.getText()) < sunOldValue){
                                   if(intRemainder1>0){
                                        sun.setText(String.valueOf(1));
                                    intRemainder1 = intRemainder1 -1;
                                  }
                                   else{
                                        sun.setText(String.valueOf(0));
                                    }
                                }
                                if(day.equals(sun) && Integer.parseInt(sun.getText()) >= sunOldValue){
                                    sun.setText(String.valueOf(0));
                                }
                            }
                    addRemainingShifts();//this will attempt to add any remaining shifts possible to maximise allocation
                    //return values to original to check which days are supposed to have zero employees
                    mon.setText(String.valueOf(monOldValue));
                    tue.setText(String.valueOf(tueOldValue));
                    wed.setText(String.valueOf(wedOldValue));
                    thu.setText(String.valueOf(thuOldValue));
                    fri.setText(String.valueOf(friOldValue));
                    sat.setText(String.valueOf(satOldValue));
                    sun.setText(String.valueOf(sunOldValue));
                    }
                }catch(NumberFormatException e){
                      
                  }
                
                try{
                //finally assign the days off
                for (LocalDate date : dates){
                 
                  if ((date.getDayOfWeek().getValue()==1 && Integer.parseInt(mon.getText())==0) ||
                      (date.getDayOfWeek().getValue()==2 && Integer.parseInt(tue.getText())==0) ||
                      (date.getDayOfWeek().getValue()==3 && Integer.parseInt(wed.getText())==0) ||
                      (date.getDayOfWeek().getValue()==4 && Integer.parseInt(thu.getText())==0) ||
                      (date.getDayOfWeek().getValue()==5 && Integer.parseInt(fri.getText())==0) ||
                      (date.getDayOfWeek().getValue()==6 && Integer.parseInt(sat.getText())==0) ||
                      (date.getDayOfWeek().getValue()==7 && Integer.parseInt(sun.getText())==0)){
                      
                      continue;
                  }                 
                  else{
                    for (Employee employee : employeesSelected){
                        
                        if(!(DAO.checkEmployeeShift(employee.getEmployeeID(), date.format(formatter))==true)){
                            
                            DAO.createShift(employee.getEmployeeID(), date.format(formatter), LocalTime.of(00, 00), LocalTime.of(00, 00), 1, 2, 0);                            
                        }                        
                    }
                  }                    
                }
                }catch(NumberFormatException n){
                     
                 }
                  //System.out.println("Finished loop");
                  Stage autoShedule = (Stage) this.autoShiftCancelButton.getScene().getWindow();
                  autoShedule.close();
                  
                  refreshCalendar();//update the calendar                 
            }          
        }
       }catch(Exception e){
           e.printStackTrace();
       }
       
    }
    
    
    private void addRemainingShifts() throws ClassNotFoundException, SQLException, ParseException{
        
        //variables for the start and end dates of chosen schedule period
        LocalDate startDate = scheduleFromDate.getValue();
        LocalDate endDate = scheduleToDate.getValue(); 
        
        //variable for the selected shift pattern
        int shiftPattern = shiftPatternComboBox.getSelectionModel().getSelectedIndex()+1;
        
        //variable for the selected shift type
        int shiftType =  shiftTypeComboBox.getSelectionModel().getSelectedIndex()+1;
        
        //start and end times for chosen shift period
        LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(startTimeMinutes.getSelectionModel().getSelectedItem()));
        LocalTime finishTime = LocalTime.of(Integer.parseInt(finishTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(finishTimeMinutes.getSelectionModel().getSelectedItem()));
        
        //variable for days between start and end dates for the selected schedule period
        long schedulePeriod = ChronoUnit.DAYS.between(startDate, endDate)+1;
        
        //variable for total number of hours in a assigned/selected shift
        int totalHours = Duration.ofHours(24).plus(Duration.between(startTime, finishTime)).toHoursPart();
           
        //variable for the minutes part of a assigned/selected shift. For example 8 hours and 15 minutes. This value would be 15 minutes.
        double totalMinutes = Double.parseDouble(df.format((Duration.between(startTime, finishTime).toMinutesPart())*0.0166));        
        
        //variable for total duration of a assigned shift
        double totalDuration = totalHours + totalMinutes;
        
        //variable shift cost value
        double shiftCost;
        
        //variable for holding the number of hours assigned to an employee in a week to evaluate for not exceeding the weekly contracted hours.
        double assignedHours1;                     
                                        
            LocalDate scheduleDate = scheduleFromDate.getValue(); //set another start date variable (this will change in loops) as the first date in the selected shedule period                
            ArrayList<LocalDate> dates = new ArrayList<>();//these are priority dates if selected by the shift planner under optional settings unless all are selected
            ArrayList<LocalDate> dates2 = new ArrayList<>();//these will be randomised and less prioritised                                                               

            employeesSelected.addAll(selectedEmployeesTable.getItems()); //get the employees and add to an ArrayList                 
            Collections.shuffle(employeesSelected);//randomise order of employees so a fair distribution of shifts can be acheived                 
            employeesSelected2.addAll(selectedEmployeesTable.getItems()); //a copy of the list to be shuffled later and used in other search loops  


                
                //At least one of these must be selected from previous method                                
                if(monTick.isSelected() || tueTick.isSelected() || wedTick.isSelected() || thuTick.isSelected() ||
                         friTick.isSelected() || satTick.isSelected() || sunTick.isSelected()){                         
                    
                    int sublist = 0;//index counter for randomising (later) dates which are not selected under optional settings   
                      
                    for (int i=0; i<schedulePeriod; i++){
                        
                        if (scheduleDate.getDayOfWeek().getValue()==1 && (monTick.isSelected() && Integer.parseInt(mon.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1;                                                        
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==2 && (tueTick.isSelected() && Integer.parseInt(tue.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }  
                        else if(scheduleDate.getDayOfWeek().getValue()==3 && (wedTick.isSelected() && Integer.parseInt(wed.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==4 && (thuTick.isSelected() && Integer.parseInt(thu.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==5 && (friTick.isSelected() && Integer.parseInt(fri.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==6 && (satTick.isSelected() && Integer.parseInt(sat.getText())>0)){
                            
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else if(scheduleDate.getDayOfWeek().getValue()==7 && (sunTick.isSelected() && Integer.parseInt(sun.getText())>0)){
                                                        
                            dates.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                            sublist = sublist+1; 
                        }
                        else{
                            dates2.add(scheduleDate);
                            scheduleDate = scheduleDate.plusDays(1);
                        }
                        
                    }
                    Collections.shuffle(dates);//randomise these priority dates first 
                    
                    if(dates2.isEmpty()==false){
                        dates.addAll(dates2); //then add the lesser priority dates                                                                 
                        Collections.shuffle(dates.subList(sublist, dates.size()));//finally randomise the lesser priority dates from index sublist
                    }
                    
                                    
                }                  
                                                                         
                //for each day in the schedule period
                //for (int i=0; i < schedulePeriod; i++){
                for (LocalDate date : dates){     
                    Collections.shuffle(employeesSelected);//randomise employees per day
                                                                
                      //for each employee selected for the projected schedule period  
                      for (Employee employee : employeesSelected){                          
                          
                        //get number of working shifts currently assigned on this day
                        int numOfShiftsOnDay = DAO.getNumOfShiftsOnDay(date.format(formatter)); 
                        int numOfShiftsOnDayAdded = 0;
                          
                        //set weekly assigned/allocated hours counter to 0  
                        assignedHours1=0;                                               
                        
                        //get and store employee contract from database as we will need this for contract constraint checks
                        Contract contract = DAO.getContract(employee.getEmployeeContract());
                        
                        //store shift cost for each shift that will be created, based on the hourly rate from the employee contract multiplied by the 
                        //duration of hours for the selected shift. This figure will be used at the point of assigning a shift.
                        shiftCost = Double.parseDouble(df.format((DAO.getHourlyRate(employee.getEmployeeID())) * totalDuration));                                   
                                
                                                                    
                        /*The following block of code then gets current number of working hours already assigned for the current employee, for the current week of current day
                        *of the projected schedule period.
                        */
                            //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                            LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
                            LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
                            //sets period of days to iterate through to check for existing shifts
                            long weekPeriod = startOfWeek.until(endOfWeek, DAYS)+1;
                                                       
                            LocalDate day = startOfWeek;
                                                                                    
                                //for each day of the first week in the projected schedule period
                                for (int a=0; a < weekPeriod; a++){                            

                                    //check if there are any existing shifts assigned
                                    if(DAO.checkEmployeeShift(employee.getEmployeeID(), day.format(formatter))==true){                                                                              
                                        
                                        //get the shift if it exists
                                        Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), day.format(formatter));
                                        
                                        //then check that this is a working shift using the shift type
                                        if(shift.getShiftType().equalsIgnoreCase("1")){
                                        
                                        //if working shift then get the duration of hours and minutes for that shift    
                                        LocalTime shiftStart = LocalTime.parse(shift.getShiftStart());
                                        LocalTime shiftEnd = LocalTime.parse(shift.getShiftEnd());
                                        int hours1 = Duration.ofHours(24).plus(Duration.between(shiftStart, shiftEnd)).toHoursPart();
                                        double minutes1 = Double.parseDouble(df.format((Duration.between(shiftStart, shiftEnd).toMinutesPart())*0.0166)); 
                                        double duration1 = hours1 + minutes1;
                                        //set a variable for the day to 0 to store this duration
                                        double assignedHours2 = 0;

                                        //adds the duration to the day variable 
                                        assignedHours2 = Double.sum(assignedHours2, duration1);
                                        
                                        //passes the duration to a running total for the week
                                        assignedHours1 = Double.sum(assignedHours1, assignedHours2);  
                                        }
                                     }                                                                                                                                               
                                    //increment a day and continue the loop to get all assigned hours for that week 
                                    day = day.plusDays(1);
                                }//end of calculating working hours for the week                                      
                                
                            //hard constraint - check if ocntracted hours have been exceeded
                            if (!(Double.sum(assignedHours1, totalDuration) > Double.parseDouble(contract.getContractedHours()))){                                
                                                                                             
                                /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                * the current date of the schedule period and if does, then to skip this day. 
                                */
                                if(!(DAO.checkEmployeeShift(employee.getEmployeeID(), date.format(formatter))==true)){
                                    
                                     /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                     * for minimum hours between shifts has not been exceeded                        
                                     */
                                     
                                     //check if a shift exists the day before this day
                                     if(DAO.checkEmployeeShift(employee.getEmployeeID(), date.minusDays(1).format(formatter))==true){
                                         
                                        //if the above is true, then get the shift
                                        Shift shift2 = DAO.getEmployeeShift(employee.getEmployeeID(), date.minusDays(1).format(formatter));
                                       
                                        //now check if the shift is a working shift  
                                        if(shift2.getShiftType().equalsIgnoreCase("1")){
                                           
                                         //if working shift - get the duration between the end of this shift and the beginning of the proposed shift as none working shifts would have zero duration                                          
                                         LocalTime shiftEnd = LocalTime.parse(shift2.getShiftEnd());
                                         LocalTime shiftStart = startTime;
                                         int hours2 = Duration.ofHours(24).plus(Duration.between(shiftEnd, shiftStart)).toHoursPart();
                                         double minutes2 = Double.parseDouble(df.format((Duration.between(shiftEnd, shiftStart).toMinutesPart())*0.0166));
                                         double duration2 = hours2 + minutes2;
                                                                                  
                                                                     
                                            //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                            if(duration2 >= Double.parseDouble(contract.getMinHoursBetweenShifts())){                                               

                                                //Now hard constraints checks finished consider any demands set by shift planner (staff required per day) and all soft constraints                                                                                                
                                                  
                                                //check that the number of currently assigned shifts have not exceeded the required limit for the day
                                                if( ((date.getDayOfWeek().getValue()==1) && (monTick.isSelected() && Integer.parseInt(mon.getText())>0) && (numOfShiftsOnDay<monOldValue)) || 
                                                    ((date.getDayOfWeek().getValue()==2) && (tueTick.isSelected() && Integer.parseInt(tue.getText())>0) && (numOfShiftsOnDay<tueOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==3) && (wedTick.isSelected() && Integer.parseInt(wed.getText())>0) && (numOfShiftsOnDay<wedOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==4) && (thuTick.isSelected() && Integer.parseInt(thu.getText())>0) && (numOfShiftsOnDay<thuOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==5) && (friTick.isSelected() && Integer.parseInt(fri.getText())>0) && (numOfShiftsOnDay<friOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==6) && (satTick.isSelected() && Integer.parseInt(sat.getText())>0) && (numOfShiftsOnDay<satOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==7) && (sunTick.isSelected() && Integer.parseInt(sun.getText())>0) && (numOfShiftsOnDay<sunOldValue))){                                                                                                           
                                                    
        //////////////////////                        //get consecutive days/shifts count for current employee                   
                                                      int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                                      LocalDate startConsDays1 = date.minusDays(consDays1); 
                                                      int consShifts1 = 0;
                                                       
                                                       for (int b=0; b < consDays1; b++){
                                                        try{
                                                                Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                                if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                                    consShifts1 = consShifts1 + 1;
                                                                }                                                           
                                                            }
                                                        catch(NullPointerException s){
                                                                }
                                                        startConsDays1 = startConsDays1.plusDays(1);
                                                       }                                                                                                  
                                                    
                                                    /*if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                    * then look for another employee who would better suit this day
                                                    */
                                                    if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                        Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                        consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){
                                                        
                                                        Collections.shuffle(employeesSelected2);
                                                        //Now we will check if any other employees are available instead - will aim to find the next available only
                                                        for (Employee employee1 : employeesSelected2){
                                                            
                                                            //set assigned/allocated hours count to 0  
                                                            double assignedHours3=0;

                                                            //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                            Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                            //store shift cost for the shift that will be created
                                                            double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                            //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                            if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                            //The following block of code then gets current number of working hours already assigned for the current employee
                                                            
                                                                //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                                LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                                LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                                //sets period of days to iterate through to check for existing shifts
                                                                long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                                LocalDate day1 = startOfWeek1;

                                                                    //for each day of the week in the projected schedule period
                                                                    for (int b=0; b < weekPeriod1; b++){                            

                                                                        //check if there are any existing shifts assigned
                                                                        if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                            //get the shift if it exists
                                                                            Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                            //then check that this is a working shift using the shift type
                                                                            if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                                //if working shift then get the duration of hours and minutes for that shift    
                                                                                LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                                LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                                int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                                double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                                double duration3 = hours3 + minutes3;
                                                                                //set a variable for the day to 0 to store this duration
                                                                                double assignedHours4 = 0;

                                                                                //adds the duration to the day variable 
                                                                                assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                                //passes the duration to a running total for the week
                                                                                assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                            }
                                                                         }                                                                                                                                               
                                                                        //increment a day and continue the loop to get all assigned hours for that week 
                                                                        day1 = day1.plusDays(1);
                                                                    }//end of calculating working hours for the week                                      


                                                                    //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                                    if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                                        /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                                        * the current date of the schedule period and if does, then to skip this day. 
                                                                        */
                                                                        if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                                        /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                                        * for minimum hours between shifts has not been exceeded                        
                                                                        */

                                                                         //check if a shift exists the day before this day
                                                                         if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                             //if the above is true, then get the shift
                                                                             Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                            //now check if the shift is a working shift  
                                                                            if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                             //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                             LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                             LocalTime shiftStart1 = startTime;
                                                                             int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                             double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                             double duration3 = hours3 + minutes3;


                                                                                //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                                if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){
                                                                                    
                                                                                    //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                                           int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                           LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                           int consShifts2 = 0;
                                                                                           for (int c=0; c < consDays2; c++){
                                                                                            try{
                                                                                                    Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                                    if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                        consShifts2 = consShifts2 + 1;
                                                                                                    }                                                           
                                                                                                }
                                                                                            catch(NullPointerException s){
                                                                                                    }
                                                                                            startConsDays2 = startConsDays2.plusDays(1);
                                                                                           }
                                                                                    //System.out.println("employee1 at line: 1785 "+employee1.getEmployeeName());
                                                                                    if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                        Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                        consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                        //assign the working shift
                                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);

                                                                                        //get updated number of working shifts currently assigned on this day
                                                                                        numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                        break;                                                                                                                                                                                                                                                                                                
                                                                                      }                                                                                    
                                                                                 }                                                        
                                                                              }
                                                                            else{
                                                                                //System.out.println("employee1 at line: 1803 "+employee1.getEmployeeName());
                                                                                //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                               int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                               LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                               int consShifts2 = 0;
                                                                               for (int c=0; c < consDays2; c++){
                                                                                try{
                                                                                        Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                        if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                            consShifts2 = consShifts2 + 1;
                                                                                        }                                                           
                                                                                    }
                                                                                catch(NullPointerException s){
                                                                                        }
                                                                                startConsDays2 = startConsDays2.plusDays(1);
                                                                               }
                                                                               
                                                                               if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                    Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                    consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                    //assign the working shift
                                                                                    DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                                    numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                    break;                                                                                                                                                                                                                                                                           
                                                                                }
                                                                            }
                                                                         }
                                                                         else{
                                                                                //System.out.println("employee1 at line: 1838 "+employee1.getEmployeeName());
                                                                                //before assigning the shift check for consecutive working days (soft constraint) before this date                                                                  
                                                                               int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                               LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                               int consShifts2 = 0;
                                                                               for (int c=0; c < consDays2; c++){
                                                                                try{
                                                                                        Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                        if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                            consShifts2 = consShifts2 + 1;
                                                                                        }                                                           
                                                                                    }
                                                                                catch(NullPointerException s){
                                                                                        }
                                                                                startConsDays2 = startConsDays2.plusDays(1);
                                                                               }
                                                                               
                                                                               if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                    Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                    consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){
                                                                                                                                                                                                                               
                                                                                    //assign the working shift
                                                                                     DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                                                                                                                                                               

                                                                                     //get updated number of working shifts currently assigned on this day
                                                                                     numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                     break;                                                                                              
                                                                                }
                                                                            }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(numOfShiftsOnDayAdded>0){
                                                            break;
                                                        }                                                        
                                                    }
                                                    else{
                                                         //assign the working shift
                                                         DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                                         //System.out.println("Working shift assigned at line 1885 for "+employee.getEmployeeName()+" "+date);
                                                    }                                                
                                                }                                                                                                                      
                                            }
                                        }
                                        else{
                                                //check that the number of currently assigned shifts have not exceeded the limit set under optional settings for the current day
                                                if( ((date.getDayOfWeek().getValue()==1) && (monTick.isSelected() && Integer.parseInt(mon.getText())>0) && (numOfShiftsOnDay<monOldValue)) || 
                                                    ((date.getDayOfWeek().getValue()==2) && (tueTick.isSelected() && Integer.parseInt(tue.getText())>0) && (numOfShiftsOnDay<tueOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==3) && (wedTick.isSelected() && Integer.parseInt(wed.getText())>0) && (numOfShiftsOnDay<wedOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==4) && (thuTick.isSelected() && Integer.parseInt(thu.getText())>0) && (numOfShiftsOnDay<thuOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==5) && (friTick.isSelected() && Integer.parseInt(fri.getText())>0) && (numOfShiftsOnDay<friOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==6) && (satTick.isSelected() && Integer.parseInt(sat.getText())>0) && (numOfShiftsOnDay<satOldValue)) ||
                                                    ((date.getDayOfWeek().getValue()==7) && (sunTick.isSelected() && Integer.parseInt(sun.getText())>0) && (numOfShiftsOnDay<sunOldValue))){
                                                                                                          
         //////////////////////                                           
                                                       int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                                       LocalDate startConsDays1 = date.minusDays(consDays1); 
                                                       int consShifts1 = 0;
                                                       
                                                       for (int b=0; b < consDays1; b++){
                                                        try{
                                                            Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                            if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                                consShifts1 = consShifts1 + 1;
                                                            }                                                           
                                                        }
                                                        catch(NullPointerException s){
                                                                }
                                                        startConsDays1 = startConsDays1.plusDays(1);
                                                       }                                                                                                  
                                                    
                                                    /*if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                    * then look for another employee who may not prefer to be off on this day
                                                    */
                                                    if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                        Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                        consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){                                                                                                          
                                                        
                                                        Collections.shuffle(employeesSelected2);
                                                        //Now we will check if any other employees are available instead - will find the next available only
                                                        for (Employee employee1 : employeesSelected2){
                                                            
                                                            //set assigned/allocated hours count to 0  
                                                            double assignedHours3=0;

                                                            //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                            Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                            //store shift cost for the shift that will be created
                                                            double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                            //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                            if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                            //The following block of code then gets current number of working hours already assigned for the current employee
                                                            
                                                                //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                                LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                                LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                                //sets period of days to iterate through to check for existing shifts
                                                                long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                                LocalDate day1 = startOfWeek1;

                                                                    //for each day of the first week in the projected schedule period
                                                                    for (int b=0; b < weekPeriod1; b++){                            

                                                                        //check if there are any existing shifts assigned
                                                                        if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                            //get the shift if it exists
                                                                            Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                            //then check that this is a working shift using the shift type
                                                                            if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                                //if working shift then get the duration of hours and minutes for that shift    
                                                                                LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                                LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                                int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                                double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                                double duration3 = hours3 + minutes3;
                                                                                //set a variable for the day to 0 to store this duration
                                                                                double assignedHours4 = 0;

                                                                                //adds the duration to the day variable 
                                                                                assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                                //passes the duration to a running total for the week
                                                                                assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                            }
                                                                         }                                                                                                                                               
                                                                        //increment a day and continue the loop to get all assigned hours for that week 
                                                                        day1 = day1.plusDays(1);
                                                                    }//end of calculating working hours for the week                                      


                                                                    //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                                    if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                                        /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                                        * the current date of the schedule period and if does, then to skip this day. 
                                                                        */
                                                                        if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                                             /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                                             * for minimum hours between shifts has not been exceeded                        
                                                                             */

                                                                         //check if a shift exists the day before this day
                                                                         if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                             //if the above is true, then get the shift
                                                                             Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                            //now check if the shift is a working shift  
                                                                            if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                             //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                             LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                             LocalTime shiftStart1 = startTime;
                                                                             int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                             double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                             double duration3 = hours3 + minutes3;


                                                                                //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                                if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){
                                                                                    
                                                                                    //before assigning the shift check for consecutive working days (soft constraint) before this date   
                                                                               
                                                                                    int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                    LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                    int consShifts2 = 0;
                                                                                    
                                                                                        for (int c=0; c < consDays2; c++){
                                                                                         try{
                                                                                                 Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                                 if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                                     consShifts2 = consShifts2 + 1;
                                                                                                 }                                                           
                                                                                             }
                                                                                         catch(NullPointerException s){
                                                                                                 }
                                                                                         startConsDays2 = startConsDays2.plusDays(1);
                                                                                        }
                                                                                    
                                                                                        if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                            Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                            consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                            //assign the working shift
                                                                                            DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                                                                                                                                                                                             
                                                                                            //update number of working shifts currently assigned on this day
                                                                                            numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                            break;                                                                                                
                                                                                    }                                                                                    
                                                                                 }                                                                               
                                                                              }
                                                                            else{
                                                                                 //before assigning the shift check for consecutive working days (soft constraint) before this date   
                                                                               
                                                                                int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                                LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                                int consShifts2 = 0;
                                                                                
                                                                                for (int c=0; c < consDays2; c++){
                                                                                 try{
                                                                                    Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                    if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                        consShifts2 = consShifts2 + 1;
                                                                                    }                                                           
                                                                                     }
                                                                                 catch(NullPointerException s){
                                                                                         }
                                                                                 startConsDays2 = startConsDays2.plusDays(1);
                                                                                }
                                                                                    
                                                                                    if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                        Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                        consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                        //assign the working shift
                                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);                  
                                                                                        //get updated number of working shifts currently assigned on this day
                                                                                        numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                        break;
                                                                                    }
                                                                            }
                                                                         }
                                                                         else{
                                                                              //before assigning the shift check for consecutive working days (soft constraint) before this date   

                                                                            int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                            LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                            int consShifts2 = 0;
                                                                            for (int c=0; c < consDays2; c++){
                                                                             try{
                                                                                Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                    consShifts2 = consShifts2 + 1;
                                                                                }                                                           
                                                                                 }
                                                                             catch(NullPointerException s){
                                                                                     }
                                                                             startConsDays2 = startConsDays2.plusDays(1);
                                                                            }

                                                                             if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                                 Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                                 consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                                 //assign the working shift
                                                                                DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);           
                                                                                //get updated number of working shifts currently assigned on this day
                                                                                numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                                break;
                                                                            }
                                                                         }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(numOfShiftsOnDayAdded>0){
                                                            break;
                                                        }
                                                    }
                                                    else{
                                                         //assign the working shift
                                                         DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                                    }
                                                }                                                
                                        }
                                     }
                                     else{
                                         //check that the number of currently assigned shifts have not exceeded the limit set under optional settings for the current day
                                        if( ((date.getDayOfWeek().getValue()==1) && (monTick.isSelected() && Integer.parseInt(mon.getText())>0) && (numOfShiftsOnDay<monOldValue)) || 
                                            ((date.getDayOfWeek().getValue()==2) && (tueTick.isSelected() && Integer.parseInt(tue.getText())>0) && (numOfShiftsOnDay<tueOldValue)) ||
                                            ((date.getDayOfWeek().getValue()==3) && (wedTick.isSelected() && Integer.parseInt(wed.getText())>0) && (numOfShiftsOnDay<wedOldValue)) ||
                                            ((date.getDayOfWeek().getValue()==4) && (thuTick.isSelected() && Integer.parseInt(thu.getText())>0) && (numOfShiftsOnDay<thuOldValue)) ||
                                            ((date.getDayOfWeek().getValue()==5) && (friTick.isSelected() && Integer.parseInt(fri.getText())>0) && (numOfShiftsOnDay<friOldValue)) ||
                                            ((date.getDayOfWeek().getValue()==6) && (satTick.isSelected() && Integer.parseInt(sat.getText())>0) && (numOfShiftsOnDay<satOldValue)) ||
                                            ((date.getDayOfWeek().getValue()==7) && (sunTick.isSelected() && Integer.parseInt(sun.getText())>0) && (numOfShiftsOnDay<sunOldValue))){
                                                                                                          
    /////////////////                        //calculate consecutive days                   
                                              int consDays1 = Integer.parseInt(contract.getMaxConsWorkDays());
                                              LocalDate startConsDays1 = date.minusDays(consDays1); 
                                              int consShifts1 = 0;

                                                for (int b=0; b < consDays1; b++){
                                                 try{
                                                      Shift shift3 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays1.format(formatter));
                                                      if (shift3.getShiftType().equalsIgnoreCase("1")){
                                                          consShifts1 = consShifts1 + 1;
                                                      }                                                           
                                                     }
                                                 catch(NullPointerException s){
                                                         }
                                                 startConsDays1 = startConsDays1.plusDays(1);
                                                }                                                                                                  
                                                    
                                                /*if either of the current employee's preferred days off are on this weekday or consecutive days are reached 
                                                * then look for another employee who may not prefer to be off on this day
                                                */
                                                if (Integer.parseInt((employee.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                    Integer.parseInt((employee.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                    consShifts1 >= Integer.parseInt(contract.getMaxConsWorkDays())){                                                                                                          
                                      
                                                    Collections.shuffle(employeesSelected2);
                                                    //Now we will check if any other employees are available instead - will find the next available only
                                                    for (Employee employee1 : employeesSelected2){

                                                        //set assigned/allocated hours count to 0  
                                                        double assignedHours3=0;

                                                        //get and store employee contract (in this current loop) from database as we will need this for contract constraint checks
                                                        Contract contract1 = DAO.getContract(employee1.getEmployeeContract());

                                                        //store shift cost for the shift that will be created
                                                        double shiftCost1 = Double.parseDouble(df.format((DAO.getHourlyRate(employee1.getEmployeeID())) * totalDuration));                                   

                                                        //hard constraint - check that the current employee contract end date has not passed the current day in the schedule period                     
                                                        if(!(date.isAfter(LocalDate.parse(contract1.getContractEndDate())))){

                                                        //The following block of code then gets current number of working hours already assigned for the current employee

                                                        //first set variables for finding the current whole week (mon-sun)in the chosen schedule period and setting the start and end dates for this week
                                                        LocalDate startOfWeek1 = date.with(DayOfWeek.MONDAY);
                                                        LocalDate endOfWeek1 = date.with(DayOfWeek.SUNDAY);
                                                        //sets period of days to iterate through to check for existing shifts
                                                        long weekPeriod1 = startOfWeek1.until(endOfWeek1, DAYS)+1;

                                                        LocalDate day1 = startOfWeek1;

                                                        //for each day of the first week in the projected schedule period
                                                        for (int b=0; b < weekPeriod1; b++){                            

                                                            //check if there are any existing shifts assigned
                                                            if(DAO.checkEmployeeShift(employee1.getEmployeeID(), day1.format(formatter))==true){                                                                              

                                                                //get the shift if it exists
                                                                Shift shift1 = DAO.getEmployeeShift(employee1.getEmployeeID(), day1.format(formatter));

                                                                //then check that this is a working shift using the shift type
                                                                if(shift1.getShiftType().equalsIgnoreCase("1")){

                                                                    //if working shift then get the duration of hours and minutes for that shift    
                                                                    LocalTime shiftStart1 = LocalTime.parse(shift1.getShiftStart());
                                                                    LocalTime shiftEnd1 = LocalTime.parse(shift1.getShiftEnd());
                                                                    int hours3 = Duration.ofHours(24).plus(Duration.between(shiftStart1, shiftEnd1)).toHoursPart();
                                                                    double minutes3 = Double.parseDouble(df.format((Duration.between(shiftStart1, shiftEnd1).toMinutesPart())*0.0166)); 
                                                                    double duration3 = hours3 + minutes3;
                                                                    //set a variable for the day to 0 to store this duration
                                                                    double assignedHours4 = 0;

                                                                    //adds the duration to the day variable 
                                                                    assignedHours4 = Double.sum(assignedHours4, duration3);

                                                                    //passes the duration to a running total for the week
                                                                    assignedHours3 = Double.sum(assignedHours3, assignedHours4);  
                                                                    }
                                                                 }                                                                                                                                               
                                                            //increment a day and continue the loop to get all assigned hours for that week 
                                                            day1 = day1.plusDays(1);
                                                        }//end of calculating working hours for the week                                      


                                                        //hard constraint - now ensures the contracted weekly hours for the current employee will not been exceeded - if exceeded then will assign day off
                                                        if (!(Double.sum(assignedHours3, totalDuration) > Double.parseDouble(contract1.getContractedHours()))){

                                                            /*hard constraint (sick days, annual leave, etc) - check if the current employee has a shift or any time-off booked already on 
                                                            * the current date of the schedule period and if does, then to skip this day. 
                                                            */
                                                            if(!(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.format(formatter))==true)){

                                                            /*hard constraint - the following block checks if the employee has a shift booked the day before the current date as if does, will need to check the constraint 
                                                            * for minimum hours between shifts has not been exceeded                        
                                                            */

                                                                //check if a shift exists the day before this day
                                                                if(DAO.checkEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter))==true){

                                                                //if the above is true, then get the shift
                                                                Shift shift3 = DAO.getEmployeeShift(employee1.getEmployeeID(), date.minusDays(1).format(formatter));

                                                                    //now check if the shift is a working shift  
                                                                    if(shift3.getShiftType().equalsIgnoreCase("1")){

                                                                    //if working shift - get the duration between the end of this shift and the beginning of the proposed shift                                          
                                                                    LocalTime shiftEnd1 = LocalTime.parse(shift3.getShiftEnd());
                                                                    LocalTime shiftStart1 = startTime;
                                                                    int hours3 = Duration.ofHours(24).plus(Duration.between(shiftEnd1, shiftStart1)).toHoursPart();
                                                                    double minutes3 = Double.parseDouble(df.format((Duration.between(shiftEnd1, shiftStart1).toMinutesPart())*0.0166));
                                                                    double duration3 = hours3 + minutes3;

                                                                    //hard constraint - now check that minimum hours between shifts as stated in the employee contract has not been exceeded
                                                                    if(duration3 >= Double.parseDouble(contract1.getMinHoursBetweenShifts())){

                                                                        //before assigning the shift check for consecutive working days (soft constraint) before this date   

                                                                        int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                        LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                        int consShifts2 = 0;
                                                                        for (int c=0; c < consDays2; c++){
                                                                         try{
                                                                                 Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                                 if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                     consShifts2 = consShifts2 + 1;
                                                                                 }                                                           
                                                                             }
                                                                         catch(NullPointerException s){
                                                                                 }
                                                                         startConsDays2 = startConsDays2.plusDays(1);
                                                                        }
                                                                                    
                                                                        if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                            Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                            consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                            //assign the working shift
                                                                            DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                            //get updated number of working shifts currently assigned on this day
                                                                             numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                             break;
                                                                        }

                                                                     }                                                        
                                                                  }
                                                                else{
                                                                    int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                    LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                    int consShifts2 = 0;
                                                                    for (int c=0; c < consDays2; c++){
                                                                     try{
                                                                             Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                             if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                                 consShifts2 = consShifts2 + 1;
                                                                             }                                                           
                                                                         }
                                                                     catch(NullPointerException s){
                                                                             }
                                                                     startConsDays2 = startConsDays2.plusDays(1);
                                                                    }

                                                                    if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                            Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                            consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                            //assign the working shift
                                                                            DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                            //get updated number of working shifts currently assigned on this day
                                                                             numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                             break;
                                                                    }
                                                                }
                                                             }
                                                            else{
                                                                int consDays2 = Integer.parseInt(contract1.getMaxConsWorkDays());
                                                                LocalDate startConsDays2 = date.minusDays(consDays2); 
                                                                int consShifts2 = 0;
                                                                for (int c=0; c < consDays2; c++){
                                                                 try{
                                                                         Shift shift4 = DAO.getEmployeeShift(employee1.getEmployeeID(), startConsDays2.format(formatter));
                                                                         if (shift4.getShiftType().equalsIgnoreCase("1")){
                                                                             consShifts2 = consShifts2 + 1;
                                                                         }                                                           
                                                                     }
                                                                 catch(NullPointerException s){
                                                                         }
                                                                 startConsDays2 = startConsDays2.plusDays(1);
                                                                }

                                                                if (!(Integer.parseInt((employee1.getPreferredDayOff1()))==date.getDayOfWeek().getValue() || 
                                                                        Integer.parseInt((employee1.getPreferredDayOff2()))==date.getDayOfWeek().getValue() ||
                                                                        consShifts2 >= Integer.parseInt(contract1.getMaxConsWorkDays()))){

                                                                        //assign the working shift
                                                                        DAO.createShift(employee1.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost1);
                                                                        //get updated number of working shifts currently assigned on this day
                                                                        numOfShiftsOnDayAdded = numOfShiftsOnDayAdded +1;
                                                                        break;
                                                                }
                                                            }
                                                        }                                                                                                                                  
                                                    }
                                                }
                                            }
                                            if(numOfShiftsOnDayAdded>0){
                                                break;
                                            }
                                        }
                                        else{
                                             //assign the working shift
                                             DAO.createShift(employee.getEmployeeID(), date.format(formatter), startTime, finishTime, shiftPattern, shiftType, shiftCost);
                                             //System.out.println("Working shift assigned at line 2496 for "+employee.getEmployeeName()+" "+date);
                                        }
                                    }                                                                                                                                                                                     
                                }
                             }  
                        }                                                                       
                    }           
                }                
    }
    
    
    public void refreshCalendar() throws SQLException, ClassNotFoundException, ParseException{
        
                calendar3.getRowConstraints().removeAll(rows);
                calendar3.getChildren().removeAll(buttons);
                calendar3.getChildren().removeAll(employeeLabels);           

                    double totalCost = 0;
                    int preferredDaysOffMet = 100;
                    int consDaysMet = 100;
                    int inContractEmployees = 0;
             try{     
                   ObservableList<Employee> employees = DAO.getEmployees(); 
            
                    //set penalties
                     for (Employee employee : employees){

                         Contract contract = DAO.getContract(employee.getEmployeeContract());
                         if(LocalDate.parse(contract.getContractEndDate()).isAfter(LocalDate.now())){

                          inContractEmployees = inContractEmployees + 1;
                        } 
                     }
                     //preferred day off penalty
                    int penalty = 100/(inContractEmployees*2); 
                    //consecutive days penalty
                    int consDaysPenalty = 100/inContractEmployees;
                    
                    int allConsDaysCounter = 0;
                    
                    for (Employee employee : employees){
                        
                        int consDaysCounter = 0;

                        Contract empContract = DAO.getContract(employee.getEmployeeContract()); 
                        
                        if(today.isBefore(LocalDate.parse(empContract.getContractEndDate()).plusDays(7)) && 
                          (!(today.isEqual(LocalDate.parse(empContract.getContractEndDate()).plusDays(7))))){

                    Button button1 = new Button();
                    Button button2 = new Button();
                    Button button3 = new Button();
                    Button button4 = new Button();
                    Button button5 = new Button();
                    Button button6 = new Button();
                    Button button7 = new Button();

                    Label label = new Label(employee.getEmployeeName());
                    label.setStyle("-fx-font-size: 17; -fx-font-family: Arial; -fx-text-fill: #575757");
                    label.setId(employee.getEmployeeID()); //stores ID of the employee

                    calendar3.getRowConstraints().add(row);
                    calendar3.add(label, 0, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button1, 1, (calendar3.getRowConstraints().lastIndexOf(row)));
                    calendar3.add(button2, 2, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button3, 3, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button4, 4, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button5, 5, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button6, 6, (calendar3.getRowConstraints().lastIndexOf(row))); 
                    calendar3.add(button7, 7, (calendar3.getRowConstraints().lastIndexOf(row))); 

                    rows.add(row);
                    employeeLabels.add(label);     
                    
                    label.setOnMouseClicked(event ->{
               
                        if(event.getClickCount()==1){

                           Contract contract = new Contract();
                            try {
                                contract = DAO.getContract(employee.getEmployeeContract());                       
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(HomescreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                try{

                                    Group root1 = new Group();
                                    Stage stage = new Stage();           
                                    FXMLLoader fxmlLoader = new FXMLLoader();
                                    fxmlLoader.setLocation(getClass().getResource("AddEmployeeFXML.fxml"));
                                    AnchorPane frame = fxmlLoader.load();
                                    AddEmployeeFXMLController b = (AddEmployeeFXMLController) fxmlLoader.getController(); 
                                    
                                    b.calendar1 = calendar3;
                                    b.monthYearLabel = monthYearLabel;
                                    b.mondayDateLabel = mondayDateLabel;
                                    b.tuesdayDateLabel = tuesdayDateLabel;
                                    b.wednesdayDateLabel = wednesdayDateLabel;
                                    b.thursdayDateLabel = thursdayDateLabel;
                                    b.fridayDateLabel = fridayDateLabel;
                                    b.saturdayDateLabel = saturdayDateLabel;
                                    b.sundayDateLabel = sundayDateLabel;
                                    b.dayLabels = dayLabels;
                                    b.row = row;
                                    b.rows = rows;
                                    b.buttons = buttons;
                                    b.employeeLabels = employeeLabels;
                                    b.labourCostTextField = labourCostTextField;
                                    b.weeklyBudgetTextField = weeklyBudgetTextField;
                                    b.prefDaysOff = prefDaysOff;
                                    b.consDays = consDays;
                                    b.today = today;
                                    b.monTotal = monTotal;
                                    b.tueTotal = tueTotal;
                                    b.wedTotal = wedTotal;
                                    b.thuTotal = thuTotal;
                                    b.friTotal = friTotal;
                                    b.satTotal = satTotal;
                                    b.sunTotal = sunTotal;
                                    b.shiftTotal = shiftTotal;
                                    b.monCost = monCost;
                                    b.tueCost = tueCost;
                                    b.wedCost = wedCost;
                                    b.thuCost = thuCost;
                                    b.friCost = friCost;
                                    b.satCost = satCost;
                                    b.sunCost = sunCost;

                                    root1.getChildren().add(frame);
                                    Scene scene = new Scene(root1);

                                    stage.setScene(scene);
                                    stage.show();

                                    b.addEmployeeTitleLabel.setText("View/Edit Employee");
                                    b.createNewEmployeeButton.setText("Edit Employee");
                                    b.removeEmployeeButton.setVisible(true);
                                    b.removeEmployeeButton.setTextFill(Color.RED);
                                    b.nameTextField.setText(employee.getEmployeeName());
                                    b.nameTextField.setId(employee.getEmployeeID());
                                    b.contractStartDatePicker.setValue(LocalDate.parse(contract.getContractStartDate()));
                                    b.contractStartDatePicker.setDisable(true);
                                    b.contractStartDatePicker.setStyle("-fx-opacity: 1");
                                    b.contractEndDatePicker.setValue(LocalDate.parse(contract.getContractEndDate()));
                                    b.contractedHoursTextField.setText(contract.getContractedHours());
                                    b.hourlyRateTextField.setText(contract.getHourlyRate());
                                    b.minHoursTextField.setText(contract.getMinHoursBetweenShifts());
                                    b.maxWorkDaysTextField.setText(contract.getMaxConsWorkDays());
                                    b.preferredDayCombo1.getSelectionModel().select(Integer.parseInt(employee.getPreferredDayOff1()));
                                    b.preferredDayCombo2.getSelectionModel().select(Integer.parseInt(employee.getPreferredDayOff2()));


                                } catch(Exception ex){

                                    System.out.println(ex);
                                }

                        }   
                    });
                    
                    buttons.add(button1);
                    buttons.add(button2);
                    buttons.add(button3);
                    buttons.add(button4);
                    buttons.add(button5);
                    buttons.add(button6);
                    buttons.add(button7);

                    Button[] buttons1 = {button1, button2, button3, button4, button5, button6, button7};

                    for (Button button : buttons1){
                        
                        button.setMinWidth(130);
                        button.setMinHeight(30);
                        
                        LocalDate buttonDate = today.with(DayOfWeek.of(GridPane.getColumnIndex(button)));
                        
                         //check for previous consecutive working shifts to add to consecutive shifts counter for calculating consecutive shifts soft constraint penalties
                        int consDays1 = Integer.parseInt(empContract.getMaxConsWorkDays());
                        LocalDate startConsDays = buttonDate.minusDays(consDays1); 
                        int consShifts = 0;
                        
                        
                            try{
                                Shift shift1 = DAO.getEmployeeShift(employee.getEmployeeID(), buttonDate.format(formatter));
                                if(shift1.getShiftType().equalsIgnoreCase("1")){
                                    consShifts = consShifts+1;
                                }
                            }
                            catch(NullPointerException x){

                            }
                            
                        for (int i=0; i < consDays1; i++){
                         try{
                                 Shift shift2 = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays.format(formatter));
                                 
                                 if (shift2.getShiftType().equalsIgnoreCase("1")){
                                     consShifts = consShifts + 1;                                     
                                 }                                                           
                             }
                         catch(NullPointerException x){

                                 }
                         startConsDays = startConsDays.plusDays(1);
                        }                       
                        
                        if (consShifts > Integer.parseInt(empContract.getMaxConsWorkDays())){
                            
                            consDaysCounter = consDaysCounter + 1;
                            allConsDaysCounter = allConsDaysCounter+1;
                            
                        }
                    
                        if(buttonDate.isAfter(LocalDate.parse(empContract.getContractEndDate())) || buttonDate.isEqual(LocalDate.parse(empContract.getContractEndDate()))){
                        button.setText("Contract Ended");
                        button.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        button.setDisable(true);
                    }
                    else{

                            if(DAO.checkEmployeeShift(employee.getEmployeeID(), dayLabels.get(GridPane.getColumnIndex(button)-1).getText().concat("/"+monthYearLabel.getText()))==true){

                              Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), dayLabels.get(GridPane.getColumnIndex(button)-1).getText().concat("/"+monthYearLabel.getText()));

                              String shiftType = shift.getShiftType();
                              
                              double cost = Double.parseDouble(shift.getShiftCost());
                              totalCost =  totalCost + cost;  

                              switch (shiftType){

                              case "1": button.setText("Working: " + shift.getShiftStart() + "-" + shift.getShiftEnd()); button.setStyle("-fx-background-color: #aed581; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Working"); break;                        
                              case "2": button.setText("Day Off"); button.setStyle("-fx-background-color: #add8e6; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Day Off"); break;
                              case "3": button.setText("Off Sick"); button.setStyle("-fx-background-color: #ffff00; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Off Sick");  break;
                              case "4": button.setText("Emergency Leave"); button.setStyle("-fx-background-color: #ff6347; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Emergency Leave");  break;
                              case "5": button.setText("Annual Leave"); button.setStyle("-fx-background-color: #9370db; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Annual Leave"); break;                               
                              }
                              
                              //add red text colour on button for soft constraint violations to aid shift planner                         
                            if(button.getId().equalsIgnoreCase("Working") && consDaysCounter > 0){

                              button.setStyle("-fx-background-color: #aed581; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                              consDaysCounter=0;
                                }                          

                            if(button.getId().equalsIgnoreCase("Working") && (Integer.parseInt(employee.getPreferredDayOff1())==GridPane.getColumnIndex(button)
                                 || Integer.parseInt((employee.getPreferredDayOff2()))==GridPane.getColumnIndex(button))){

                                 preferredDaysOffMet = preferredDaysOffMet - penalty;
                                 button.setStyle("-fx-background-color: #aed581; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                                }
                              
                              button.setOnAction(new EventHandler<ActionEvent>(){
                                
                                @Override 
                                public void handle(ActionEvent e){ 
                                try{        

                                Group root = new Group();
                                Stage stage = new Stage();

                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("AddShiftFXML.fxml"));
                                AnchorPane frame = fxmlLoader.load();
                                AddShiftFXMLController c = (AddShiftFXMLController) fxmlLoader.getController();
                                c.calendar2 = calendar3;
                                c.monthYearLabel = monthYearLabel;
                                c.mondayDateLabel = mondayDateLabel;
                                c.tuesdayDateLabel = tuesdayDateLabel;
                                c.wednesdayDateLabel = wednesdayDateLabel;
                                c.thursdayDateLabel = thursdayDateLabel;
                                c.fridayDateLabel = fridayDateLabel;
                                c.saturdayDateLabel = saturdayDateLabel;
                                c.sundayDateLabel = sundayDateLabel;
                                c.dayLabels = dayLabels;
                                c.row = row;
                                c.rows = rows;
                                c.buttons = buttons;
                                c.employeeLabels = employeeLabels;
                                c.labourCostTextField = labourCostTextField;
                                c.weeklyBudgetTextField = weeklyBudgetTextField;
                                c.prefDaysOff = prefDaysOff;
                                c.consDays = consDays;
                                c.today = today;
                                c.monTotal = monTotal;
                                c.tueTotal = tueTotal;
                                c.wedTotal = wedTotal;
                                c.thuTotal = thuTotal;
                                c.friTotal = friTotal;
                                c.satTotal = satTotal;
                                c.sunTotal = sunTotal;
                                c.shiftTotal = shiftTotal;
                                c.monCost = monCost;
                                c.tueCost = tueCost;
                                c.wedCost = wedCost;
                                c.thuCost = thuCost;
                                c.friCost = friCost;
                                c.satCost = satCost;
                                c.sunCost = sunCost;

                                root.getChildren().add(frame);
                                Scene scene = new Scene(root);

                                stage.setScene(scene);
                                stage.show();
                                
                                c.addEditShiftLabel.setText("View/Edit Shift");
                                c.createShiftButton.setText("Edit Shift");

                                int day = GridPane.getColumnIndex(button); 
                                    calendar3.getChildren().forEach((node) -> {

                                        if(node.equals(label)){
                                         c.employeeIdTxtField.setText(label.getId());
                                         c.employeeNameTxtField.setText(label.getText());
                                         c.employeeIdTxtField.setStyle("-fx-opacity: 1");
                                         c.employeeNameTxtField.setStyle("-fx-opacity: 1");
                                         
                                         }
                                        switch(day){
                                            case 1: c.shiftDateTxtField.setText(mondayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 2: c.shiftDateTxtField.setText(tuesdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 3: c.shiftDateTxtField.setText(wednesdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 4: c.shiftDateTxtField.setText(thursdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 5: c.shiftDateTxtField.setText(fridayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 6: c.shiftDateTxtField.setText(saturdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                            case 7: c.shiftDateTxtField.setText(sundayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                        }
                                        
                                        c.shiftTypeComboBox.setValue(button.getId());
                                        c.startTimeHours.setValue(shift.getShiftStart().substring(0, 2));
                                        c.startTimeMinutes.setValue(shift.getShiftStart().substring(3));
                                        c.finishTimeHours.setValue(shift.getShiftEnd().substring(0, 2));
                                        c.finishTimeMinutes.setValue(shift.getShiftEnd().substring(3));
                                        c.shiftPatternComboBox.getSelectionModel().select(Integer.parseInt(shift.getShiftPattern())-1);
                                        c.removeShiftButton.setVisible(true); c.removeShiftButton.setTextFill(Color.RED);
                                  }); 

                                 } catch(Exception ex){            

                                     }           
                                }
                             });
                            }                              
                            else{         
                                   button.setText("+ Add Shift"); 
                                   button.setStyle("-fx-background-color: #FFFFFF; -fx-outer-border-color: #dcdcdc;"); 

                                        button.setOnAction(new EventHandler<ActionEvent>(){
                                        @Override 
                                        public void handle(ActionEvent e){ 
                                        try{        

                                        Group root = new Group();
                                        Stage stage = new Stage();

                                        FXMLLoader fxmlLoader = new FXMLLoader();
                                        fxmlLoader.setLocation(getClass().getResource("AddShiftFXML.fxml"));
                                        AnchorPane frame = fxmlLoader.load();
                                        AddShiftFXMLController c = (AddShiftFXMLController) fxmlLoader.getController();
                                        c.calendar2 = calendar3;
                                        c.monthYearLabel = monthYearLabel;
                                        c.mondayDateLabel = mondayDateLabel;
                                        c.tuesdayDateLabel = tuesdayDateLabel;
                                        c.wednesdayDateLabel = wednesdayDateLabel;
                                        c.thursdayDateLabel = thursdayDateLabel;
                                        c.fridayDateLabel = fridayDateLabel;
                                        c.saturdayDateLabel = saturdayDateLabel;
                                        c.sundayDateLabel = sundayDateLabel;
                                        c.dayLabels = dayLabels;
                                        c.row = row;
                                        c.rows = rows;
                                        c.buttons = buttons;
                                        c.employeeLabels = employeeLabels;
                                        c.labourCostTextField = labourCostTextField;
                                        c.weeklyBudgetTextField = weeklyBudgetTextField;
                                        c.prefDaysOff = prefDaysOff;
                                        c.consDays = consDays;
                                        c.today = today;
                                        c.monTotal = monTotal;
                                        c.tueTotal = tueTotal;
                                        c.wedTotal = wedTotal;
                                        c.thuTotal = thuTotal;
                                        c.friTotal = friTotal;
                                        c.satTotal = satTotal;
                                        c.sunTotal = sunTotal;
                                        c.shiftTotal = shiftTotal;
                                        c.monCost = monCost;
                                        c.tueCost = tueCost;
                                        c.wedCost = wedCost;
                                        c.thuCost = thuCost;
                                        c.friCost = friCost;
                                        c.satCost = satCost;
                                        c.sunCost = sunCost;

                                        root.getChildren().add(frame);
                                        Scene scene = new Scene(root);

                                        stage.setScene(scene);
                                        stage.show();

                                        int day = GridPane.getColumnIndex(button); 
                                            calendar3.getChildren().forEach((node) -> {

                                                if(node.equals(label)){
                                                 c.employeeIdTxtField.setText(label.getId());
                                                 c.employeeNameTxtField.setText(label.getText());
                                                 c.employeeIdTxtField.setStyle("-fx-opacity: 1");
                                                 c.employeeNameTxtField.setStyle("-fx-opacity: 1");

                                                 }
                                                switch(day){
                                                    case 1: c.shiftDateTxtField.setText(mondayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 2: c.shiftDateTxtField.setText(tuesdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 3: c.shiftDateTxtField.setText(wednesdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 4: c.shiftDateTxtField.setText(thursdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 5: c.shiftDateTxtField.setText(fridayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 6: c.shiftDateTxtField.setText(saturdayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                    case 7: c.shiftDateTxtField.setText(sundayDateLabel.getText().concat("/"+monthYearLabel.getText())); c.shiftDateTxtField.setStyle("-fx-opacity: 1"); break;
                                                }
                                                c.removeShiftButton.setVisible(false);
                                          }); 

                                         } catch(Exception ex){            

                                             }           
                                           }
                                        });
                                    
                                   }
                                   
                                 }
                               }           
                           }                        
                      }   
                    //check employee consecutive working days                    
                    if(allConsDaysCounter > 0){

                        consDaysMet = consDaysMet - consDaysPenalty*allConsDaysCounter;
                   }
                    monTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(mondayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    monTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    tueTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(tuesdayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    tueTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    wedTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(wednesdayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    wedTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    thuTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(thursdayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    thuTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    friTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(fridayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    friTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    satTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(saturdayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    satTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    sunTotal.setText(String.valueOf(DAO.getNumOfShiftsOnDay(sundayDateLabel.getText().concat("/"+monthYearLabel.getText()))));
                    sunTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");

                    shiftTotal.setText(String.valueOf(Integer.parseInt(monTotal.getText())+Integer.parseInt(tueTotal.getText())+Integer.parseInt(wedTotal.getText())
                                +Integer.parseInt(thuTotal.getText())+Integer.parseInt(friTotal.getText())+Integer.parseInt(satTotal.getText())+Integer.parseInt(sunTotal.getText())));
                    shiftTotal.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d; -fx-font-weight: bold;");
                    weeklyBudgetTextField.setText(String.valueOf(DAO.getWeeklyBudget()));
                    weeklyBudgetTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d; -fx-font-weight: bold;");
                    
                    monCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(mondayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    monCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    tueCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(tuesdayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    tueCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    wedCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(wednesdayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    wedCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    thuCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(thursdayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    thuCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    friCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(fridayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    friCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    satCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(saturdayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    satCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    sunCost.setText("£"+String.valueOf(df.format(DAO.getDailyCost(sundayDateLabel.getText().concat("/"+monthYearLabel.getText())))));
                    sunCost.setStyle("-fx-opacity: 1; -fx-text-inner-color: black;");
                    
                    labourCostTextField.setText("£"+Double.parseDouble(df.format(totalCost)));


                    if(totalCost > Double.parseDouble(weeklyBudgetTextField.getText())){

                        labourCostTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: red; -fx-font-weight: bold;");                
                    }
                    else{
                        labourCostTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d;-fx-font-weight: bold;");                  
                    }

                prefDaysOff.setText(String.valueOf(preferredDaysOffMet) + "%");
                prefDaysOff.setStyle("-fx-opacity: 1; -fx-font-weight: bold;");

                consDays.setText(String.valueOf(consDaysMet)+ "%");
                consDays.setStyle("-fx-opacity: 1; -fx-font-weight: bold;");
                
                } catch (NullPointerException | SQLException e){
                
                } 
    }
    
    
    @FXML
    private void cancelButton(ActionEvent e){
                   
        Stage autoShedule = (Stage) this.autoShiftCancelButton.getScene().getWindow();
        autoShedule.close();
    }
    
}
