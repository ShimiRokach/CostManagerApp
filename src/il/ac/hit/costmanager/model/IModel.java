
package il.ac.hit.costmanager.model;

import java.time.LocalDate;
import java.util.List;

/**
 * IModel interface represents the model(data)
 */

public interface IModel {

    //addCostItem method will add new input item to costs item table
    void addCostItem(CostItem item) throws CostManagerException;

    //deleteCostItem method will delete input item from costs item table
    void deleteCostItem(CostItem item) throws CostManagerException;

    //addCategory method will add a new input category to categories table
    void addCategory(Category category) throws CostManagerException;

    //getCostItems method will return a list of all the items in the costs table
    List<CostItem> getCostItems() throws CostManagerException;

    //getCatTable method will return a list of all the categories in the categories table
    List<Category> getCatTable() throws CostManagerException;

    //initDB method will create the table for costs items and for categories if they don't exist
    void initDB() throws CostManagerException;

    //generateReport method will return a list of cost items in range of the input date
    List<CostItem> generateReport(LocalDate startDate, LocalDate endDate) throws CostManagerException;


}
