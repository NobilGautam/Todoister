package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> allTasks;
    private final OnTodoClickListener todoClickListener;

    public RecyclerViewAdapter(List<Task> allTasks, OnTodoClickListener todoClickListener) {
        this.allTasks = allTasks;
        this.todoClickListener = todoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Task task = allTasks.get(position);
        holder.task.setText(task.getTask());

        //Changing color of views according to priority
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[] {-android.R.attr.state_enabled},
                new int[] {android.R.attr.state_enabled}
        }, new int[]{
                Color.LTGRAY,
                Utils.priorityColor(task)
        });
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);

        String formattedDate = Utils.formattedDate(task.getDueDate());
        holder.todayChip.setText(formattedDate);
    }

    @Override
    public int getItemCount() { return allTasks.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RadioButton radioButton;
        public TextView task;
        public Chip todayChip;

        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.onTodoClickListener = todoClickListener;

            radioButton = itemView.findViewById(R.id.todo_radio_button);
            radioButton.setOnClickListener(this);

            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Task currTask = allTasks.get(getAdapterPosition());
            if (id == R.id.todo_row_layout) {
                onTodoClickListener.onTodoClick(currTask);
            } else if (id == R.id.todo_radio_button) {
                onTodoClickListener.onRadioButtonClick(currTask);
            }
        }
    }
}
