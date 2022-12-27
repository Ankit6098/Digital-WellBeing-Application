package timeline.android.digitalwellbeing.TYProject.contract;

import android.provider.BaseColumns;

public class WeightContract {

    private WeightContract() {
    }

    public static final class WeightEntry implements BaseColumns {
        public static final String TABLE_NAME = "weightHistoryList";
        public static final String COLUMN_NAME = "weight";

        public static final String TIME = "time";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WASTE = "waste";

    }
}
