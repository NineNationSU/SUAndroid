package com.android.suapp.suapp.server.database.objects;

import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Message {
    @Expose
    @SerializedName("message_id")
    private Integer messageId;

    @Expose
    @SerializedName("sender_id")
    private Integer senderId;

    @Expose(serialize = false, deserialize = false)
    private Integer idInSenderList;

    @Expose
    @SerializedName("read_state")
    private Integer readState;

    @Expose
    @SerializedName("sender_name")
    private String senderName;

    @Expose
    private Integer out;

    @Expose
    private String body;


    // TODO заменить на Integer
    @Expose
    private List<Student> recipients;

    @Expose
    private Long time;

    public Message(){}

    public Message(ResultSet set) throws SQLException {
        messageId = set.getInt("id");
        senderId = set.getInt("sender_id");

        readState = set.getInt("read_state");
        out = set.getInt("out");
        if (out == 0){
            idInSenderList = set.getInt("id_in_sender_list");
        }else {
            body = set.getString("body");
            String[] studentsId = set.getString("recipients").split(" ");
            recipients = new ArrayList<>();
            for (String _id_ : studentsId) {
                recipients.add(new Student().setId(Integer.parseInt(_id_)));
            }
        }
        time = set.getLong("time");
    }

    public Integer getMessageId() {
        return messageId;
    }

    public Message setMessageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Message setSenderId(Integer senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getSenderName() {
        return senderName;
    }

    public Message setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public Integer getIdInSenderList() {
        return idInSenderList;
    }

    public Message setIdInSenderList(Integer idInSenderList) {
        this.idInSenderList = idInSenderList;
        return this;
    }

    public Integer getReadState() {
        return readState;
    }

    public Message setReadState(Integer readState) {
        this.readState = readState;
        return this;
    }

    public Integer getOut() {
        return out;
    }

    public Message setOut(Integer out) {
        this.out = out;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }

    public List<Student> getRecipients() {
        return recipients;
    }

    public Message setRecipients(List<Student> recipients) {
        this.recipients = recipients;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public Message setTime(Long time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        System.out.println(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this));
        return  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    public String toSQLOutputMessage() throws IllegalObjectStateException {
        if (senderId == null || body == null
                || recipients == null || recipients.isEmpty()
                ||time == null){
            throw new IllegalObjectStateException("Заполнены не все поля отправляемого сообщения");
        }
        StringBuilder b = new StringBuilder();
        b.append("sender_id = ").append(senderId).append(',');
        b.append("read_state = 1, `out` = 1, body = '");
        b.append(body).append("', recipients = '");
        for (int i = 0; i < recipients.size(); i++) {
            b.append(recipients.get(i).getId());
            if (i != recipients.size() - 1)
                b.append(" ");
        }
        b.append("', time = ").append(time);
        // TODO
        System.out.println(b.toString());
        return b.toString();
    }

    public String toSQLReceivedMessage() throws IllegalObjectStateException {
        if (senderId == null || idInSenderList == null
                || time == null){
            throw new IllegalObjectStateException("Заполнены не все поля отправляемого сообщения");
        }
        StringBuilder b = new StringBuilder();
        b.append("sender_id = ").append(senderId).append(", ");
        b.append("id_in_sender_list = ").append(idInSenderList).append(", ");
        b.append("read_state = 0, `out` = 0, ");
        b.append("time = ").append(time);
        return b.toString();
    }
}
