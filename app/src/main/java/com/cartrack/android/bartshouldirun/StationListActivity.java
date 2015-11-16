package com.cartrack.android.bartshouldirun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class StationListActivity extends Activity {

    ArrayList<Station> mAllStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        mAllStations = new ArrayList<>();
        // Richmond
        mAllStations.add(new Station("Richmond","rich", -122.3526, 37.9372 ));
        mAllStations.add(new Station("El Cerrito del Norte", "deln", -122.3170, 37.9252));
        mAllStations.add(new Station("El Cerrito Plaza", "plza", -122.2990, 37.9027));
        mAllStations.add(new Station("North Berkeley", "nbrk", 0, 0));
        mAllStations.add(new Station("Downtown Berkeley", "dbrk",0,0));
        mAllStations.add(new Station("Ashby (Berkeley)", "ashb",0,0));
        // Pittsburg
        mAllStations.add(new Station("Pittsburg/Bay Point", "pitt",0,0));
        mAllStations.add(new Station("North Concord/Martinez", "ncon",0,0));
        mAllStations.add(new Station("Concord", "conc",0,0));
        mAllStations.add(new Station("Pleasant Hill", "phil",0,0));
        mAllStations.add(new Station("Walnut Creek", "wcrk",0,0));
        mAllStations.add(new Station("Lafayette", "lafy",0,0));
        mAllStations.add(new Station("Orinda", "orin",0,0));
        mAllStations.add(new Station("Rockridge (Oakland)", "rock",0,0));
        // Dublin
        mAllStations.add(new Station("Dublin/Pleasanton", "dubl",0,0));
        mAllStations.add(new Station("West Dublin", "wdub",0,0));
        mAllStations.add(new Station("Castro Valley", "cast",0,0));
        // Fremont
        mAllStations.add(new Station("Fremont", "frmt",0,0));
        mAllStations.add(new Station("Union City", "ucty",0,0));
        mAllStations.add(new Station("South Hayward", "shay",0,0));
        mAllStations.add(new Station("Hayward", "hayw",0,0));
        mAllStations.add(new Station("Bay Fair (San Leandro)", "bayf",0,0));
        mAllStations.add(new Station("San Leandro", "sanl",0,0));
        mAllStations.add(new Station("Coliseum", "cols",0,0));
        mAllStations.add(new Station("Fruitvale (Oakland)", "ftvl",0,0));
        mAllStations.add(new Station("Lake Merritt (Oakland)", "lake",0,0));
        // MacArthur ina
        mAllStations.add(new Station("MacArthur (Oakland)", "mcar",0,0));
        mAllStations.add(new Station("19th St. Oakland", "19th",0,0));
        mAllStations.add(new Station("12th St. Oakland City Center", "12th",0,0));
        mAllStations.add(new Station("West Oakland", "woak",0,0));
        // West Coast
        mAllStations.add(new Station("Embarcadero (SF)", "embr",0,0));
        mAllStations.add(new Station("Montgomery St. (SF)", "mont",0,0));
        mAllStations.add(new Station("Powell St. (SF)", "powl",0,0));
        mAllStations.add(new Station("Civic Center (SF)", "civc",0,0));
        mAllStations.add(new Station("16th St.Mission (SF)", "16th",0,0));
        mAllStations.add(new Station("24 th St.Mission(SF)", "24th",0,0));
        mAllStations.add(new Station("Glen Park (SF)", "glen",0,0));
        mAllStations.add(new Station("Balboa Park(SF)", "balb",0,0));
        mAllStations.add(new Station("Daly City", "daly",0,0));
        mAllStations.add(new Station("Colma", "colm",0,0));
        mAllStations.add(new Station("South San Francisco", "ssan",0,0));
        mAllStations.add(new Station("San Bruno", "sbrn",0,0));
        mAllStations.add(new Station("San Francisco Int'l Airport", "sfia", 0,0));
        mAllStations.add(new Station("Millbrae", "mlbr",0,0));

        mAllStations.add(new Station("Montgomery St. (SF)", "mont", -122.4007, 37.7904));

        final ListView stationList = (ListView) findViewById(R.id.stations_listview);
        StationListAdapter stationListAdapter = new StationListAdapter(this, mAllStations);
        stationList.setAdapter(stationListAdapter);
        stationList.setClickable(true);
        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(StationListActivity.this, TrainsActivity.class);
                Station station =  mAllStations.get(position);
                intent.putExtra("name", station.getName());
                intent.putExtra("abbr", station.getAbbr());
                StationListActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

}
