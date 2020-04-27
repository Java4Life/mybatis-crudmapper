package com.wuxudu.mybatis.crudmapper.sample.domain;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "t_employee")
public class Employee extends AbstractEntity {

    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_years")
    private Integer employedYears;

    @Column(name = "employee_salary")
    private BigDecimal employeeSalary;

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployedYears() {
        return employedYears;
    }

    public void setEmployedYears(Integer employedYears) {
        this.employedYears = employedYears;
    }

    public BigDecimal getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(BigDecimal employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employedYears=" + employedYears +
                ", employeeSalary=" + employeeSalary +
                '}' + "\t" + super.toString();
    }
}
