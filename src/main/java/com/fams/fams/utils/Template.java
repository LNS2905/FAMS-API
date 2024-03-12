package com.fams.fams.utils;

import com.fams.fams.models.entities.ReservedClass;
import com.fams.fams.models.entities.User;

public class Template {
    public String afterReservation(String receiver, String trainer, String phone) {
        return "<p>Hello " + receiver + ",</p>"
                + "<p>Good day to you! I am " + trainer+ ", the Admin of Fresher Academy"
                + "</p>"
                + "<p>We are delighted that you have decided to retain your enrollment at Fresher Academy to maintain your knowledge and skills. Currently, I would like to remind you that your retention period will end in about a month.</p>"
                + "<br>"
                + "<p>To help you easily return and continue your learning journey, we suggest that you contact us before your retention period expires. This way, we can assist you in the class placement process and ensure that you will have the best learning experience at Fresher Academy.</p>"
                +"<p>Please reach out to us via the phone number:" + phone +". We will be happy to support you with any inquiries or requests you may have.</p>"
                +"<p>Thank you sincerely for your interest and commitment to the program at Fresher Academy. We look forward to welcoming you back and sharing new knowledge with you.</p>"
                +"<h3>Best regards,</h3>"
                +"<p>" + trainer +"</p>"
                +"<p>Fresher Academy Admin</p>";
    }

    public String informReservationSuccessful(ReservedClass reservedClass, User user, String email) {
        return "<h2>Dear " + reservedClass.getStudents().getFullName() + ",</h2>"
                +"<p>We are thrilled to inform you that your reservation at our study center has been successfully confirmed!</p>"
                +"<p>Your reservation details are as follows:</p>"
                +"</br>"
                +"<p>Reservation ID: "+ reservedClass.getReservedclassid() +"</p>"
                +"<p>Full Name: " + reservedClass.getStudents().getFullName() + "</p>"
                +"<p>Student Code: " + reservedClass.getStudents().getStudentCode() + "</p>"
                +"<p>StartDate: " + reservedClass.getStartDate() + "</p>"
                +"<p>EndDate: " + reservedClass.getEndDate() + "</p>"
                +"<p>Reason: " + reservedClass.getReason() + "</p>"
                + "</br>"
                +"<p>Your reservation has been secured as requested is reserved exclusively for you during the specified time slot.</p>"
                +"<p>Should you have any questions or require further assistance, please feel free to reach out to us at " + email + ".</p>"
                + "</br>"
                +"<h3>Best regards,</h3>"
                +"<p>" + user.getFullName() + "</p>"
                +"<p>Fresher Academy Admin</p>";
    }

    public String generalTemplate(String receiver, String sender, String[] description) {
        StringBuilder myOriginalString = new StringBuilder();
        for(String tmp: description){
            myOriginalString.append("<p>" + tmp + "</p>");
        }
        return "<h2>Dear " + receiver + ",</h2>"
                +"</br>"
                +"</br>"
                +myOriginalString
                +"<h3>Best regards,</h3>"
                +"<p>" + sender + "</p>"
                +"<p>Fresher Academy Admin</p>";
    }

}
