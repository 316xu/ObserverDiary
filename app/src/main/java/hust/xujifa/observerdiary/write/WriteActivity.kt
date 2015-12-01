package hust.xujifa.observerdiary.write

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import hust.xujifa.observerdiary.R
import hust.xujifa.observerdiary.helper.*
import hust.xujifa.observerdiary.ui.Continuousslider
import hust.xujifa.observerdiary.ui.Recoder
import hust.xujifa.observerdiary.ui.TestPlayer
import hust.xujifa.observerdiary.ui.TrigleToRect
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xujifa on 2015/11/12.
 */
class WriteActivity:AppCompatActivity(),View.OnClickListener{

    var cd=null as AlertDialog
    var toolbarlay=null as CollapsingToolbarLayout
    var appbar=null as AppBarLayout
    var toolbar=null as Toolbar
    var testlay=null as RelativeLayout
    var camera=null as FloatingActionButton
    var content_bg=null as LinearLayout
    var title=null as EditText
    var imageuri=null as Uri
    var image=null as ImageView
    var id=0
    val TAKE_PHOTO=1000
    var b:Bitmap=null as Bitmap
    var color=0
    var start=false
    var recod=null as MediaRecorder
    var player=null as MediaPlayer
    var speakB=null as AlertDialog.Builder
    var audio_play=null as TrigleToRect
    var delete_img=null as ImageView
    var audio_palyer=null as TestPlayer
    var myapp=null as MyApp
    var gps=null as GpsTracker
    var lat=0.0f
    var lng=0.0f
    var content=null as EditText
    var diary2:Diary2=Diary2()

    var playing=false
    val cancleplaying:Handler=object:Handler(){
        override fun handleMessage(msg: Message){
            if(msg.what==0){
                playing=false;
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        val ext:Bundle=intent.extras
        id= ext.getInt("id")
        diary2.id=id
        audio_play= TrigleToRect(this)
        delete_img=findViewById(R.id.delete_image)as ImageView
        image=findViewById(R.id.image) as ImageView
        toolbarlay=findViewById(R.id.toolbarlayout) as CollapsingToolbarLayout
        appbar=findViewById(R.id.appbar) as AppBarLayout
        toolbar=findViewById(R.id.toolbar) as Toolbar
        testlay=findViewById(R.id.testlay) as RelativeLayout
        content=findViewById(R.id.content) as EditText
        content_bg=findViewById(R.id.content_bg) as LinearLayout
        setSupportActionBar(toolbar)
        supportActionBar.setHomeButtonEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)


        camera=findViewById(R.id.camera) as FloatingActionButton
        title=findViewById(R.id.title) as EditText
        audio_palyer=findViewById(R.id.player) as TestPlayer


        toolbarlay.title=" "

        image.setOnClickListener(this)
        camera.setOnClickListener(this)
        delete_img.setOnClickListener(this)
        content_bg.setOnClickListener(this)
        audio_palyer.setOnClickListener(this)
        audio_palyer.setH(cancleplaying);
        myapp= MyApp.getInstance()

        gps=GpsTracker(this@WriteActivity)
        lat=gps.latitude.toFloat()
        lng=gps.longitude.toFloat()
        diary2.lat=lat
        diary2.lng=lat
        val params = HashMap<String, String>()

        params.put("location", java.lang.Float.toString(lat) + "," + java.lang.Float.toString(lng))
        params.put("output", "json")
        title.hint = SimpleDateFormat("yyyy-MM-dd").format(Date())
        diary2.time=SimpleDateFormat("yyyy-MM-dd:HH/mm").format(Date())
        val df = SimpleDateFormat("yyyyMMddHHmmss")
        val weatherUrl = java.lang.String.format("https://route.showapi.com/9-5?from=1&lat=%f&lng=%f&needIndex=0&needMoreDay=0&showapi_appid=4386&showapi_timestamp=%s&showapi_sign=5594ed6589cc46dca8a46192a231b50c",
                lat, lng, df.format(Date()))


        val request= JsonObjectRequest(weatherUrl, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) {
                diary2.loc = response.getJSONObject("showapi_res_body").getJSONObject("cityInfo")
                        .getString("c7") + "," + response.getJSONObject("showapi_res_body").getJSONObject("cityInfo")
                        .getString("c3")
                title.hint = title.hint.toString() + "@" + diary2.loc
                diary2.we = response.getJSONObject("showapi_res_body").getJSONObject("f1").getString("day_weather")
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
            }
        })
        myapp.addToQueue(request, this@WriteActivity.javaClass.simpleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            TAKE_PHOTO->{
                if(resultCode== Activity.RESULT_OK) {
                    diary2.photo=1;

                    val opts = BitmapFactory.Options()
                    opts.inSampleSize=20
                    image.setImageBitmap(BitmapFactory.decodeFile(Utils.filePath+"/"+(id)+".jpg",opts))




                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.detail->1==1;
            R.id.delete->1==1;
            R.id.color->{
                val colordialog=AlertDialog.Builder(this)
                //<!--color picker-->//
                var b=AlertDialog.Builder(this)
                var bd:AlertDialog
                colordialog.setView(R.layout.color_dialog).setTitle("选择日记颜色")
                colordialog.setPositiveButton("自定义",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        b.setView(R.layout.color_picker).setPositiveButton("ok",object:DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                toolbarlay.setStatusBarScrimColor(Color.rgb((color and 0xff0000) shr 16,
                                        (color and 0x00ff00)shr 8,(color and 0x0000ff)))
                                toolbarlay.setBackgroundColor(Color.rgb((color and 0xff0000) shr 16,
                                        (color and 0x00ff00)shr 8,(color and 0x0000ff)))
                                toolbarlay.setContentScrimColor(Color.rgb((color and 0xff0000) shr 16,
                                        (color and 0x00ff00)shr 8,(color and 0x0000ff)))
                                diary2.color=color;
                            }
                        }).setNegativeButton("取消",object:DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                cd.hide()
                            }
                        })
                        bd=b.show()


                        val red=bd.findViewById(R.id.red) as TextView
                        val green=bd.findViewById(R.id.green) as TextView
                        val blue=bd.findViewById(R.id.blue) as TextView
                        val colortext=bd.findViewById(R.id.colortext) as TextView
                        val hred:Handler=object:Handler(){
                            override fun handleMessage(msg: Message){
                                red.text=(255*msg.what/1000).toString()
                                color=(red.text.toString().toInt()*65536)+(green.text.toString().toInt()*256)+blue.text.toString().toInt()
                                colortext.text="#"+Integer.toString(color,16).toUpperCase()
                                colortext.setBackgroundColor(Color.rgb(red.text.toString().toInt(),green.text.toString().toInt(),
                                        blue.text.toString().toInt()))

                            }
                        }
                        val hgreen:Handler=object:Handler(){
                            override fun handleMessage(msg: Message){
                                green.text=(255*msg.what/1000).toString()
                                color=(red.text.toString().toInt()*65536)+(green.text.toString().toInt()*256)+blue.text.toString().toInt()
                                colortext.text="#"+Integer.toString(color,16).toUpperCase()
                                colortext.setBackgroundColor(Color.rgb(red.text.toString().toInt(),green.text.toString().toInt(),
                                        blue.text.toString().toInt()))
                            }
                        }
                        val hblue:Handler=object:Handler(){
                            override fun handleMessage(msg: Message){
                                blue.text=(255*msg.what/1000).toString()
                                color=(red.text.toString().toInt()*65536)+(green.text.toString().toInt()*256)+blue.text.toString().toInt()
                                colortext.text="#"+Integer.toString(color,16).toUpperCase()
                                colortext.setBackgroundColor(Color.rgb(red.text.toString().toInt(),green.text.toString().toInt(),
                                        blue.text.toString().toInt()))                            }
                        }
                        (bd.findViewById(R.id.red_slider) as Continuousslider).handler=hred
                        (bd.findViewById(R.id.blue_slider) as Continuousslider).handler=hblue
                        (bd.findViewById(R.id.green_slider) as Continuousslider).handler=hgreen
                    }

                }).setNegativeButton("取消",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        cd.hide()
                    }
                })

                cd=colordialog.show()
                cd.findViewById(R.id.gray).setOnClickListener(object:View.OnClickListener{
                    override fun onClick(v: View){
                        toolbarlay.setStatusBarScrimColor(R.color.gray)
                        toolbarlay.setBackgroundColor(R.color.gray)
                        toolbarlay.setContentScrimColor(R.color.gray)
                        diary2.color=R.color.gray
                        cd.hide()
                    }
                })
                cd.findViewById(R.id.blue).setOnClickListener(object:View.OnClickListener{
                    override fun onClick(v: View){
                        toolbarlay.setStatusBarScrimColor(R.color.blue)
                        toolbarlay.setBackgroundColor(R.color.blue)
                        toolbarlay.setContentScrimColor(R.color.blue)
                        diary2.color=R.color.blue
                        cd.hide()
                    }
                })
                cd.findViewById(R.id.green).setOnClickListener(object:View.OnClickListener{
                    override fun onClick(v: View){
                        toolbarlay.setStatusBarScrimColor(R.color.green)
                        toolbarlay.setBackgroundColor(R.color.green)
                        toolbarlay.setContentScrimColor(R.color.green)
                        diary2.color=R.color.green
                        cd.hide()
                    }
                })
                cd.findViewById(R.id.red).setOnClickListener(object:View.OnClickListener{
                    override fun onClick(v: View){
                        toolbarlay.setStatusBarScrimColor(R.color.red)
                        toolbarlay.setBackgroundColor(R.color.red)
                        toolbarlay.setContentScrimColor(R.color.red)
                        diary2.color=R.color.red
                        cd.hide()
                    }
                })
            }
            R.id.audio->{
                val path = Utils.filePath + "/" + id.toString() + ".3gp";
                speakB=AlertDialog.Builder(this@WriteActivity)
                speakB.setView(R.layout.dialog_audio)

                val d=speakB.show()
                d.setOnCancelListener(object:DialogInterface.OnCancelListener{
                    override fun onCancel(dialog: DialogInterface?) {
                        if(start==true){
                            recod.stop()
                            start=false;
                            recod.release()
                        }
                    }
                })
                val recoder=d.findViewById(R.id.recoder) as Recoder
                val recode:Handler=object:Handler(){
                    override fun handleMessage(msg: Message){
                        if(msg.what==1){
                            recod = MediaRecorder()
                            recod.setAudioSource(1)
                            recod.setOutputFormat(1)
                            recod.setOutputFile(path)
                            recod.setAudioEncoder(1)
                            recod.prepare()
                            recod.start();
                            start=true
                            audio_palyer.visibility=View.VISIBLE
                        }else if(msg.what==0){
                            recod.stop()
                            recod.release()
                            diary2.recod=1
                            start=false
                        }
                    }
                }
               recoder.setHandler(recode);

            }
            else->{

                val intent =  Intent();
                if(diary2.title==""&&diary2.content==""&&diary2.photo==0&&diary2.recod==0) {
                    finish()
                    setResult(RESULT_CANCELED, intent);

                }else {
                    diary2.title = title.text.toString()
                    diary2.content = content.text.toString()
                    val s = SqlHelper(applicationContext)
                    s.open()
                    s.insert(diary2)
                    setResult(RESULT_CANCELED, intent);
                    finish()
                }
            }
        }

        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent =  Intent();
            if(diary2.title==""&&diary2.content==""&&diary2.photo==0&&diary2.recod==0) {
                setResult(RESULT_CANCELED, intent);
                finish()
                return true
            }else {
                diary2.title = title.text.toString()
                diary2.content = content.text.toString()
                val s = SqlHelper(applicationContext)
                Log.d("AAA-write", diary2.toString());
                s.open()
                s.insert(diary2)
                setResult(RESULT_OK, intent);

                finish()
                return true;
            }
        }
        return false
    }

    override fun onClick(v: View) {

        when(v.id){
            R.id.camera-> {
                imageuri = Uri.fromFile(File(Utils.filePath, id.toString()+".jpg"))

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)

                startActivityForResult(intent, TAKE_PHOTO)

            }
            R.id.image->{
                val dialog= Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_image)
                (dialog.findViewById(R.id.image) as ImageView).setImageBitmap(b)
                dialog.show()
            }
            R.id.delete_image->{

                val f=File(Utils.filePath,id.toString()+".jpg")
                f.delete()
                image.visibility=View.GONE
                delete_img.visibility=View.GONE
                diary2.photo=0;
            }
            R.id.content_bg->{
                content.requestFocus()
            }
            R.id.player->{
                if(playing==false) {
                    player = MediaPlayer()
                    val path = Utils.filePath + "/" + id.toString() + ".3gp";
                    val f = File(path)
                    if (f.exists()) {
                        player.setDataSource(path)
                        player.prepare()
                        player.start()
                        audio_palyer.start(player.duration)
                        playing=true
                    }
                }else{
                    player.stop()
                    playing=false

                   audio_palyer.stop()
                }


            }
        }
    }


}