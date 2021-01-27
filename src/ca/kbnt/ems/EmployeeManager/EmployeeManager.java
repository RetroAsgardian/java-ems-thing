package ca.kbnt.ems.EmployeeManager;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle Benton
 */
public class EmployeeManager implements Iterable<Employee> {

    // <editor-fold desc="EVENTS">
    // <editor-fold desc="DataChangedEvent">
    public interface DataChangedEventListener {

        void onChange(DataChangedEvent event);
    }

    public enum DataChangeOperation {
        New, Modified, Removed, ChangedDataType
    }

    public static class DataChangedEvent {

        public final DataChangeOperation operation;
        public final EmployeeData newData, oldData;

        public DataChangedEvent(EmployeeData newData, EmployeeData oldData, DataChangeOperation change) {
            this.operation = change;
            this.newData = newData;
            this.oldData = oldData;
        }
    }

    private final List<DataChangedEventListener> dataChangedListeners = new CopyOnWriteArrayList<>();

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
    int nextID = 1;

    final HashTable<Employee> table = new HashTable<>();

    public EmployeeManager() {
    }

    public EmployeeManager(Iterable<Employee> startingList) {
        this();
        int max = Integer.MIN_VALUE;

        for (var e : startingList) {
            try {
                addEmployee(e);
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
        this.nextID = ID + 1;
        return ID;
    }

    public void addEmployee(Employee emp) throws HashTable.IDInUseError {

        table.add(emp);
        this.notifyDataChangedListeners(new DataChangedEvent(emp.getData(), null, DataChangeOperation.New));
        emp.addDataChangedListener(this::notifyDataChangedListeners);
    }

    public void removeEmployee(int ID) {
        var emp = table.remove(ID);
        emp.removeDataChangedListener(this::notifyDataChangedListeners);
        if (emp != null) {
            this.notifyDataChangedListeners(new DataChangedEvent(null, emp.getData(), DataChangeOperation.Removed));
        }
    }

    public void removeEmployee(Employee e) {
        removeEmployee(e.getID());
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
        addEmployee(emp);
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

    @Override
    public Iterator<Employee> iterator() {
        return table.iterator();
    }
}
