package carsharing;

import carsharing.Dao.CarDao;
import carsharing.Dao.CompanyDao;
import carsharing.Dao.CustomerDao;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import carsharing.entity.Databasemanager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SharingCar {
    private Databasemanager databasemanager;
    private String dbname;
    private CompanyDao companyDao;
    private CarDao carDao;
    private CustomerDao customerDao;

    public SharingCar(String dbname){
        this.databasemanager = new Databasemanager();
        this.companyDao = new CompanyDao();
        this.carDao = new CarDao();
        this.customerDao = new CustomerDao();
        this.dbname = dbname;
    }
    public void main(){
        this.databasemanager.createTableCompany(databasemanager.createConnection(dbname));
        this.databasemanager.createTableCar(databasemanager.createConnection(dbname));
        this.databasemanager.createTableCustomer(databasemanager.createConnection(dbname));
        //delete();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        do{
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer \n0. Exit");
            int choice = scanner.nextInt();
            switch (choice){
                case 0:
                    exit = true;
                    break;
                case 1:
                    LoginasManager();
                    break;
                case 2:
                    LoginasCustomer();
                    break;
                case 3:
                    CreateCustomer();
                    break;
            }

        }while (exit==false);
    }

    private void CreateCustomer() {
        System.out.println("Enter the customer name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        customerDao.saveCustomer(name, databasemanager.createConnection(dbname));
        System.out.println("The customer was added!");
    }

    private void LoginasCustomer() {
        Scanner scanner = new Scanner(System.in);
        List<Customer> list= this.customerDao.getCustomers(databasemanager.createConnection(dbname));
        if(list.size()==0){
            System.out.println("The customer list is empty!");
        }else {
            System.out.println("Choose a customer:");
            Collections.sort(list, new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                    return o1.getId() > o2.getId() ? 1 : -1;
                }
            });
            int i=1;
            for(Customer customer: list){
                System.out.println(i+". "+customer.getName());
                i++;
            }
            System.out.println("0. Back");
            int customerindex = scanner.nextInt();
            if(customerindex!=0){
                customerService(list.get(customerindex-1).getId(), list.get(customerindex-1).getName(),list.get(customerindex-1).getRented_car_id());
            }
        }
    }

    private void customerService(int customerid, String name, int rented_car_id) {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        do{
            System.out.println("1. Rent a car \n2. Return a rented car\n3. My rented car\n0. Back");
            int choice = scanner.nextInt();
            switch (choice){
                case 0:
                    back=true;
                    break;
                case 1:
                    RentCar(customerid);
                    break;
                case 2:
                    ReturnCar(customerid, rented_car_id);
                    break;
                case 3:
                    MyrentedCar(customerid);
            }
        }while (back==false);
    }

    private void RentCar(int customerid) {
        Customer customer = customerDao.getcustomer(customerid,databasemanager.createConnection(dbname));
        if(customer.getRented_car_id()!=0){
            System.out.println("You've already rented a car!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        List<Company> list= this.companyDao.getCompanies(databasemanager.createConnection(dbname));
        if(list.size()==0){
            System.out.println("The company list is empty!");
        }else {
            System.out.println("Choose a company:");
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return o1.getID() > o2.getID() ? 1 : -1;
                }
            });
            int i=1;
            for(Company company: list){
                System.out.println(i+". "+company.getName());
                i++;
            }
            System.out.println("0. Back");
            int companyindex = scanner.nextInt();
            if(companyindex!=0){
                cartoRent(customerid,list.get(companyindex-1).getID());
            }
        }
    }

    private void cartoRent(int customerid, int companyid) {
        Customer customer = customerDao.getcustomer(customerid,databasemanager.createConnection(dbname));
        if(customer.getRented_car_id()!=0){
            System.out.println("You've already rented a car!");
            return;
        }
        List<Car> list= this.carDao.getCarsAvailable(companyid,databasemanager.createConnection(dbname));
        if(list.size()==0){
            System.out.println("No available cars in the 'Company name' company");
        }else {
            Collections.sort(list, new Comparator<Car>() {
                @Override
                public int compare(Car o1, Car o2) {
                    return o1.getId() > o2.getId() ? 1 : -1;
                }
            });
            System.out.println("Choose a car:");
            int i=1;
            for(Car car: list){
                System.out.println(i+". "+car.getName());
                i++;
            }
            System.out.println("0. Back");
            Scanner scanner = new Scanner(System.in);
            int cartorent = scanner.nextInt();
            if(cartorent!=0){
                customerDao.setRentedId(customerid, list.get(cartorent-1).getId(), databasemanager.createConnection(dbname));
                System.out.println("You rented '"+ list.get(cartorent-1).getName()+"'");
            }
        }
    }

    private void ReturnCar(int id, int rented_car_id) {
        if(rented_car_id==0){
            System.out.println("You didn't rent a car!");
        }else{
            customerDao.setRentedId(id, 0, databasemanager.createConnection(dbname));
        }
    }

    private void MyrentedCar(int customerid) {
        Customer customer = customerDao.getcustomer(customerid,databasemanager.createConnection(dbname));
        if(customer.getRented_car_id()==0){
            System.out.println("You didn't rent a car!");
        }else {
            Car myrentedCar=carDao.getCar(customer.getRented_car_id(),databasemanager.createConnection(dbname));
            Company company=companyDao.getCompany(myrentedCar.getCompany_ID(),databasemanager.createConnection(dbname));
            System.out.println("Your rented car:\n"+myrentedCar.getName()+"\nCompany:\n"+company.getName());
        }
    }

    private void LoginasManager() {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        do{
            System.out.println("1. Company list \n2. Create a company\n0. Back");
            int choice = scanner.nextInt();
            switch (choice){
                case 0:
                    back = true;
                    break;
                case 1:
                    CompanyList();
                    break;
                case 2:
                    CreateCompany();
                    break;
            }

        }while (back==false);
    }

    public void delete(){
        carDao.delete(databasemanager.createConnection(dbname));
        companyDao.delete(databasemanager.createConnection(dbname));
        customerDao.delete(databasemanager.createConnection(dbname));
    }

    private void CreateCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
//        scanner.nextLine();
        String companyname = scanner.nextLine();
        companyDao.saveCompany(companyname, databasemanager.createConnection(dbname));
        System.out.println("The company was created!");
    }

    private void CompanyList() {
        Scanner scanner = new Scanner(System.in);
        List<Company> list= this.companyDao.getCompanies(databasemanager.createConnection(dbname));
        if(list.size()==0){
            System.out.println("The company list is empty!");
        }else {
            System.out.println("Choose a company:");
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return o1.getID() > o2.getID() ? 1 : -1;
                }
            });
            int i=1;
            for(Company company: list){
                System.out.println(i+". "+company.getName());
                i++;
            }
            System.out.println("0. Back");
            int companyindex = scanner.nextInt();
            if(companyindex!=0){
                companyService(list.get(companyindex-1).getID(), list.get(companyindex-1).getName());
            }
        }

    }

    private void companyService(int companyId, String name) {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        do{
            System.out.println(name+" company:");
            System.out.println("1. Car list \n2. Create a car\n0. Back");
            int choice = scanner.nextInt();
            switch (choice){
                case 0:
                    back=true;
                    break;
                case 1:
                    CarList(companyId, name);
                    break;
                case 2:
                    CreateCar(companyId);
                    break;
            }
        }while (back==false);

    }

    private void CreateCar(int companyId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
//        scanner.nextLine();
        String carname = scanner.nextLine();
        carDao.saveCar(carname,companyId, databasemanager.createConnection(dbname));
        System.out.println("The car was added!");
    }

    private void CarList(int id,String name) {
        List<Car> list= this.carDao.getCars(id,databasemanager.createConnection(dbname));
        if(list.size()==0){
            System.out.println("The car list is empty!");
        }else {
            Collections.sort(list, new Comparator<Car>() {
                @Override
                public int compare(Car o1, Car o2) {
                    return o1.getId() > o2.getId() ? 1 : -1;
                }
            });
            System.out.println(name+" cars:");
            int i=1;
            for(Car car: list){
                System.out.println(i+". "+car.getName());
                i++;
            }
        }
    }
}
