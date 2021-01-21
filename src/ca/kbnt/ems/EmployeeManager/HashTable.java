package ca.kbnt.ems.EmployeeManager;

import java.util.*;

public class HashTable<T extends HashTable.IHashable> implements Iterable<T> {

    public interface IHashable {

        int getID();
    }

    public static class IDInUseError extends Exception {

        IDInUseError(int ID) {
            super("ID " + ID + "is already in use.");

        }
    }

    private List<List<T>> buckets;
    private int bucketCount;

    public HashTable() {
        this(5);
    }

    public HashTable(int bucketCount) {
        this.bucketCount = bucketCount;
        this.buckets = new ArrayList<>(this.bucketCount);

        for (int i = 0; i < bucketCount; i++) {
            this.buckets.add(i, new ArrayList<>());
        }
    }

    private int doHash(T s) {
        return this.doHash(s.getID());
    }

    private int doHash(int num) {
        return num % this.bucketCount;
    }

    public void add(T s) throws IDInUseError {
        if (this.contains(s.getID())) {
            throw new IDInUseError(s.getID());
        }
        this.buckets.get(this.doHash(s)).add(s);
    }

    public void set(T s) {
        if (this.contains(s.getID())) {
            this.remove(s.getID());
        }
        this.buckets.get(this.doHash(s)).add(s);
    }

    public T get(int ID) {
        for (T s : this.buckets.get(doHash(ID))) {
            if (s.getID() == ID) {
                return s;
            }
        }
        return null;
    }

    public T remove(int ID) {
        for (T s : this.buckets.get(doHash(ID))) {
            if (ID == s.getID()) {
                this.buckets.get(doHash(ID)).remove(s);
                return s;
            }
        }
        return null;
    }

    public T remove(T objToRemove) {
        if (this.buckets.get(doHash(objToRemove)).remove(objToRemove)) {
            return objToRemove;
        } else {
            return null;
        }

    }

    public boolean contains(int studentNumber) {
        return this.get(studentNumber) != null;
    }

    public boolean contains(T s) {
        return this.contains(s.getID());
    }

    public List<T> list() {
        List<T> ret = new ArrayList<>();
        this.buckets.forEach(ret::addAll);
        return ret;
    }

    public int size() {
        int count = 0;
        for (List<T> list : buckets) {
            count += list.size();
        }
        return count;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Iterator<List<T>> bucketsIterator = buckets.iterator();
            Iterator<T> currentIterator = new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new NoSuchElementException();
                }

            };

            @Override
            public boolean hasNext() {
                return seek();
            }

            @Override
            public T next() {
                if (!seek()) {
                    throw new NoSuchElementException();
                }
                return currentIterator.next();
            }

            private boolean seek() {
                while (!currentIterator.hasNext()) {
                    if (!bucketsIterator.hasNext()) {
                        return false;
                    } else {
                        currentIterator = bucketsIterator.next().iterator();
                    }
                }
                return true;
            }

        };
    }
}
