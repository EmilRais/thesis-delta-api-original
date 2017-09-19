package dk.developer.delta.api;

public interface Mailer {
    MailStatus feedbackMail(String message, String userId, String name);

    MailStatus proposalMail(String message, String userId, String userEmail, String userName,  String name);

    enum MailStatus {
        SENT, QUEUED, REJECTED, INVALID;
    }
}
