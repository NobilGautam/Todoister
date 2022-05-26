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
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment {
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
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarButton.setOnClickListener(v -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });

        saveTodoButton.setOnClickListener(v -> {
            String taskName = enterTodo.getText().toString().trim();
            if (!TextUtils.isEmpty(taskName)) {
                Task task = new Task(taskName, Priority.HIGH, dueDate,
                        Calendar.getInstance().getTime(), false);
                TaskViewModel.insertTask(task);
            }
        });

    }
}