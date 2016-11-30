package com.example.billingbilling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chanjun2016 on 16/11/26.
 */
public class AccountListFragment extends android.support.v4.app.Fragment {

    private ListView listView;
    private Button addBtn;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_list,container,false);

        listView = (ListView) rootView.findViewById(R.id.list_account);
        addBtn = (Button) rootView.findViewById(R.id.btn_add);
        spinner = (Spinner)rootView.findViewById(R.id.spinner);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddAccountActivity.class);

                getActivity().startActivityForResult(intent,0);
            }
        });

        final Map<Integer,AccountCategory> mapCategory = User.getInstance().getCategoryMap();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.textaccount);
        adapter.add("all");

        final Map<String,Integer> map = new HashMap<String, Integer>();
        for(Integer key : mapCategory.keySet()){
            adapter.add(mapCategory.get(key).getDescription());
            map.put(mapCategory.get(key).getDescription(),mapCategory.get(key).getCid());
        }

        spinner.setAdapter(adapter);

        List<String> list2 = new ArrayList<String>();

        final Map<Integer,AccountItem> mapAccount = User.getInstance().getAcntMap();
        for (Integer key : mapAccount.keySet()){
            list2.add(mapAccount.get(key).getDescription());
        }

        final List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for (Integer key : mapAccount.keySet()){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("name",mapAccount.get(key).getDescription());
            listItem.put("date",mapAccount.get(key).getDate());
            listItem.put("money", (mapAccount.get(key).getMoney()>0?"+":"-")  + mapAccount.get(key).getMoney());
            listItems.add(listItem);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),listItems,R.layout.list_acnt_item,
                new String[]{"name","date","money"},
                new int[]{R.id.name,R.id.date,R.id.money}
                );

//        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list2);

        listView.setAdapter(simpleAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = spinner.getSelectedItem().toString();
                if (name.equals("all")){
                    listView.setAdapter(simpleAdapter);
                    return ;
                }

                int cid=  map.get(spinner.getSelectedItem().toString());

                List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();

                for (Integer key : mapAccount.keySet()){
                    if (cid == mapAccount.get(key).getCategory()){
                        Map<String,Object> listItem = new HashMap<String,Object>();
                        listItem.put("name",mapAccount.get(key).getDescription());
                        listItem.put("date",mapAccount.get(key).getDate());
                        listItem.put("money", (mapAccount.get(key).getMoney()>0?"+":"-")  + mapAccount.get(key).getMoney());
                        listItems.add(listItem);
                    }
                }

                SimpleAdapter simpleAdapter1 = new SimpleAdapter(getActivity(),listItems,R.layout.list_acnt_item,
                        new String[]{"name","date","money"},
                        new int[]{R.id.name,R.id.date,R.id.money}
                );

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
                listView.setAdapter(simpleAdapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        final Map<Integer,AccountItem> mapAccount = User.getInstance().getAcntMap();
        final List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for (Integer key : mapAccount.keySet()){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("name",mapAccount.get(key).getDescription());
            listItem.put("date",mapAccount.get(key).getDate());
            listItem.put("money", (mapAccount.get(key).getMoney()>0?"+":"-")  + mapAccount.get(key).getMoney());
            listItems.add(listItem);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),listItems,R.layout.list_acnt_item,
                new String[]{"name","date","money"},
                new int[]{R.id.name,R.id.date,R.id.money}
        );

//        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list2);

        listView.setAdapter(simpleAdapter);

        Log.d("%%%%%%%%%%%%%","hh");
    }
}
