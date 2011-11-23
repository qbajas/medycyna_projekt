package heartdoctor.DataModel;

/**
 * Klasa przechowująca dane użytkownika
 * @author michal
 */
public class User {

    private String login;
    private String password;
    private String name;
    private String lastName;
    private String role;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
//    ROLES
    private static final String DOCTOR_STRING = "doctor";
    private static final String ADMIN_STRING = "admin";

    /**
     * Sprawdza czy użytkownik jest lekarzem
     * @return true jeśli użytkownik jest lekarzem
     */
    public boolean isDoctor() {
        return getRole().equals(DOCTOR_STRING);
    }

    /**
     * Sprawdza czy użytkownik jest administratorem
     * @return true jeśli użytkownik jest administratorem
     */
    public boolean isAdmin() {
        return getRole().equals(ADMIN_STRING);
    }
}
