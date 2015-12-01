package hust.xujifa.observerdiary.helper


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by xujifa on 2015/11/13.
 */
class SqlOpenHelper:SQLiteOpenHelper{

    companion object{
        val DATABASE_NAME="oberverdiary.db"
        val ID="_id"
        val TIME="_time"
        val LAT="_lat"
        var LNG="_lng"
        val LOC="_loc"
        val TITLE="_title"
        val CONTENT="_content"
        val WEATHER="_weather"
        val COLOR="_color"

        val PHOTO="_photo"
        val RECOD="_recod"





    }

    constructor(context: Context):super(context,DATABASE_NAME,null,1)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS diary ($ID integer primary key autoincrement,$TIME varchar(14),$LOC varchar(20),$TITLE varchar(20),$CONTENT text,$LAT float,$LNG float,$WEATHER varchar(20),$COLOR integer,$PHOTO tinyint,$RECOD tinyint);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "diary")
        onCreate(db)    }

}