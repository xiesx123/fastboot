# 数据签名

xxx

## 注解

- `@GoEnableSigner`
- `@GoSigner`

## 参数

:::tip `@GoSigner`

- ignore：是否忽略，默认关闭 false
  :::

## 配置

```yml
fastboot:               # fastboot
  signer:               # ======= 数据签名
    header: sign        # 签名键，默认：sign
    secret: fastboot!@# # 加密串，默认：fastboot!@#
```

## 签名

```java
public static void main(String[] args) {
    Map<String, Object> params = Maps.newConcurrentMap();
    params.put("p1", 1);
    params.put("p2", "2");
    params.put("p3", Lists.newArrayList("31", "32"));
    params.put("p4", new int[] {41, 42});
    String sign = SignerHelper.getSignature(params, "fastboot!@#");
    System.out.println(sign);
}
```

```
4302166d85eedf4139155991d2d183da
```

## 示例

```java
@GoSigner
@GetMapping("sign")
public Result sign(String p1, String p2) {
    return R.succ(Lists.newArrayList(p1, p2));
}
```
- bash curl
```bash
curl --request GET \
  --url 'http://localhost:8080/signer/sign?p1=1&p2=2&p3%5B0%5D=31&p3%5B1%5D=32&p4%5B0%5D=41&p4%5B1%5D=42' \
  --header 'sign: 4302166d85eedf4139155991d2d183da'
```
- java OkHttp
```java
OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder()
  .url("http://localhost:8080/signer/sign?p1=1&p2=2&p3%5B0%5D=31&p3%5B1%5D=32&p4%5B0%5D=41&p4%5B1%5D=42")
  .get()
  .addHeader("sign", "4302166d85eedf4139155991d2d183da")
  .build();
Response response = client.newCall(request).execute();
```
- python requests
```python
import requests
url = "http://localhost:8080/signer/sign"
querystring = {"p1":"1","p2":"2","p3":["31","32"],"p4":["41","42"]}
headers = {"sign": "4302166d85eedf4139155991d2d183da"}
response = requests.get(url, headers=headers, params=querystring)
print(response.json())
```

### 不传

```json
{
  "code": 6000,
  "msg": "Signature Error: 非法请求",
  "status": false
}
```

### 传错

```json
{
  "code": 6000,
  "msg": "Signature Error: 验签失败",
  "status": false
}
```

### 正确

```json
{
  "code": 0,
  "data": ["1", "2"],
  "msg": "success",
  "status": true
}
```
