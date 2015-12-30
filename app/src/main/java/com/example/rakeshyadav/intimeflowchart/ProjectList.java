package com.example.rakeshyadav.intimeflowchart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ProjectList extends AppCompatActivity {
    String fileN;
    String dirPath;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        show();
    }
    public void show()
    {
        File root = Environment.getExternalStorageDirectory();
        dirPath = root.getAbsolutePath() + File.separator + "DroiDia_Project";
        ListView listView = (ListView) findViewById(R.id.listView);
        File dir = new File(dirPath);
        File[] filelist = dir.listFiles();
        arrayList=new ArrayList<>();
        for(int i=0;i< filelist.length;i++)
        {
            arrayList.add(filelist[i].getName());
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileN = String.valueOf(parent.getItemAtPosition(position));
                Intent newActivity = new Intent(getBaseContext(), MainActivity.class);
                newActivity.putExtra("fname", fileN);
                startActivity(newActivity);
            }

        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;
        if (item.getTitle() == "Delete")
        {
            String name=arrayList.get( index);
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            String nameOfFile=dirPath + "/"+name;
            File file = new File(nameOfFile);
            boolean deleted = file.delete();
            show();
        }
        else {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_project_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.add_button)
        {
            startActivity(new Intent(this,AddProject.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
