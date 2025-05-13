package com.example.nutriwish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarTaskListAdapter extends RecyclerView.Adapter<CalendarTaskListAdapter.TaskViewHolder> {

    private List<CalendarTaskItem> taskList;
    private OnItemClickListener listener;

    // 클릭 이벤트 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(CalendarTaskItem taskItem);
    }

    // 어댑터의 생성자에서 클릭 리스너를 받아옴
    public CalendarTaskListAdapter(List<CalendarTaskItem> taskList, OnItemClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_calendar, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        CalendarTaskItem task = taskList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(task));  // 클릭 이벤트 처리
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_title);  // 아이템의 제목을 표시하는 TextView
        }
    }
}
