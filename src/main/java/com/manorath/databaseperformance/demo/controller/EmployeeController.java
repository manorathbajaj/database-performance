package com.manorath.databaseperformance.demo.controller;

import com.manorath.databaseperformance.demo.model.Salary;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.URLConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "v1/demo/file",
            method = RequestMethod.GET)
    public void demoAppp(HttpServletRequest request, HttpServletResponse response) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Salary> salaries = jdbcTemplate.query("select * from salaries",
                (rs, rowNum) ->
                        new Salary(rs.getInt("emp_no"),rs.getInt("salary"),rs.getDate("from_date"),rs.getDate("to_date")));
        try {
            File file = new File("test.dat");
            file.createNewFile();

            FileWriter myWriter = new FileWriter("test.dat");

            // create dat file sample demo
            for(int i =0 ; i < 100;i ++) {
                String line = salaries.get(i).emp_no + ": salary: " + salaries.get(i).salary;
                myWriter.write(line);
                System.out.println(line);
            }
            myWriter.close();

            // .dat prolly is a text/html
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e ) {
            System.out.println(e);
        }

    }
}
