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
import org.json.JSONObject;
// Jackson
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class CustomersMailCloudRequest {
  public String send(String url, CustomersMailCloudMail mail) throws CustomersMailCloudException  {
    String result = "";
    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.writeValueAsString(mail);
      StringEntity entity = new StringEntity(json, "UTF-8");
      HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
      httpPost.setEntity(entity);
      CloseableHttpClient client = HttpClients.createDefault();
      CloseableHttpResponse response = client.execute(httpPost);
      // 閉じる
      client.close();
      result = EntityUtils.toString(response.getEntity());
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