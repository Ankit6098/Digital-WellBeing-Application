package timeline.android.digitalwellbeing.TYProject.contract;

import android.provider.BaseColumns;

public class FocusContract {
    private FocusContract() {
    }

    public static final class FocusEntry implements BaseColumns {
        public static final String TABLE_NAME = "focusHistoryList";
        public static final String COLUMN_NAME = "breathing";
        public static final String TIME = "time";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WASTE = "waste";


    }
}
