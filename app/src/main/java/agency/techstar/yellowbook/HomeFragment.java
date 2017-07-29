package agency.techstar.yellowbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private SliderLayout mDemoSlider;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    View view;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        String[] myDataset={"","","",""};
        int[]myImages = {R.drawable.ic_home_black_24dp ,R.drawable.ic_home_black_24dp ,R.drawable.ic_home_black_24dp,
                R.drawable.ic_home_black_24dp};

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
//                    .setOnSliderClickListener(getContext());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);

        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myDataset,myImages);
        mRecyclerView.setAdapter(mAdapter);
        return view;


    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] mDataset;
        private int[] mImages;

        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView mTextView;
            public ImageView mImageView;
            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                mImageView = (ImageView)v.findViewById(R.id.img);
                mTextView = (TextView)v.findViewById(R.id.text);
            }

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(HomeFragment.this, LoginActivity.class);
//                startActivity(intent);
            }
        }

        public MyAdapter(String[] myDataset, int[] myImages) {
            mDataset = myDataset;
            mImages = myImages;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mImageView.setImageResource(mImages[position]);
        }

        public int getItemCount() {
            return mDataset.length;
        }
    }
}
