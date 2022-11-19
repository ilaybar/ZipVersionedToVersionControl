package ilay.bar.uno;

import static ilay.bar.uno.Utils2.handleMainMenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordsActivity extends Activity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener
{

    Intent intent;

    TextView tvResult, tvDummy;
    ListView lvPlayers;
    MyArrayAdapter adapter;
    ArrayList<Player> players; //persons

    private void initData()
    {
        players = new ArrayList<Player>();
        players.add(new Player("Paul", this));
        players.add(new Player("John", this));
        players.add(new Player("George", this));
        players.add(new Player("Ringo", this));

        /*
		Bitmap photo = Utils.stringDrawableBitmap(this, "paul");
		items2.add(new Person("Paul", "McCartney", photo));
		 */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        initData();
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvDummy = (TextView) findViewById(R.id.tvDummy);
        lvPlayers = (ListView) findViewById(R.id.lvData);

        adapter = new MyArrayAdapter(this, players);

        lvPlayers.setAdapter(adapter);
        lvPlayers.setOnItemClickListener(this);
        lvPlayers.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (handleMainMenu(item, this))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void endActivity(View view){
        intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(getApplicationContext(), "select: " + players.get(position), Toast.LENGTH_LONG).show();
        tvResult.setText(players.get(position).toString());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(getApplicationContext(),
                "del: " + players.get(position),
                Toast.LENGTH_LONG).show();
        // players.remove(position);
        adapter.notifyDataSetChanged(); // Update the ListView + GridView
        // spinnerAdapter2.notifyDataSetChanged(); // Update the Spinner
        return true;  // i.e. all ended well
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        tvResult.setText(players.get(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}