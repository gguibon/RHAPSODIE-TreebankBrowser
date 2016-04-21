package cnrs.lsis.appgestion.view;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import cnrs.lsis.appgestion.MainApp;
import cnrs.lsis.appgestion.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

/**
 * The controller for the birthday statistics view.
 * 
 * @author Marco Jakob
 */
public class ContractStatisticsController {

	// Reference to the main application.
    private MainApp mainApp;
	
    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> contractNames = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {        
    }

    /**
     * Sets the persons to show the statistics for.
     * 
     * @param persons
     */
    public void setPersonData(List<Person> persons) {
    	
    	HashMap<String, Integer> mapCount = new HashMap<String, Integer>();
    	for (Person p : persons){
    		String contract = p.getCity();
    		
    		if(!contractNames.contains(contract)){
    			contractNames.add(contract);
    		}
    		
    		// Check current time
            Calendar cal = Calendar.getInstance();
            java.util.Date date= new Date();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            NumberFormat formatter = new DecimalFormat("00");
            
            String exactDate = year+""+formatter.format(month)+""+formatter.format(day);
            int actualDate = Integer.parseInt(exactDate);
            
            String exitDateStr = p.getExitDate().toString().replaceAll("-", "");
        	int exitDate = Integer.parseInt(exitDateStr);
    		
        	if(actualDate < exitDate){        	
	    		mapCount.putIfAbsent(contract, (Integer)0);
	    		mapCount.computeIfPresent(contract, (k,v) -> v+1);
        	}
    	}
    	
    	// Assign the team names as categories for the horizontal axis.
        xAxis.setCategories(contractNames);

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        for (Entry<String, Integer> entry : mapCount.entrySet()) {
        	series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }
}