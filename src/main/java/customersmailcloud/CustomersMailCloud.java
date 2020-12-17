package customersmailcloud;

class CustomersMailCloud {
  private String API_USER;
  private String API_KEY;
  private String URLs[] = {
    "https://sandbox.smtps.jp/api/v2/emails/send.json",
    "https://te.smtps.jp/api/v2/emails/send.json",
    "https://SUBDOMAIN.smtps.jp/api/v2/emails/send.json"
  };
  private String URL;

  public CustomersMailCloud(String apiUser, String apiKey) {
    API_USER = apiUser;
    API_KEY = apiKey;
  }

  public String apiKey() {
    return API_KEY;
  }
  
  public String apiUser() {
    return API_USER;
  }

  public void trial() {
    URL = URLs[0];
  }

  public void standard() {
    URL = URLs[1];
  }

  public void pro(String subdomain) {
    URL = URLs[2].replace("SUBDOMAIN", subdomain);
  }

  public CustomersMailCloudMail getMail() {
    CustomersMailCloudMail mail = new CustomersMailCloudMail();
    mail.api_user = API_USER;
    mail.api_key = API_KEY;
    return mail;
  }

  public String send(CustomersMailCloudMail mail) throws CustomersMailCloudException {
    CustomersMailCloudRequest request = new CustomersMailCloudRequest();
    try {
      return request.send(URL, mail);
    } catch (CustomersMailCloudException e) {
      throw e;
    }
  }
}

