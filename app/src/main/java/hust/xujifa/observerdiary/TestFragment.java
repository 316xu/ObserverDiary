package hust.xujifa.observerdiary;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hust.xujifa.observerdiary.helper.Diary2;
import hust.xujifa.observerdiary.main.TestAdapter;

/**
 * Created by xujifa on 2015/11/29.
 */
public class TestFragment extends Fragment {
    TestAdapter adapter;
    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    List<Diary2>diaries;
    public TestFragment(){
        Log.d("AAA", "testf");
    }
    public void setArray(List<Diary2>l){
        this.diaries=l;
        adapter=new TestAdapter(diaries);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.main_recycler,container,false);
        rv=(RecyclerView)v.findViewById(R.id.diary_list);
        rv.setHasFixedSize(true);
        manager=new LinearLayoutManager(container.getContext());

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        return rv;



    }
}
