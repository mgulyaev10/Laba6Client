package network;

import main.Main;

import java.io.Serializable;

public class User implements Serializable {
    private String password;
    private String login;
    private String email;

    public User(String login, String password, String email){
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    private User(){}

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ПОльзователь " + login;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!login.equals(other.login))
            return false;
        return password.equals(other.password);
    }


}
