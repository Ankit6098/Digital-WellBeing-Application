package timeline.android.digitalwellbeing.TYProject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timeline.android.digitalwellbeing.TYProject.contract.FocusContract;

public class FocusDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "focusListDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "focusHistoryList";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "drank";

    private static final String TIME = "time";


    // creating a constructor for our database handler.
    public FocusDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                FocusContract.FocusEntry.TABLE_NAME + " (" +
                FocusContract.FocusEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FocusContract.FocusEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FocusContract.FocusEntry.TIME + " TEXT NOT NULL, " +
                FocusContract.FocusEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                FocusContract.FocusEntry.COLUMN_WASTE + " TEXT" +

                ");";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
