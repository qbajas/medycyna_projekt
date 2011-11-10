/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.DataModel;

/**
 *
 * @author michal
 */
public class User {
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String role;
    
    public User(){}
    
    public User(String login,String password){
        this.login=login;
        this.password= password;
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
    
}
