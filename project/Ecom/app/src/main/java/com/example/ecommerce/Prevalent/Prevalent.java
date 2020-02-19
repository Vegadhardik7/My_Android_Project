package com.example.ecommerce.Prevalent;

import com.example.ecommerce.Model.Users;

public class Prevalent {

    public static Users CurrOnlineUser;



    public static final String UserPhoneKey ="UserPhone";
    public static final String UserPasswordKey ="UserPassword";

    public static Users getCurrOnlineUser() {
        return CurrOnlineUser;
    }

    public static void setCurrOnlineUser(Users currOnlineUser) {
        CurrOnlineUser = currOnlineUser;
    }


}
