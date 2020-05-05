package com.manorath.databaseperformance.demo.model;
import java.util.Date;

public class Salary {

    public  int emp_no;
    public  int salary;
    public Date from_date;
    public Date to_date;

    public Salary(int emp_no,int salary,Date from_date,Date to_date) {
        this.emp_no = emp_no;
        this.salary = salary;
        this.from_date = from_date;
        this.to_date = to_date;
    }
}
