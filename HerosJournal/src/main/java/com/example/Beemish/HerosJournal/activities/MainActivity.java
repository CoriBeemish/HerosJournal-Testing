package com.example.Beemish.HerosJournal.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Beemish.HerosJournal.OnStartDragListener;
import com.example.Beemish.HerosJournal.R;
import com.example.Beemish.HerosJournal.SimpleItemTouchHelperCallback;
import com.example.Beemish.HerosJournal.adapters.PendingTodoAdapter;
import com.example.Beemish.HerosJournal.helpers.IntentExtras;
import com.example.Beemish.HerosJournal.helpers.SettingsHelper;
import com.example.Beemish.HerosJournal.helpers.TagDBHelper;
import com.example.Beemish.HerosJournal.helpers.TodoDBHelper;
import com.example.Beemish.HerosJournal.models.PendingTodoModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnStartDragListener{

    private RecyclerView pendingTodos;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PendingTodoModel> pendingTodoModels;
    private PendingTodoAdapter pendingTodoAdapter;
    private FloatingActionButton addNewTodo;
    private TagDBHelper tagDBHelper;
    private String getTagTitleString, getRepeatString;
    private TodoDBHelper todoDBHelper;
    private LinearLayout linearLayout;

    //--------------------Drag and Drop
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsHelper.applyTheme(this); // Applies settings if there has been a choice
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar)); // Custom tool bar
        SettingsHelper.applyThemeToolbar((Toolbar)findViewById(R.id.toolbar),this); // Applies settings if there has been a choice
        setTitle(getString(R.string.app_title)); //Loads a different name for the current page on your phone
        showDrawerLayout(); // Custom drawer
        navigationMenuInit();
        loadPendingTodos();


    }

    //loading all the pending activities
    private void loadPendingTodos(){
        pendingTodos=(RecyclerView)findViewById(R.id.pending_todos_view);
        linearLayout=(LinearLayout)findViewById(R.id.no_pending_todo_section);
        tagDBHelper=new TagDBHelper(this); // Gets tag database
        todoDBHelper=new TodoDBHelper(this); // Gets todo database

        if(todoDBHelper.countTodos()==0){
            linearLayout.setVisibility(View.VISIBLE);
            pendingTodos.setVisibility(View.GONE);
        }else{
            pendingTodoModels=new ArrayList<>();
            pendingTodoModels=todoDBHelper.fetchAllTodos();
            pendingTodoAdapter=new PendingTodoAdapter(pendingTodoModels,this,this);
        }
        linearLayoutManager=new LinearLayoutManager(this);
        pendingTodos.setAdapter(pendingTodoAdapter);
        pendingTodos.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(pendingTodoAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(pendingTodos);

        addNewTodo=(FloatingActionButton)findViewById(R.id.fabAddTodo);
        addNewTodo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAddTodo:
                if(tagDBHelper.countTags() >= 1){ // Weird fanegaling done by cori to get the right dialog to open
                    showDialog();
                }else{
                    showNewTodoDialog();
                }
                break;
        }
    }


    // Adds extra functionality when the back button is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Adds the drawer layout
    private void showDrawerLayout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,(Toolbar) findViewById(R.id.toolbar) , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Initializes the navigation menu
    private void navigationMenuInit(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // This enables the dropdown menu from the front page / pending activities page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pending_task_options,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<PendingTodoModel> newPendingTodoModels=new ArrayList<>();
                for(PendingTodoModel pendingTodoModel:pendingTodoModels){
                    String getTodoTitle=pendingTodoModel.getTodoTitle().toLowerCase();
                    String getTodoContent=pendingTodoModel.getTodoContent().toLowerCase();
                    String getTodoTag=pendingTodoModel.getTodoTag().toLowerCase();

                    if(getTodoTitle.contains(newText) || getTodoContent.contains(newText) || getTodoTag.contains(newText)){
                        newPendingTodoModels.add(pendingTodoModel);
                    }
                }
                pendingTodoAdapter.filterTodos(newPendingTodoModels);
                pendingTodoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Sets the actions of which option is selected from the drop down menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                return true;
            case R.id.all_tags:
                startActivity(new Intent(this,AllTags.class));
                return true;
            case R.id.completed:
                startActivity(new Intent(this,CompletedTodos.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this,AppSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This is the actions of the drawer menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pending_todos) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.completed_todos) {
            startActivity(new Intent(this,CompletedTodos.class));
        } else if (id == R.id.tags) {
            startActivity(new Intent(this,AllTags.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(this,AppSettings.class));
        } else if (id == R.id.github) {
            IntentExtras.findOnGithub(MainActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //show dialog if there is no tag in the database
    // This shouldn't show at all unless you're on the tag page itself
    private void showNewTodoDialog(){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(R.string.tag_create_dialog_title_text);
            builder.setMessage(R.string.no_tag_in_the_db_text);
            builder.setPositiveButton(R.string.create_new_tag, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this,AllTags.class));
                }
            }).setNegativeButton(R.string.tag_edit_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
    }



    //show add new todos dialog and adding the todos into the database
    private void showDialog(){
        //getting current calendar credentials
        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        // Builds the add new dialog
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.add_new_todo_dialog,null);
        builder.setView(view);
        SettingsHelper.applyThemeTextView((TextView)view.findViewById(R.id.add_todo_dialog_title),this); // Settings

        //finding the input text fields of add_new_todo_dialog
        final TextInputEditText todoTitle=(TextInputEditText)view.findViewById(R.id.todo_title);
        final TextInputEditText todoContent=(TextInputEditText)view.findViewById(R.id.todo_content);

        // ATTRIBUTE SPINNER
        final Spinner todoTags=(Spinner)view.findViewById(R.id.todo_tag);
        // Stores all the tags title in string format
        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, tagDBHelper.fetchTagStrings());
        // Setting dropdown view resouce for spinner
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the spinner adapter
        todoTags.setAdapter(tagsModelArrayAdapter);
        todoTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTagTitleString = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Finding the date and time for the activity reminder
        final TextInputEditText todoDate=(TextInputEditText)view.findViewById(R.id.todo_date);
        final TextInputEditText todoTime=(TextInputEditText)view.findViewById(R.id.todo_time);

        // Getting the tododate
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR,i);
                        calendar.set(Calendar.MONTH,i1);
                        calendar.set(Calendar.DAY_OF_MONTH,i2);
                        todoDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        // Getting the todos time
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY,i);
                        calendar.set(Calendar.MINUTE,i1);
                        String timeFormat=DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });

        // Finding the Cancel and Add to do Buttons of add_new_todo_dialog
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
        TextView addTodo=(TextView)view.findViewById(R.id.add_new_todo);

        // Applies settings
        SettingsHelper.applyTextColor(cancel,this);
        SettingsHelper.applyTextColor(addTodo,this);

        // Sends information to todoDBHelper
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the values from add new todos dialog
                String getTodoTitle=todoTitle.getText().toString();
                String getTodoContent=todoContent.getText().toString();
                int todoTagID=tagDBHelper.fetchTagID(getTagTitleString);
                String getTodoDate=todoDate.getText().toString();
                String getTime=todoTime.getText().toString();

                //checking the data fiels
                boolean isTitleEmpty=todoTitle.getText().toString().isEmpty();
                boolean isContentEmpty=todoContent.getText().toString().isEmpty();
                boolean isDateEmpty=todoDate.getText().toString().isEmpty();
                boolean isTimeEmpty=todoTime.getText().toString().isEmpty();

                //adding the todos
                if(isTitleEmpty){
                    todoTitle.setError("Title Required!");
                //}else if(todoDBHelper.addNewTodo(new PendingTodoModel(getTodoTitle,getTodoContent, getPriority, getDifficulty, getRepeat, String.valueOf(todoTagID),getTodoDate,getTime)
                }else if(todoDBHelper.addNewTodo(new PendingTodoModel(getTodoTitle,getTodoContent, String.valueOf(todoTagID), getTodoDate, getTime)
                )){
                    Toast.makeText(MainActivity.this, R.string.todo_title_add_success_msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
