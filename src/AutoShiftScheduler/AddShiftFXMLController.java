
package AutoShiftScheduler;

import Database.DAO;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
 * FXML Controller class for adding and editing a shift
 */
public class AddShiftFXMLController implements Initializable {

    private ObservableList<String> shiftType = FXCollections.observableArrayList("Working", "Day Off", "Off Sick", "Emergency Leave", "Annual Leave");
    private ObservableList<String> hours = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    private ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
    private ObservableList<String> patterns = FXCollections.observableArrayList("No Pattern","Days", "Evenings");
    
    
    protected GridPane calendar2;
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
    DecimalFormat df = new DecimalFormat ("#.##");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    
    @FXML
    protected ComboBox<String> shiftTypeComboBox;
    @FXML
    private Button cancelButton;
    @FXML
    protected Button createShiftButton;
    @FXML
    protected TextField employeeIdTxtField;
    @FXML
    protected TextField shiftDateTxtField;
    @FXML
    protected ComboBox<String> shiftPatternComboBox;
    @FXML
    protected ComboBox<String> startTimeHours;
    @FXML
    protected ComboBox<String> startTimeMinutes;
    @FXML
    protected ComboBox<String> finishTimeHours;
    @FXML
    protected ComboBox<String> finishTimeMinutes;
    @FXML
    protected TextField employeeNameTxtField;
    @FXML
    protected Label addEditShiftLabel;
    @FXML
    protected Button removeShiftButton;
    
    /**
     * Undergraduate final year individual computing project - KV6003
     * Northumbria University - 2019/20
     * 
     * @author Syed Ali - w17023496
     *
     * FXML Controller class for home screen
     *
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        df.setRoundingMode(RoundingMode.UP); 
        setShiftType();       
        setStartHours();
        setStartMinutes();
        setFinishHours();
        setFinishMinutes();
        setShiftPattern();
        setComboBoxes();  
    }    
    
    @FXML
    private void setShiftType(){
        shiftTypeComboBox.setItems(shiftType);                        
    }
    
    @FXML
    private void setComboBoxes(){
        startTimeHours.setDisable(true);
        startTimeMinutes.setDisable(true);
        finishTimeHours.setDisable(true);
        finishTimeMinutes.setDisable(true); 
        shiftPatternComboBox.setDisable(true);
        startTimeHours.setStyle("-fx-opacity: 1");
        startTimeMinutes.setStyle("-fx-opacity: 1");
        finishTimeHours.setStyle("-fx-opacity: 1");
        finishTimeMinutes.setStyle("-fx-opacity: 1");
        createShiftButton.setDisable(true);        
    }
    
    @FXML
    private void updateComboBoxes(){
                    
        if (shiftTypeComboBox.getSelectionModel().getSelectedIndex() == 0){
            startTimeHours.setDisable(false);
            startTimeMinutes.setDisable(false);
            finishTimeHours.setDisable(false);
            finishTimeMinutes.setDisable(false); 
            shiftPatternComboBox.setDisable(false);
            createShiftButton.setDisable(false);
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
            shiftPatternComboBox.setDisable(true); 
            startTimeHours.setStyle("-fx-opacity: 1");
            startTimeMinutes.setStyle("-fx-opacity: 1");
            finishTimeHours.setStyle("-fx-opacity: 1");
            finishTimeMinutes.setStyle("-fx-opacity: 1");
            shiftPatternComboBox.setValue("No Pattern");
            createShiftButton.setDisable(false);            
        }            
    }
    
    
    @FXML
    private void setStartHours(){
  
        startTimeHours.setItems(hours);
        startTimeHours.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void setStartMinutes(){
  
        startTimeMinutes.setItems(minutes);
        startTimeMinutes.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void setFinishHours(){
  
        finishTimeHours.setItems(hours);
        finishTimeHours.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void setFinishMinutes(){
  
        finishTimeMinutes.setItems(minutes);
        finishTimeMinutes.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void setShiftPattern(){
        
        shiftPatternComboBox.setItems(patterns);
        shiftPatternComboBox.getSelectionModel().selectFirst();                 
    }
    
    @FXML
    private void updateHours(){
        
     
        if (shiftPatternComboBox.getSelectionModel().getSelectedItem().equals(patterns.get(0))) {

        startTimeHours.getSelectionModel().select("00");
        startTimeMinutes.getSelectionModel().select("00");
        finishTimeHours.getSelectionModel().select("00");
        finishTimeMinutes.getSelectionModel().select("00");   
        startTimeHours.setDisable(false);
        startTimeMinutes.setDisable(false);
        finishTimeHours.setDisable(false);
        finishTimeMinutes.setDisable(false);
        createShiftButton.setDisable(false);

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
         createShiftButton.setDisable(false);
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
         createShiftButton.setDisable(false);
         }                    
    }
    
    @FXML
    private void createShift(ActionEvent e) throws ClassNotFoundException, SQLException{
        
        double shiftCost = 0;
        double totalDuration = 0;
        LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(startTimeMinutes.getSelectionModel().getSelectedItem()));
        LocalTime finishTime = LocalTime.of(Integer.parseInt(finishTimeHours.getSelectionModel().getSelectedItem()), Integer.parseInt(finishTimeMinutes.getSelectionModel().getSelectedItem()));
        int totalHours = Duration.between(startTime, finishTime).toHoursPart();        
        
        DecimalFormat df = new DecimalFormat ("#.##");
        df.setRoundingMode(RoundingMode.UP);
        
        double totalMinutes = Double.parseDouble(df.format((Duration.between(startTime, finishTime).toMinutesPart())*0.0166));        
        totalDuration = totalHours + totalMinutes;
        shiftCost = Double.parseDouble(df.format((DAO.getHourlyRate(employeeIdTxtField.getText())) * totalDuration));
        
        int shiftPattern = shiftPatternComboBox.getSelectionModel().getSelectedIndex()+1;
        int shiftType =  shiftTypeComboBox.getSelectionModel().getSelectedIndex()+1;
    
        if (shiftTypeComboBox.getSelectionModel().getSelectedIndex() == 0 && (totalDuration < 1 || totalDuration <= 0)){
                  
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("You must enter a shift with at least 1 hour");
                errorAlert.setContentText("Click OK and ensure you have added a minimum of 1 hour for the shift.");
                errorAlert.showAndWait();
               }            
        else{
            
                if(createShiftButton.getText().equalsIgnoreCase("+Create Shift")){

                Alert createShiftConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                createShiftConfirmation.setHeaderText("Are you sure you want to create a shift for this employee?");
                createShiftConfirmation.showAndWait();                        
                
                    if(createShiftConfirmation.getResult().getText().equalsIgnoreCase("OK")){
                                              
                        try{    
                            DAO.createShift(employeeIdTxtField.getText(), shiftDateTxtField.getText(), startTime, finishTime, shiftPattern, shiftTypeComboBox.getSelectionModel().getSelectedIndex()+1, shiftCost);
                            System.out.println("Shift added successfully");                            
                            
                            Stage addEmployee = (Stage) this.createShiftButton.getScene().getWindow();
                            addEmployee.close();
                                                                                   
                            refreshGridPane();
                            
                        } catch (SQLException ex){
                            System.out.println(ex);
                        }
                    }
                }
                
                else if(createShiftButton.getText().equalsIgnoreCase("Edit Shift")){
                    
                   Shift shift = DAO.getEmployeeShift(employeeIdTxtField.getText(), shiftDateTxtField.getText());                                         
                    
                    if ((Integer.parseInt(shift.getShiftType())==shiftType) && (shift.getShiftStart().equalsIgnoreCase(startTime.toString())) && (shift.getShiftEnd().equalsIgnoreCase(finishTime.toString())) 
                            && (Integer.parseInt(shift.getShiftPattern())==shiftPattern)){
                                
                        Alert noChanges = new Alert(Alert.AlertType.ERROR);
                        noChanges.setHeaderText("You have not made any changes to this shift.");
                        noChanges.showAndWait();
                    }
                    else{
                    Alert editShiftConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    editShiftConfirmation.setHeaderText("Are you sure you want to edit this shift?");
                    editShiftConfirmation.showAndWait();
                        
                        if(editShiftConfirmation.getResult().getText().equalsIgnoreCase("OK")){
                       
                            try{       
                            DAO.editShift(startTime, finishTime, shiftPattern, shiftTypeComboBox.getSelectionModel().getSelectedIndex()+1, 
                                    shiftCost, employeeIdTxtField.getText(), shiftDateTxtField.getText());
                            
                            System.out.println("Shift Amended successfully");
                                                       
                            Stage addEmployee = (Stage) this.createShiftButton.getScene().getWindow();
                            addEmployee.close();
                            
                            refreshGridPane();
                            
                            } catch (SQLException ex){
                                System.out.println(ex);
                            }                                        
                        }
                    }                           
                }   
            }
    }
  
    
    protected void refreshGridPane() throws ClassNotFoundException, SQLException{
                
                calendar2.getRowConstraints().removeAll(rows);
                calendar2.getChildren().removeAll(buttons);
                calendar2.getChildren().removeAll(employeeLabels);           

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

                    calendar2.getRowConstraints().add(row);
                    calendar2.add(label, 0, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button1, 1, (calendar2.getRowConstraints().lastIndexOf(row)));
                    calendar2.add(button2, 2, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button3, 3, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button4, 4, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button5, 5, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button6, 6, (calendar2.getRowConstraints().lastIndexOf(row))); 
                    calendar2.add(button7, 7, (calendar2.getRowConstraints().lastIndexOf(row))); 

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
                                    
                                    b.calendar1 = calendar2;
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
                        
                        //variable to conveniently reference date of each column
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
                                c.calendar2 = calendar2;
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
                                    calendar2.getChildren().forEach((node) -> {

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
                                        c.calendar2 = calendar2;
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
                                            calendar2.getChildren().forEach((node) -> {

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
    
    
    @FXML
    protected void removeShift(ActionEvent e) throws ClassNotFoundException, SQLException{
        
        Alert removeShiftConfim = new Alert(Alert.AlertType.CONFIRMATION);
        removeShiftConfim.setHeaderText("Are you sure you want to remove this shift?");
        removeShiftConfim.showAndWait();
                        
            if(removeShiftConfim.getResult().getText().equalsIgnoreCase("OK")){
                
                DAO.removeShift(employeeIdTxtField.getText(), shiftDateTxtField.getText());
                                                
                Stage addEmployee = (Stage) this.cancelButton.getScene().getWindow();
                addEmployee.close();
                
                refreshGridPane();
            }        
    }
    
    
    @FXML
    private void cancelButton(ActionEvent e){
        Stage addEmployee = (Stage) this.cancelButton.getScene().getWindow();
        addEmployee.close();
    }
    
}
