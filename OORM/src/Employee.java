import java.util.HashSet;
import java.util.Set;

public class Employee {
    private String name;
    private Set<Department> departments = new HashSet<>();

    public Employee(String name) {
        this.name = name;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }
}
