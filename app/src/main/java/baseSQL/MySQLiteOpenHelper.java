package baseSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "MoodMessage :MySql";
    private SQLiteDatabase mSQLiteDatabase;

    private static final int VERSION_DB = 2;
    private static final String DB_NAME = "history.db";
    private final String TABLE_NAME = "mood";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_DAY = "_date";
    private final String COLUMN_MOOD = "smiley";
    private final String COLUMN_COMMENT = "comment";

    private final String CREATE_TABLE = "create table  "
            + TABLE_NAME + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DAY + " text,"+ COLUMN_MOOD + " text,"
            + COLUMN_COMMENT + " text) ";

    /**
     * Constructor
     *
     * @param context
     */
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_DB);
    }

    /**
     * create table
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * update version table
     * drop current table
     * and create an new table
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSQL = "drop table if exists " + TABLE_NAME;
        db.execSQL(strSQL);
        onCreate(db);
    }

    /**
     * downgrade version table
     * drop current table
     * and create an new table
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSQL = "drop table if exists " + TABLE_NAME;
        db.execSQL(strSQL);
        onCreate(db);
    }

}
