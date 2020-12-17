# Customers Mail Cloud Java SDK

## 共通処理

初期化は次のように行います。

```java
String apiKey = System.getenv("API_KEY");;
String apiUser = System.getenv("API_USER");;
client =  new CustomersMailCloud(apiUser, apiKey);
```

## 契約内容の設定

契約状態によってメソッドが異なります。

- トライアル `client.trial();`
- スタンダード `client.standard();`
- プロ `client.pro(subdomain);`

## メール送信

```java
CustomersMailCloudMail mail = client.getMail();
mail.subject = "Test mail";
mail.text = "Mail body";
CustomersMailCloudMailAddress toAddress = new CustomersMailCloudMailAddress();
toAddress.name = "Example user";
toAddress.address = "example@example.com";
mail.to = new CustomersMailCloudMailAddress[1];
mail.to[0] = toAddress;
CustomersMailCloudMailAddress fromAddress = new CustomersMailCloudMailAddress();
fromAddress.name = "Admin";
fromAddress.address = "info@smtps.jp";
mail.from = fromAddress;
String id = client.send(mail);
System.out.println(id);
```

結果はIDが返ってきます。

```
<f23ec790-403f-11eb-a4d7-9ca3ba311822@mta03.sandbox.smtps.jp>
```

## エラーの場合

エラーの場合は `CustomersMailCloudException` が返ってきます。

```
try {
  // 略
} catch (CustomersMailCloudException e) {
  System.err.println(e.getMessage());
}
```

エラーメッセージが返ってきます。

## LICENSE

MIT.
