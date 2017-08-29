package example.com.veehan.birthdayreminder.Domain;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 * Birthday class
 */

public class Birthday {
    // Initialization
    private long id;
    private String name;
    private String birthday;
    private String birthmonth;
    private String birthyear;
    private String email;
    private String note;

    // Constructor
    public Birthday(){}

    public Birthday(String name, String birthday, String birthmonth, String birthyear, String
            email, String note){
        this.name = name;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.birthyear = birthyear;
        this.email = email;
        this.note = note;
    }

    public Birthday(long id, String name, String birthday, String birthmonth, String birthyear,
                    String email, String note){
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.birthyear = birthyear;
        this.email = email;
        this.note = note;
    }

    // Get functions
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getBirthMonth() {
        return birthmonth;
    }
    public String getBirthYear() {
        return birthyear;
    }
    public String getEmail() {
        return email;
    }
    public String getNote() {
        return note;
    }

    // Set function
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
