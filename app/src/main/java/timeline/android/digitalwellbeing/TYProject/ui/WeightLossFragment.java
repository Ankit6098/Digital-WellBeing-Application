package timeline.android.digitalwellbeing.TYProject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import timeline.android.digitalwellbeing.TYProject.R;


public class WeightLossFragment extends Fragment {

    public WeightLossFragment() {
        // Required empty public constructor
    }


    public static WeightLossFragment newInstance(String tip, String tipDesc, int image){
        WeightLossFragment weightLossFragment = new WeightLossFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tip", tip);
        bundle.putString("tipDesc",tipDesc);
        bundle.putInt("lossImage", image);
        weightLossFragment.setArguments(bundle);
        return weightLossFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_loss, container, false);
        Bundle bundle = getArguments();
        TextView textView = (TextView) view.findViewById(R.id.loss_heading);
        TextView textView2 = (TextView) view.findViewById(R.id.loss_description);
        ImageView imageView = view.findViewById(R.id.loss_image);

        imageView.setImageResource(bundle.getInt("lossImage"));
        textView.setText(bundle.getString("tip"));
        textView2.setText(bundle.getString("tipDesc"));

        return view;
    }
}