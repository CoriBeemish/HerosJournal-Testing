package com.example.Beemish.HerosJournal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Beemish.HerosJournal.adapters.PendingTodoAdapter;
import com.example.Beemish.HerosJournal.models.PendingTodoModel;

import java.util.ArrayList;


/**
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListFragment extends Fragment {

    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<PendingTodoModel> pendingTodoModels;
    Context context;

    public RecyclerListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PendingTodoAdapter adapter = new PendingTodoAdapter(pendingTodoModels,context);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pending_todos_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
