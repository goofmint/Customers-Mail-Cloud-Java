package customersmailcloud;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

public class AttachmentTest {
  static CustomersMailCloud client;

  @BeforeAll
  public static void setup() {
    String apiKey = System.getenv("API_KEY");
    String apiUser = System.getenv("API_USER");
    System.out.println("API_USER: " + apiUser);
    System.out.println("API_KEY: " + apiKey);

    // If environment variables are not set, use test values
    if (apiUser == null || apiUser.isEmpty()) {
      apiUser = "test_user";
    }
    if (apiKey == null || apiKey.isEmpty()) {
      apiKey = "test_key";
    }

    client = new CustomersMailCloud(apiUser, apiKey);
    client.trial();
  }

  @Test
  public void testSendWithAttachments() {
    try {
      // Use existing files from project root
      File testPng = new File("test.png");
      File readmeMd = new File("README.md");

      // Verify files exist
      assertTrue(testPng.exists(), "test.png should exist in project root");
      assertTrue(readmeMd.exists(), "README.md should exist in project root");

      CustomersMailCloudMail mail = client.getMail();
      mail.subject = "Test mail with attachments";
      mail.text = "Mail body with attachments";

      CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
      toAddress.name = "Atsushi";
      toAddress.address = "atsushi@moongift.jp";
      mail.to = new CustomersMailCloudMailAddress[1];
      mail.to[0] = toAddress;

      CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
      fromAddress.name = "Admin";
      fromAddress.address = "info@dxlabo.com";
      mail.from = fromAddress;

      // Add attachments
      mail.addAttachment(testPng.getAbsolutePath());
      mail.addAttachment(readmeMd.getAbsolutePath());

      // Verify attachments were added
      assertNotNull(mail.attachments);
      assertEquals(2, mail.attachments.size());
      assertEquals(testPng.getAbsolutePath(), mail.attachments.get(0));
      assertEquals(readmeMd.getAbsolutePath(), mail.attachments.get(1));

      // Send mail
      String id = client.send(mail);
      System.out.println("Mail sent with ID: " + id);

    } catch (CustomersMailCloudException e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  public void testAddSingleAttachment() {
    CustomersMailCloudMail mail = client.getMail();
    mail.subject = "Test mail";
    mail.text = "Mail body";

    CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
    toAddress.name = "Test";
    toAddress.address = "test@example.com";
    mail.to = new CustomersMailCloudMailAddress[1];
    mail.to[0] = toAddress;

    CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
    fromAddress.name = "Sender";
    fromAddress.address = "sender@example.com";
    mail.from = fromAddress;

    // Add single attachment using test.png
    File testPng = new File("test.png");
    assertTrue(testPng.exists(), "test.png should exist in project root");

    mail.addAttachment(testPng.getAbsolutePath());

    assertNotNull(mail.attachments);
    assertEquals(1, mail.attachments.size());
    assertEquals(testPng.getAbsolutePath(), mail.attachments.get(0));
  }

  @Test
  public void testAddMultipleAttachments() {
    CustomersMailCloudMail mail = client.getMail();
    mail.subject = "Test mail";
    mail.text = "Mail body";

    CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
    toAddress.name = "Test";
    toAddress.address = "test@example.com";
    mail.to = new CustomersMailCloudMailAddress[1];
    mail.to[0] = toAddress;

    CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
    fromAddress.name = "Sender";
    fromAddress.address = "sender@example.com";
    mail.from = fromAddress;

    // Add multiple attachments using real files
    File testPng = new File("test.png");
    File readmeMd = new File("README.md");

    assertTrue(testPng.exists(), "test.png should exist in project root");
    assertTrue(readmeMd.exists(), "README.md should exist in project root");

    mail.addAttachment(testPng.getAbsolutePath());
    mail.addAttachment(readmeMd.getAbsolutePath());

    assertNotNull(mail.attachments);
    assertEquals(2, mail.attachments.size());
    assertEquals(testPng.getAbsolutePath(), mail.attachments.get(0));
    assertEquals(readmeMd.getAbsolutePath(), mail.attachments.get(1));
  }

  @Test
  public void testAddNullAttachment() {
    CustomersMailCloudMail mail = client.getMail();
    mail.subject = "Test mail";
    mail.text = "Mail body";

    CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
    toAddress.name = "Test";
    toAddress.address = "test@example.com";
    mail.to = new CustomersMailCloudMailAddress[1];
    mail.to[0] = toAddress;

    CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
    fromAddress.name = "Sender";
    fromAddress.address = "sender@example.com";
    mail.from = fromAddress;

    // Try to add null attachment
    assertThrows(IllegalArgumentException.class, () -> {
      mail.addAttachment(null);
    });
  }

  @Test
  public void testAttachmentsInitializedAsEmptyList() {
    CustomersMailCloudMail mail = client.getMail();

    // Check attachments list is initialized
    assertNotNull(mail.attachments);
    assertTrue(mail.attachments.isEmpty());
  }
}