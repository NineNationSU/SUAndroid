package com.android.suapp.suapp.server.database.objects;

import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teacher {

    @Expose
    private Integer id;

    @Expose(serialize = false, deserialize = false)
    private String login;

    @Expose(serialize = false, deserialize = false)
    private String password;

    @Expose
    @SerializedName("first_name")
    private String firstName;

    @Expose
    @SerializedName("middle_name")
    private String middleName;

    @Expose
    @SerializedName("last_name")
    private String lastName;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    private String email;

    public Integer getId() {
        return id;
    }

    public Teacher setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Teacher setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Teacher setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Teacher setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Teacher setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Teacher setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Teacher setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Teacher setPassword(String password) {
        this.password = password;
        return this;
    }

    public Teacher setFullName(String fullName){
        if(fullName != null && !fullName.isEmpty()) {
            String[] temp = fullName.split(" ");
            lastName = temp[0];
            firstName = temp[1];
            middleName = temp[2];
        }
        return this;
    }

    public String getFullName(){
        String name = "";
        if(lastName != null){
            name += lastName + " ";
        }

        if (firstName != null){
            name += firstName + " ";
        }
        if (middleName != null){
            name += middleName;
        }
        return name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     *
     * @return строка специального вида для занесения записи в базу данных
     * @throws IllegalObjectStateException если заполнены не все поля
     */
    public String toSQLInsertString() throws IllegalObjectStateException {
        if (login == null || password == null
                ||firstName == null || middleName == null
                || lastName == null ){
            throw new IllegalObjectStateException("Заполнены не все обязательные поля объекта 'Преподаватель'");
        }
        StringBuilder answer = new StringBuilder();
        answer.append("login='").append(login).append('\'');
        answer.append(", password='").append(password).append('\'');
        answer.append(", firstName='").append(firstName).append('\'');
        answer.append(", middleName='").append(middleName).append('\'');
        answer.append(", lastName='").append(lastName).append('\'');
        if (phoneNumber != null) {
            answer.append(", phoneNumber='").append(phoneNumber).append('\'');
        }
        if (email != null){
            answer.append(", email=").append('\'').append(email).append('\'');
        }
        return answer.toString();
    }

    public static Teacher loadFromJson(String jsonString){
        return new Gson().fromJson(jsonString, Teacher.class);
    }
}
