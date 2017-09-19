package dk.developer.delta.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;

public class MandrillMailerTest {
    MandrillMailer mailer;

    @BeforeClass
    public void setUp() throws Exception {
        mailer = new MandrillMailer("thomas@mail.com");
    }

    @Test(enabled = false)
    public void shouldSendFeedbackEmail() throws Exception {
        Mailer.MailStatus status = mailer.feedbackMail("Dette er en support besked", "12345", "Thomas Moore");
        ASSERT.that(status).isEqualTo(Mailer.MailStatus.SENT);
    }

    @Test(enabled = false)
    public void shouldSendProposalEmail() throws Exception {
        Mailer.MailStatus status = mailer.proposalMail("Dette er et forslag", "1234", "test@mail.com", "Thomas Moore", "Company");
        ASSERT.that(status).isEqualTo(Mailer.MailStatus.SENT);
    }
}
