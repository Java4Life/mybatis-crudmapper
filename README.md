# mybatis-crudmapper
MyBatis Generic CrudMapper with Spring Boot

Apache Maven:

<dependency>
  <groupId>com.wuxudu</groupId>
  <artifactId>mybatis-crudmapper-spring-boot-starter</artifactId>
  <version>1.0</version>
</dependency>

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
        InsertParam<Employee> param = new InsertParam<>();
        param.values(employee1, employee2);
        int rows = this.mapper.insert(param);
        assertEquals(rows, 2);
        Condition byCreateTime = Condition.by("createTime").greaterThanEqual(now);
        SelectParam query = new SelectParam();
        query.where(byCreateTime);
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }
