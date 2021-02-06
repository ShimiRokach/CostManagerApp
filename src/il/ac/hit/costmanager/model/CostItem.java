
package il.ac.hit.costmanager.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * CostItem class is the item that the user will insert to the application
 * and we will stored it in the cost items table
 */

public class CostItem {

    private Category category;
    private double sum;
    private Currency currency;
    private String description;
    private int id;
    private LocalDate date;

    /**
     * Class constructor with auto generate id
     */
    public CostItem(Category category, double sum, Currency currency, String description, LocalDate date) throws CostManagerException {
        setCategory(category);
        setSum(sum);
        setCurrency(currency);
        setDescription(description);
        setId(getLastId());
        setDate(date);
    }

    /**
     * Class constructor with custom id
     */
    public CostItem(Category category, double sum, Currency currency, String description, int id, LocalDate date) {
        setCategory(category);
        setSum(sum);
        setCurrency(currency);
        setDescription(description);
        setId(id);
        setDate(date);
    }

    //getters and setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        if (sum >= 0) {
            this.sum = sum;
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * getLastId method will return the ID of the last item inserted by the user
     */
    public int getLastId() throws CostManagerException {

        //lastId will contain the ID of the last item inserted by the user
        int lastId = 0;
        Connection connection = null;
        ResultSet rs = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(DerbyDBModel.protocol);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT ID FROM costs");
            if (!(rs.first())) {
                lastId = 0;
            } else {
                rs.last();
                lastId = rs.getInt("ID");
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error Loading DB", e);
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

        return lastId + 1;
    }

    @Override
    public String toString() {

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return "{id=" + id +
                ", category= " + category.getName() +
                ", sum=" + sum +
                ", currency=" + currency +
                ", description= '" + description + '\'' +
                ", date= " + formattedDate +
                '}';
    }


}
