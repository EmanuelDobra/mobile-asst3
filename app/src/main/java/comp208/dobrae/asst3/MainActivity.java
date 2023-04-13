package comp208.dobrae.asst3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 The MainActivity class is responsible for displaying and updating reviews.
 It manages the layout of the application, the ListView, and the database.
 It add functionality for all crud operations over the 4 different layouts.
 */
public class MainActivity extends AppCompatActivity {

    ListView listView; // The ListView to display the reviews.
    List<Data> allReviews; // Data to load the listview with
    DataDB dataDB; // dataDB object

    ArrayAdapter<Data> arrayAdapter; // Adapter object to store listview items

    // edit layout elements
    EditText reviewId;
    Button deleteAllBtn;
    Button deleteByIdBtn;
    Button addNewReview;

    // update layout elements
    EditText updateTitle;
    EditText updateScore;
    Button updateBtn;
    Button deleteBtn;

    // add layout elements
    EditText newTitle;
    EditText newScore;
    Button addReviewBtn;
    Button backBtn;

    // Handler object to use in thread
    Handler handler = new Handler();

    /**
     Initializes the activity and sets the content view to the activity_main layout.
     Initializes the database object.
     @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDB = DataDB.getInstance(this);
    }

    /**
     Displays the Edit layout with all the reviews in the database.
     Sets the click listeners for the Delete All, Delete By Id, and Add Review buttons.
     @param view The view object.
     */
    public void edit(View view) {
        // Create a runnable for the listview generation
        Runnable display = ()->{
            // get the listview element, create the array adapter, and add it in
            listView = findViewById(R.id.myListView);
            arrayAdapter = new ArrayAdapter<Data>(this, android.R.layout.simple_list_item_1, allReviews);
            listView.setAdapter(arrayAdapter);

            // add event listeners to all list items, which will lead to the update layout
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                Toast.makeText(this, "Item Selected: " + allReviews.get(i).toString(), Toast.LENGTH_SHORT).show();
                setContentView(R.layout.update_layout);
                loadUpdateLayout(this.findViewById(R.id.updateView), allReviews.get(i));
            });
        };

        // Generate runnable to fetch db data and post the display
        Runnable adder = ()->{
            allReviews = dataDB.dataDAO().findAllData();
            handler.post(display);
        };

        // set the content view to the list page, and start the thread
        setContentView(R.layout.edit_layout);
        Thread thread = new Thread(adder);
        thread.start();

        // sets the click listener for the Delete All button
        deleteAllBtn = findViewById(R.id.btnDeleteAllReviews);
        deleteAllBtn.setOnClickListener( (View view1) -> {
            // delete all db data, and notify user of success
            dataDB.dataDAO().deleteAll();
            Toast.makeText(this, "All reviews deleted! ", Toast.LENGTH_SHORT).show();
            // return to the list page
            edit(this.findViewById(R.id.editView));
        });

        // sets the click listener for the Delete By Id button
        reviewId = findViewById(R.id.txtReviewId);
        deleteByIdBtn = findViewById(R.id.btnDeleteReviewById);
        deleteByIdBtn.setOnClickListener( (View view2) -> {
            // get the string of the review id textbox
            String revId = reviewId.getText().toString();
            // validate input
            if (!revId.isEmpty()) {
                // check that data with id exists
                if (dataDB.dataDAO().findDataById(Long.valueOf(revId)) != null) {
                    // delete review and notify user
                    dataDB.dataDAO().deleteById(Long.valueOf(revId));
                    Toast.makeText(this, "Review deleted! ", Toast.LENGTH_SHORT).show();
                    // return back to the list page
                    edit(this.findViewById(R.id.editView));
                } else {
                    // notify user of failure
                    Toast.makeText(this, "Review does not exist! ", Toast.LENGTH_SHORT).show();
                }
            } else {
                // notify user to enter valid id
                Toast.makeText(this, "Please enter a valid id! ", Toast.LENGTH_SHORT).show();
            }
        });

        // sets the click listener for the find View By Id button
        addNewReview = findViewById(R.id.btnAddReview);
        addNewReview.setOnClickListener( (View view3) -> {
            // move to the add page
            setContentView(R.layout.add_layout);
            // load the layout
            loadAddLayout(this.findViewById(R.id.addView));
        });
    }

    /**
     This method is responsible for loading the update layout with the details of the selected review.
     It updates the review title and score based on the user's input, and allows the user to delete the review.
     @param view The view associated with the update layout.
     @param selectedReview The review that the user wants to update or delete.
     */
    public void loadUpdateLayout(View view, Data selectedReview) {
        // set the update title and score views
        updateTitle = findViewById(R.id.txtUpdateTitle);
        updateScore = findViewById(R.id.txtUpdateScore);

        // set the text of the update title and score views to the selected review's title and score
        updateTitle.setText(selectedReview.title);
        updateScore.setText((selectedReview.score).toString());

        // find the update button and set its listener
        updateBtn = findViewById(R.id.btnUpdateReview);
        updateBtn.setOnClickListener( (View view1) -> {
            // fetch the title and score
            String title = updateTitle.getText().toString();
            String score = updateScore.getText().toString();
            // validate input
            if (!title.isEmpty() && !score.isEmpty()) {
                // check for valid review score
                if (Integer.parseInt(score) < 0 || Integer.parseInt(score) > 5) {
                    // notify user of valid context
                    Toast.makeText(this, "Score must be between 0 and 5! ", Toast.LENGTH_SHORT).show();
                } else {
                    // update title and score of review object
                    selectedReview.title = title;
                    selectedReview.score = Integer.valueOf(score);
                    // update review
                    dataDB.dataDAO().updateData(selectedReview);
                    // notify of success
                    Toast.makeText(this, "Review successfully updated! ", Toast.LENGTH_SHORT).show();
                    // return back to the list page and load its data
                    setContentView(R.layout.edit_layout);
                    edit(this.findViewById(R.id.editView));
                }
            } else {
                // notify user of invalid input
                Toast.makeText(this, "Please enter a valid title and score! ", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn = findViewById(R.id.btnDeleteReview);
        deleteBtn.setOnClickListener( (View view2) -> {
            dataDB.dataDAO().delete(selectedReview);
            Toast.makeText(this, "Review deleted! ", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.edit_layout);
            edit(this.findViewById(R.id.editView));
        });
    }

    /**
     Loads the add layout for a new review and creates a new Data object for it.
     Initializes the UI elements for the layout (newTitle, newScore, addReviewBtn)
     Allows user to add in new review to the db.
     @param view the current view of the activity
     */
    public void loadAddLayout(View view) {
        // create new review obj
        Data newReview = new Data();

        // set the input elements
        newTitle = findViewById(R.id.txtNewTitle);
        newScore = findViewById(R.id.txtNewScore);

        // find review button and add click event listener to it
        addReviewBtn = findViewById(R.id.btnAddRev);
        addReviewBtn.setOnClickListener( (View view1) -> {
            // fetch the input texts
            String title = newTitle.getText().toString();
            String score = newScore.getText().toString();
            // validate input
            if (!title.isEmpty() && !score.isEmpty()) {
                // update the new review object
                newReview.title = title;
                newReview.score = Integer.valueOf(score);
                // insert it to our db
                dataDB.dataDAO().insert(newReview);
                // notify user of success
                Toast.makeText(this, "Review successfully added! ", Toast.LENGTH_SHORT).show();
                // go back to list page and load its elements
                setContentView(R.layout.edit_layout);
                edit(this.findViewById(R.id.editView));
            } else {
                // notify user of invalid input
                Toast.makeText(this, "Please enter a valid title and score! ", Toast.LENGTH_SHORT).show();
            }
        });

        // find back button and add click event listener to it
        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener( (View view2) -> {
            // go back to list page and load its elements
            setContentView(R.layout.edit_layout);
            edit(this.findViewById(R.id.editView));
        });
    }
}