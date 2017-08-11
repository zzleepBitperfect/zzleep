package pe.geekadvice.zzleep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.geekadvice.utils.JsonTask;
import pe.geekadvice.zzleep.adapters.StopItemAdapter;
import pe.geekadvice.zzleep.adapters.StopItemAdapterSinActivador;
import pe.geekadvice.zzleep.adapters.ZzleepAlarm;
import pe.geekadvice.zzleep.adapters.ZzleepAudio;
import pe.geekadvice.zzleep.adapters.ZzleepAudioAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepPlace;

/**
 * Created by YaguarRuna on 19/06/2017.
 */

public class ListaParaderos extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ArrayList<ZzleepPlace> arrayList;
    private ArrayList<ZzleepPlace> destinies;
    private StopItemAdapterSinActivador destinyadapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        init();
    }
    void init(){
        arrayList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();

        StopItemAdapter adaptador = new StopItemAdapter(this,0, arrayList);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        JsonTask peticionParaderos = new JsonTask(ListaParaderos.this, 6);
        String aux_url="http://clientes.geekadvice.pe/zzleep-server/public/api/v1/places?user_id=" + user.getUid();
        peticionParaderos.execute(aux_url);

        ListView lv = (ListView) findViewById(R.id.listaPlaces);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adaptador);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ZzleepPlace place= (ZzleepPlace) parent.getAdapter().getItem(position);

    }

    public void getArrayStopItem(String result) {
        try {
            destinies = new ArrayList<>();
            JSONObject auxStop = new JSONObject(result);

            JSONArray parentArray = auxStop.getJSONArray("data");
            //se recorre el json de paraderos
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                String name = finalObject.getString("name");
                String lat = finalObject.getString("lat");
                String lng = finalObject.getString("lng");
                String address = finalObject.getString("address");
                String type = finalObject.getString("type");
                String image = finalObject.getString("image");

                Double range = 100D;
                if (!finalObject.get("range").equals(null)) {
                    range = Double.parseDouble(finalObject.getString("range"));
                }
                ZzleepPlace destiny;
                //posible for para guardar alarma y audio
                destiny = new ZzleepPlace(name, address, lat, lng, range, type, image);
                if (!finalObject.getString("id").equals(null)) {
                    int id = finalObject.getInt("id");
                    destiny.setId(id);
                }
                JSONObject auxAlarma=finalObject.getJSONObject("alarm");
                JSONObject auxSound=finalObject.getJSONObject("sound");
                ZzleepAlarm alarmaAux= new ZzleepAlarm(auxAlarma.getString("name"),"",0,0,"","");
                ZzleepAudio audioAux= new ZzleepAudio(0,auxSound.getString("name"),"","",0,0.0,0.0,0,"",0);
                destiny.alarma=alarmaAux;
                destiny.audio=audioAux;
                destinies.add(destiny);

            }
            destinyadapter = new StopItemAdapterSinActivador(this, 0, destinies);

            //Asociamos el adaptador a la vista.
            ListView lv = (ListView) findViewById(R.id.listaPlaces);
            lv.setOnItemClickListener(this);
            lv.setAdapter(destinyadapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
