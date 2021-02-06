
package il.ac.hit.costmanager.viewmodel;

import il.ac.hit.costmanager.model.Category;
import il.ac.hit.costmanager.model.CostItem;
import il.ac.hit.costmanager.model.CostManagerException;
import il.ac.hit.costmanager.model.IModel;
import il.ac.hit.costmanager.view.IView;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel class will connect between the model and the view
 */

public class ViewModel implements IViewModel {


    //model is variable that will store the reference of IModel interface
    private IModel model;

    //view is variable that will store the reference of IView interface
    private IView view;

    private ExecutorService pool;

    /**
     * Class constructor.
     */
    public ViewModel() {
        pool = Executors.newFixedThreadPool(10);
    }


    /**
     * setView method set the view var as variable for ViewModel class
     */

    @Override
    public void setView(IView view) {

        this.view = view;
    }


    /**
     * setModel method set the model var as variable for ViewModel class
     */
    @Override
    public void setModel(IModel model) {
        this.model = model;
    }


    /**
     * addCostItem method will add the input item to the application and display the list of the items
     */

    @Override
    public void addCostItem(CostItem item) {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //adding the input item to the DB and display proper message
                    model.addCostItem(item);
                    view.showMessage("cost item was added successfully");

                    //display the cost items list
                    List<CostItem> items = model.getCostItems();
                    view.showItems(items);

                } catch (CostManagerException e) {
                    view.showMessage("failed to add cost item.." + e.getMessage());

                }
            }
        });
    }

    /**
     * deleteCostItem method will delete the input item from the application and display the list of the items
     */

    @Override
    public void deleteCostItem(CostItem item) {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //delete the input item from the DB and display proper message
                    model.deleteCostItem((item));
                    view.showMessage("cost item was deleted successfully");

                    //display the cost items list
                    List<CostItem> items = model.getCostItems();
                    view.showItems(items);

                } catch (CostManagerException e) {
                    view.showMessage("failed to delete cost item.." + e.getMessage());

                }
            }
        });

    }

    /**
     * generateReport method will display the cost items list in range of the input date
     */

    @Override
    public void generateReport(LocalDate startDate, LocalDate endDate) {


        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //get the cost items list in the input date range and display proper message
                    List<CostItem> report = model.generateReport(startDate, endDate);
                    view.showMessage("report was generate successfully");

                    //display the report
                    view.showReport(report);

                } catch (CostManagerException e) {
                    view.showMessage("failed to generate report.." + e.getMessage());

                }
            }
        });

    }

    /**
     * addCategory method will add the input category to the application and display the list of categories
     */

    @Override
    public void addCategory(Category category) {


        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //adding the input category to the DB and display proper message
                    model.addCategory(category);
                    view.showMessage("category was added successfully");

                    //display the categories list
                    List<Category> categories = model.getCatTable();
                    view.showCategories(categories);

                } catch (CostManagerException e) {
                    view.showMessage("failed to add category.." + e.getMessage());

                }
            }
        });

    }

    /**
     * showPieChart method will display the pie chart
     */

    @Override
    public void showPieChart(LocalDate startDate, LocalDate endDate) {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //get the cost items list in the input date range and display proper message
                    List<CostItem> pie = model.generateReport(startDate, endDate);
                    view.showMessage("Pie Chart was generate successfully");

                    //display the pie chart
                    view.showPieChart(pie);

                } catch (CostManagerException e) {
                    view.showMessage("failed to show pie chart.." + e.getMessage());
                }
            }
        });
    }

    /**
     * getCategoryList method use for passing categories from the model to the view
     */

    @Override
    public void getCategoryList() {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //get the category list and pass it to view
                    List<Category> categories = model.getCatTable();
                    view.setCategories(categories);
                } catch (CostManagerException e) {
                    view.showMessage("failed to set category list.." + e.getMessage());

                }
            }
        });
    }

    /**
     * getCostItems method use for passing cost items from the model to the view
     */

    @Override
    public void getCostItems() {


        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //get the cost items list and pass it to view
                    List<CostItem> items = model.getCostItems();
                    view.setItems(items);
                } catch (CostManagerException e) {
                    view.showMessage("failed to set items list.." + e.getMessage());

                }
            }
        });

    }

    /**
     * getReport method use for passing cost items by date range from the model to the view
     */

    @Override
    public void getReport(LocalDate startDate, LocalDate endDate) {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //get the cost items list by date range and pass it to view
                    List<CostItem> report = model.generateReport(startDate, endDate);
                    view.setReport(report);
                } catch (CostManagerException e) {
                    view.showMessage("failed to set report list.." + e.getMessage());

                }
            }
        });


    }

}
