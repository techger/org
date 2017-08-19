package agency.techstar.child.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import agency.techstar.child.AppConfig;
import agency.techstar.child.adapter.ProjectAdapter;
import agency.techstar.child.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectFragment extends Fragment {

    private SliderLayout mDemoSlider;
    Context context;
    View view;
    private Handler mHandler;

    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    GridView simpleList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, container, false);
        // Inflate the layout for this fragment
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        simpleList = (GridView) view.findViewById(R.id.simpleGridView);

        mHandler = new Handler(Looper.getMainLooper()); // udaan hurdanii shalgax davtalt

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Өсвөр үеийн эрүүл ирээдүйн төлөө", "https://www.colourbox.com/preview/1282705-colorful-child-hand-prints-on-white-background.jpg");
        url_maps.put("Өсвөр үеийн эрүүл ирээдүйн төлөө", "https://image.freepik.com/free-vector/fantastic-background-of-children-playing-together_23-2147608068.jpg");
        url_maps.put("Өсвөр үеийн эрүүл ирээдүйн төлөө", "https://ak2.picdn.net/shutterstock/videos/14967505/thumb/1.jpg");

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

        simpleList.setNumColumns(2);
        simpleList.setVisibility(View.VISIBLE);

        getProject();

        return view;

    }

    private void getProject() {
        String uri = AppConfig.ProjectService;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(uri)
                .build();

        Log.e("" , request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", "Алдаа:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                mHandler.post(() -> {
                    try {
                        JSONObject prod = new JSONObject(String.valueOf("{project=" + res+"}"));
                        JSONArray prodItems = prod.getJSONArray("project");
                        Log.e("", prodItems + "");
                        simpleList.setAdapter(new ProjectAdapter(getActivity(), prodItems));
                    } catch (JSONException ex){
                        ex.printStackTrace();
                    }
                });
            }
        });
    }
}
