# mybatis-crudmapper
MyBatis Generic CrudMapper with Spring Boot

Apache Maven:

    <dependency>
        <groupId>com.wuxudu</groupId>
        <artifactId>mybatis-crudmapper-spring-boot-starter</artifactId>
        <version>1.6</version>
    </dependency>
        
Prerequisites：
JDK 1.8+，mybatis 3.5.3，SpringBoot 2.2.5.RELEASE, mybatis-spring-boot-starter 2.1.1

Entity:

    public abstract class AbstractEntity {
        @Id
        @Column(name = "id", insertable = false, updatable = false)
        private Long id;

        @Column(name = "create_time", insertable = false, updatable = false)
        private LocalDateTime createTime;

        @Column(name = "update_time", insertable = false, updatable = false)
        private LocalDateTime updateTime;
    ...
    }

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
    ...
    }

Mapper:

    @Mapper
    public interface EmployeeMapper extends CrudMapper<Employee> {
    }

Usage(more in mybatis-crudmapper-spring-boot-sample testcases):

    @Test
    void select() {
        Condition byName = Condition.by("employeeName").in("Sarah", "Sophia");
        Condition bySalaryBtm = Condition.by("employeeSalary").greaterThanEqual(new BigDecimal(10000));
        Condition bySalaryTop = Condition.by("employeeSalary").lessThanEqual(new BigDecimal(80000));
        Condition bySalary = Condition.and(bySalaryBtm, bySalaryTop);
        Condition condition = Condition.or(bySalary, byName);
        SelectParam param = new SelectParam();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }
    
    @Test
    void delete() {
        Condition condition = Condition.by("id").equal(1);
        DeleteParam param = new DeleteParam();
        param.where(condition);
        int rows = this.mapper.delete(param);
        assertEquals(rows, 1);
    }
    
    
    @Test
    void update() {
        Employee toUpdate = new Employee();
        toUpdate.setEmployeeSalary(new BigDecimal(99999));
        toUpdate.setEmployedYears(1);
        Condition condition = Condition.by("id").equal(1);
        UpdateParam<Employee> param = new UpdateParam<>();
        param.value(toUpdate);
        param.where(condition);
        int rows = this.mapper.update(param);
        assertEquals(rows, 1);
        SelectParam query = new SelectParam();
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }
    
    @Test
    void insert() {
        LocalDateTime now = LocalDateTime.now();
        Employee employee1 = new Employee();
        employee1.setEmployeeNo("no1");
        employee1.setEmployeeName("name1");
        employee1.setEmployedYears(1);
        employee1.setEmployeeSalary(new BigDecimal("1"));
        Employee employee2 = new Employee();
        employee2.setEmployeeNo("no2");
        employee2.setEmployeeName("name2");
        employee2.setEmployedYears(2);
        employee2.setEmployeeSalary(new BigDecimal("2"));
        int rows = this.mapper.insertAll(Arrays.asList(employee1,employee2));
        assertEquals(rows, 2);
        Condition byCreateTime = Condition.by("createTime").greaterThanEqual(now);
        SelectParam query = new SelectParam();
        query.where(byCreateTime);
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }
