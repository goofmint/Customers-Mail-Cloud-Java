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

public class SendTest {
  static CustomersMailCloud client;
  
  @BeforeAll
  public static void setup() {
    String apiKey = System.getenv("API_KEY");;
    String apiUser = System.getenv("API_USER");;
    client =  new CustomersMailCloud(apiUser, apiKey);
    client.trial();
  }
  
  @Test
  public void testSend() {
    try {
      CustomersMailCloudMail mail = client.getMail();
      mail.subject = "Test mail";
      mail.text = "Mail body";
      CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
      toAddress.name = "Atsushi";
      toAddress.address = "atsushi@moongift.jp";
      mail.to = new CustomersMailCloudMailAddress[1];
      mail.to[0] = toAddress;
      CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
      fromAddress.name = "Admin";
      fromAddress.address = "info@dxlabo.com";
      mail.from = fromAddress;
      String id = client.send(mail);
      System.out.println(id);
    } catch (CustomersMailCloudException e) {
      System.err.println(e.getMessage());
    }
  }
}