package com.example.billingbilling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chanjun2016 on 16/11/27.
 */
public class MyselfFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_myself,container,false);

        TextView addModule = (TextView) rootView.findViewById(R.id.module_add);
        TextView exit = (TextView)rootView.findViewById(R.id.exit);

        TextView btnIncome = (TextView)rootView.findViewById(R.id.module_income);
        TextView btnPay = (TextView)rootView.findViewById(R.id.module_pay);

        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddMoudleActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("billingbilling",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                User.getInstance().reset();
                getActivity().finish();
            }
        });

        btnIncome.setOnClickListener(onViewModule);
        btnPay.setOnClickListener(onViewModule);

        TextView name = (TextView)rootView.findViewById(R.id.name);
        name.setText(User.getInstance().getName());

        return rootView;
    }

    private View.OnClickListener  onViewModule = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ViewModuleActivity.class);
            intent.putExtra("flag",v.getId() == R.id.module_income ? 1 : 0);
            startActivity(intent);
        }
    };

}
