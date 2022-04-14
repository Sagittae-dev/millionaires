package com.example.milionerzy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milionerzy.R;
import com.example.milionerzy.model.Question;

import java.util.List;

public class AllQuestionsListAdapter extends RecyclerView.Adapter<AllQuestionsListAdapter.ViewHolder> {
    Context context;
    List<Question> questionsList;

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
        holder.contentOfQuestionTextView.setText(questionsList.get(position).getContentOfQuestion());
        holder.answerATextView.setText(questionsList.get(position).getAnswerA());
        holder.answerBTextView.setText(questionsList.get(position).getAnswerB());
        holder.answerCTextView.setText(questionsList.get(position).getAnswerC());
        holder.answerDTextView.setText(questionsList.get(position).getAnswerD());
        holder.correctAnswerTextView.setText(questionsList.get(position).getCorrectAnswer());
        holder.particularElementOfList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentOfQuestionTextView;
        TextView answerATextView;
        TextView answerBTextView;
        TextView answerCTextView;
        TextView answerDTextView;
        TextView correctAnswerTextView;
        ConstraintLayout particularElementOfList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            particularElementOfList = itemView.findViewById(R.id.databaseListItem);
            contentOfQuestionTextView = itemView.findViewById(R.id.contentOfQuestionTextView_DatabaseList);
            answerATextView = itemView.findViewById(R.id.answerATextView_DatabaseList);
            answerBTextView = itemView.findViewById(R.id.answerBTextView_DatabaseList);
            answerCTextView = itemView.findViewById(R.id.answerCTextView_DatabaseList);
            answerDTextView = itemView.findViewById(R.id.answerDTextView_DatabaseList);
            correctAnswerTextView = itemView.findViewById(R.id.correctAnswerTextView_DatabaseList);
        }
    }
}
