package eu.epitech.vladwp.appcontacts;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public List<Model> ListModel;
    public DBHandler myDB;
    private MyAdapter monAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        FloatingActionButton buttonAdd = (FloatingActionButton) findViewById(R.id.add_task);
        myDB = new DBHandler(this);
        ListModel = myDB.getAllContacts();
        monAdapter = new MyAdapter(this, ListModel);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        monAdapter.setOnItemClickListener(new MyAdapter.ClickListener() {
            @Override
            public void onClick(MyAdapter.MyViewHolder holder, int position) {
                Log.e("ClickNormal", Integer.valueOf(holder.IdDBElement).toString());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constantes.ID_KEY, Integer.valueOf(holder.IdDBElement).toString());
                intent.putExtra(Constantes.NAME_KEY, holder.mTextName.getText().toString());
                intent.putExtra(Constantes.NUMBER_KEY, holder.mTextNumber.getText().toString());
                startActivity(intent);
            }


            /*@Override
            public void clickDelete(int position, MyAdapter.MyViewHolder holder) {
                Log.e("DELETE", "onItemLongClick pos = " + position + "IDELEMENT : "  + Integer.valueOf(holder.IdDBElement).toString());
                myDB.deleteContact(Integer.valueOf(holder.IdDBElement).toString());
                monAdapter.remove(position);
            }*/
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNew = new Intent(MainActivity.this, AddtaskActivity.class);
                startActivity(intentNew);
            }
        });

        mRecyclerView.setAdapter(monAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1 :
                myDB.deleteContact(Integer.valueOf(monAdapter.getIDitemRemove(item.getGroupId())).toString());
                monAdapter.remove(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
       if (ListModel.isEmpty()){
            ListModel = myDB.getAllContacts();
            monAdapter = new MyAdapter(MainActivity.this, ListModel);
            mRecyclerView.setAdapter(monAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ListModel.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Model> newList = new ArrayList<>();
        ListModel = myDB.getAllContacts();

        for(Model ListModel1 : ListModel){
            if (ListModel1.getName().toLowerCase().contains(userInput) || ListModel1.getNumber().toLowerCase().contains(userInput)){
                newList.add(ListModel1);
            }
        }
        monAdapter.updateList(newList);
        return true;
    }
}
