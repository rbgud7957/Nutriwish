package com.example.nutriwish;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {


    private CalendarView calendarView;
    private Button addTaskButton;
    private RecyclerView taskList;
    private CalendarTaskListAdapter taskListAdapter;
    private List<CalendarTaskItem> tasks;
    private FirebaseFirestore db;
    private String selectedTime = "";  // 선택된 시간 저장
    private String currentSelectedDate = "";  // 현재 선택된 날짜 저장

    public CalendarFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        db = FirebaseFirestore.getInstance();
        calendarView = view.findViewById(R.id.calendarView);
        addTaskButton = view.findViewById(R.id.add_task_button);
        taskList = view.findViewById(R.id.task_list);
        tasks = new ArrayList<>();
        taskListAdapter = new CalendarTaskListAdapter(tasks, this::showTaskDetailsDialog);

        taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList.setAdapter(taskListAdapter);

        // 일정마다 구분선을 추가함
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        taskList.addItemDecoration(dividerItemDecoration);

        addTaskButton.setVisibility(View.VISIBLE);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            currentSelectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            loadTasksForDate(currentSelectedDate);  // 특정 날짜 클릭 시 해당 날짜의 일정 불러오기
            addTaskButton.setVisibility(View.VISIBLE);
        });

        addTaskButton.setOnClickListener(v -> {
            if (!currentSelectedDate.isEmpty()) {
                showAddTaskDialog(currentSelectedDate);
            } else {
                Toast.makeText(getContext(), "날짜를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // 화면 복귀 시 데이터 다시 로드
    @Override
    public void onResume() {
        super.onResume();
        // 사용자가 특정 날짜를 선택한 경우
        if (!currentSelectedDate.isEmpty()) {
            loadTasksForDate(currentSelectedDate);  // 화면에 다시 돌아왔을 때 현재 선택된 날짜의 일정 데이터를 불러오기
        } else {
            //loadTasksFromFirestore();
        }
    }

    // 특정 날짜 선택시 파이어베이스의 calendar 컬렉션에서 일정 데이터를 불러옴
    private void loadTasksForDate(String date) {
        // "date" 필드가 입력받은 날짜와 동일한 문서만 필터링함
        db.collection("calendar").whereEqualTo("date", date).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) { // Firestore 쿼리가 성공적으로 완료됐으면
                tasks.clear(); // 기존의 일정 리스트 초기화해 중복 데이터 방지
                for (DocumentSnapshot document : task.getResult()) {
                    CalendarTaskItem taskItem = document.toObject(CalendarTaskItem.class);
                    taskItem.setId(document.getId());
                    tasks.add(taskItem);
                }
                taskListAdapter.notifyDataSetChanged();
                addTaskButton.setVisibility(View.VISIBLE); // 일정 추가 버튼 화면에 표시
            } else {
                // Firestore 쿼리 실패 시
                Toast.makeText(getContext(), "해당 날짜의 일정을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 일정 추가 다이얼로그를 표시하는 메서드
    private void showAddTaskDialog(String selectedDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task_calendar, null);
        builder.setView(dialogView);

        EditText taskNameInput = dialogView.findViewById(R.id.task_name_input);
        TextView taskTimeDisplay = dialogView.findViewById(R.id.task_time_display);
        EditText taskMemoInput = dialogView.findViewById(R.id.task_memo_input);
        Button setTimeButton = dialogView.findViewById(R.id.set_time_button);

        setTimeButton.setOnClickListener(v -> showTimePicker(taskTimeDisplay));

        builder.setPositiveButton("저장", (dialog, which) -> {
            String taskName = taskNameInput.getText().toString();
            String taskMemo = taskMemoInput.getText().toString();

            if (!taskName.isEmpty() && !selectedTime.isEmpty()) {
                long notificationTimeInMillis = convertTimeToMillis(selectedDate, selectedTime);

                Log.d("test", "입력한 날짜: " + selectedDate + ", 시간: " + selectedTime);
                Log.d("test", "밀리초로 변환된 시간: " + notificationTimeInMillis);

                DocumentReference newDocRef = db.collection("calendar").document();
                CalendarTaskItem newTask = new CalendarTaskItem(newDocRef.getId(), taskName, selectedDate, selectedTime, taskMemo);

                newDocRef.set(newTask).addOnSuccessListener(documentReference -> {
                    tasks.add(newTask);
                    taskListAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "일정이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    MyFirebaseMessagingService firebaseService = new MyFirebaseMessagingService();
                    firebaseService.scheduleNotification(getContext(), taskName + " 복용하세요!", taskMemo, notificationTimeInMillis);
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "일정 저장 실패", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getContext(), "영양제 이름과 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 날짜와 시간을 밀리초로 변환하는 함수
    private long convertTimeToMillis(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            Date parsedDate = sdf.parse(date + " " + time);
            if(parsedDate != null){
                return parsedDate.getTime(); // 밀리초 반환
            } else {
                Log.e("test", "시간 변환 실패");
            }
            Log.d("test", "- 입력한 날짜: " + date + ", 시간: " + time);
            Log.d("test", "- 밀리초로 변환된 시간: " + convertTimeToMillis(date, time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void showTaskDetailsDialog(CalendarTaskItem taskItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task_calendar, null);
        builder.setView(dialogView);

        EditText taskNameInput = dialogView.findViewById(R.id.task_name_input);
        TextView taskTimeDisplay = dialogView.findViewById(R.id.task_time_display);
        EditText taskMemoInput = dialogView.findViewById(R.id.task_memo_input);
        Button setTimeButton = dialogView.findViewById(R.id.set_time_button);

        taskNameInput.setText(taskItem.getTaskName());
        taskTimeDisplay.setText(taskItem.getTime());
        taskMemoInput.setText(taskItem.getMemo());

        // 수정 모드 전환 버튼 추가
        builder.setNeutralButton("수정", null);

        // 삭제 버튼 추가
        builder.setNegativeButton("삭제", (dialog, which) -> {
            db.collection("calendar").document(taskItem.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        tasks.remove(taskItem);
                        taskListAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "일정 삭제 실패", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // 수정 버튼 클릭 시 동작 설정
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            taskNameInput.setEnabled(true);
            taskMemoInput.setEnabled(true);
            setTimeButton.setEnabled(true);
            setTimeButton.setOnClickListener(view -> showTimePicker(taskTimeDisplay));

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("저장");
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                String updatedTaskName = taskNameInput.getText().toString();
                String updatedTaskMemo = taskMemoInput.getText().toString();
                String updatedTaskTime = taskTimeDisplay.getText().toString();

                if (!updatedTaskName.isEmpty() && !updatedTaskTime.isEmpty()) {
                    taskItem.setTaskName(updatedTaskName);
                    taskItem.setTime(updatedTaskTime);
                    taskItem.setMemo(updatedTaskMemo);

                    if(taskItem.getId() != null){
                        db.collection("calendar").document(taskItem.getId()).set(taskItem)
                                .addOnSuccessListener(aVoid -> {
                                    taskListAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "일정이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "일정 수정 실패", Toast.LENGTH_SHORT).show());
                    } else {
                        // 이름이나 시간이 비어 있을 경우의 경고 메시지
                        Toast.makeText(getContext(), "영양제 이름과 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }



    // 타임 피커 다이얼로그 표시
    private void showTimePicker(TextView taskTimeDisplay) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute);  // 선택한 시간 저장
                    taskTimeDisplay.setText(selectedTime);  // 선택한 시간을 표시
                    // Log.d("test","선택된 시간 : " + selectedTime);
                }, 0, 0, true);  // 기본 값 00:00으로 설정 (24시간 형식)
        timePickerDialog.show();
    }


}
