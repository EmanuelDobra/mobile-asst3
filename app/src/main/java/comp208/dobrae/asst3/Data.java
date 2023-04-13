package comp208.dobrae.asst3;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 represents a data object containing an id, a title, and a score for a review
 */
@Entity
public class Data {
    /**
     The unique identifier for the data object.
     */
    @PrimaryKey(autoGenerate = true)
    long id;

    /**
     the title column of the review
     */
    @ColumnInfo(name="title")
    String title;

    /**
     the score rating column of the review
     */
    @ColumnInfo(name="score")
    Integer score;


    /**
     constructor that generates the info of the new data object
     @param title the title of the review
     @param score the score of the review
     */
    public Data(String title, Integer score) {
        this.title = title;
        this.score = score;
    }

    /**
     constructs a new, empty Data object.
     */
    public Data() { }

    /**
     generate a new toString function to output the review data neatly
     @return a string containing the id, title, and score of the review
     */
    @Override
    public String toString() {
        return "ID: " + id + " (Title) " + title + " (Rating) " + score + "/5";
    }
}
