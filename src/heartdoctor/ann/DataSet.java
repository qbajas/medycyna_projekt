package heartdoctor.ann;

import java.util.ArrayList;

/**
 * Kolekcja próbek medycznych pacjentów.
 *
 * @author empitness
 */
public class DataSet {

  /*
   * Lista próbek medycznych pacjentów
   */
    public ArrayList<DataEntry> entries;

	/*
	 * Tworzy nową instancję kolekcji próbek
	 */
    public DataSet() {
        entries = new ArrayList<DataEntry>();
    }

	/*
	 * Tworzy nową instację kolekcji próbek, inicjalizując
	 * dodatkowo wewnętrzny kontener z góry narzuconą pojemnością.
	 * 
	 * @param initialCapacity początkowa pojemność kolekcji próbek
	 */
    public DataSet(int initialCapacity) {
        entries = new ArrayList<DataEntry>(initialCapacity);
    }
}
