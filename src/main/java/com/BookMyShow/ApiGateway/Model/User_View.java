package com.BookMyShow.ApiGateway.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Immutable
@Table(name = "user_model")
public class User_View {
    @Id
    private int userid ;
    private String emailid;
    private String phone_number;
    private String password;

    public String getEmailid() {
        return emailid;
    }

    public String getPhone_number() {
        return phone_number;
    }


}