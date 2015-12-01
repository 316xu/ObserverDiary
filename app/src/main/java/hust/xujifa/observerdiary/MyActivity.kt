package hust.xujifa.observerdiary

import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import hust.xujifa.observerdiary.helper.*
import hust.xujifa.observerdiary.main.DiaryListFragment
import hust.xujifa.observerdiary.main.TestAdapter
import hust.xujifa.observerdiary.ui.FloatingActionButtonMenu
import hust.xujifa.observerdiary.ui.Recoder
import hust.xujifa.observerdiary.write.WriteActivity
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xujifa on 2015/11/11.
 */
class MyActivity: AppCompatActivity() {
    var fabm:FloatingActionButtonMenu =null as FloatingActionButtonMenu
    var id=0;
    var sqlO: SqlHelper = null as SqlHelper
    var diaries=null as ArrayList<Diary2>
    var sp:SharedPreferences=null as SharedPreferences
    var editor:SharedPreferences.Editor =null as SharedPreferences.Editor
    var recod=null as MediaRecorder
    var gps=null as GpsTracker
    var lat=0f
    var lng=0f
    var loc=""
    var we=""
    var myapp= MyApp.getInstance()
    var imageUri=null as Uri
    val TAKE_PHOTO=2000
    val WRITE=3000
    var d=null as AlertDialog
    var frame=null as FrameLayout
    var manger=null as FragmentManager
    var rv=null as RecyclerView
    var myAdapter=null as TestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my);
        sp=getSharedPreferences("observerdiary", Context.MODE_PRIVATE)
        editor=sp.edit()


        fabm=findViewById(R.id.fabm) as FloatingActionButtonMenu
        manger=fragmentManager
        sqlO= SqlHelper(getApplicationContext())
        diaries=ArrayList()
        rv=findViewById(R.id.diary_list) as RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //sqlO.getAll(diaries)
        myAdapter  = TestAdapter(diaries);
        rv.setAdapter(myAdapter);
        sqlO.open()
        val dir:File=Environment.getExternalStorageDirectory()
        if(!sp.getBoolean("started",false)) {
            val f: File = File(dir, "ObserverDiary")
            f.mkdir()
            editor.putBoolean("started",true)
            editor.putString("path",f.toString())
            editor.apply()
        }
        Utils.filePath=sp.getString("path","")
        fabm.start(300)


    }

    override fun onResume() {

        super.onResume()
        Log.d("AAA","resume")
        gps= GpsTracker(this@MyActivity)

        diaries.clear()
        sqlO.getAll(diaries)
        myAdapter.notifyDataSetChanged()
        if(diaries.size==0){
            id=1;
        }
        else
            id= diaries[0].id+1

        lat=gps.latitude.toFloat()
        lng=gps.longitude.toFloat()
        val params = HashMap<String, String>()

        params.put("location", java.lang.Float.toString(lat) + "," + java.lang.Float.toString(lng))
        params.put("output", "json")
        val df = SimpleDateFormat("yyyyMMddHHmmss")
        val weatherUrl = java.lang.String.format("https://route.showapi.com/9-5?from=1&lat=%f&lng=%f&needIndex=0&needMoreDay=0&showapi_appid=4386&showapi_timestamp=%s&showapi_sign=5594ed6589cc46dca8a46192a231b50c",
                lat, lng, df.format(Date()))


        val request= JsonObjectRequest(weatherUrl, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) {
                try {
                    loc = response.getJSONObject("showapi_res_body").getJSONObject("cityInfo")
                            .getString("c7") + "," + response.getJSONObject("showapi_res_body").getJSONObject("cityInfo")
                            .getString("c3")
                    we = response.getJSONObject("showapi_res_body").getJSONObject("f1").getString("day_weather")
                }catch(e:Exception){
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
            }
        })
        myapp.addToQueue(request, this@MyActivity.javaClass.simpleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK){
            when(requestCode){
                TAKE_PHOTO->{
                    val di=Diary2()
                    di.photo=1
                    val db=AlertDialog.Builder(this@MyActivity)
                    db.setView(R.layout.dialog_camera)
                    db.setPositiveButton("ok", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            di.content=(d.findViewById(R.id.edit) as EditText).text.toString()

                            save(di)
                        }
                    })


                    d=db.show()

                    val opts = BitmapFactory.Options()
                    opts.inSampleSize=20
                    (d.findViewById(R.id.photo) as ImageView).setImageBitmap(BitmapFactory.decodeFile(Utils.filePath+"/"+(id)+".jpg",opts))

                }
                WRITE->{
                    id++
                }
            }
        }
    }

    fun clickB(view:View){

        when(view.id){
            R.id.expand->{
                fabm.start(500)
            }
            R.id.write->{
                val i:Intent=Intent(this@MyActivity, WriteActivity::class.java)
                i.putExtra("id",id);

                startActivityForResult(i,WRITE)
            }
            R.id.speak->{
                val path = Utils.filePath + "/" + id.toString() + ".3gp";
                val speakB= AlertDialog.Builder(this@MyActivity)
                speakB.setView(R.layout.dialog_audio)

                val d=speakB.show()
                val dr=Diary2()
                val recoder=d.findViewById(R.id.recoder) as Recoder
                val recode: Handler =object: Handler(){
                    override fun handleMessage(msg: Message){
                        if(msg.what==1){
                            recod = MediaRecorder()
                            recod.setAudioSource(1)
                            recod.setOutputFormat(1)
                            recod.setOutputFile(path)
                            recod.setAudioEncoder(1)
                            recod.prepare()
                            recod.start();
                            dr.recod=1
                        }else if(msg.what==0){
                            recod.stop()
                            recod.release()


                        }
                    }
                }
                d.setOnCancelListener(object:DialogInterface.OnCancelListener{
                    override fun onCancel(dialog: DialogInterface?) {
                        if(dr.recod==1){
                            save(dr)

                        }
                    }
                })
                recoder.handler = recode;
            }
            R.id.camera->{
                imageUri = Uri.fromFile(File(Utils.filePath, id.toString()+".jpg"))

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                startActivityForResult(intent, TAKE_PHOTO)
            }

        }


    }
    fun save(dr:Diary2){

        dr.we=we
        dr.lat=lat
        dr.lng=lng
        dr.loc=loc
        dr.id=id
        id++
        dr.time=SimpleDateFormat("yyyy-MM-dd:HH/mm").format(Date())
        sqlO.insert(dr)
        diaries.add(0,dr)
        myAdapter.notifyDataSetChanged()
        Log.d("AAA-my",dr.toString())
    }
}