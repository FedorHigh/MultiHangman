
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EmployeeService {
    /**
     * Приходит список Employee
     * Необходимо вернуть список уникальных имён сотрудников (name)
     */
    public List<String> getUniqueEmployeeNames(List<Employee> employees) {
        return employees.stream()
                .map(employee -> employee.getName())
                .distinct()
                .toList()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть только тех сотрудников, чей возраст меньше 30 лет
     */
    public List<Employee> getYoungEmployees(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getAge()<30)
                .toList()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть количество сотрудников без подчинённых
     */
    public long countEmployeesWithoutSubordinates(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getSubordinates() == null || e.getSubordinates().size() == 0)
                .count()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо удвоить стаж каждого сотрудника
     */
    public List<Employee> doubleEmployeeExperience(List<Employee> employees) {
        employees.stream()
                .forEach(e -> e.setExperience(e.getExperience()*2))
                ;
        return  employees.stream().toList();
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть Employee с минимальным значением возраста
     */
    public Optional<Employee> getYoungestEmployee(List<Employee> employees) {
        return employees.stream()
                .min(Comparator.comparing(Employee::getAge))

                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть список возрастов всех сотрудников
     */
    public List<Integer> getEmployeeAges(List<Employee> employees) {
        return employees.stream()
                .map(Employee::getAge)
                .toList()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо преобразовать его в Map, где ключ - name, а значение - возраст
     */
    public Map<String, Integer> mapEmployeeNameToAge(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getAge))
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть не более трёх сотрудников, у которых есть хотя бы один подчинённый
     */
    public List<Employee> getEmployeesWithSubordinates(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getSubordinates().size()<=3)
                .limit(3)
                .toList()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть отсортированный по количеству подчинённых список Employee
     */
    public List<Employee> getSortedEmployeesBySubordinates(List<Employee> employees) {

        return employees.stream()
                .sorted(Comparator.comparing(e->e.getSubordinates().size()))
                .toList()
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо посчитать суммарный стаж всех сотрудников
     */
    public int getTotalEmployeeExperience(List<Employee> employees) {
        AtomicInteger c = new AtomicInteger();
        employees.stream()
                .forEach(e -> c.addAndGet(e.getExperience()));
                ;
                return c.get();
    }

    /**
     * Приходит список Employee
     * Проверить, что у всех сотрудников старше 40 лет есть подчинённые
     */
    public Boolean checkSeniorEmployeesHaveSubordinates(List<Employee> employees) {

        return employees.stream()
                .filter(e -> e.getAge()>40)
                .noneMatch(e -> e.getSubordinates().isEmpty())
        ;
    }

    /**
     * Приходит список Employee
     * Проверить, что есть хотя бы один сотрудник с опытом больше 15 лет
     */
    public Boolean checkAnyExperiencedEmployee(List<Employee> employees) {

        return employees.stream()
                .anyMatch(e -> e.getExperience()>15)
                ;
    }

    /**
     * Приходит список Employee
     * Необходимо вернуть любого сотрудника с подчинёнными больше 5 человек
     */
    public Employee getManagerWithManySubordinates(List<Employee> employees) {
        List<Employee> tmp = employees.stream()
                .filter(e -> e.getSubordinates().size()>5)
                .toList();
        return tmp.getFirst();

    }

}
