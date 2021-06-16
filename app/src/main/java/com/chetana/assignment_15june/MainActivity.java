package com.chetana.assignment_15june;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context parent;
    EditText editText_first, editText_last;
    TextView textView;
    Button button;
    Spinner spinner;

    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayList<DataClass> viewList;
    ArrayList<String> choice;

    int id = 0;

    String JSON = "[" +
            "  {" +
            "    \"type\": \"textbox\"," +
            "    \"prompt\": \"First Name\"," +
            "    \"identifier\": \"unique_id_of_first_name\"," +
            "    \"value\": \"\"" +
            "  }," +
            "  {" +
            "    \"type\": \"textbox\"," +
            "    \"prompt\": \"Last Name\"," +
            "    \"identifier\": \"unique_id_of_last_name\"," +
            "    \"value\": \"\"" +
            "  }," +
            "  {" +
            "    \"type\": \"formula\"," +
            "    \"prompt\": \"Full Name\"," +
            "    \"identifier\": \"unique_id_of_full_name_formula\"," +
            "    \"jslogic\": \"function compute(){return unique_id_of_first_name + ' '+unique_id_of_last_name}\"," +
            "    \"value\": \"\"" +
            "  }," +
            "  {" +
            "    \"type\": \"date_picker\"," +
            "    \"prompt\": \"Date\"," +
            "    \"identifier\": \"unique_id_of_date_picker\"," +
            "    \"value\": \"\"" +
            "  }," +
            "  {" +
            "    \"type\": \"choice\"," +
            "    \"identifier\": \"unique_id_of_choice\"," +
            "    \"choices\": [" +
            "      \"choice1\"," +
            "      \"choice2\"," +
            "      \"choice3\"" +
            "    ]," +
            "    \"value\": \"\"" +
            "  }" +
            "]";

    private Object DataClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        init();

        parseJson();

        setListeners();

    }

    private void init(){

        parent = MainActivity.this;

        viewList = new ArrayList<DataClass>();
        choice = new ArrayList<String>();

    }

    private void setListeners(){
        editText_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() != null){
                    textView.setText(s.toString()+" "+editText_last.getText().toString());

                }else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_last.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() != null){
                    textView.setText(editText_first.getText().toString()+" "+s.toString());

                }else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                String date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;

                                button.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Choose Date");
                datePickerDialog.show();

            }
        });
    }

    private void parseJson(){
        try {
            jsonArray = new JSONArray(JSON);

            for(int i=0; i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);

                String type ="", prompt="",identifier ="", value = "",jslogic="";

                type = jsonObject.getString("type");
                identifier = jsonObject.getString("identifier");
                value = jsonObject.getString("value");

                //textbox,formula,datepick
                if(jsonObject.has("prompt")){
                    prompt = jsonObject.getString("prompt");
                }

                //formula
                if(jsonObject.has("jslogic")){
                    jslogic = jsonObject.getString("jslogic");
                }

                //choices
                if(jsonObject.has("choices")){
                    choice.clear();
                    JSONArray jChoice = new JSONArray(jsonObject.getString("choices"));
                    for(int j=0; j<jChoice.length();j++){
                        String a = jChoice.getString(j);

                        choice.add(a);

                    }
                }

                DataClass d = new DataClass(type,prompt,identifier,value,jslogic,choice);

                viewList.add(d);

            }

            setViewList(viewList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("ResourceType")
    private void setViewList(ArrayList<DataClass> viewList){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //Create SCrollView
        ScrollView scrollView = new ScrollView(this);


        //Create a layout---------------
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(20,20,20,20);

        for(int v=0; v<viewList.size(); v++){

            switch (viewList.get(v).getType()){
                case "textbox":
                    //----Create a Edittext------

                    //check condition for hint
                    if(viewList.get(v).getPrompt().equals("First Name")){
                        editText_first = new EditText(this);
                        editText_first.setHint("Enter first name");
                        editText_first.setId(id);
                        id++;
                        editText_first.setLayoutParams(params);
                        linearLayout.addView(editText_first);
                    }else {
                        editText_last = new EditText(this);
                        editText_last.setHint("Enter last name");
                        editText_last.setId(id);
                        id++;
                        editText_last.setLayoutParams(params);
                        linearLayout.addView(editText_last);
                    }

                    break;
                case "formula":
                    //----Create a TextView------
                    textView = new TextView(this);
                    textView.setHint("First Name Last Name");
                    textView.setId(id);
                    textView.setPadding(10,10,10,10);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(parent.getResources().getColor(R.color.colorAccent));
                    textView.setTextSize(18);
                    textView.setBackgroundColor(parent.getResources().getColor(R.color.grey));
                    id++;
                    textView.setLayoutParams(params);
                    linearLayout.addView(textView);
                    break;
                case "date_picker":
                    button = new Button(this);

                    button.setText("Date");
                    button.setId(id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        button.setBackground(getResources().getDrawable(R.drawable.border));
                    }
                    button.setHeight(35);
                    button.setTextSize(18);
                    button.setTextColor(Color.WHITE);
                    id++;
                    button.setLayoutParams(params);
                    linearLayout.addView(button);
                    break;
                case "choice":

                    spinner = new Spinner(this);
                    spinner.setId(id);
                    id++;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, choice);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setLayoutParams(params);
                    linearLayout.addView(spinner);
                    break;
            }

        }

        scrollView.addView(linearLayout);

        //---Create a layout param for the layout-----------------
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        this.addContentView(scrollView, layoutParams);


    }

}
