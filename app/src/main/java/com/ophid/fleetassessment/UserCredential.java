package com.ophid.fleetassessment;

public class UserCredential {

    private String employeeNumber="";
     private  String employeeName="";
     private   String email="";
     private  String supervisorName="";
     private String supervisorEmail="";
     private  int driverOrRider=0;
     private String districtName="";
     private boolean supervisor=false;
     private  int districtId=0;
     private  String password="";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public void setSupervisorEmail(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public int getDriverOrRider() {
        return driverOrRider;
    }

    public void setDriverOrRider(int driverOrRider) {
        this.driverOrRider = driverOrRider;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int district) {
        this.districtId = district;
    }

    @Override
    public String toString() {
        return "UserCredential{" +
                "employeeNumber='" + employeeNumber + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", email='" + email + '\'' +
                ", supervisorName='" + supervisorName + '\'' +
                ", supervisorEmail='" + supervisorEmail + '\'' +
                ", driverOrRider=" + driverOrRider +
                ", districtName='" + districtName + '\'' +
                ", supervisor=" + supervisor +
                ", districtId=" + districtId +
                '}';
    }
}
