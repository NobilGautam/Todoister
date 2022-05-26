package com.bawp.todoister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;
import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private int counter;
    private BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing bottom sheet fragment
        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        counter = 0;

        //Initializing recycler view
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Instantiating task view model
        TaskViewModel taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        //Getting list of tasks from view model
        taskViewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        //Setting up FAB to show bottom sheet fragment
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showBottomSheetDialogue();
        });
    }

    private void showBottomSheetDialogue() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(int position, Task task) {
        Log.d("IRClick", "item row clicked");
    }

    @Override
    public void onRadioButtonClick(Task task) {

        Log.d("RBClick", "Radio button clicked");

        //Create the object of Alert Dialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Log.d("RBClick", "builder object created");

        //Setting the message to be shown
        builder.setMessage("This task will be removed permanently");

        //Set Alert title
        builder.setTitle("Task Done?");

        //Set cancellable false so that when user clicks
        //outside the box the box will still be visible
        builder.setCancelable(false);

        //Setting the positive button
        builder.setPositiveButton("YES", (dialog, which) -> {
            //When the user clicks "YES" the dialog box will close
            TaskViewModel.deleteTask(task);
            recyclerViewAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        //Setting the negative button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //When the user clicks "NO" the dialog box will cancel
            dialog.cancel();
        });

        //Create the alert dialog box
        AlertDialog alertDialog = builder.create();

        //Show the alert dialog box
        alertDialog.show();
    }
}