package eu.epitech.vladwp.appcontacts;

import android.content.Intent;
import android.support.annotation.Nullable;
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
    private SearchView searchView;


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
                intent.putExtra("MAPOSITION", position);
                intent.putExtra(Constantes.ID_KEY, Integer.valueOf(holder.IdDBElement).toString());
                intent.putExtra(Constantes.NAME_KEY, holder.mTextName.getText().toString());
                intent.putExtra(Constantes.NUMBER_KEY, holder.mTextNumber.getText().toString());
                startActivityForResult(intent, 2);
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
                startActivityForResult(intentNew, 1);
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
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                Log.e("RETOUR DB MAI", "RETOUR DB MAIN" + data.getExtras().getInt("IDRETOURDB"));
                ListModel.add(new Model(data.getExtras().getInt("IDRETOURDB"), data.getStringExtra(Constantes.NAME_KEY), data.getStringExtra(Constantes.NUMBER_KEY), data.getStringExtra(Constantes.EMAIL_KEY), data.getByteArrayExtra(Constantes.IMAGE_KEY)));
                synchronized (monAdapter){
                    monAdapter.notifyDataSetChanged();
                    onQueryTextChange("");
                    searchView.setQuery("", false);
                    searchView.setIconified(false);
                }
            }
            else if (requestCode == 2){
                ListModel.set(data.getExtras().getInt("MAPOSITIONRETOUR"), new Model(Integer.valueOf(data.getStringExtra(Constantes.ID_KEY)), data.getStringExtra(Constantes.NAME_KEY), data.getStringExtra(Constantes.NUMBER_KEY), data.getStringExtra(Constantes.EMAIL_KEY), data.getByteArrayExtra(Constantes.IMAGE_KEY)));
                monAdapter.notifyItemChanged(data.getExtras().getInt("MAPOSITIONRETOUR"));
                onQueryTextChange("");
                searchView.setQuery("", false);
                searchView.setIconified(false);
            }
        }
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
