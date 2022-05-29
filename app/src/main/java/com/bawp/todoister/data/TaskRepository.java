package com.bawp.todoister.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {
    private final LiveData<List<Task>> allTasks;
    private LiveData<Task> task;
    private static TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return allTasks; }

    public LiveData<Task> get(long id) { return taskDao.get(id); }

    public void delete(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
    }

    public void deleteAll() {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.deleteAll());
    }

    public void update(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.update(task));
        Log.d("Update_task", "update: task called");
    }

    public void insert(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.insert(task));
    }
}
