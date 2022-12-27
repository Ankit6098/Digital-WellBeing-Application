package timeline.android.digitalwellbeing.TYProject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import timeline.android.digitalwellbeing.TYProject.R;


public class CardsFragment extends Fragment {


    public CardsFragment() {
        // Required empty public constructor
    }


    public static CardsFragment newInstance(String coins, String tipDesc, int image){
        CardsFragment cardsFragment = new CardsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("coins",coins);
        bundle.putString("coinsDesc",tipDesc);
        bundle.putInt("logo", image);
        cardsFragment.setArguments(bundle);
        return cardsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        Bundle bundle = getArguments();
        TextView textView = (TextView) view.findViewById(R.id.no_of_coins);
        TextView textView2 = (TextView) view.findViewById(R.id.gain_description);
        ImageView imageView = view.findViewById(R.id.logo_imageview);

        imageView.setImageResource(bundle.getInt("logo"));
        textView.setText(bundle.getString("coins"));
        textView2.setText(bundle.getString("coinsDesc"));

        return view;
    }
}