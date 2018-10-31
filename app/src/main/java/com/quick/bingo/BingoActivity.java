package com.quick.bingo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BingoActivity extends AppCompatActivity {

    private static final int NUMBER_COUNT = 25;
    private RecyclerView recyclerView;
    private TextView info;
    private String roomId;
    private boolean creator;
    private List<Integer> randomNumbers;
    private FirebaseRecyclerAdapter<Boolean, NumberHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);
        findViews();
        roomId = getIntent().getStringExtra("ROOM_ID");
        creator = getIntent().getBooleanExtra("IS_CREATOR", false);
        generateRandomNumbers();

        if (isCreator()) {
            //fill firebase numbers
            for (int i = 0; i < NUMBER_COUNT; i++) {
                FirebaseDatabase.getInstance().getReference("rooms")
                        .child(roomId)
                        .child("numbers")
                        .child((i+1) + "")
                        .setValue(false);
            }
        } else { //for joiner

        }
        //Recyclerview
        Query query = FirebaseDatabase.getInstance().getReference("rooms")
                .child(roomId)
                .child("numbers")
                .orderByKey();
        FirebaseRecyclerOptions<Boolean> options = new FirebaseRecyclerOptions.Builder<Boolean>()
                .setQuery(query, Boolean.class).build();
        adapter = new FirebaseRecyclerAdapter<Boolean, NumberHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NumberHolder holder, int position, @NonNull Boolean model) {

            }

            @NonNull
            @Override
            public NumberHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(BingoActivity.this).inflate(R.layout.single_number, viewGroup, false);
                return null;
            }
        };
    }

    class NumberHolder extends RecyclerView.ViewHolder {
        NumberButton button;
        public NumberHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.number);
        }
    }
    private void generateRandomNumbers() {
        randomNumbers = new ArrayList<>();
        for (int i = 0; i < NUMBER_COUNT; i++) {
            randomNumbers.add(i+1);
        }
        Collections.shuffle(randomNumbers);
        List<NumberButton> buttons = new ArrayList<>();
        for (int i = 0; i < NUMBER_COUNT; i++) {
            NumberButton button = new NumberButton(this);
            button.setText(randomNumbers.get(i) + "");
            button.setNumber(randomNumbers.get(i));
            buttons.add(button);
        }
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recycler);
        info = findViewById(R.id.info);
    }

    public boolean isCreator() {
        return creator;
    }

    public void setCreator(boolean creator) {
        this.creator = creator;
    }
}
