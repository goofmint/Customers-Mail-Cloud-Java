package customersmailcloud;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;

public class CustomersMailCloudMail {
  public String api_user;
  public String api_key;
  public String subject;
  public String text;
  public String html;
  @JsonProperty("envfrom")
  public String env_from;
  @JsonProperty("replyto")
  public CustomersMailCloudMailAddress reply_to;
  public String charset = "UTF-8";
  public List<Map<String, String>> headers = new ArrayList<>();
  public CustomersMailCloudMailAddress from;
  public CustomersMailCloudMailAddress[] to;

  // Add headers
  public void addHeader(String key, String value) {
    if (key == null || value == null) {
      throw new IllegalArgumentException("Header key and value cannot be null");
    }
    Map<String, String> entry = new HashMap<>();
    entry.put("name", key);
    entry.put("value", value);
    headers.add(entry);
  }
}