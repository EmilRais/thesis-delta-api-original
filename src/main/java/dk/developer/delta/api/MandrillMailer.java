package dk.developer.delta.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MandrillMailer implements Mailer {
    private final String supportMail;

    public MandrillMailer(String supportMail) {
        this.supportMail = supportMail;
    }

    private Response sendMail(String json) {
        Client client = ClientBuilder.newClient();
        return client.target("https://mandrillapp.com/api/1.0/messages/send-template.json")
                .request()
                .post(Entity.json(json));
    }

    private String loadMail(String fileName) {
        Client client = ClientBuilder.newClient();
        return client.target("http://localhost/mail/" + fileName)
                .request()
                .get(String.class);
    }

    public MailStatus feedbackMail(String message, String userId, String name) {
        String mail = loadMail("Feedback.json")
                .replace("#MESSAGE#", message)
                .replace("#USER_ID#", userId)
                .replace("#TIME#", CurrentTimeTimestamper.DATE_FORMAT.format(new Date()))
                .replace("#EMAIL#", supportMail)
                .replace("#NAME#", name);

        Response response = sendMail(mail);
        return parseResponse(response);
    }

    public MailStatus proposalMail(String message, String userId, String userEmail, String userName,  String name) {
        String mail = loadMail("Proposal.json")
                .replace("#MESSAGE#", message)
                .replace("#USER_NAME#", userName)
                .replace("#USER_EMAIL#", userEmail)
                .replace("#USER_ID#", userId)
                .replace("#TIME#", CurrentTimeTimestamper.DATE_FORMAT.format(new Date()))
                .replace("#EMAIL#", supportMail)
                .replace("#NAME#", name);

        Response response = sendMail(mail);
        return parseResponse(response);
    }

    private MailStatus parseResponse(Response response) {
        List<Object> data = response.readEntity(List.class);
        response.close();
        if ( data.size() < 1 )
            return MailStatus.INVALID;
        System.out.println(data);
        Map<String, Object> result = (Map<String, Object>) data.get(0);

        String status = (String) result.get("status");
        switch ( status ) {
            case "sent": return MailStatus.SENT;
            case "queued": return MailStatus.QUEUED;
            case "rejected": return MailStatus.REJECTED;
            case "invalid": return MailStatus.INVALID;
        }
        return MailStatus.INVALID;
    }
}
