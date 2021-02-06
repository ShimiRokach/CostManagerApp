
package il.ac.hit.costmanager.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DerbyDBModel class store and remove data from the DB by the View request through the ViewModel
 * it's implements the IModel interface
 */

public class DerbyDBModel implements IModel {

    public static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String protocol = "jdbc:derby:CostManagerDB;create=true";

    /**
     * Class constructor.
     */
    public DerbyDBModel() throws CostManagerException {
        try {
            initDB();
        } catch (CostManagerException e) {
            throw new CostManagerException("ERROR Loading the DataBase", e);
        }
    }

    /**
     * initDB method will create the table for costs items and for categories if they don't exist
     */
    @Override
    public void initDB() throws CostManagerException {

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();

            //create costs table if not exists
            if (!(isTableExist("costs"))) {
                createTable("costs");
            }

            //create categories table if not exists
            if (!(isTableExist("categories"))) {
                createTable("Categories");
            }
        } catch (CostManagerException | SQLException | ClassNotFoundException e) {
            throw new CostManagerException("ERROR Loading the DataBase", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * createTable method will create the input table
     */
    public void createTable(String tableName) throws CostManagerException {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();
            if (tableName.equals("costs")) {
                int res = statement.executeUpdate("create table costs ("
                        + "ID INT,"
                        + "Category VARCHAR(255),"
                        + "Amount DOUBLE,"
                        + "Currency VARCHAR(255),"
                        + "Description VARCHAR(255),"
                        + "Date VARCHAR(255))");
                //check the return value of executeUpdate to validate that create table worked
                if (res < 0) {
                    throw new CostManagerException("create costs table failed");
                }
            } else if (tableName.equals("Categories")) {
                int res = statement.executeUpdate("create table Categories ("
                        + "Name VARCHAR(255))");
                if (res < 0) {
                    throw new CostManagerException("create categories table failed");
                }
                insertIntoCat("Food");
                insertIntoCat("Electric");
                insertIntoCat("Entertainment");
            }

        } catch (SQLException | CostManagerException e) {
            throw new CostManagerException("ERROR Loading the DataBase", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * isTableExist is a boolean method that return true
     * if input table name already exist and false if it doesn't exist
     */
    public boolean isTableExist(String tableName) throws CostManagerException {

        Connection connection = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(protocol);
            if (connection != null) {
                DatabaseMetaData dbmd = connection.getMetaData();
                rs = dbmd.getTables(null, null, tableName.toUpperCase(), null);

                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new CostManagerException("Table ERROR", e);
        } finally {
            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;

    }


    /**
     * getCostItems method will return a list of all the items in the costs table
     */
    public List<CostItem> getCostItems() throws CostManagerException {

        //costItems will contain the items return from cost items table
        List<CostItem> costItems = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM costs");
            while (rs.next()) {

                String category = rs.getString("Category");
                double sum = rs.getDouble("Amount");
                String currency = rs.getString("Currency");
                String description = rs.getString("Description");
                String date = rs.getString("Date");
                LocalDate localDate = stringToDate(date);
                int id = rs.getInt("ID");

                //create the CostItem and add it to the CostItem list
                CostItem item = new CostItem(new Category(category), sum, Currency.valueOf(currency), description, id, localDate);
                costItems.add(item);
            }

        } catch (SQLException e) {
            throw new CostManagerException("Failed to view cost table!", e);
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //sorting the cost items by date
        costItems.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return costItems;

    }


    /**
     * getCatTable method will return a list of all the categories in the categories table
     */
    public List<Category> getCatTable() throws CostManagerException {

        //costItems will contain the items return from cost items table
        List<Category> categoryList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Categories");
            while (rs.next()) {
                //create the Category and add it to the categoryList
                String catName = rs.getString("Name");
                categoryList.add(new Category(catName));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Failed to view Categories table!", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return categoryList;

    }


    /**
     * addCostItem method will add new input item to cost items table
     */
    @Override
    public void addCostItem(CostItem item) throws CostManagerException {

        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DriverManager.getConnection(protocol);
            //check if the category exists in the category table
            //if yes check also if the item is exist
            if (catExists(item.getCategory().getName())) {
                if (itemExists(item)) {
                    throw new CostManagerException("item already exists");
                } else {
                    String query = "INSERT INTO costs VALUES (?,?,?,?,?,?)";
                    pst = connection.prepareStatement(query);
                    pst.setInt(1, item.getId());
                    pst.setString(2, item.getCategory().getName());
                    pst.setDouble(3, item.getSum());
                    pst.setString(4, item.getCurrency().name());
                    pst.setString(5, item.getDescription());
                    LocalDate date = item.getDate();
                    String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    pst.setString(6, formattedDate);
                    pst.executeUpdate();
                }
            }

        } catch (SQLException | CostManagerException e) {
            throw new CostManagerException("Insert Data Failed", e);
        } finally {

            if (pst != null) {

                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * deleteCostItem method will delete input item from costs item table
     */
    @Override
    public void deleteCostItem(CostItem item) throws CostManagerException {

        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DriverManager.getConnection(protocol);
            String query = "DELETE FROM costs WHERE ID=?";
            pst = connection.prepareStatement(query);
            pst.setInt(1, item.getId());
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new CostManagerException("Delete Data Failed", e);
        } finally {
            if (pst != null) {

                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * addCategory method will add a new input category to categories table by using the method insertIntoCat
     */
    @Override
    public void addCategory(Category category) throws CostManagerException {

        try {
            //insert the data
            insertIntoCat(category.getName());

        } catch (CostManagerException e) {
            throw new CostManagerException("Insert Data Failed", e);
        }
    }

    /**
     * insertIntoCat method will add a new input String to categories table
     */
    public void insertIntoCat(String cat) throws CostManagerException {

        Connection connection = null;
        PreparedStatement pst = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM Categories");

            //check if the category already exists
            if (!catExists(cat)) {
                String query = "INSERT INTO Categories VALUES (?)";
                pst = connection.prepareStatement(query);
                pst.setString(1, cat);
                pst.executeUpdate();
            }


        } catch (SQLException | CostManagerException e) {
            throw new CostManagerException("Insert Data Failed", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (pst != null) {

                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * catExists is a boolean method that return true if the input category name already exist in the categories table and false if it doesn't exist
     */
    public boolean catExists(String cat) throws CostManagerException {

        //the flag will determine if the input category name already exist in the categories table
        boolean flag = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();

            //check if the category already exists
            rs = statement.executeQuery("SELECT * FROM Categories");
            while (rs.next()) {
                if (cat.equals(rs.getString("Name"))) {
                    //the input category name already exist in the categories table
                    flag = true;
                    break;
                }
            }

        } catch (SQLException e) {
            throw new CostManagerException("Insert Data Failed", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return flag;

    }


    /**
     * itemExists is a boolean method that return true if the input CostItem already exist
     * in the cost items table and false if it doesn't exist
     */
    public boolean itemExists(CostItem item) throws CostManagerException {

        //the flag will determine if the input CostItem already exist in the cost items table
        boolean flag = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();

            //check if the cost already exists
            rs = statement.executeQuery("SELECT ID FROM costs");
            while (rs.next()) {
                if (item.getId() == rs.getInt("ID")) {
                    //the CostItem already exist in the cost items table
                    flag = true;
                    break;
                }
            }

        } catch (SQLException e) {
            throw new CostManagerException("Insert Data Failed", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        return flag;

    }

    /**
     * generateReport method will return a list of cost items in range of the input date
     */
    @Override
    public List<CostItem> generateReport(LocalDate startDate, LocalDate endDate) throws CostManagerException {

        //check if the input dates is valid
        if (startDate.isAfter(endDate)) {
            throw new CostManagerException("Date Range Error!");
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        //report will contain all the items in the input dates range
        List<CostItem> report = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(protocol);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM costs");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String category = rs.getString("Category");
                double sum = rs.getDouble("Amount");
                String currency = rs.getString("Currency");
                String description = rs.getString("Description");
                String date = rs.getString("Date");
                LocalDate localDate = stringToDate(date);

                //check if the CostItem date is in the input dates range and add it to the report if yes
                if (localDate.isAfter(startDate.minusDays(1)) && localDate.isBefore(endDate.plusDays(1))) {
                    report.add(new CostItem(new Category(category), sum, Currency.valueOf(currency), description, id, localDate));
                }
            }

        } catch (SQLException e) {
            throw new CostManagerException("Report ERROR", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //sorting the report by date
        report.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return report;
    }

    /**
     * stringToDate method will convert the input String into LocalDate
     */
    public LocalDate stringToDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);

    }

}

