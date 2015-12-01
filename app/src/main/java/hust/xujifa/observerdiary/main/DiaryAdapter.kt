package hust.xujifa.observerdiary.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hust.xujifa.observerdiary.R
import hust.xujifa.observerdiary.helper.Diary2

/**
 * Created by xujifa on 2015/11/29.
 */
class DiaryAdapter(internal var diaries:List<Diary2>): RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {
    var context=null as Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder? {
        context=parent.context
        Log.d("AAA","adapter");
        val view= LayoutInflater.from(context).inflate(R.layout.diary_card,parent,false)
        val dh=DiaryHolder(view)
        return dh
    }

    override fun getItemCount(): Int {
        return diaries.size
    }

    override fun onBindViewHolder(holder: DiaryHolder?, position: Int) {

    }

    class DiaryHolder(view: View): RecyclerView.ViewHolder(view) {

    }
}