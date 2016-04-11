package csc550.memegenerator;

import android.provider.BaseColumns;

/**
 * Created by Christopher on 4/10/2016.
 */
public final class MemeContract {
    public MemeContract() {}

    public static abstract class MemeEntry implements BaseColumns {
        public static final String TABLE_NAME = "meme";
        public static final String COLUMN_NAME_DISPLAY_NAME = "displayname";
        public static final String COLUMN_NAME_GENERATOR_NAME = "generatorname";
        public static final String COLUMN_NAME_INSTANCE_URL = "instanceurl";
        public static final String COLUMN_NAME_IS_MINE = "ismine";
    }
}
