/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.databasefiller;

import heartdoctor.DataModel.MedicalData;
import heartdoctor.DataModel.PatientData;
import heartdoctor.Util.DBUtil;
import heartdoctor.Util.PatientController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author michal
 */
public class PatientDataGenerator {

    private String[] maleNames;
    private String[] femaleNames;
    private String[] lastNames;
    private String[] cities;
    private String[] streets;
    private String[] female;
    private String[] male;
    private Random rand;

    public PatientDataGenerator() {
        initComponents();

    }

    private void clearDB() throws SQLException {
        Connection conn = null;
        Statement stm = null;
        try {
            conn = DBUtil.getConnection();
            stm = conn.createStatement();
            stm.executeUpdate("truncate table Patients");
        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    public MedicalData processResultSet(ResultSet rs) throws SQLException {
        MedicalData data = new MedicalData();
        data.setDbID(rs.getInt(1));
        data.setAge(rs.getDouble(2));
        data.setSex(rs.getDouble(3));
        data.setChestPain(rs.getDouble(4));
        data.setBloodPressure(rs.getDouble(5));
        data.setCholestoral(rs.getDouble(6));
        data.setBloodSugar(rs.getDouble(7));
        data.setRestecg(rs.getDouble(8));
        data.setMaxHeartRate(rs.getDouble(9));
        data.setAngina(rs.getDouble(10));
        data.setOldpeak(rs.getDouble(11));
        data.setSlope(rs.getDouble(12));
        data.setCa(rs.getDouble(13));
        data.setThal(rs.getDouble(14));
        data.setDiagnosis(rs.getDouble(15));
        data.setProgramDiagnosis(rs.getDouble(17));
        return data;
    }

    private PatientData generatePatient(double sex, double age) {
        PatientData data = new PatientData();
        data.setCity(selectRandom(cities));
        data.setAddress(selectRandom(streets) + generateAddress());
        data.setPostalCode(generateCode());
        data.setLastName(selectRandom(lastNames));
        if (sex == 0) {
            data.setName(selectRandom(femaleNames));
            data.setMiddleName(selectRandom(femaleNames));
            String last=data.getLastName();
            if(last.endsWith("i"))
                data.setLastName(last.substring(0, last.length()-1)+"a");
        } else if (sex == 1) {
            data.setName(selectRandom(maleNames));
            data.setMiddleName(selectRandom(maleNames));       
        }
        
        data.setPesel(generatePesel(sex, age));
        return data;
    }

    private String generatePesel(double sex, double age) {
        String str = "";
        int a=(int)age;
        Calendar cal=GregorianCalendar.getInstance();
        cal.roll(Calendar.YEAR,-a);
        cal.set(Calendar.MONTH, rand.nextInt(12)+1);
        cal.set(Calendar.DAY_OF_MONTH, rand.nextInt(28)+1);
        Date d=cal.getTime();
        SimpleDateFormat f=new SimpleDateFormat("yyMMdd");
        str=f.format(d);
        str+=""+ rand.nextInt(10)+""+rand.nextInt(10)+""+rand.nextInt(10);
        if(sex==0)
            str+=selectRandom(female);
        else
            str+=selectRandom(male);
        str+=controlSum(str);
        return str;
    }

    private String controlSum(String str){
        int [] tokens=new int[10];
       
        for(int j=0;j<10;j++){
            char [] chars=new char[1];
            chars[0]=str.charAt(j);
            tokens[j]=Integer.parseInt(new String(chars));
        }
            
        int sum=tokens[0]+3*tokens[1]+7*tokens[2]+9*tokens[3]+
                tokens[4]+3*tokens[5]+7*tokens[6]+9*tokens[7] +tokens[8] + 3* tokens[9];
        int rest=sum % 10;
        if(rest==0)
            return "0";
        return ""+ (10-rest);
    }
    
    private String generateAddress() {
        String str;
        str = " " + rand.nextInt(100);
        if (rand.nextInt(50) < 35) {
            str += "/" + rand.nextInt(100);
        }
        return str;
    }

    private String generateCode() {
        String str="";
        int code=rand.nextInt(100);
        if(code<10)
            str+="0";
        str +="" + code;
        str += "-";
        int token = rand.nextInt(700);
        if (token < 100) 
            str += "0";
        if(token<10)
            str+="0";
        str += "" + token;
        return str;
    }

    private void generate() throws SQLException {
        clearDB();
        Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;
        MedicalData data=null;
        PatientData patient=null;
        int counter=0;
        try {
            conn = DBUtil.getConnection();
            stm = conn.createStatement();
            rs = stm.executeQuery("select * from LearnDataSet");
            rs.setFetchSize(500);
            while (rs.next()) {
                System.out.println("Generating patient "+ (++counter) );
                data=processResultSet(rs);
                patient=generatePatient(data.getSex(), data.getAge());
                patient.setMedicalData(data);
                updateDB(patient);
            }
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
        DBUtil.getConnection();
    }
    
    public void updateDB(PatientData patient) throws SQLException{
        Connection conn=null;
        PreparedStatement stm=null;
        ResultSet rs=null;
        try{
            PatientController.addPatient(patient);
            conn=DBUtil.getConnection();
            stm=conn.prepareStatement("update LearnDataSet set patient_id=? where id=?");
            stm.setInt(1, patient.getID());
            stm.setInt(2, patient.getMedicalData().getDbID());
            stm.executeUpdate();
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
        
    }

    private String selectRandom(String[] list) {
        return list[rand.nextInt(list.length)];
    }

    public static void main(String[] argv) {
        try {
            new PatientDataGenerator().generate();
        } catch (SQLException ex) {
            System.err.println("Can't generate patient data");
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        rand = new Random(System.currentTimeMillis());
        maleNames = new String[]{
            "Adam", "Adrian", "Adolf", "Albert", "Albin", "Aleksander", "Aleksy",
            "Alfons", "Alfred", "Alojzy", "Ambroży", "Anatol", "Andrzej", "Antoni",
            "Anzelm", "Apolinary", "Aureliusz", "Arkadiusz", "Artur", "Augustyn", "Bartosz",
            "Bogdan", "Bogusław", "Bolesław", "Cezary", "Cyprian", "Czesław", "Damian",
            "Daniel", "Dariusz", "Dawid", "Dionizy", "Dominik", "Edward", "Emil", "Ernest",
            "Eryk", "Feliks", "Filip", "Fryderyk", "Grzegorz", "Gwidon", "Henryk", "Herbert",
            "Ireneusz", "Jacek", "Jakub", "Jan", "Jarek", "Jerzy", "Juliusz", "Kajetan",
            "Kamil", "Kacper", "Karol", "Kazimierz", "Konrad", "Krystian", "Krzysztof",
            "Lech", "Leopold", "Leszek", "Lucjan", "Łukasz", "Maciej", "Maksymilian",
            "Marcin", "Marek", "Mariusz", "Mateusz", "Michał", "Mikołaj", "Mirosław",
            "Norbert", "Olgierd", "Oskar", "Paweł", "Patryk", "Piotr", "Przemysław",
            "Radosław", "Rafał", "Remigiusz", "Robert", "Roman", "Sebastian", "Sławomir",
            "Stanisław", "Stefan", "Sylwek", "Szymon", "Tadeusz", "Tomasz", "Wacław", "Waldemar",
            "Wawrzyniec", "Wiktor", "Wincenty", "Wit", "Witold", "Władysław", "Włodzimierz",
            "Wojciech", "Zbigniew", "Zbyszek", "Zdzisław", "Zenobiusz", "Zenon"
        };

        femaleNames = new String[]{
            "Ada", "Adela", "Agata", "Agnieszka", "Aldona", "Aleksandra", "Alicja", "Alina",
            "Anastazja", "Aneta", "Angelika", "Aniela", "Anita", "Anna", "Antonina", "Apolonia",
            "Balbina", "Barbara", "Beata", "Berenika", "Bernadeta", "Berta", "Blanka", "Bogumiła",
            "Bogusława", "Bożena", "Brygida", "Cecylia", "Celina", "Czesława", "Dagmara", "Daniela",
            "Danuta", "Daria", "Diana", "Dominika", "Dorota", "Edyta", "Elena", "Eleonora", "Eliza",
            "Elwira", "Elżbieta", "Emilia", "Eugenia", "Ewa", "Ewelina", "Felicja", "Franciszka",
            "Gabriela", "Genowefa", "Gertruda", "Grażyna", "Halina", "Helena", "Henryka", "Honorata",
            "Huberta", "Ida", "Iga", "Irena", "Irmina", "Iwona", "Iza", "Izabela", "Jadwiga", "Janina",
            "Joanna", "Jolanta", "Judyta", "Julia", "Justyna", "Kalina", "Kamila", "Karolina",
            "Katarzyna", "Kinga", "Klara", "Klaudia", "Klementyna", "Kornelia", "Krystyna",
            "Laura", "Lidia", "Liliana", "Lucyna", "Ludmiła", "Ludwika", "Łucja", "Magda",
            "Magdalena", "Maja", "Malwina", "Małgorzata", "Marcelina", "Maria", "Marlena",
            "Marta", "Martyna", "Marzena", "Matylda", "Milena", "Monika", "Natalia", "Natasza",
            "Nina", "Olga", "Oliwia", "Otylia", "Patrycja", "Paulina", "Regina", "Renata",
            "Roksana", "Rozalia", "Róża", "Sabina", "Salomea", "Sandra", "Sławomira", "Stefania",
            "Sylwia", "Tekla", "Teresa", "Urszula", "Wacława", "Wanda", "Weronika", "Wiktoria",
            "Wioletta", "Władysława", "Zofia", "Zuzanna", "Żaklina", "Żaneta"
        };

        lastNames = new String[]{
            "Nowak", "Kowalski", "Wiśniewski", "Dąbrowski", "Lewandowski", "Wójcik",
            "Kamiński", "Kowalczyk", "Zieliński", "Szymański", "Woźniak", "Kozłowski",
            "Jankowski", "Wojciechowski", "Kwiatkowski", "Kaczmarek", "Mazur", "Krawczyk",
            "Piotrowski", "Grabowski", "Nowakowski", "Pawłowski", "Michalski", "Nowicki",
            "Adamczyk", "Dudek", "Zając", "Wieczorek", "Jabłoński", "Król", "Majewski",
            "Olszewski", "Jaworski", "Wróbel", "Malinowski", "Pawlak", "Witkowski",
            "Walczak", "Stępień", "Górski", "Rutkowski", "Michalak", "Sikora", "Ostrowski",
            "Baran", "Duda", "Szewczyk", "Tomaszewski", "Pietrzak", "Marciniak", "Wróblewski",
            "Zalewski", "Jakubowski", "Jasiński", "Zawadzki", "Sadowski", "Bąk", "Chmielewski",
            "Włodarczyk", "Borkowski", "Czarnecki", "Sawicki", "Sokołowski", "Urbański",
            "Kubiak", "Maciejewski", "Szczepański", "Kucharski", "Wilk", "Kalinowski",
            "Lis", "Mazurek", "Wysocki", "Adamski", "Kaźmierczak", "Wasilewski",
            "Sobczak", "Czerwiński", "Andrzejewski", "Cieślak", "Głowacki", "Zakrzewski",
            "Kołodziej", "Sikorski", "Krajewski", "Gajewski", "Szymczak", "Szulc",
            "Baranowski", "Laskowski", "Brzeziński", "Makowski", "Ziółkowski",
            "Przybylski", "Domański", "Nowacki", "Borowski", "Błaszczyk", "Chojnacki",
            "Ciesielski", "Mróz", "Szczepaniak", "Wesołowski", "Górecki", "Krupa",
            "Kaczmarczyk", "Leszczyński", "Lipiński", "Kowalewski", "Urbaniak",
            "Kozak", "Kania", "Mikołajczyk", "Czajkowski", "Mucha", "Tomczak",
            "Kozioł", "Markowski", "Kowalik", "Nawrocki", "Brzozowski", "Janik",
            "Musiał", "Wawrzyniak", "Markiewicz", "Orłowski", "Tomczyk", "Jarosz",
            "Kołodziejczyk", "Kurek", "Kopeć", "Żak", "Wolski", "Łuczak", "Dziedzic",
            "Kot", "Stasiak", "Stankiewicz", "Piątek", "Jóźwiak", "Urban", "Dobrowolski",
            "Pawlik", "Kruk", "Domagała", "Piasecki", "Wierzbicki", "Karpiński", "Jastrzębski",
            "Polak", "Zięba", "Janicki", "Wójtowicz", "Stefański", "Sosnowski", "Bednarek",
            "Majchrzak", "Bielecki", "Małecki", "Maj", "Sowa", "Milewski", "Gajda", "Klimek",
            "Olejniczak", "Ratajczak", "Romanowski", "Matuszewski", "Śliwiński", "Madej",
            "Kasprzak", "Wilczyński", "Grzelak", "Socha", "Czajka", "Marek", "Kowal",
            "Bednarczyk", "Skiba", "Wrona", "Owczarek", "Marcinkowski", "Matusiak",
            "Orzechowski", "Sobolewski", "Kędzierski", "Kurowski", "Rogowski", "Olejnik",
            "Dębski", "Barański", "Skowroński", "Mazurkiewicz", "Pająk", "Czech",
            "Janiszewski", "Bednarski", "Łukasik", "Chrzanowski", "Bukowski", "Leśniak",
            "Cieślik", "Kosiński", "Jędrzejewski", "Muszyński", "Świątek", "Kozieł",
            "Osiński", "Czaja", "Lisowski", "Kuczyński", "Żukowski", "Grzybowski",
            "Kwiecień", "Pluta", "Morawski", "Czyż", "Sobczyk", "Augustyniak", "Rybak",
            "Krzemiński", "Marzec", "Konieczny", "Marczak", "Zych", "Michalik", "Michałowski",
            "Kaczor", "Świderski", "Kubicki", "Gołębiowski", "Paluch", "Białek", "Niemiec",
            "Sroka", "Stefaniak", "Cybulski", "Kacprzak", "Marszałek", "Kasprzyk", "Małek",
            "Szydłowski", "Smoliński", "Kujawa", "Lewicki", "Przybysz", "Stachowiak",
            "Popławski", "Piekarski", "Matysiak", "Janowski", "Murawski", "Cichocki",
            "Witek", "Niewiadomski", "Staniszewski", "Bednarz", "Lech", "Rudnicki",
            "Kulesza", "Piątkowski", "Turek", "Chmiel", "Biernacki", "Sowiński",
            "Skrzypczak", "Podgórski", "Cichoń", "Rosiński", "Karczewski", "Żurek",
            "Drozd", "Żurawski", "Pietrzyk", "Komorowski", "Antczak", "Gołębiewski",
            "Góra", "Banach", "Filipiak", "Grochowski", "Krzyżanowski", "Graczyk",
            "Przybyła", "Gruszka", "Banaś", "Klimczak", "Siwek", "Konieczna", "Serafin",
            "Bieniek", "Godlewski", "Rak", "Kulik", "Maćkowiak", "Zawada", "Mikołajczak",
            "Różański", "Cieśla", "Długosz", "Śliwa", "Ptak", "Strzelecki", "Zarzycki",
            "Mielczarek", "Kłos", "Bartkowiak", "Leśniewski", "Krawiec", "Górka", "Janiak",
            "Kaczyński", "Bartczak", "Winiarski", "Tokarski", "Gil", "Panek", "Konopka",
            "Frankowski", "Banasiak", "Grzyb", "Rakowski", "Kuś", "Dudziński", "Zaremba",
            "Skowron", "Fijałkowski", "Dobosz", "Witczak", "Nawrot", "Królikowski",
            "Młynarczyk", "Sienkiewicz", "Frączek", "Słowik", "Frąckowiak", "Czyżewski",
            "Kostrzewa", "Kucharczyk", "Gawroński", "Rybicki", "Pałka", "Biernat", "Różycki",
            "Bogusz", "Rogalski", "Szymczyk", "Janus", "Szczepanik", "Szczygieł", "Buczek",
            "Szostak", "Kaleta", "Kaczorowski", "Żebrowski", "Tkaczyk", "Grzegorczyk",
            "Drzewiecki", "Trojanowski", "Bagiński", "Książek", "Jurek", "Trzciński",
            "Gawron", "Wojtczak", "Rogala", "Kula", "Kubik", "Maliszewski", "Radomski",
            "Dąbek", "Kisiel", "Cebula", "Rosiak", "Zaręba", "Gąsior", "Grzesiak",
            "Gawlik", "Cygan", "Rojek", "Dębowski", "Bogucki", "Więckowski", "Mikulski",
            "Walkowiak", "Malec", "Burzyński", "Flis", "Wąsik", "Czapla", "Drozdowski",
            "Kwaśniewski", "Wójcicki", "Rzepka", "Gałązka", "Łukasiewicz", "Pawelec",
            "Lipski", "Wnuk", "Kołodziejski", "Andrzejczak", "Zaborowski", "Sokół", "Urbańczyk "
        };

        cities = new String[]{
            "Bierutów", "Bogatynia", "Bolesławiec", "Bolków", "Chocianów", "Chojnów", "Dzierżoniów", "Głogów", "Karpacz", "Kłodzko", "Legnica", "Lubań", "Międzylesie", "Oleśnica", "Polkowice", "Syców", "Wałbrzych", "Zgorzelec", "Ziębice", "Złotoryja", "Żmigród",
            "Brodnica", "Bydgoszcz", "Chełmża", "Ciechocinek", "Golub-Dobrzyń", "Grudziądz", "Inowrocław", "Kruszwica", "Mrocza", "Nakło", "Nowe", "Rypin", "Świecie", "Toruń", "Tuchola", "Wąbrzeźno", "Więcbork", "Włocławek", "Żnin",
            "Chełm", "Kraśnik", "Lubartów", "Lublin", "Łuków", "Puławy", "Świdnik", "Zamość",
            "Drezdenko", "Gorzów Wlkp", "Kostrzyn", "Nowa Sól", "Słubice", "Sulęcin", "Zbąszynek", "Żagań",
            "Bełchatów", "Brzeziny", "Głowno", "Kutno", "Łowicz", "Łódź", "Pabianice", "Radomsko", "Sieradz", "Skierniewice", "Wieruszów", "Zelów", "Zgierz",
            "Kraków", "Limanowa", "Myślenice", "Niepołomice", "Olkusz", "Oświęcim", "Skawina", "Tarnów", "Trzebinia", "Wieliczka", "Zakopane",
            "Ciechanów", "Kobyłka", "Kozienice", "Legionowo", "Łaskarzew", "Łosice", "Marki", "Otwock", "Piaseczno", "Piastów", "Płock", "Płońsk", "Pruszków", "Radom", "Serock", "Siedlce", "Sochaczew", "Szydłowiec", "Tłuszcz", "Warszawa",
            "Brzeg", "Kędzierzyn-Koźle", "Kluczbork", "Nysa", "Opole",
            "Dukla", "Jarosław", "Jasło", "Kolbuszowa", "Mielec", "Przemyśl", "Przeworsk", "Rzeszów", "Sanok", "Tarnobrzeg",
            "Augustów", "Kolno", "Łomża", "Sokółka", "Suwałki", "Zambrów",};

        streets = new String[]{
            "Antoniny", "Antonińska", "Aptekarska", "Artyleryjska", "Akacjowa", "Bajkowa",
            "Berwińskich", "Boczna", "Borowikowa", "Borówkowa", "Bracka", "Brzozowa",
            "Budowlanych", "Bułgarska", "Belgijska", "Dąbrówki", "Dębowa", "Dolna",
            "Dożynkowa", "Dworcowa", "Działkowa", "Duńska", "Energetyków", "Estkowskiego",
            "Ewarysta", "Chocimska", "Choinkowa", "Cicha", "Czarnoleska", "Fabryczna",
            "Francuska", "Żniwna", "Żołnierska", "Zachodnia", "Zacisze", "Zagłoby", "Zakątek",
            "Zielna", "Zielona", "Złotnicza", "Zwycięstwa", "Kosmonautów", "Kosynierów", "Kościelna",
            "Kręta", "Krótka", "Krzywa", "Kujawska", "Kurkowa", "Kubańska", "Kwiatowa", "Saperska",
            "Serbska", "Skarbowa", "Skryta", "Słoneczna", "Słowiańska", "Smardzowa", "Snycerska",
            "Sokoła", "Solna", "Sosnowa", "Spacerowa", "Spokojna", "Spółdzielcza", "Starozamkowa",
            "Stawowa", "Strumykowa", "Strzelecka", "Studzienna", "Szeroka", "Szewska", "Szkolna",
            "Szybowników", "Szyszkowa", "Szwedzka"
        };
        
        female=new String[]{"0","2","4","6","8"};
        male=new String[]{"1","3","5","7","9"};
    }
}
