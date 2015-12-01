package hust.xujifa.observerdiary.main

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hust.xujifa.observerdiary.R
import hust.xujifa.observerdiary.helper.Diary2
import hust.xujifa.observerdiary.helper.MyApp
import java.util.*

/**
 * Created by xujifa on 2015/11/29.
 */
class DiaryListFragment (): Fragment() {
    val myapp= MyApp.getInstance()
    var adapter=null as DiaryAdapter
    var rv=null as RecyclerView
    var lm=null as RecyclerView.LayoutManager
    var d=null as ArrayList<Diary2>
    fun setd(d:ArrayList<Diary2>){
        this.d=d
        adapter= DiaryAdapter(d)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AAA","dl")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rv=inflater.inflate(R.layout.main_recycler,container,false) as RecyclerView
        rv.setHasFixedSize(true)


        lm=LinearLayoutManager(context)
        rv.layoutManager=lm
        rv.adapter=adapter

        return rv
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}