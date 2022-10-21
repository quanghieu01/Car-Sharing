package carsharing.Dao;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao {
    public List<Company> getCompanies(Connection connection){
        Statement Stmt = null;
        List<Company> companyList = new ArrayList<Company>();
        try {
            Stmt = connection.createStatement();
            String sql = "Select * from company";
            ResultSet rs = Stmt.executeQuery(sql);
            while (rs.next()){
                companyList.add(new Company(rs.getInt("ID"),rs.getString("NAME")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return companyList;
    }

    public void saveCompany(String name, Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql = "INSERT INTO COMPANY (NAME) values('"+name+"');";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Company getCompany(int ID, Connection connection){
        Statement Stmt = null;
        try {
            Stmt = connection.createStatement();
            String sql = "Select * from company where ID = "+ID;
            ResultSet rs = Stmt.executeQuery(sql);
            if(rs.next()){
                return new Company(rs.getInt("ID"),rs.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void delete(Connection connection){
        try {
            Statement Stmt = connection.createStatement();
            String sql = "delete from company";
            Stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
