package agency.techstar.yellowbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    GridView simpleList;
    ArrayList birdList=new ArrayList<>();
    View view;
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_news, container, false);
        simpleList = (GridView) view.findViewById(R.id.NewsGridView);

        birdList.add(new Item("Bird 1",R.drawable.ic_1));
        birdList.add(new Item("Bird 2",R.drawable.ic_2));
        birdList.add(new Item("Bird 3",R.drawable.ic_3));
        birdList.add(new Item("Bird 4",R.drawable.ic_4));

        MyAdapter myAdapter=new MyAdapter(getActivity(),R.layout.grid_item,birdList);
        simpleList.setAdapter(myAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),BaiguullagaActivity.class));
            }
        });
        return view;

    }

}
