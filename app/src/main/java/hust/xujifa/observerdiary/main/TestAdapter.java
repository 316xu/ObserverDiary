package hust.xujifa.observerdiary.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hust.xujifa.observerdiary.R;
import hust.xujifa.observerdiary.helper.Diary2;
import hust.xujifa.observerdiary.helper.Utils;
import hust.xujifa.observerdiary.ui.TestPlayer;
import hust.xujifa.observerdiary.ui.TrigleToRect;

/**
 * Created by xujifa on 2015/11/30.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.DiaryHolder>{
    MediaPlayer player;
    List<Diary2> list;
    boolean playing=false;
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0)playing=false;
        }
    };
    BitmapFactory.Options options=new BitmapFactory.Options();
    public TestAdapter(List<Diary2> l){
        this.list=l;
        Log.d("AAA", "adapter"+list.size());
        options.inSampleSize=5;
        player=new MediaPlayer();

    }

    @Override
    public DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_card,parent,false);

        return new DiaryHolder(v);
    }

    @Override
    public void onBindViewHolder(final DiaryHolder holder, int position) {
        final Diary2 d=list.get(position);
        int id=d.id;
        int flag=0;
        Log.d("AAA-read",d.toString());
        if(d.photo==1){
            holder.cont.setVisibility(View.VISIBLE);
            flag=2;

            holder.title.setText(d.title);


            options.inSampleSize=20;
            holder.image.setImageBitmap(BitmapFactory.decodeFile(Utils.filePath + "/" + (id) + ".jpg", options));

        }else{
            holder.cont.setVisibility(View.GONE);
        }


        Drawable d1 = holder.card.getBackground();
        d1.setColorFilter(Color.rgb((d.color & 0xff0000) >> 16,
                (d.color & 0x00ff00) >> 8, (d.color & 0x0000ff)), PorterDuff.Mode.MULTIPLY);
        holder.card.setBackgroundDrawable(d1);
        if(d.recod==1){
            holder.player.setVisibility(View.VISIBLE);
            holder.player.setH(h);
            holder.player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(playing==false) {
                        player=new MediaPlayer();
                        String path = Utils.filePath + "/" + d.id + ".3gp";
                        File f =new File(path);
                        if (f.exists()) {
                            try {
                                player.setDataSource(path);
                                player.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.start();
                            holder.player.start(player.getDuration());
                            playing=true;
                        }
                    }else{
                        player.stop();
                        playing=false;
                        holder.player.stop();
                    }
                }
            });
        }else{
            holder.player.setVisibility(View.GONE);
        }
        if(flag==1){
            holder.title2.setText(d.title);

        }else{
            holder.title2.setText(d.content);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DiaryHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        RelativeLayout text_panel;
        TextView title2;
        TextView info;
        TextView watch;
        TestPlayer player;
        CardView card;
        RelativeLayout cont;
        public DiaryHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.im);
            title=(TextView)itemView.findViewById(R.id.title);
            title2=(TextView)itemView.findViewById(R.id.title2);
            info=(TextView)itemView.findViewById(R.id.info);
            watch=(TextView)itemView.findViewById(R.id.watch);

            text_panel=(RelativeLayout)itemView.findViewById(R.id.text_panel);
            cont=(RelativeLayout)itemView.findViewById(R.id.image_container);
            card=(CardView)itemView.findViewById(R.id.card_view);
            player=(TestPlayer)itemView.findViewById(R.id.player);

        }
    }
}
