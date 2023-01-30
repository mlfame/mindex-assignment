package com.mindex.challenge.service.impl;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.money.CurrencyUnitFormatter;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) throws RuntimeException {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employee id is provided or record of employee is not found: " + id);
        }
        LOG.debug("Number of Reports required for this employee id [{}]", employee);
        ReportingStructure report = new ReportingStructure();
        LOG.debug("Number of Reports required for this employee id 111---- [{}]", employee);
        int numberOfReport = getNumberOfReports(employee);
        report.setnumberOfReports(numberOfReport);
        report.setEmployee(employee);
        return report;
    }

    private int getNumberOfReports(Employee emp) {
        LOG.debug("Computing the number of Reports for employee [{}]", emp);
        Integer numberOfReports = 0;
        ArrayDeque<Employee> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.add((emp));

        while (!queue.isEmpty()) {
            Employee currEmp = queue.poll();
            String currEmpId = currEmp.getEmployeeId();

            if(!visited.contains(currEmpId)){

                List<Employee> currEmpDirectReport = currEmp.getDirectReports();
                visited.add(currEmpId);

                if(currEmpDirectReport!=null) {
                    queue.addAll(currEmpDirectReport);
                }
                    
                numberOfReports+=1;

            }
        }
        return numberOfReports;
    }


    
}
