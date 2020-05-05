package com.manorath.databaseperformance.demo.controller;

import com.manorath.databaseperformance.demo.model.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "v1/demo/",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Salary> demoApp() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Salary> salaries = jdbcTemplate.query("select * from salaries",
                (rs, rowNum) ->
                        new Salary(rs.getInt("emp_no"),rs.getInt("salary"),rs.getDate("from_date"),rs.getDate("to_date")));
        stopWatch.stop();
        System.out.println("time taken by server to fetch all data (in milliseconds"  + stopWatch.getLastTaskTimeMillis());
        System.out.println("size: " + salaries.size());
        return salaries;
    }
}
