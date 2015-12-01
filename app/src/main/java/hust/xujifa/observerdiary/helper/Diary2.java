package hust.xujifa.observerdiary.helper;

/**
 * Created by xujifa on 2015/11/19.
 */
public class Diary2 {
    public int id;
    public String time;
    public String loc;
    public String title;
    public String content;
    public float lat;
    public float lng;
    public String we;
    public int color;
    public int photo;

    public Diary2(int id, String time, String loc, String title, String content, float lat, float lng, String we, int color, int photo, int recod) {
        this.id = id;
        this.time = time;
        this.loc = loc;
        this.title = title;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.we = we;
        this.color = color;
        this.photo = photo;
        this.recod = recod;
    }

    public int recod;

    public Diary2(int id, String time, String loc, String title, String content, float lat, float lng, String we, int color) {

        this.id = id;
        this.time = time;
        this.loc = loc;
        this.title = title;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.we = we;
        this.color = color;
    }

    public Diary2() {
        this.id = -1;
        this.time = "";
        this.loc = "";
        this.title = "";
        this.content = "";
        this.lat = 0f;
        this.lng = 0f;
        this.we = "";
        this.color =-1;
        this.recod=0;
        this.photo=0;




    }
    public String toString(){

        return id+","+time+","+loc+","+title+","+content+","+lat+","+lng+","+we+","+color+","+photo+","+recod;


    }
}
