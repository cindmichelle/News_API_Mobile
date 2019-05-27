package id.ac.umn.cindymichelle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView txtNim,  txtNama, txtLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtNim = findViewById(R.id.txtNim);
        txtNama = findViewById(R.id.txtNama);
        txtLib = findViewById(R.id.txtLibrary);

        String nim = "NIM : 00000016358\n";
        String nama = "Nama : Cindy Michelle\n\n";
        String library = "Library yang dipakai :\n" +
                "- RecyclerView ('com.android.support:recyclerview-v7:27.0.0')\n" +
                "- CardView ('com.android.support:cardview-v7:27.0.0')\n" +
                "- sqliteDatabase ('com.readystatesoftware.sqliteasset:sqliteassethelper:+')\n"+
                "- Android Spinkit ('com.github.ybq:Android-SpinKit:1.2.0')\n"+
                "\n\n\nusing API from : https://newsapi.org/";

        txtNim.setText(nim);
        txtNama.setText(nama);
        txtLib.setText(library);
    }
}
