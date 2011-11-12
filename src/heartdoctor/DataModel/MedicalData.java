package heartdoctor.DataModel;

/**
 * Klasa do reprezentacji danych z bazy. 
 * Dodane enumy dla mapowania i ułatwienia roboty z GUI.
 * @author michal
 */
public class MedicalData {

    private int dbID;
    private int age;
    private Sex sex;
    private ChestPain chestPain;
    private int bloodPressure;
    private int cholestoral;
    private int bloodSugar;
    private RestingECGResult restecg;
    private int maxHeartRate;
    private int angina;
    private int oldpeak;
    private Slope slope;
    private int ca;
    private Thal thal;
    private int diagnosis;
    private int verifiedDiagnosis=-1;

    public MedicalData(int dbID,int age, Sex sex, ChestPain chestPain, int bloodPressure, int cholestoral, int bloodSugar, RestingECGResult restecg, int maxHeartRate, int angina, int oldpeak, Slope slope, int ca, Thal thal, int diagnosis) {
        this.age = age;
        this.sex = sex;
        this.chestPain = chestPain;
        this.bloodPressure = bloodPressure;
        this.cholestoral = cholestoral;
        this.bloodSugar = bloodSugar;
        this.restecg = restecg;
        this.maxHeartRate = maxHeartRate;
        this.angina = angina;
        this.oldpeak = oldpeak;
        this.slope = slope;
        this.ca = ca;
        this.thal = thal;
        this.diagnosis = diagnosis;
    }

    public MedicalData() {
    }

    public int getVerifiedDiagnosis() {
        return verifiedDiagnosis;
    }

    public void setVerifiedDiagnosis(int verifiedDiagnosis) {
        this.verifiedDiagnosis = verifiedDiagnosis;
    }

    public void setChestPain(ChestPain chestPain) {
        this.chestPain = chestPain;
    }

    public void setRestecg(RestingECGResult restecg) {
        this.restecg = restecg;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setSlope(Slope slope) {
        this.slope = slope;
    }

    public void setThal(Thal thal) {
        this.thal = thal;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAngina() {
        return angina;
    }

    public void setAngina(int angina) {
        this.angina = angina;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(int bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    public int getChestPain() {
        return chestPain.getValue();
    }

    public void setChestPain(int chestPain) {
        switch (chestPain) {
            case 1:
                this.chestPain = ChestPain.TypicalAngina;
                break;
            case 2:
                this.chestPain = ChestPain.AntypicalAngina;
                break;
            case 3:
                this.chestPain = ChestPain.NonAnginal;
                break;
            case 4:
                this.chestPain = ChestPain.Asymptomatic;
                break;
            default:
                this.chestPain = null;
        }
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }
    
    public int getCholestoral() {
        return cholestoral;
    }

    public void setCholestoral(int cholestoral) {
        this.cholestoral = cholestoral;
    }

    public int getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(int diagnosis) {
        this.diagnosis = diagnosis;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getOldpeak() {
        return oldpeak;
    }

    public void setOldpeak(int oldpeak) {
        this.oldpeak = oldpeak;
    }

    public int getRestecg() {
        return restecg.getValue();
    }

    public void setRestecg(int restecg) {
        switch (restecg) {
            case 0:
                this.restecg = RestingECGResult.Normal;
                break;
            case 1:
                this.restecg = RestingECGResult.STTAbnormal;
                break;
            case 2:
                this.restecg = RestingECGResult.leftVentricularHypertrophy;
                break;
            default:
                this.restecg = null;
        }
    }

    public int getSex() {
        return sex.getValue();
    }

    public void setSex(int sex) {
        switch (sex) {
            case 0:
                this.sex = Sex.Female;
                break;
            case 1:
                this.sex = Sex.Male;
                break;
            default:
                this.sex = null;
        }
    }

    public int getSlope() {
        return slope.getValue();
    }

    public void setSlope(int slope) {
        switch (slope) {
            case 1:
                this.slope = Slope.UpSloping;
                break;
            case 2:
                this.slope = Slope.Flat;
                break;
            case 3:
                this.slope = Slope.DownSloping;
                break;
            default:
                this.slope = null;
        }
    }

    public int getThal() {
        return thal.getValue();
    }

    public void setThal(int thal) {
        switch (thal) {
            case 3:
                this.thal = Thal.Normal;
                break;
            case 6:
                this.thal = Thal.FixedDefect;
                break;
            case 7:
                this.thal = Thal.ReversableDefect;
                break;
            default:
                this.thal = null;
        }
    }

    public ChestPain getChestPainEnum() {
        return chestPain;
    }

    public Sex getSexEnum() {
        return sex;
    }

    public RestingECGResult RestingECGResultEnum() {
        return restecg;
    }

    public Slope getSlopeEnum() {
        return slope;
    }

    public Thal getThalEnum() {
        return thal;
    }

    public enum ChestPain {

        TypicalAngina,
        AntypicalAngina,
        NonAnginal,
        Asymptomatic;

        public int getValue() {
            return ordinal() + 1;
        }
    }

    public enum Sex {

        Female,
        Male;

        public int getValue() {
            return ordinal();
        }
    }

    public enum RestingECGResult {

        Normal,
        STTAbnormal,
        leftVentricularHypertrophy;

        public int getValue() {
            return ordinal();
        }
    }

    public enum Slope {

        UpSloping,
        Flat,
        DownSloping;

        public int getValue() {
            return ordinal() + 1;
        }
    }

    public enum Thal {

        Normal(3),
        FixedDefect(6),
        ReversableDefect(7);
        private int mapping;

        Thal(int nbr) {
            mapping = nbr;
        }

        public int getValue() {
            return mapping;
        }
    }
}
