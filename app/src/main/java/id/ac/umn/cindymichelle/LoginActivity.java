package id.ac.umn.cindymichelle;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String DB_NAME = "credential.db";

    private User authUser;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Fire News");
        getUserInfo();

        username = findViewById(R.id.edText_username);
        password = findViewById(R.id.edText_password);

        Button btn = findViewById(R.id.btn_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(authUser.getUsername()) &&
                        password.getText().toString().equals(authUser.getPassword())
                ){
//                  Toast.makeText(LoginActivity.this, "yes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, SourceActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Invalid username/password. Please Try Again",
                            Toast.LENGTH_SHORT
                    ).show();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
           case R.id.aboutme:{
                Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }


            default:{

                return super.onOptionsItemSelected(item);
            }

        }

    }


    private void getUserInfo(){
        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getApplicationContext(),DB_NAME,null,1);

        dbHelper.openDatabase();

        Cursor cursor = dbHelper.getUserInfo();

        cursor.moveToFirst();

        authUser = new User();
        do{
             authUser.setUsername(cursor.getString(0));
             authUser.setPassword(cursor.getString(1));
        } while (cursor.moveToNext());

        dbHelper.close();

    }
}
