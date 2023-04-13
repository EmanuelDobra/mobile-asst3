package comp208.dobrae.asst3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 class representing the room db that contains the data for the reviews,
 the db is built using the room persistence library and contains a single table for the reviews
 */
@Database(entities={Data.class}, version = 1, exportSchema = false)
public abstract class DataDB  extends RoomDatabase {
        /**
         name of the database, and instance of the db.
         */
        public static  final String DB_NAME = "data_db";
        private static DataDB  INSTANCE = null;

        /**
         returns the  instance of the db, creating it if necessary
         @param context context used to create the database.
         @return the instance of the database.
         */
        public static DataDB getInstance(Context context)
        {
            if (INSTANCE==null)
                synchronized (DataDB.class)
                {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataDB.class,
                            DB_NAME
                    )
                            .allowMainThreadQueries()
                            .build();
                }
            return  INSTANCE;
        }

        /**
         get the DAO object for the reviews
         @return the dataDAO object.
         */
        public abstract DataDAO dataDAO();
}
