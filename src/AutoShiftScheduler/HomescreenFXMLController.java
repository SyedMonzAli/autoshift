
package AutoShiftScheduler;

import Database.DAO;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
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
public class HomescreenFXMLController implements Initializable {
    
    ArrayList<Label> dayLabels = new ArrayList<>();
    private static LocalDate today = LocalDate.now();
    RowConstraints row = new RowConstraints(55);
    ArrayList<RowConstraints> rows = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> employeeLabels = new ArrayList<>();
    DecimalFormat df = new DecimalFormat ("#.##");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    @FXML
    private Button lastWeekButton;
    @FXML
    private Button nextWeekButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    public GridPane calendarGridPane;
    @FXML
    private Button autoShiftsButton;
    @FXML
    private TextField weeklyBudgetTextField;
    @FXML
    private TextField labourCostTextField;
    @FXML
    private Button editBudgetButton;
    @FXML
    protected RowConstraints dateRow0;
    @FXML
    private Label monthYearLabel;
    @FXML
    private Label mondayDateLabel;
    @FXML
    private Label tuesdayDateLabel;
    @FXML
    private Label wednesdayDateLabel;
    @FXML
    private Label thursdayDateLabel;
    @FXML
    private Label fridayDateLabel;
    @FXML
    private Label saturdayDateLabel;
    @FXML
    private Label sundayDateLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button addEmployeeButton;
    @FXML
    private TextField prefDaysOff;
    @FXML
    private TextField consDays;
    @FXML//totals at bottom
    private TextField monTotal;
    @FXML
    private TextField tueTotal;
    @FXML
    private TextField wedTotal;
    @FXML
    private TextField thuTotal;
    @FXML
    private TextField friTotal;
    @FXML
    private TextField satTotal;
    @FXML
    private TextField sunTotal;
    @FXML
    private TextField shiftTotal;
    @FXML
    private TextField monCost;
    @FXML
    private TextField tueCost;
    @FXML
    private TextField wedCost;
    @FXML
    private TextField thuCost;
    @FXML
    private TextField friCost;
    @FXML
    private TextField satCost;
    @FXML
    private TextField sunCost;
    
    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { 
            df.setRoundingMode(RoundingMode.UP); 
            setYearMonthLabel();
            setDayLabels2();
            loadEmployees();
           
            
        } catch (SQLException ex) {
            Logger.getLogger(HomescreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomescreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(HomescreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
        
    
    private void setYearMonthLabel(){
        
        monthYearLabel.setText(today.getYear()+"");
    }    
    
    private void setDayLabels2(){                
        
        dayLabels.clear();
        dayLabels.add(mondayDateLabel);
        dayLabels.add(tuesdayDateLabel);
        dayLabels.add(wednesdayDateLabel);
        dayLabels.add(thursdayDateLabel);
        dayLabels.add(fridayDateLabel);
        dayLabels.add(saturdayDateLabel);
        dayLabels.add(sundayDateLabel);
                   
        for (int i = 0; i < dayLabels.size(); i++){
 
            dayLabels.get(i).setText("" + today.with(DayOfWeek.of(i+1)).getDayOfMonth() + "/" + today.with(DayOfWeek.of(i+1)).getMonth().getValue() + "");          
        }     
    }
   
    
    @FXML
    private void nextWeek(ActionEvent e) throws SQLException, ClassNotFoundException, ParseException{
          
        today = today.with(today.plusDays(7));       
        
        setDayLabels2();
        setYearMonthLabel();
        loadEmployees();          
    }
    
    @FXML
    private void lastWeek(ActionEvent e) throws SQLException, ClassNotFoundException, ParseException{
  
        today = today.with(today.minusDays(7));                            
        setDayLabels2();
        setYearMonthLabel();
        loadEmployees();             
    }
    
    @FXML
    private void addEmployee(){
        
        try{
            Group root1 = new Group();
            Stage stage = new Stage();           
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AddEmployeeFXML.fxml"));
            AnchorPane frame = fxmlLoader.load();
            AddEmployeeFXMLController b = (AddEmployeeFXMLController) fxmlLoader.getController();
            
            b.calendar1 = calendarGridPane;
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
            
            b.removeEmployeeButton.setVisible(false);
            
            
        } catch(IOException ex){
            
            System.out.println(ex +" at line 252");
        }
    }
    
  
   
    public void loadEmployees() throws SQLException, ClassNotFoundException, ParseException{           
                         
            calendarGridPane.getRowConstraints().removeAll(rows);
            calendarGridPane.getChildren().removeAll(buttons);
            calendarGridPane.getChildren().removeAll(employeeLabels);                    
                 
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
                                           
            calendarGridPane.getRowConstraints().add(row);
            calendarGridPane.add(label, 0, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button1, 1, (calendarGridPane.getRowConstraints().lastIndexOf(row)));
            calendarGridPane.add(button2, 2, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button3, 3, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button4, 4, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button5, 5, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button6, 6, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            calendarGridPane.add(button7, 7, (calendarGridPane.getRowConstraints().lastIndexOf(row))); 
            
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
                            
                            b.calendar1 = calendarGridPane;
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
                            System.out.println(ex +" at line 398");
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
                
                 //for each button    
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
                    else if(DAO.checkEmployeeShift(employee.getEmployeeID(), dayLabels.get(GridPane.getColumnIndex(button)-1).getText().concat("/"+monthYearLabel.getText()))==true){
                                                       
                          Shift shift = DAO.getEmployeeShift(employee.getEmployeeID(), dayLabels.get(GridPane.getColumnIndex(button)-1).getText().concat("/"+monthYearLabel.getText()));                         
                          String shiftType = shift.getShiftType();
                          
                          double cost = Double.parseDouble(shift.getShiftCost());
                          totalCost =  totalCost + cost;                             
                             
                            switch (shiftType){

                              case "1": button.setText("Working: " + shift.getShiftStart() + "-" + shift.getShiftEnd()); button.setStyle("-fx-background-color: #aed581; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Working"); break;                        
                              case "2": button.setText("Day Off"); button.setStyle("-fx-background-color: #add8e6; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Day Off"); break;
                              case "3": button.setText("Off Sick"); button.setStyle("-fx-background-color: #ffff00; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Off Sick");  break;
                              case "4": button.setText("Emergency Leave"); button.setStyle("-fx-background-color: #ff6347; -fx-text-fill: #494949; -fx-font-weight: bold;"); button.setId("Emergency Leave");break;
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
                                    c.calendar2 = calendarGridPane;
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
                                        calendarGridPane.getChildren().forEach((node) -> {
                                            
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
                                        c.shiftPatternComboBox.getSelectionModel().clearAndSelect(Integer.parseInt(shift.getShiftPattern())-1);
                                        c.removeShiftButton.setVisible(true); c.removeShiftButton.setTextFill(Color.RED);
                                      }); 

                                     } catch(Exception ex){            
                                         System.out.println(ex+"at line 579");
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
                                    c.calendar2 = calendarGridPane;
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
                                        calendarGridPane.getChildren().forEach((node) -> {


                                            if(node.equals(label)){
                                             c.employeeIdTxtField.setText(label.getId()); c.employeeIdTxtField.setStyle("-fx-opacity: 1");
                                             c.employeeNameTxtField.setText(label.getText()); c.employeeNameTxtField.setStyle("-fx-opacity: 1");
                                             
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
                                         System.out.println(ex+"at line 663");
                                         }           
                                     }
                               });                                    
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
                labourCostTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d; -fx-font-weight: bold;");                  
            }
                
            prefDaysOff.setText(String.valueOf(preferredDaysOffMet) + "%");
            prefDaysOff.setStyle("-fx-opacity: 1; -fx-font-weight: bold;");            
            
            consDays.setText(String.valueOf(consDaysMet)+ "%");
            consDays.setStyle("-fx-opacity: 1; -fx-font-weight: bold;");                       
            
        } catch (SQLException | ArithmeticException e){
               
        } 
    }
    

    
    @FXML
    private void editBudget(ActionEvent e) throws ClassNotFoundException, SQLException{
        
        double weeklyBudget = DAO.getWeeklyBudget();
        
        if (weeklyBudgetTextField.isDisable()==true){
        weeklyBudgetTextField.setDisable(false);
        weeklyBudgetTextField.setEditable(true);
        weeklyBudgetTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: black; -fx-font-weight: bold;");
        }        
        else if(!weeklyBudgetTextField.getText().strip().matches("^[1-9]\\d*(\\.\\d+)?$")){
            Alert budgetChars = new Alert(Alert.AlertType.ERROR);
            budgetChars.setHeaderText("Please enter numbers only without spaces for editing the weekly budget.");
            budgetChars.showAndWait();
            
            weeklyBudgetTextField.setText(String.valueOf(weeklyBudget));
        }
        else{                       
            
            weeklyBudget = Double.parseDouble(weeklyBudgetTextField.getText().strip());
            
            DAO.setWeeklyBudget(weeklyBudget);
          
            if(Double.parseDouble(labourCostTextField.getText().substring(1)) > Double.parseDouble(weeklyBudgetTextField.getText())){

                labourCostTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: red; -fx-font-weight: bold;");                   
               }
            else{
                labourCostTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d; -fx-font-weight: bold;");                
            }            
         weeklyBudgetTextField.setDisable(true);
         weeklyBudgetTextField.setStyle("-fx-opacity: 1; -fx-text-inner-color: #5d5d5d; -fx-font-weight: bold;");         
        }                
    }
    

    
    @FXML
    private void autoShifts (ActionEvent e){
        
        try{
            Group root1 = new Group();
            Stage stage = new Stage();           
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AutomatedScheduleFXML.fxml"));
            AnchorPane frame = fxmlLoader.load();
            AutomatedScheduleFXMLController d = (AutomatedScheduleFXMLController) fxmlLoader.getController(); 
            
            d.calendar3 = calendarGridPane;
            d.monthYearLabel = monthYearLabel;
            d.mondayDateLabel = mondayDateLabel;
            d.tuesdayDateLabel = tuesdayDateLabel;
            d.wednesdayDateLabel = wednesdayDateLabel;
            d.thursdayDateLabel = thursdayDateLabel;
            d.fridayDateLabel = fridayDateLabel;
            d.saturdayDateLabel = saturdayDateLabel;
            d.sundayDateLabel = sundayDateLabel;
            d.dayLabels = dayLabels;
            d.row = row;
            d.rows = rows;
            d.buttons = buttons;
            d.employeeLabels = employeeLabels;
            d.labourCostTextField = labourCostTextField;
            d.weeklyBudgetTextField = weeklyBudgetTextField;
            d.prefDaysOff = prefDaysOff;
            d.consDays = consDays;
            d.today = today;
            d.monTotal = monTotal;
            d.tueTotal = tueTotal;
            d.wedTotal = wedTotal;
            d.thuTotal = thuTotal;
            d.friTotal = friTotal;
            d.satTotal = satTotal;
            d.sunTotal = sunTotal;
            d.shiftTotal = shiftTotal;
            d.monCost = monCost;
            d.tueCost = tueCost;
            d.wedCost = wedCost;
            d.thuCost = thuCost;
            d.friCost = friCost;
            d.satCost = satCost;
            d.sunCost = sunCost;
    
            root1.getChildren().add(frame);
            Scene scene = new Scene(root1);
            
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException ex){

            System.out.println(ex+"at line 834");
        }       
    }

    @FXML
    private void exitApplication(ActionEvent e){
        
        System.exit(0);        
    }  

}
