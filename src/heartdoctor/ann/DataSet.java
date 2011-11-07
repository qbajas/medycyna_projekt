package heartdoctor.ann;

import java.util.ArrayList;

/**
 *
 * @author empitness
 */
public class DataSet {

    public ArrayList<DataEntry> entries;

    public DataSet() {
        entries = new ArrayList<DataEntry>();
    }

    public DataSet(int initialCapacity) {
        entries = new ArrayList<DataEntry>(initialCapacity);
    }
}
