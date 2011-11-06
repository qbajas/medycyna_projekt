package heartdoctor.DataModel;

/**
 * Klasa reprezentujaca dane pacjenta z bazy
 * @author michal
 */
public class PatientData {

    int ID; // id z bazy, również id klienta, np. pesel
    private String name;
    private String middleName;
    private String lastName;
    private int socialSecurityNbr;
    private String address;
    private String postalCode;
    private String city;
    private MedicalData medicalData;

    public PatientData() {
    }

    public PatientData(int ID, String name, String middleName, String lastName, int socialSecurityNbr, String address, String postalCode, String city, MedicalData medicalData) {
        this.ID = ID;
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.socialSecurityNbr = socialSecurityNbr;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.medicalData = medicalData;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MedicalData getMedicalData() {
        return medicalData;
    }

    public void setMedicalData(MedicalData medicalData) {
        this.medicalData = medicalData;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getSocialSecurityNbr() {
        return socialSecurityNbr;
    }

    public void setSocialSecurityNbr(int socialSecurityNbr) {
        this.socialSecurityNbr = socialSecurityNbr;
    }
}
