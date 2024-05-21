import java.util.HashSet;
import java.util.Set;

public class Department {
    private String name;
    private Set<Employee> employees = new HashSet<>();

    public Department(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
