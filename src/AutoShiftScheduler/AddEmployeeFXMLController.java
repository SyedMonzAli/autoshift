
package AutoShiftScheduler;

import Database.DAO;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * Undergraduate final year individual computing project - KV6003
 * Northumbria University - 2019/20
 * 
 * @author Syed Ali - w17023496
 *
 * FXML Controller class for adding and editing an employee record
 */
public class AddEmployeeFXMLController implements Initializable {
    
    ObservableList<String> days = FXCollections.observableArrayList("No Preference", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    ArrayList<Label> dayLabels = new ArrayList<>();
    RowConstraints row = new RowConstraints(55);
    ArrayList<RowConstraints> rows = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> employeeLabels = new ArrayList<>();
    DecimalFormat df = new DecimalFormat ("#.##");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");    
    
    protected GridPane calendar1;
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
    
    
    @FXML
    protected TextField nameTextField;
    @FXML
    protected DatePicker contractStartDatePicker;
    @FXML
    protected TextField contractedHoursTextField;
    @FXML
    protected DatePicker contractEndDatePicker;
    @FXML
    protected TextField hourlyRateTextField;
    @FXML
    protected TextField minHoursTextField;
    @FXML
    protected TextField maxWorkDaysTextField;
    @FXML
    protected Button createNewEmployeeButton;
    @FXML
    protected Button cancelButton;
    @FXML
    protected ComboBox<String> preferredDayCombo1;
    @FXML
    protected ComboBox<String> preferredDayCombo2;
    @FXML
    protected Label addEmployeeTitleLabel;
    @FXML
    protected Button removeEmployeeButton;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setRoundingMode(RoundingMode.UP);
        setComboBox1();
        setComboBox2();        
    }    
    
    @FXML
    private void setComboBox1(){  
        preferredDayCombo1.setItems(days);
    }
    
    @FXML
    private void setComboBox2(){  
        preferredDayCombo2.setItems(days);
    }
    
    @FXML
    private void checkContractStartDate(){        
        if(contractStartDatePicker.getValue().isBefore(LocalDate.now())){            
        }
    }
    
    //create or edit an employee
    @FXML
    private void createEmployee(ActionEvent e) throws ClassNotFoundException, SQLException, IOException{
       
        if(nameTextField.getText().isBlank() || (contractStartDatePicker.getValue()==null) || (contractEndDatePicker.getValue()==null) || 
          (contractedHoursTextField.getText().isBlank()) || (hourlyRateTextField.getText().isBlank()) || (minHoursTextField.getText().isBlank()) || 
          (maxWorkDaysTextField.getText().isBlank())){
            
            Alert allFields = new Alert(Alert.AlertType.ERROR);
            allFields.setHeaderText("Please fill in all of the fields.");
            allFields.showAndWait();            
        }
        
        else if(preferredDayCombo1.getSelectionModel().getSelectedIndex()== -1 || preferredDayCombo2.getSelectionModel().getSelectedIndex()== -1){
            
            Alert dayPreference = new Alert(Alert.AlertType.ERROR);
            dayPreference.setHeaderText("Please select day off preferences");
            dayPreference.showAndWait(); 
        }
        else if(!contractedHoursTextField.getText().strip().matches("^[1-9]\\d*(\\.\\d+)?$") || !hourlyRateTextField.getText().strip().matches("^[1-9]\\d*(\\.\\d+)?$") ||
                !minHoursTextField.getText().strip().matches("\\d*") || !maxWorkDaysTextField.getText().strip().matches("\\d*")){
            
            Alert numbersOnly = new Alert(Alert.AlertType.ERROR);
            numbersOnly.setHeaderText("Please use numbers only in the following fields: \n\n"
                    + "Contracted hours per week \n"
                    + "Hourly rate (allows decimals) \n"
                    + "Minumum hours between shifts \n"
                    + "Maximum consective working days");
            numbersOnly.showAndWait();
        }
        else if(createNewEmployeeButton.getText().equalsIgnoreCase("+Create new employee") && contractStartDatePicker.getValue().isBefore(LocalDate.now())){
            
            Alert beforeToday = new Alert(Alert.AlertType.ERROR);
            beforeToday.setHeaderText("Contract start date must not be before today ("+ LocalDate.now()+")");
            beforeToday.showAndWait();       
            
        }
        else if(createNewEmployeeButton.getText().equalsIgnoreCase("+Create new employee") && contractEndDatePicker.getValue().isBefore(contractStartDatePicker.getValue())){
            
            Alert beforeStartDate = new Alert(Alert.AlertType.ERROR);
            beforeStartDate.setHeaderText("Contract end date cannot be before the contract start date.");
            beforeStartDate.showAndWait();
                       
        }
        else if(createNewEmployeeButton.getText().equalsIgnoreCase("Edit Employee") && contractEndDatePicker.getValue().isBefore(contractStartDatePicker.getValue())){
            
            Alert beforeStartDate = new Alert(Alert.AlertType.ERROR);
            beforeStartDate.setHeaderText("Contract end date cannot be before the contract start date.");
            beforeStartDate.showAndWait();
        }
        else if(Double.parseDouble(contractedHoursTextField.getText().strip())>168.0){  
            
            Alert maxHours = new Alert(Alert.AlertType.ERROR);
            maxHours.setHeaderText("There are a maximum of 168 hours in a week. \n"
                    + "Please enter a number up to this value for the employee contracted hours");
            maxHours.showAndWait();
        }
        else if(Integer.parseInt(minHoursTextField.getText().strip())<=0 || Integer.parseInt(maxWorkDaysTextField.getText().strip())<=0){
            
            Alert maxHours = new Alert(Alert.AlertType.ERROR);
            maxHours.setHeaderText("Minimum hours between shifts and maximum consecutive working days cannot be 0 or less.");
            maxHours.showAndWait();
        }
        else if(nameTextField.getText().length()>50){
            Alert nameMax = new Alert(Alert.AlertType.ERROR);
            nameMax.setHeaderText("Please enter a name under 50 characters including spaces");
            nameMax.showAndWait();
        }
        else if(!nameTextField.getText().matches("^[\\p{L} .'-]+$")){
            Alert nameChars = new Alert(Alert.AlertType.ERROR);
            nameChars.setHeaderText("Please remove numbers and special characters from the name field");
            nameChars.showAndWait();
        }
        else if(Double.parseDouble(hourlyRateTextField.getText().strip())>100.0){
            Alert nameChars = new Alert(Alert.AlertType.ERROR);
            nameChars.setHeaderText("Please set an hourly rate of under £100 or you may contact your administrator to have this limit increased");
            nameChars.showAndWait();
        }
        else {                          
            if (createNewEmployeeButton.getText().equalsIgnoreCase("+Create new employee")){
            
            Alert removeShiftConfim = new Alert(Alert.AlertType.CONFIRMATION);
            removeShiftConfim.setHeaderText("Are you sure you want to add a new employee?");
            removeShiftConfim.showAndWait();

                if(removeShiftConfim.getResult().getText().equalsIgnoreCase("OK")){
                    try{

                    DAO.addEmployee(nameTextField.getText(), preferredDayCombo1.getSelectionModel().getSelectedIndex(), preferredDayCombo2.getSelectionModel().getSelectedIndex());

                    DAO.addContract(getEmpID(), contractStartDatePicker.getValue().toString(), contractedHoursTextField.getText().strip(), contractEndDatePicker.getValue().toString(), 
                            hourlyRateTextField.getText().strip(), minHoursTextField.getText().strip(), maxWorkDaysTextField.getText().strip());

                    DAO.addContractToEmployee();
                    
                    Stage addEmployee = (Stage) this.createNewEmployeeButton.getScene().getWindow();
                    addEmployee.close();  

                    Alert employeeAdded = new Alert(Alert.AlertType.INFORMATION);
                    employeeAdded.setHeaderText("The new employee has been added successfully");
                    employeeAdded.showAndWait();

                    refreshGridPane();

                        } catch (SQLException ex){

                        }                       
                 }
            }
            
            else if(createNewEmployeeButton.getText().equalsIgnoreCase("Edit Employee")){ 
                
                Employee employee = DAO.getEmployee(nameTextField.getId());
                Contract contract = DAO.getContract(employee.getEmployeeContract());        
                              
                    if(nameTextField.getText().equalsIgnoreCase(employee.getEmployeeName()) && contractStartDatePicker.getValue().isEqual(LocalDate.parse(contract.getContractStartDate())) 
                      && contractEndDatePicker.getValue().isEqual(LocalDate.parse(contract.getContractEndDate())) && contractedHoursTextField.getText().equalsIgnoreCase(contract.getContractedHours()) 
                      && hourlyRateTextField.getText().equalsIgnoreCase(contract.getHourlyRate()) && minHoursTextField.getText().equalsIgnoreCase(contract.getMinHoursBetweenShifts()) 
                      && maxWorkDaysTextField.getText().equalsIgnoreCase(contract.getMaxConsWorkDays()) && preferredDayCombo1.getSelectionModel().getSelectedIndex()==Integer.parseInt(employee.getPreferredDayOff1()) 
                      && preferredDayCombo2.getSelectionModel().getSelectedIndex()==Integer.parseInt(employee.getPreferredDayOff2())) {

                    Alert noChanges = new Alert(Alert.AlertType.ERROR);
                    noChanges.setHeaderText("You have not made any changes to this employee.");
                    noChanges.showAndWait();                    
                    }

                    else{
                    Alert updateShiftConfim = new Alert(Alert.AlertType.CONFIRMATION);
                    updateShiftConfim.setHeaderText("Are you sure you want to edit this employee?");
                    updateShiftConfim.showAndWait();

                        if(updateShiftConfim.getResult().getText().equalsIgnoreCase("OK")){
                        
                            try{

                            DAO.editEmployee(nameTextField.getId(), contract.getContractID(), nameTextField.getText(), String.valueOf(preferredDayCombo1.getSelectionModel().getSelectedIndex()), String.valueOf(preferredDayCombo2.getSelectionModel().getSelectedIndex()),
                            contractStartDatePicker.getValue().toString(), contractedHoursTextField.getText().strip(), contractEndDatePicker.getValue().toString(), hourlyRateTextField.getText().strip(), minHoursTextField.getText().strip(), 
                            maxWorkDaysTextField.getText().strip());

                            Stage addEmployee = (Stage) this.createNewEmployeeButton.getScene().getWindow();
                            addEmployee.close();
                            
                            Alert shiftEdited = new Alert(Alert.AlertType.INFORMATION);
                            shiftEdited.setHeaderText("Employee record for " + employee.getEmployeeName() + " has been updated successfully");
                            shiftEdited.showAndWait();
                   
                            refreshGridPane();
                            
                            }
                            catch (SQLException ex){
                                    System.out.println(ex);
                                }
                        }
                    }           
            }          
        }
    }
    
    
    
    protected void refreshGridPane() throws ClassNotFoundException, SQLException{
    
            calendar1.getRowConstraints().removeAll(rows);
            calendar1.getChildren().removeAll(buttons);
            calendar1.getChildren().removeAll(employeeLabels);           
            
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
                                           
            calendar1.getRowConstraints().add(row);
            calendar1.add(label, 0, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button1, 1, (calendar1.getRowConstraints().lastIndexOf(row)));
            calendar1.add(button2, 2, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button3, 3, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button4, 4, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button5, 5, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button6, 6, (calendar1.getRowConstraints().lastIndexOf(row))); 
            calendar1.add(button7, 7, (calendar1.getRowConstraints().lastIndexOf(row))); 
            
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
                            
                            b.calendar1 = calendar1;
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
                                Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), buttonDate.format(formatter));
                                if(shift.getShiftType().equalsIgnoreCase("1")){
                                    consShifts = consShifts+1;
                                }
                            }
                            catch(NullPointerException x){

                            }
                            
                        for (int i=0; i < consDays1; i++){
                         try{
                                 Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), startConsDays.format(formatter));
                                 
                                 if (shift.getShiftType().equalsIgnoreCase("1")){
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
                              case "3": button.setText("Off Sick"); button.setStyle("-fx-background-color: #ffff00; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Off Sick"); break;
                              case "4": button.setText("Emergency Leave"); button.setStyle("-fx-background-color: #ff6347; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Emergency Leave"); break;
                              case "5": button.setText("Annual Leave"); button.setStyle("-fx-background-color: #9370db; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Annual Leave"); break;                           
                          }
                          
                         //add red border on button for soft constraint violations to aid shift planner                         
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
                            c.calendar2 = calendar1;
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
                                calendar1.getChildren().forEach((node) -> {


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
                                 System.out.println(ex);
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
                        c.calendar2 = calendar1;
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
                            calendar1.getChildren().forEach((node) -> {

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
                             System.out.println(ex);
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
        System.out.println(e);  
        }     
    }   
   
    
    private int getEmpID() throws ClassNotFoundException, SQLException{
        
        int empID = 0;
        
        try{
            empID = DAO.getEmployeeID();        
        }
        catch(SQLException ex){ 
            System.out.println(ex);
        }
        return empID;
    }
    
    @FXML
    private void removeEmployee() throws ClassNotFoundException, SQLException, InterruptedException {
        
        Alert deleteEmployeeConfim = new Alert(Alert.AlertType.CONFIRMATION);
        deleteEmployeeConfim.setTitle("Remove Employee");
        deleteEmployeeConfim.setHeaderText("Are you sure you want to end contract for this employee?");
        deleteEmployeeConfim.setContentText("This will remove employee from the calendar and retain employee information including all previous shifts");
        deleteEmployeeConfim.showAndWait();

        if(deleteEmployeeConfim.getResult().getText().equalsIgnoreCase("OK")){
            
            try{
               DAO.removeEmployee(nameTextField.getId());
               
               Stage addEmployee = (Stage) this.createNewEmployeeButton.getScene().getWindow();
               addEmployee.close();
               
               Alert shiftEdited = new Alert(Alert.AlertType.INFORMATION);
               shiftEdited.setHeaderText("Employee record has been updated successfully");
               shiftEdited.showAndWait();
               
               refreshGridPane();
            }
            catch(ClassNotFoundException ex){
                    System.out.println(ex);
            }
        }    
    }
    
    
    @FXML
    private void cancelButton(ActionEvent e){
        Stage addEmployee = (Stage) this.cancelButton.getScene().getWindow();
        addEmployee.close();
    }    
    
}
