package id.ac.umn.cindymichelle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static String DB_PATH;
    public static String DB_NAME;
    public SQLiteDatabase db;

    public Context context;

    private static final String TABLE_LYRICS = "users";

    public static final String COLUMN_INDEX = "\"index\"";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_PASSWORD = "user_password";

    public DatabaseOpenHelper(
            Context context, String name,
            SQLiteDatabase.CursorFactory factory, int version
    ) {
        super(context, name, factory, version);
        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";
        DB_NAME = name;

        this.context = context;
        openDatabase();
    }

    public SQLiteDatabase openDatabase(){
        String path = DB_PATH + DB_NAME;
        if (db == null){
            createDatabase();
            db = SQLiteDatabase.openDatabase(
                    path, null,
                    SQLiteDatabase.OPEN_READWRITE
            );
        }
        return db;
    }

    //create db if its not created yet
    public void createDatabase(){
        boolean dbExist = checkDatabase();
        if(!dbExist){
            this.getReadableDatabase();
            try{
                copyDatabase();
            } catch (IOException e){
                Log.e(
                        this.getClass().toString(),
                        "Copying error"
                );
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(
                    this.getClass().toString(),
                    "Database already exists!"
            );
        }
    }

    private boolean checkDatabase(){
        String path = DB_PATH + DB_NAME;
        File dbFile = new File(path);
        return dbFile.exists();
    }

    //method copy db
    private void copyDatabase() throws IOException{
        //open a stream for reading from the exists database
        //the stream source is located in the assets

        InputStream externalDBStream = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream localDBStream = new FileOutputStream(outFileName);

        //copying db

        byte[] buffer = new byte[1024];

        int bytesRead;

        while ((bytesRead = externalDBStream.read(buffer)) > 0){
            localDBStream.write(buffer, 0, bytesRead);
        }

        localDBStream.flush();

        localDBStream.close();
        externalDBStream.close();
    }

    public synchronized void close(){
        if(db != null){
            db.close();
        }
        super.close();
    }

    //retrieve all data
    public Cursor getUserInfo(){
        Cursor cursor = db.query(
                TABLE_LYRICS,
                new String[] {
                        COLUMN_USER_NAME,
                        COLUMN_USER_PASSWORD
                },
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
