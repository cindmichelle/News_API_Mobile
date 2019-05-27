package id.ac.umn.cindymichelle;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsHolder>{

    private Context context;
    private ArrayList<News> newsList;
    private NotificationManagerCompat notificationManager;

    public NewsListAdapter() {
    }

    public NewsListAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_row, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        News news = newsList.get(position);
        holder.setDetails(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private NotificationManagerCompat notificationManager;

        private TextView txtTitle, txtDesc, txtAuthor;
        private ImageView imageNews;

        public NewsHolder(final View itemView) {
            super(itemView);
            notificationManager = NotificationManagerCompat.from(context);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imageNews = itemView.findViewById(R.id.image_news);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(itemView.getContext(),Integer.toString(position), Toast.LENGTH_SHORT).show();

                int position = getAdapterPosition();

                Intent intent = new Intent(itemView.getContext(), NewsActivity.class);
                intent.putExtra("newsUrl", newsList.get(position).getUrl());
                intent.putExtra("newsTitle", newsList.get(position).getTitle());

                view.getContext().startActivity(intent);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(),
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
                    return true;
                }
            });
        }

        public void sendOnChannel(){
            int position = getAdapterPosition();

            Notification notification = new NotificationCompat.Builder(context, "channel")
                    .setSmallIcon(R.drawable.ic_touch_app_black_24dp)
                    .setContentTitle(newsList.get(position).getTitle())
                    .setContentText(newsList.get(position).getUrl())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();

            notificationManager.notify(1, notification);
        }
        private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
            ImageView imageView;

            public DownLoadImageTask(ImageView imageView){
                this.imageView = imageView;
            }

            /*
                doInBackground(Params... params)
                    Override this method to perform a computation on a background thread.
             */
            protected Bitmap doInBackground(String...urls){
                String urlOfImage = urls[0];
                Bitmap logo = null;
                try{
                    InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                    logo = BitmapFactory.decodeStream(is);
                }catch(Exception e){ // Catch the download exception
                    Log.d(TAG, "error dude : " + e.getMessage());
                }
                return logo;
            }

            /*
                onPostExecute(Result result)
                    Runs on the UI thread after doInBackground(Params...).
             */
            protected void onPostExecute(Bitmap result){
                imageView.setImageBitmap(result);
            }
        }

        public void setDetails(News news){
            new DownLoadImageTask(imageNews).execute(news.getImageUrl());

            txtTitle.setText(news.getTitle());
            txtAuthor.setText(news.getAuthor());
            txtDesc.setText(news.getDescription());
        }
    }
}
