
package il.ac.hit.costmanager.viewmodel;

import il.ac.hit.costmanager.model.Category;
import il.ac.hit.costmanager.model.CostItem;
import il.ac.hit.costmanager.model.IModel;
import il.ac.hit.costmanager.view.IView;

import java.time.LocalDate;

/**
 * IViewModel interface responsible for communication between the model(data) and view(UI)
 */


public interface IViewModel {

    //setView method set the input View as a variable for ViewModel class
    public void setView(IView view);

    //setModel method set the input model as a variable for ViewModel class
    public void setModel(IModel model);

    //addCostItem method will add the input item to the application and display the list of the items
    public void addCostItem(CostItem item);

    //deleteCostItem method will delete the input item from the application and display the list of the items
    public void deleteCostItem(CostItem item);

    //generateReport method will display the cost items list in range of the input date
    public void generateReport(LocalDate startDate, LocalDate endDate);

    //addCategory method will add the input category to the application and display the list of categories
    public void addCategory(Category category);

    //showPieChart method will display the pie chart
    public void showPieChart(LocalDate startDate, LocalDate endDate);

    //getCategoryList method use for passing categories from the model to the view
    public void getCategoryList();

    //getCostItems method use for passing cost items from the model to the view
    public void getCostItems();

    //getReport method use for passing cost items by date range from the model to the view
    public void getReport(LocalDate startDate, LocalDate endDate);

}
