package hust.xujifa.observerdiary.helper

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.*

/**
 * Created by xujifa on 2015/11/13.
 */
class SqlHelper
{
    var openHelper:SqlOpenHelper=null as SqlOpenHelper
    var db:SQLiteDatabase=null as SQLiteDatabase
    val allC=arrayOf(SqlOpenHelper.ID,SqlOpenHelper.TIME,SqlOpenHelper.LOC,
            SqlOpenHelper.TITLE,SqlOpenHelper.CONTENT,SqlOpenHelper.LAT,SqlOpenHelper.LNG
            ,SqlOpenHelper.WEATHER,SqlOpenHelper.COLOR,SqlOpenHelper.PHOTO,SqlOpenHelper.RECOD);

    constructor(context: Context){
        openHelper= SqlOpenHelper(context)
    }
    fun open(){
        db=openHelper.writableDatabase
    }
    fun close(){
        db.close()
    }
    fun insert(time:String,loc:String,title:String,content:String,lat:Float,lng:Float,we:String,color:Int,Photo:Int,recod:Int){
        db.execSQL("INSERT INTO diary ("+allC[1]+","+allC[2]+","+allC[3]+","+allC[4]+","+
                allC[5]+","+allC[6]+","+allC[7]+","+allC[8]+","+allC[9]+","+allC[10]+")VALUES("+
                "\""+time+"\",\""+loc+"\",\""+title+"\",\""+content+"\",\""+lat+"\",\""+lng+"\",\""+we+"\","+color+"\",\""+Photo+"\",\""+recod+
                ")")
    }
    fun insert(diary:Diary2){
        db.execSQL("INSERT INTO diary ("+allC[1]+","+allC[2]+","+allC[3]+","+allC[4]+","+
                allC[5]+","+allC[6]+","+allC[7]+","+allC[8]+","+allC[9]+","+allC[10]+")VALUES("+
                "\""+diary.time+"\",\""+diary.loc+"\",\""+diary.title+"\",\""+diary.content+"\",\""+diary.lat+"\",\""+diary.lng+"\",\""+diary.we+"\",\""+diary.color+"\",\""+diary.photo+"\",\""+diary.recod+"\""+
                ")")
    }

    fun getAll(diaries: ArrayList<Diary2>){
        val cursor:Cursor =db.query("diary",allC,null,null,null,null,null)

        cursor.moveToFirst()
        while(!cursor.isAfterLast){
            val a:Diary2= Diary2(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),cursor.getString(4),
                    cursor.getFloat(5),cursor.getFloat(6),cursor.getString(7),
                    cursor.getInt(8),cursor.getInt(9),cursor.getInt(10))
            diaries.add(0,a)
            Log.d("AAA",a.toString());
            cursor.moveToNext()
        }
        cursor.close()
    }
    fun delete(id:String){
        db.execSQL("DELETE FROM diary WHERE _id="+id)
    }
}