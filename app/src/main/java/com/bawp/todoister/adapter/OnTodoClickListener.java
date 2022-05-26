package com.bawp.todoister.adapter;

import com.bawp.todoister.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(int position, Task task);
}
