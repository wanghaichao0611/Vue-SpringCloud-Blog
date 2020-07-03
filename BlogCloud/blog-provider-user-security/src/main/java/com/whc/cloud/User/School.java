package com.whc.cloud.User;


import org.springframework.stereotype.Repository;

@Repository
public class School {
    private String realname;
    private String schoolnumber;
    private String school;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSchoolnumber() {
        return schoolnumber;
    }

    public void setSchoolnumber(String schoolnumber) {
        this.schoolnumber = schoolnumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String shool) {
        this.school = shool;
    }
}
