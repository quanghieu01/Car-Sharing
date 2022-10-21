package carsharing.Dao;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    public List<Car> getCars(int companyID, Connection connection){
        Statement Stmt = null;
        List<Car> carList = new ArrayList<Car>();
        try {
            Stmt = connection.createStatement();
            String sql = "Select * from car where COMPANY_ID = "+companyID;
            ResultSet rs = Stmt.executeQuery(sql);
            while (rs.next()){
                carList.add(new Car(rs.getInt("ID"),rs.getInt("COMPANY_ID"),rs.getString("NAME")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }

    public Car getCar(int ID, Connection connection){
        Statement Stmt = null;
        try {
            Stmt = connection.createStatement();
            String sql = "Select * from car where ID = "+ID;
            ResultSet rs = Stmt.executeQuery(sql);
            if(rs.next()) {
                return new Car(rs.getInt("ID"), rs.getInt("COMPANY_ID"), rs.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Car> getCarsAvailable(int companyID, Connection connection){
        Statement Stmt = null;
        List<Car> carList = new ArrayList<Car>();
        try {
            Stmt = connection.createStatement();
            String sql = "Select ID, NAME FROM CAR where COMPANY_ID = "+companyID+" EXCEPT SELECT CUSTOMER.RENTED_CAR_ID, CAR.NAME from CUSTOMER " +
                    "LEFT JOIN CAR where CUSTOMER.RENTED_CAR_ID = CAR.ID";
            ResultSet rs = Stmt.executeQuery(sql);
            while (rs.next()){
                carList.add(new Car(rs.getInt("ID"),companyID,rs.getString("NAME")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }



    public void saveCar(String carname,int companyId, Connection connection) {
        try {
            Statement Stmt = connection.createStatement();
            String sql = "INSERT INTO car (COMPANY_ID,NAME) values("+companyId+",'"+carname+"');";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql = "delete from car";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
