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
        New,
        Modified,
        Removed,
        ChangedDataType
    }

    public class DataChangedEvent {

        final DataChangeOperation operation;
        final Employee changedEmployee;

        public DataChangedEvent(Employee changedEmployee, DataChangeOperation change) {
            this.operation = change;
            this.changedEmployee = changedEmployee;
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
                Logger.getLogger(EmployeeManager.class.getName()).log(Level.WARNING, "Duplicate IDs Found when importing data", ex);
            }
        }
        if (max > this.nextID) {
            this.nextID = max;
        }
    }

    public int generateID() {
        return nextID++;
    }

    public void addEmployee(Employee e) throws HashTable.IDInUseError {

        table.add(e);
        this.notifyDataChangedListeners(new DataChangedEvent(e, DataChangeOperation.New));

    }

    public void removeEmployee(int ID) {
        var emp = table.remove(ID);
        if (emp != null) {
            this.notifyDataChangedListeners(new DataChangedEvent(emp, DataChangeOperation.Removed));
        }
    }

    public void removeEmployee(Employee e) {
        var emp = table.remove(e);
        if (emp != null) {
            this.notifyDataChangedListeners(new DataChangedEvent(emp, DataChangeOperation.Removed));
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

    public FullTimeEmployee newFTEmployee(int ID) throws HashTable.IDInUseError {
        FullTimeEmployee emp = new FullTimeEmployee(ID);
        table.add(emp);
        this.notifyDataChangedListeners(new DataChangedEvent(emp, DataChangeOperation.New));
        return emp;
    }

    public FullTimeEmployee newFTEmployee() {
        int ID = this.generateID();

        try {
            FullTimeEmployee emp = newFTEmployee(ID);
            return emp;
        } catch (HashTable.IDInUseError ex) {
            Logger.getLogger(EmployeeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public PartTimeEmployee newPTEmployee(int ID) throws HashTable.IDInUseError {
        PartTimeEmployee emp = new PartTimeEmployee(ID);
        table.add(emp);
        this.notifyDataChangedListeners(new DataChangedEvent(emp, DataChangeOperation.New));
        return emp;
    }

    public PartTimeEmployee newPTEmployee() {
        int ID = this.generateID();

        try {
            PartTimeEmployee emp = newPTEmployee(ID);
            return emp;
        } catch (HashTable.IDInUseError ex) {
            Logger.getLogger(EmployeeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
