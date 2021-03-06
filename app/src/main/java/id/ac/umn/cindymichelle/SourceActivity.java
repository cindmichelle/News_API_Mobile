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
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;

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

public class SourceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Source> sources;
    private SourceAdapter adapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        setTitle("News Sources");
        Toast.makeText(SourceActivity.this, "Welcome User!", Toast.LENGTH_SHORT).show();
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        sources = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        new FetchData().execute();
    }

    private class FetchData extends AsyncTask<String, Void, String>{
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
                String urlString = "https://newsapi.org/v2/sources?apiKey=16d22da23aa1448a836fb3731807d707";

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

                Log.d("FETCHDATA", jsonString);

                //konversi dari string -> sesuatu yg akan digunakan pada UI.
                JSONObject jsonObject = new JSONObject(jsonString);

                String statusCode = jsonObject.getString("status");

                if(statusCode.equals("ok")){
                    JSONArray jsonArray = jsonObject.getJSONArray("sources");

                    //Loop data from API
                    for(int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject sourceObj = jsonArray.getJSONObject(i);

                        Source source = new Source();
                        source.setId(sourceObj.getString("id"));
                        source.setName(sourceObj.getString("name"));
                        source.setDescription(sourceObj.getString("description"));
                        source.setUrl(sourceObj.getString("url"));

                        sources.add(source);
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

            recyclerView.setLayoutManager(new LinearLayoutManager(SourceActivity.this));
            adapter = new SourceAdapter(SourceActivity.this, sources);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
