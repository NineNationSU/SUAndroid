package com.android.suapp.suapp.server.database.objects;

import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.android.suapp.suapp.server.utility.MD5Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Данный класс описывает запись в таблице suappdatabase_test.students
 */
public class Student {

    @Expose
    private Integer id;

    @Expose(serialize = false, deserialize = false)
    private String login;

    @Expose(serialize = false, deserialize = false)
    private String password;

    @Expose(serialize = false, deserialize = false)
    private String token;

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
    private String birthday;

    @Expose
    private String gender;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    private StudyGroup group;

    @Expose
    @SerializedName("group_president")
    private Integer groupPresident;

    @Expose
    @SerializedName("group_proforg")
    private Integer groupProforg;

    @Expose
    @SerializedName("group_manager")
    private Integer groupManager;

    public Student(){}

    public Student(ResultSet set) throws SQLException {
        birthday = set.getDate("birthday").toString();
        firstName = set.getString("first_name");
        gender = set.getString("gender");
        group = new StudyGroup().setNumber(set.getString("group"));
        groupManager = set.getInt("group_manager");
        groupProforg = set.getInt("group_proforg");
        groupPresident = set.getInt("group_president");
        id = set.getInt("id");
        lastName = set.getString("last_name");
        login = set.getString("login");
        middleName = set.getString("middle_name");
        password = set.getString("password");
        phoneNumber = set.getString("phone_number");
        token = set.getString("token");
    }


    public String getToken() {
        return token;
    }

    public Student setToken(String token) {
        this.token = token;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Student setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Student setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Student setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Student setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Student setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Student setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     *
     * @return номер группы
     */
    public String getGroup() {
        return group.getNumber();
    }

    public Student setGroup(StudyGroup group) {
        this.group = group;
        return this;
    }

    public Boolean isGroupPresident() {
        return groupPresident != 0;
    }

    public Student setGroupPresident(Boolean groupPresident) {
        this.groupPresident = groupPresident ? 1 : 0;
        return this;
    }

    public Boolean isGroupProforg() {
        return groupProforg != 0;
    }

    public Student setGroupProforg(Boolean groupProforg) {
        this.groupProforg = groupProforg ? 1 : 0;
        return this;
    }

    public Boolean isGroupManager() {
        return groupManager != 0;
    }

    public Student setGroupManager(Boolean groupManager) {
        this.groupManager = groupManager ? 1 : 0;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    /**
     *
     * @return строка специального вида для занесения записи в базу данных
     * @throws IllegalObjectStateException Ошибка в заполнении полей
     */
    public String toSQLInsertString() throws IllegalObjectStateException, NoSuchAlgorithmException {
        if (login == null || password == null
                || firstName == null || middleName == null
                || lastName == null || birthday == null
                || gender == null || group == null){
            throw new IllegalObjectStateException("Заполнены не все обязательные поля объекта 'Студент'");
        }
        if (login.equals("") || password.equals("")
                || firstName.equals("") || middleName.equals("")
                || lastName.equals("") || birthday.equals("")
                || gender.equals("") || group.getNumber().equals("")){
            throw new IllegalObjectStateException("Заполнены не все обязательные поля объекта 'Студент'");
        }
        StringBuilder answer = new StringBuilder();
        answer.append("login='").append(login).append('\'');
        answer.append(", token='").append(MD5Utility.getMD5(login+password)).append('\'');
        answer.append(", password='").append(password).append('\'');
        answer.append(", first_name='").append(firstName).append('\'');
        answer.append(", middle_name='").append(middleName).append('\'');
        answer.append(", last_name='").append(lastName).append('\'');
        answer.append(", birthday='").append(birthday).append('\'');
        answer.append(", gender='").append(gender).append('\'');
        if (phoneNumber != null) {
            answer.append(", phone_number='").append(phoneNumber).append('\'');
        }
        answer.append(", `group`='").append(group.getNumber()).append('\'');
        if (groupPresident != null){
            answer.append(", group_president=").append(groupPresident);
        }
        if (groupProforg != null){
            answer.append(", group_proforg=").append(groupProforg);
        }
        if (groupManager != null){
            answer.append(", group_manager=").append(groupManager);
        }
        return answer.toString();
    }

    public static Student loadFromJson(String jsonString){
        return new Gson().fromJson(jsonString, Student.class);
    }
}