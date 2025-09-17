package customersmailcloud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AttachmentTest {

    private CustomersMailCloudMail mail;

    @BeforeEach
    public void setUp() {
        mail = new CustomersMailCloudMail();
        mail.api_user = "test_user";
        mail.api_key = "test_key";
        mail.subject = "Test Subject";
        mail.text = "Test Body";

        CustomersMailCloudMailAddress from = new CustomersMailCloudMailAddress();
        from.address = "from@example.com";
        from.name = "Sender";
        mail.from = from;

        CustomersMailCloudMailAddress to = new CustomersMailCloudMailAddress();
        to.address = "to@example.com";
        to.name = "Recipient";
        mail.to = new CustomersMailCloudMailAddress[]{to};
    }

    @Test
    public void testAddSingleAttachment() {
        String filePath = "/path/to/file.pdf";
        mail.addAttachment(filePath);

        assertNotNull(mail.attachments);
        assertEquals(1, mail.attachments.size());
        assertEquals(filePath, mail.attachments.get(0));
    }

    @Test
    public void testAddMultipleAttachments() {
        String filePath1 = "/path/to/file1.pdf";
        String filePath2 = "/path/to/file2.docx";
        String filePath3 = "/path/to/file3.jpg";

        mail.addAttachment(filePath1);
        mail.addAttachment(filePath2);
        mail.addAttachment(filePath3);

        assertNotNull(mail.attachments);
        assertEquals(3, mail.attachments.size());
        assertEquals(filePath1, mail.attachments.get(0));
        assertEquals(filePath2, mail.attachments.get(1));
        assertEquals(filePath3, mail.attachments.get(2));
    }

    @Test
    public void testAddNullAttachment() {
        assertThrows(IllegalArgumentException.class, () -> {
            mail.addAttachment(null);
        });
    }

    @Test
    public void testAttachmentsInitializedAsEmptyList() {
        assertNotNull(mail.attachments);
        assertTrue(mail.attachments.isEmpty());
    }

    @Test
    public void testSendWithAttachment() throws CustomersMailCloudException, IOException {
        // Create a temporary test file
        File testFile = File.createTempFile("test", ".txt");
        testFile.deleteOnExit();
        FileWriter writer = new FileWriter(testFile);
        writer.write("Test content");
        writer.close();

        mail.addAttachment(testFile.getAbsolutePath());

        CustomersMailCloudRequest request = new CustomersMailCloudRequest();

        // Note: Actual sending would require a mock or test server
        // This test verifies the structure is correct
        assertNotNull(mail.attachments);
        assertEquals(1, mail.attachments.size());
        assertTrue(new File(mail.attachments.get(0)).exists());
    }

    @Test
    public void testSendWithNonExistentAttachment() {
        mail.addAttachment("/non/existent/file.pdf");

        CustomersMailCloudRequest request = new CustomersMailCloudRequest();
        // This should throw an exception when trying to send
        assertThrows(CustomersMailCloudException.class, () -> {
            request.send("http://test.example.com/api/send", mail);
        });
    }
}