package id.ac.umn.cindymichelle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity {

    private String sourceId;
    private String sourceName;
    private ArrayList<News> newsLists;

    private RecyclerView recyclerView;
    private NewsListAdapter adapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        recyclerView = findViewById(R.id.recyclerView_newsList);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            sourceId = bundle.getString("sourceId");
            sourceName = bundle.getString("sourceName");

            setTitle(sourceName);
            newsLists = new ArrayList<>();

            new FetchData().execute();
        }
    }

    private class FetchData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;

            BufferedReader bufferedReader = null;

            //utk save hasil dari json file
            String jsonString = null;

            try{
                String urlString = "https://newsapi.org/v2/top-headlines?sources=" + sourceId + "&apiKey=16d22da23aa1448a836fb3731807d707";

                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();

                //method for access
                urlConnection.setRequestMethod("GET");

                //connect to the API
                urlConnection.connect();

                int lengthOfFile = urlConnection.getContentLength();

                //convert output from API into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                //error handling if fetch API return null
                if(inputStream == null){
                    return null;
                }

                //put API output into buffered reader
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    //fill output from API into stringBuffer
                    stringBuffer.append(line);
                }

                // error handling 2
                if(stringBuffer.length() == 0){
                    return null;
                }

                //masukkin smua data ke variable output dalam bentuk string
                jsonString = stringBuffer.toString();

                Log.d("FETCHDATA NEWS LIST", jsonString);

                //konversi dari string -> sesuatu yg akan digunakan pada UI.
                JSONObject jsonObject = new JSONObject(jsonString);

                String statusCode = jsonObject.getString("status");

                if(statusCode.equals("ok")){
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    //Loop data from API
                    for(int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject sourceObj = jsonArray.getJSONObject(i);

                        JSONObject source = sourceObj.getJSONObject("source");

//                        Log.d("ID DAPET", source.getString("id"));
                        News news = new News();
                        news.setId(source.getString("id"));
                        news.setName(source.getString("name"));
                        news.setTitle(sourceObj.getString("title"));
                        news.setAuthor(sourceObj.getString("author"));
                        news.setDescription(sourceObj.getString("description"));
                        news.setUrl(sourceObj.getString("url"));
                        news.setImageUrl(sourceObj.getString("urlToImage"));

                        newsLists.add(news);
                    }
                }

            }
            catch (MalformedURLException e){
                Log.e("MALFORMED", "MalformedURLException : " + e.getMessage());
            }
            catch (IOException e){
                Log.e("IO", "IOException : " + e.getMessage());
            }
            catch (JSONException e){
                Log.e("JSON", "JSONException : " + e.getMessage());
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException e) {
                        Log.e("BUFFEREDIOEXCEPTION", "IOException : " + e.getMessage());
                    }
                }
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(NewsListActivity.this));
            adapter = new NewsListAdapter(NewsListActivity.this, newsLists);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
