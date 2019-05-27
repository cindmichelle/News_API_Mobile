package id.ac.umn.cindymichelle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivityy extends AppCompatActivity {

    private String newsUrl;
    private String newsTitle;
    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newss);


        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            newsUrl = bundle.getString("newsUrl");
            newsTitle = bundle.getString("newsTitle");

            setTitle(newsTitle);
            webView = findViewById(R.id.news);

            webSettings = webView.getSettings();

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(newsUrl);
        }
    }
}
