package carsharing.Dao;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public List<Customer> getCustomers(Connection connection){
        Statement Stmt = null;
        List<Customer> customerList = new ArrayList<Customer>();
        try {
            Stmt = connection.createStatement();
            String sql = "Select * from customer";
            ResultSet rs = Stmt.executeQuery(sql);
            while (rs.next()){
                customerList.add(new Customer(rs.getInt("ID"),rs.getString("NAME"), rs.getInt("RENTED_CAR_ID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    public Car getrentedCar(int customerID, Connection connection){
        Statement Stmt = null;
        try {
            Stmt = connection.createStatement();
            String sql = "Select car.ID, car.COMPANY_ID, car.NAME from customer LEFT JOIN CAR ON customer.RENTED_CAR_ID = car.ID AND customer.ID = "+customerID;
            ResultSet rs = Stmt.executeQuery(sql);
            return new Car(rs.getInt("ID"),rs.getInt("COMPANY_ID"),rs.getString("NAME"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getcustomer(int customerID, Connection connection){
        Statement Stmt = null;
        try {
            Stmt = connection.createStatement();
            String sql = "select * from customer where ID= "+customerID;
            ResultSet rs = Stmt.executeQuery(sql);
            if(rs.next()) {
                return new Customer(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("RENTED_CAR_ID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void saveCustomer(String name, Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) values('"+name+"',"+null+");";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRentedId(int id, int rentedId, Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql ="";
            if(rentedId!=0){
                sql = "UPDATE CUSTOMER SET RENTED_CAR_ID= " + rentedId + "WHERE ID= "+id;
            }else {
                sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = null WHERE ID= "+id;
            }
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql = "delete from customer";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
