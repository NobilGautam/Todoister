package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private static TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return allTasks; }
    public LiveData<Task> getTask(long id) { return taskRepository.get(id); }
    public static void insertTask(Task task) { taskRepository.insert(task); }
    public static void deleteTask(Task task) { taskRepository.delete(task); }
    public static void updateTask(Task task) { taskRepository.update(task); }


}
