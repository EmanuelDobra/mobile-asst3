package comp208.dobrae.asst3;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 the DataDAO interface is responsible for defining the methods used for accessing the data in the database.
 */
@Dao
public interface DataDAO {
    /**
     fetches all data from the database.
     */
    @Query("SELECT * FROM data")
    List<Data> findAllData();

    /**
     fetches data by its ID.
     @param id the ID of the Data object to fetch.
     */
    @Query("SELECT * FROM data WHERE id=:id")
    Data findDataById(Long id);

    /**
     inserts a new Data object into the database.
     @param data the Data object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert (Data data);

    /**
     updates an existing Data object in the database.
     @param data the Data object to update.
     */
    @Update
    int updateData(Data data);

    /**
     deletes a Data object from the database by its ID.
     @param id the ID of the Data object to delete.
     */
    @Query ("DELETE FROM data WHERE id=:id")
    void deleteById(Long id);

    /**
     deletes all Data objects from the database.
     */
    @Query ("DELETE FROM data")
    void deleteAll();

    /**
     deletes a specific Data object from the database.
     @param data the Data object to delete.
     */
    @Delete
    int delete (Data data);

}
