package com.louisgeek.louisareaselectdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.louisgeek.dropdownviewlib.DropDownView;
import com.louisgeek.louisareaselectdemo.Adapter.MyBaseAreaAdapter;
import com.louisgeek.louisareaselectdemo.Adapter.MyBaseCityAdapter;
import com.louisgeek.louisareaselectdemo.Adapter.MyBaseProvinceAdapter;
import com.louisgeek.louisareaselectdemo.Bean.Area;
import com.louisgeek.louisareaselectdemo.Bean.City;
import com.louisgeek.louisareaselectdemo.Bean.Province;
import com.louisgeek.louisareaselectdemo.R;
import com.louisgeek.louisareaselectdemo.util.SharedPreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    String areaJsonUrl="https://raw.githubusercontent.com/louisgeek/LouisAreaSelectDemo/master/app/src/main/res/raw/ssq.json";
    List<Province> provinceList;
    List<City> cityList;
    List<Area> areaList;
    Spinner idspprovince;

    Spinner idspcity;

    Spinner idsparea;

    int nowProvincePos = 0;
    int nowCityPos = 0;
    int nowAreaPos = 0;

    MyBaseProvinceAdapter myBaseProvinceAdapter;
    MyBaseCityAdapter myBaseCityAdapter;
    MyBaseAreaAdapter myBaseAreaAdapter;

    int nowShengPos=0;
    DropDownView dropDownViews;
    DropDownView dropDownViewshi;
    DropDownView dropDownViewqu;

            String ssq_json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button idbtn = (Button) findViewById(R.id.id_btn);
        idbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SelectAreaActivity.class);
                startActivity(intent);
            }
        });


        idsparea = (Spinner) findViewById(R.id.id_sp_area);
        idspcity = (Spinner) findViewById(R.id.id_sp_city);
        idspprovince = (Spinner) findViewById(R.id.id_sp_province);


        // ssq_json= getStringFromRaw(R.raw.ssq);
        if (SharedPreferencesUtil.containsKey(this)&&!SharedPreferencesUtil.getValue(this).equals("")){
            ssq_json=SharedPreferencesUtil.getValue(this);
            Toast.makeText(MainActivity.this, "缓存", Toast.LENGTH_SHORT).show();
            initData();
            initSpinner();
            initDropDownView();
        }else{
            Toast.makeText(MainActivity.this, "请求服务器", Toast.LENGTH_SHORT).show();

            idsparea.setVisibility(View.INVISIBLE);
            idspcity.setVisibility(View.INVISIBLE);
            idspprovince.setVisibility(View.INVISIBLE);

            OkHttpUtils
                    .get()
                    .url(areaJsonUrl)
                    .addParams("username", "abc")
                    .addParams("password", "123")
                    .build()
                    .execute(new StringCallback()
                    {
                        @Override
                        public void onError(Call call, Exception e)
                        {

                        }

                        @Override
                        public void onResponse(String response)
                        {
                            if (response!=null&&!response.equals("")) {
                                ssq_json=response;
                                SharedPreferencesUtil.saveValue(MainActivity.this, ssq_json);
                                initData();
                                initSpinner();
                                initDropDownView();
                                idsparea.setVisibility(View.VISIBLE);
                                idspcity.setVisibility(View.VISIBLE);
                                idspprovince.setVisibility(View.VISIBLE);

                            }else{

                            }

                        }
                    });
        }






    }

    private void  initDropDownView(){
        dropDownViews= (DropDownView) findViewById(R.id.id_sheng);
        dropDownViewshi= (DropDownView) findViewById(R.id.id_shi);
        dropDownViewqu= (DropDownView) findViewById(R.id.id_qu);
        //////

        List<Map<String, Object>> nameStateList=new ArrayList<>();
        for (int i = 0; i <provinceList.size() ; i++) {
            Map<String, Object> map=new HashMap<>();
            map.put("name",provinceList.get(i).getProvinceName());
            map.put("index",i);
            nameStateList.add(map);
        }
        dropDownViews.setupNameStateList(nameStateList);
        dropDownViews.setOnItemClickListener(new DropDownView.OnItemClickListener() {
            @Override
            public void onItemClick(Map<String, Object> map) {
                nowShengPos= Integer.parseInt(map.get("index").toString());
                initInnerShi(nowShengPos);
                initInnerQu(nowShengPos,0);
            }
        });
        initInnerShi(0);
        initInnerQu(0,0);
    }
    private void initInnerShi(int sheng_pos) {
        dropDownViewshi.setText(dropDownViewshi.getDefaultText());
        List<Map<String, Object>> nameStateList_shi=new ArrayList<>();
        for (int i = 0; i <provinceList.get(sheng_pos).getCites().size(); i++) {
            Map<String, Object> map=new HashMap<>();
            map.put("name",provinceList.get(sheng_pos).getCites().get(i).getCityName());
            map.put("index",i);
            nameStateList_shi.add(map);
        }
        dropDownViewshi.setupNameStateList(nameStateList_shi);
        dropDownViewshi.setOnItemClickListener(new DropDownView.OnItemClickListener() {
            @Override
            public void onItemClick(Map<String, Object> map) {
                initInnerQu(nowShengPos,Integer.parseInt(map.get("index").toString()));
            }
        });
    }

    private void initInnerQu(int sheng_pos,int shi_pos) {
        dropDownViewqu.setText(dropDownViewshi.getDefaultText());
        List<Map<String, Object>> nameStateList_qu=new ArrayList<>();
        for (int i = 0; i <provinceList.get(sheng_pos).getCites().get(shi_pos).getAreas().size(); i++) {
            Map<String, Object> map=new HashMap<>();
            map.put("name",provinceList.get(sheng_pos).getCites().get(shi_pos).getAreas().get(i).getAreaName());
            map.put("index",i);
            nameStateList_qu.add(map);
        }
        dropDownViewqu.setupNameStateList(nameStateList_qu);
    }




    private void initSpinner() {
        myBaseProvinceAdapter = new MyBaseProvinceAdapter(provinceList, this);
        idspprovince.setAdapter(myBaseProvinceAdapter);
        idspprovince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowProvincePos = position;
                cityList = provinceList.get(nowProvincePos).getCites();
                myBaseCityAdapter = new MyBaseCityAdapter(cityList, MainActivity.this);
                idspcity.setAdapter(myBaseCityAdapter);

                //Toast.makeText(MainActivity.this, "click province" + provinceList.get(nowProvincePos).getProvinceName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///
        cityList = provinceList.get(nowProvincePos).getCites();

        myBaseCityAdapter = new MyBaseCityAdapter(cityList, this);
        idspcity.setAdapter(myBaseCityAdapter);
        idspcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowCityPos = position;
                areaList = cityList.get(nowCityPos).getAreas();
                myBaseAreaAdapter = new MyBaseAreaAdapter(areaList, MainActivity.this);
                idsparea.setAdapter(myBaseAreaAdapter);

                // Toast.makeText(MainActivity.this, "click city"+provinceList.get(nowProvincePos).getCites().get(nowCityPos).getCityName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///
        areaList = cityList.get(nowCityPos).getAreas();

        myBaseAreaAdapter = new MyBaseAreaAdapter(areaList, this);
        idsparea.setAdapter(myBaseAreaAdapter);
        idsparea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowAreaPos = position;
                Area area = provinceList.get(nowProvincePos).getCites().get(nowCityPos).getAreas().get(nowAreaPos);

               // Toast.makeText(MainActivity.this, "click area" + area.getAreaName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void initData() {

        try {

            //
            JSONObject jsonObject = new JSONObject(ssq_json);
            JSONObject o_provinces = jsonObject.getJSONObject("provinces");
            JSONArray a_province = o_provinces.getJSONArray("province");
            //===
            provinceList = new ArrayList<>();
            for (int i = 0; i < a_province.length(); i++) {
                JSONObject o_province = (JSONObject) a_province.get(i);
                String ssqid = o_province.getString("ssqid");
                String ssqname = o_province.getString("ssqname");
                String ssqename = o_province.getString("ssqename");
                JSONObject o_cities = o_province.getJSONObject("cities");
                JSONArray a_city = o_cities.getJSONArray("city");
                //===
                List<City> cityList = new ArrayList<>();
                for (int j = 0; j < a_city.length(); j++) {
                    //===
                    JSONObject o_city = (JSONObject) a_city.get(j);
                    String ssqid_C = o_city.getString("ssqid");
                    String ssqname_C = o_city.getString("ssqname");
                    String ssqename_C = o_city.getString("ssqename");
                    JSONObject o_areas_C = o_city.getJSONObject("areas");
                    JSONArray a_area = o_areas_C.getJSONArray("area");
                    //===
                    List<Area> areaList = new ArrayList<>();
                    for (int k = 0; k < a_area.length(); k++) {
                        JSONObject O_area = (JSONObject) a_area.get(k);
                        String ssqid_A = O_area.getString("ssqid");
                        String ssqname_A = O_area.getString("ssqname");
                        String ssqename_A = O_area.getString("ssqename");
                        //===
                        Area area = new Area(ssqid_A, ssqname_A, "简称", ssqename_A);
                        areaList.add(area);
                    }
                    //===
                    City city = new City(ssqid_C, ssqname_C, "简称", ssqename_C, areaList);
                    cityList.add(city);
                }
                //===
                Province province = new Province(ssqid, ssqname, "简称", ssqename, cityList);
                provinceList.add(province);
            }

            Log.d("XXX", "LOUIS" + provinceList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("XXX", "LOUIS" + e.getMessage());
        }
    }


    public String getStringFromRaw(int rawID) {
        String result = "";
        try {
            InputStream ssq_is = getResources().openRawResource(rawID);

            InputStreamReader inputReader = new InputStreamReader(ssq_is);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //从assets 文件夹中获取文件并读取数据
    public String getStringFromAssets(String fileName) {
        String result = "";
        try {
            //open("qq.txt");
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
