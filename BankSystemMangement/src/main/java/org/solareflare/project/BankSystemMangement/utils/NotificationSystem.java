package org.solareflare.project.BankSystemMangement.utils;




public interface NotificationSystem {

        void sendEmail(String to, String subject, String body);
        void sendSms(String to, String message);

}
