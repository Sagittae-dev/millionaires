package com.example.milionerzy.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.activities.AdminActivity;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;

import java.util.List;

public class AllQuestionsListAdapter extends RecyclerView.Adapter<AllQuestionsListAdapter.ViewHolder> {
    private final Context context;
    private final List<Question> questionsList;

    public AllQuestionsListAdapter(Context context, List<Question> questionsList) {
        this.context = context;
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        fillQuestionTextViews(holder, position);

        holder.particularElementOfList.setOnClickListener(v ->
                Toast.makeText(context, "Item: " + position, Toast.LENGTH_SHORT).show());

        holder.removeButton.setOnClickListener(b -> showRemoveAlertDialog(position));

        holder.editButton.setOnClickListener(b -> editQuestionFromDatabase(position));
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    private void fillQuestionTextViews(ViewHolder holder, final int position) {
        holder.contentOfQuestionTextView.setText(questionsList.get(position).getContentOfQuestion());
        holder.answerATextView.setText(questionsList.get(position).getAnswerA());
        holder.answerBTextView.setText(questionsList.get(position).getAnswerB());
        holder.answerCTextView.setText(questionsList.get(position).getAnswerC());
        holder.answerDTextView.setText(questionsList.get(position).getAnswerD());
        holder.correctAnswerTextView.setText(questionsList.get(position).getCorrectAnswer());
    }

    private void editQuestionFromDatabase(int position) {
        Intent intent = new Intent(context, AdminActivity.class);
        int questionId = questionsList.get(position).getId();
        intent.putExtra("questionId", questionId);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void showRemoveAlertDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Remove question")
                .setMessage("Are you sure you want to remove this question?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> removeQuestionFromDatabase(position))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removeQuestionFromDatabase(int position) {
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        QuestionService questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(context);
        int id = questionsList.get(position).getId();
        Log.i("Id from adapter", "id: " + id);
        questionService.deleteQuestion(id);
        questionsList.remove(position);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contentOfQuestionTextView;
        private final TextView answerATextView;
        private final TextView answerBTextView;
        private final TextView answerCTextView;
        private final TextView answerDTextView;
        private final TextView correctAnswerTextView;
        private final ConstraintLayout particularElementOfList;
        private final Button removeButton;
        private final Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            particularElementOfList = itemView.findViewById(R.id.databaseListItem);
            contentOfQuestionTextView = itemView.findViewById(R.id.contentOfQuestionTextView_DatabaseList);
            answerATextView = itemView.findViewById(R.id.answerATextView_DatabaseList);
            answerBTextView = itemView.findViewById(R.id.answerBTextView_DatabaseList);
            answerCTextView = itemView.findViewById(R.id.answerCTextView_DatabaseList);
            answerDTextView = itemView.findViewById(R.id.answerDTextView_DatabaseList);
            correctAnswerTextView = itemView.findViewById(R.id.correctAnswerTextView_DatabaseList);
            removeButton = itemView.findViewById(R.id.removeQuestionFromDatabaseButton);
            editButton = itemView.findViewById(R.id.editQuestionFromDatabaseButton);
        }
    }
}
