package id.ac.umn.cindymichelle;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    private String newsUrl;
    private String newsTitle;
    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        notificationManager = NotificationManagerCompat.from(this);

        if (bundle != null) {
            newsUrl = bundle.getString("newsUrl");
            newsTitle = bundle.getString("newsTitle");

            setTitle(newsTitle);
            webView = findViewById(R.id.news);

            webSettings = webView.getSettings();

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(newsUrl);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsActivity.this,
                    "Data registration success! Wait 5 seconds to see new notification from this app.",
                    Toast.LENGTH_SHORT
                ).show();

                new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            sendOnChannel();
                        }
                    },
                5000);
            }
        });
    }

    public void sendOnChannel(){
        Notification notification = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.ic_touch_app_black_24dp)
                .setContentTitle(newsTitle)
                .setContentText(newsUrl)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

}
