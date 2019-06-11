package network;

import database.DBConst;
import database.Queryable;
import security.StringHasher;

import java.io.Serializable;

public class User implements Serializable, Queryable {
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

    public String getHashedPassword() {
        String hash = StringHasher.getHashedString(password, StringHasher.Algorithms.MD2);
        return hash;
    }

    public String getPassword(){
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
        return "Пользователь " + login;
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

    @Override
    public int hashCode() {
        int prime = 5;
        int result = 18;
        result = result * prime + login.hashCode() * (int) Math.pow(prime,2);
        result = result * prime + password.hashCode() * (int) Math.pow(prime,3);
        result = result * prime + email.hashCode() * (int) Math.pow(prime,4);
        return result;
    }

    @Override
    public String getInsertSqlQuery() {
        return "INSERT INTO "+DBConst.USERS_TABLE+" VALUES("+hashCode()+",'"+getLogin()+"','"+getHashedPassword()+"','"+getEmail()+"');";
    }

    @Override
    public String getDeleteSqlQuery() {
        return "DELETE FROM "+DBConst.USERS_TABLE+" WHERE "+DBConst.TABLES_ID+"="+hashCode()+";";
    }
}
