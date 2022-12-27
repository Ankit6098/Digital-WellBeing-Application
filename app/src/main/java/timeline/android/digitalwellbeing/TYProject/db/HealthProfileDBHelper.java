package timeline.android.digitalwellbeing.TYProject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timeline.android.digitalwellbeing.TYProject.contract.WeightContract;


public class HealthProfileDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "weightListDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "weightHistoryList";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "weight";

    private static final String TIME = "time";


    // creating a constructor for our database handler.
    public HealthProfileDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                WeightContract.WeightEntry.TABLE_NAME + " (" +
                WeightContract.WeightEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeightContract.WeightEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                WeightContract.WeightEntry.TIME + " TEXT NOT NULL, " +
                WeightContract.WeightEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                WeightContract.WeightEntry.COLUMN_WASTE + " TEXT" +

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
