package android.ucam.edu.seiries.beans;


public class User {

    private String nick;
    private String pass;
    private String name;
    private String surname;

    public User(String nick, String pass){
        this.nick=nick;
        this.pass=pass;
    }

    public User (String nick, String pass, String name, String surname){
        this.nick=nick;
        this.pass=pass;
        this.name=name;
        this.surname=surname;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNick() {

        return nick;
    }
}
