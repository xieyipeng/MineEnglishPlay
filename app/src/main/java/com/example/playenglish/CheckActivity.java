package com.example.playenglish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playenglish.bean.Player;
import com.example.playenglish.bean.Word;
import com.youdao.sdk.app.YouDaoApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckActivity extends AppCompatActivity {
    /**
     * 爱词霸
     */

    private static final String TAG = "CheckActivity";
    private String getResultUrl = null;
    private Word word = new Word();
    private String resultUrl = null;
    private Player player=new Player();
//    private String wordDictPron = null;

    private Button checkBackButton;
    private Button checkSearchButton;
    private EditText editWord;
    //    private SearchView searchView;
    private ImageView dictPronImageView;
    private TextView tv_word;
    private TextView tv_phonetic;
    private TextView tv_trans;
    private TextView tv_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        YouDaoApplication.init(this, "0daee4533f3fc7d6");

        init();
        inClick();
//        searchView.setOnQueryTextListener(this);//设置该SearchView默认是否自动缩小为图标
//        searchView.setIconifiedByDefault(false);//设置是否显示三角按钮
//        searchView.setSubmitButtonEnabled(true);//设置该SearchView内默认显示的提示文本
//        searchView.setQueryHint("搜索");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE//弹出默认输入法
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void inClick() {
        checkBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editWord.getText().toString())) {
                    Toast.makeText(CheckActivity.this, "请填写要查询的单词", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResultUrl = editWord.getText().toString();
                if (getResultUrl == null) {
                    Toast.makeText(CheckActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    resultUrl = "http://dict-co.iciba.com/api/dictionary.php?w=" + getResultUrl + "&key=05C4F33DDAE7C18C6C24259B7C289136";
                    sendRequestWithOkHttp();
                }
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(TextUtils.isEmpty(query)) {
//                    Toast.makeText(CheckActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
//                } else {
//                    //输了内容之后进入，还有当删除一个字符也会进入前提输入框了不为空。
//                    Toast.makeText(CheckActivity.this, "你输入的内容为"+query, Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//            //单击三角搜索按钮时激发该方法，如果输入框为空则不调用
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        dictPronImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playDictPron();
            }
        });

    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(resultUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseXMLWithPull(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        http://dict-co.iciba.com/api/dictionary.php?w=word&key=05C4F33DDAE7C18C6C24259B7C289136
                        if ("key".equals(nodeName)) {
                            word.setKey(xmlPullParser.nextText());
                        } else if ("ps".equals(nodeName)) {
                            word.setPs(xmlPullParser.nextText());
                        } else if ("pron".equals(nodeName)) {
                            word.setDict_pron(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("dict".equals(nodeName)) {
                            Log.d(TAG, "parseXMLWithPull: " + word.getKey());
                            Log.d(TAG, "parseXMLWithPull: " + word.getPsEnglish());
                            Log.d(TAG, "parseXMLWithPull: " + word.getDict_pron());
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, "parseXMLWithPull: ", e);
        }
    }

    private void init() {
        checkBackButton = findViewById(R.id.Back_Check_Button);
        checkSearchButton = findViewById(R.id.Search_Check_Button);
        editWord = findViewById(R.id.Edit_Check_EditText);
//        searchView=findViewById(R.id.Edit_Check_EditText);
        dictPronImageView=findViewById(R.id.Check_Dict_Pron_ImageView);
        tv_word = findViewById(R.id.The_Word_TextView);
        tv_phonetic = findViewById(R.id.The_Pronunciation_TextView);
        tv_trans = findViewById(R.id.The_Translation_TextView);
        tv_source = findViewById(R.id.The_From_TextView);
    }
    private void playDictPron(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                player.playUrl(word.getDict_pron());
            }
        }).start();
    }
}
