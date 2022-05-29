package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private CalendarView calendarView;
    private Group calendarGroup;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private ImageButton todayCalendarButton;
    private ImageButton saveTodoButton;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private RadioGroup priorityRadioGroup;
    private Date dueDate;
    private Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private TaskViewModel taskViewModel;
    private Priority priority;


    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        saveTodoButton = view.findViewById(R.id.save_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        todayCalendarButton = view.findViewById(R.id.today_calendar_button);


        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isEdit = sharedViewModel.getIsEdit();

        if (sharedViewModel.getSelectedItem().getValue() != null) {
            if (isEdit) {
                Task task = sharedViewModel.getSelectedItem().getValue();
                enterTodo.setText(task.getTask());
            } else {
                enterTodo.setText("");
            }

        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);


        calendarButton.setOnClickListener(v -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            Utils.hideSoftKeyboard(v);
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });

        priorityButton.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v);
            priorityRadioGroup.setVisibility(
                    priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                selectedButtonId = checkedId;
                selectedRadioButton = view.findViewById(selectedButtonId);
                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
                    if (selectedRadioButton.getId() == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    } else {
                        priority = Priority.LOW;
                    }
                } else {
                    priority = Priority.LOW;
                }
            });
        });

        saveTodoButton.setOnClickListener(v -> {
            String taskName = enterTodo.getText().toString().trim();
            if (!TextUtils.isEmpty(taskName) && dueDate != null && priority != null) {
                Task task = new Task(taskName, priority, dueDate,
                        Calendar.getInstance().getTime(), false);
                if (isEdit) {
                    Task updatedTask = sharedViewModel.getSelectedItem().getValue();
                    updatedTask.setTask(taskName);
                    updatedTask.setDueCreated(Calendar.getInstance().getTime());
                    updatedTask.setDueDate(dueDate);
                    updatedTask.setPriority(priority);
                    TaskViewModel.updateTask(updatedTask);
                    sharedViewModel.setIsEdit(false);
                } else {
                    TaskViewModel.insertTask(task);
                }
            }
            if (this.isVisible()) {
                this.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip) {
            //set today as due date
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
        } else if (id == R.id.tomorrow_chip) {
            //set tomorrow as due date
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
        } else if (id == R.id.next_week_chip) {
            //set next week as due date
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }
    }
}