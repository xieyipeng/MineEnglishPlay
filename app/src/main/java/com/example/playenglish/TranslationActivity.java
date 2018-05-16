package com.example.playenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.playenglish.bean.Translate;
import com.google.gson.Gson;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import static com.baidu.ocr.sdk.utils.Util.md5;

/**
 * 百度api
 */
public class TranslationActivity extends AppCompatActivity {

    TextView resultTranslationTextView;
    TextView resultTranslation2TextView;
    Button startTranslationButton;
    public static final String APP_ID = "20180515000159566";
    public static final String PASSWORD = "gijguhKtgTWcaFmet5Ak";
    public static final String TAG = "TranslationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        initView();
        initClick();
    }

    private void initClick() {
        startTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            //文本不能有中文字符
                            String q = "I have a very good friend, and we knew each other when I was in my hometown. Now as I move to another city, we don't see each other often, but we keep in touch with each other by writing letters. Though computer provides people a very convenient way to communicate, we like the original way, because we want to share every happy moment. When writing down these things, we can recall of the happy moment again. What's more, writing a letter is much sincere, which is the important reason why we can keep our friendship. I cherish our friendship so much and hope it can last forever.";
//                            String q = "how are you";
                            String from = "en";
                            String to = "zh";
                            int salt = 1435660288;


                            String sign = APP_ID + q + salt + PASSWORD;

//                            sign = getUTF8XMLString(sign);
//                            Log.d(TAG, "run: 1"+sign);
//                            sign=sign.replace("+","%20");

                            String overSign = md5(sign);
                            Log.d(TAG, "run: 2" + sign);
                            Log.d(TAG, "run: overSign:" + overSign);
                            //377e1210478feaf8381388b55f8d074a


                            String UrlString = "http://api.fanyi.baidu.com/api/trans/vip/translate?" + "q=" + q + "&from=" + from + "&to=" + to + "&appid=" + APP_ID + "&salt=" + salt + "&sign=" + overSign;
                            Log.d(TAG, "run: UrlString: before: " + UrlString);

//                            http://api.fanyi.baidu.com/api/trans/vip/translate?q=how%20are%20you
// &from=en&to=zh&appid=20180515000159566&salt=1435660288&sign=28ff87bafaf96b786645c80beb4412c7
//                            http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple
// &from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4

//                          UrlString: http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=en&to=zh
//                          &appid=20180515000159566&salt=1435660288&sign=c790e68107fb6d2e48a05e4f143ed58d
                            UrlString = UrlString.replace(" ", "%20");

                            Log.d(TAG, "run: UrlString: after: " + UrlString);

                            URL url = new URL(UrlString);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream inputStream = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            showResponse(response.toString());
                        } catch (MalformedURLException e) {
                            Log.e(TAG, "run: MalformedURLException:", e);
                        } catch (IOException e) {
                            Log.e(TAG, "run: IOException:", e);
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "run: IOException", e);
                                }
                            }
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private void initView() {
        resultTranslationTextView = findViewById(R.id.result_translate_TextView);
        resultTranslation2TextView = findViewById(R.id.result_translate2_TextView);
        startTranslationButton = findViewById(R.id.start_translate_Button);
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                resultTranslationTextView.setText(response);
                Log.d(TAG, "run: response" + response);
                try {
                    Translate translate = new Translate();
                    //解析json
                    JSONObject jsonObject1 = new JSONObject(response);
                    String result1 = jsonObject1.getString("trans_result");
                    Log.d(TAG, "run: result" + result1);
                    //去掉
                    result1 = result1.replace("[", "");
                    result1 = result1.replace("]", "");
                    JSONObject jsonObject2 = new JSONObject(result1);
                    //设置文本
                    translate.setSrc(jsonObject2.getString("src"));
                    translate.setDst(jsonObject2.getString("dst"));
                    resultTranslationTextView.setText(translate.getSrc());
                    resultTranslation2TextView.setText(translate.getDst());
                } catch (JSONException e) {
                    Log.e(TAG, "run: JSONException", e);
                }
            }
        });
    }

    public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
            byte[] bs = str.getBytes();
            Log.d(TAG, "run: byte: " + Arrays.toString(bs));

            return new String(bs, newCharset);    //用新的字符编码生成字符串
        }
        return null;
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }
}
