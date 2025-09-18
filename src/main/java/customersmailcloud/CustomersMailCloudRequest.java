package customersmailcloud;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONObject;
// Jackson
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomersMailCloudRequest {
  public String send(String url, CustomersMailCloudMail mail) throws CustomersMailCloudException  {
    String result = "";
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    HttpPost httpPost = new HttpPost(url);

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      // Check if we have attachments
      if (mail.attachments != null && !mail.attachments.isEmpty()) {
        // Use multipart for attachments
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // Add individual form fields
        builder.addTextBody("api_user", mail.api_user);
        builder.addTextBody("api_key", mail.api_key);
        builder.addTextBody("subject", mail.subject);
        builder.addTextBody("text", mail.text);
        builder.addTextBody("from", mapper.writeValueAsString(mail.from));
        builder.addTextBody("to", mapper.writeValueAsString(mail.to));
        if (mail.charset != null) builder.addTextBody("charset", mail.charset);
        if (mail.reply_to != null) builder.addTextBody("reply_to", mapper.writeValueAsString(mail.reply_to));
        if (mail.env_from != null) builder.addTextBody("env_from", mail.env_from);
        if (mail.headers != null) builder.addTextBody("headers", mapper.writeValueAsString(mail.headers));

        // Add attachments count
        builder.addTextBody("attachments", String.valueOf(mail.attachments.size()));

        // Add attachments with correct field names (attachment1, attachment2, etc.)
        int attachmentIndex = 1;
        for (String filePath : mail.attachments) {
          File file = new File(filePath);
          if (!file.exists() || !file.isFile()) {
            throw new CustomersMailCloudException("Attachment file not found: " + filePath);
          }
          builder.addBinaryBody(
            "attachment" + attachmentIndex,
            file,
            ContentType.APPLICATION_OCTET_STREAM,
            file.getName()
          );
          attachmentIndex++;
        }

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
      } else {
        // Use JSON for no attachments
        String json = mapper.writeValueAsString(mail);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
        httpPost.setEntity(entity);
      }

      try (CloseableHttpResponse response = client.execute(httpPost)) {
        result = EntityUtils.toString(response.getEntity());
      }
    } catch (IOException e) {
      System.err.println(e);
      throw new CustomersMailCloudException("ネットワークエラー");
    }

    try {
      Map<String, String> map = new HashMap<String, String>();
      HashMap<String, String> res = mapper.readValue(result, new TypeReference<Map<String, String>>(){});
      return res.get("id");
    } catch (IOException e) {
    }
    try {
      TypeReference<Map<String, List<Map<String, String>>>> type = new TypeReference<Map<String, List<Map<String, String>>>>(){};
      Map<String, List<Map<String, String>>> list = mapper.readValue(result, type);
      throw new CustomersMailCloudException(list.get("errors"));
    } catch (IOException e) {
      System.err.println(e);
      throw new CustomersMailCloudException("ネットワークエラー");
    }
    /*
    } catch (JsonProcessingException e) {
      throw new CustomersMailCloudException("JSON解析エラー");
    }
    */
  }
}