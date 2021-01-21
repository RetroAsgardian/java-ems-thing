package ca.kbnt.ems.EmployeeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle Benton
 */
public class EmployeeManager {

    // <editor-fold desc="EVENTS">
    // <editor-fold desc="DataChangedEvent">
    public interface DataChangedEventListener {

        void onChange(DataChangedEvent event);
    }

    public enum DataChangeOperation {
        New, Modified, Removed, ChangedDataType
    }

    public static class DataChangedEvent {

        final DataChangeOperation operation;
        final EmployeeData newData, oldData;

        public DataChangedEvent(EmployeeData newData, EmployeeData oldData, DataChangeOperation change) {
            this.operation = change;
            this.newData = newData;
            this.oldData = oldData;
        }
    }

    private final List<DataChangedEventListener> dataChangedListeners = new ArrayList<>();

    public void addDataChangedListener(DataChangedEventListener l) {
        dataChangedListeners.add(l);
    }

    public void removeDataChangedListener(DataChangedEventListener l) {
        dataChangedListeners.remove(l);
    }

    private void notifyDataChangedListeners(DataChangedEvent e) {
        dataChangedListeners.forEach(l -> {
            l.onChange(e);
        });
    }

    // </editor-fold>
    // </editor-fold>
    int nextID = 0;

    final HashTable<Employee> table = new HashTable<>();

    public EmployeeManager() {
    }

    public EmployeeManager(Iterable<Employee> startingList) {
        this();
        int max = Integer.MIN_VALUE;

        for (var e : startingList) {
            try {
                table.add(e);
                if (e.getID() > max) {
                    max = e.getID();
                }
            } catch (HashTable.IDInUseError ex) {
                Logger.getLogger(EmployeeManager.class.getName()).log(Level.WARNING,
                        "Duplicate IDs Found when importing data", ex);
            }
        }
        if (max > this.nextID) {
            this.nextID = max;
        }
    }

    public int generateID() {
        int ID = nextID;
        while (!this.checkVacantID(ID)) {
            ID++;
        }
        return ID;
    }

    public void addEmployee(Employee emp) throws HashTable.IDInUseError {

        table.add(emp);
        this.notifyDataChangedListeners(new DataChangedEvent(emp.getData(), null, DataChangeOperation.New));
        emp.addDataChangedListener((e) -> {
            this.notifyDataChangedListeners(e);
        });
    }

    public void removeEmployee(int ID) {
        var emp = table.remove(ID);
        if (emp != null) {
            this.notifyDataChangedListeners(new DataChangedEvent(null, emp.getData(), DataChangeOperation.Removed));
        }
    }

    public void removeEmployee(Employee e) {
        var emp = table.remove(e);
        if (emp != null) {
            this.notifyDataChangedListeners(new DataChangedEvent(null, emp.getData(), DataChangeOperation.Removed));
        }
    }

    public Employee getEmployee(int ID) {
        return table.get(ID);
    }

    public List<Employee> getEmployees() {
        return table.list();
    }

    public Boolean checkVacantID(int ID) {
        return !table.contains(ID);
    }

    public Employee newEmployee(EmployeeData data) throws HashTable.IDInUseError {
        Employee emp = new Employee(data);
        table.add(emp);
        this.notifyDataChangedListeners(new DataChangedEvent(emp.getData(), null, DataChangeOperation.New));
        emp.addDataChangedListener((e) -> {
            this.notifyDataChangedListeners(e);
        });
        return emp;
    }

    public Employee newEmployee() {
        int ID = this.generateID();

        try {
            Employee emp = newEmployee(new EmployeeData(ID));
            return emp;
        } catch (HashTable.IDInUseError ex) {
            Logger.getLogger(EmployeeManager.class.getName()).log(Level.SEVERE, "Generated ID was not vacant!", ex);
            return null;
        }

    }

    public int size() {
        return table.size();
    }
}
