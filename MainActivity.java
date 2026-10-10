package ru.granta.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    Dashboard dashboard;
    @Override public void onCreate(Bundle b) { super.onCreate(b); getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE); dashboard = new Dashboard(); setContentView(dashboard); }
    void launch(String action) {
        try {
            Intent i;
            switch(action) {
                case "maps": i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=")); break;
                case "music": i = new Intent("android.intent.action.MUSIC_PLAYER"); break;
                case "youtube": i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com")); break;
                case "radio": i = new Intent(Intent.ACTION_MAIN); i.addCategory(Intent.CATEGORY_APP_MUSIC); break;
                case "bluetooth": i = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS); break;
                case "settings": i = new Intent(Settings.ACTION_SETTINGS); break;
                case "apps": i = new Intent(Intent.ACTION_MAIN); i.addCategory(Intent.CATEGORY_HOME); break;
                case "phone": i = new Intent(Intent.ACTION_DIAL); break;
                default: i = new Intent(Intent.ACTION_MAIN); i.addCategory(Intent.CATEGORY_HOME);
            }
            startActivity(i);
        } catch(Exception e) { Toast.makeText(this, "Приложение не найдено на магнитоле", Toast.LENGTH_SHORT).show(); }
    }
    class Dashboard extends View {
        Canvas c; Paint p = new Paint(3); float sx, sy; long last=0; Bitmap car;
        int bg=Color.rgb(3,10,18), panel=Color.rgb(5,18,31), blue=Color.rgb(0,151,255), white=Color.rgb(242,247,255), muted=Color.rgb(174,198,220);
        Dashboard() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE,null); try { car=BitmapFactory.decodeResource(getResources(), R.drawable.lada_granta); } catch(Exception ignored){} }
        void fill(float l,float t,float r,float b,int color,float rad) { p.setColor(color); p.setStyle(Paint.Style.FILL); c.drawRoundRect(l,t,r,b,rad,rad,p); }
        void stroke(float l,float t,float r,float b,int color,float rad,float sw) { p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(sw);c.drawRoundRect(l,t,r,b,rad,rad,p);p.setStyle(Paint.Style.FILL); }
        void text(String s,float x,float y,float size,int color,boolean bold) { p.setColor(color);p.setTextSize(size);p.setTypeface(bold?Typeface.create("sans-serif",Typeface.BOLD):Typeface.create("sans-serif",Typeface.NORMAL));p.setStyle(Paint.Style.FILL);c.drawText(s,x,y,p); }
        protected void onDraw(Canvas canvas) { super.onDraw(canvas); c=canvas; sx=getWidth()/1280f; sy=getHeight()/720f; c.save(); c.scale(sx,sy);
            p.setShader(new LinearGradient(0,0,1280,720,Color.rgb(3,9,17),Color.rgb(5,29,49),Shader.TileMode.CLAMP)); c.drawRect(0,0,1280,720,p); p.setShader(null);
            // status strip
            fill(0,0,1280,38,Color.rgb(0,0,0),0); text("‹     ○     □",24,27,25,white,false); text(new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()),1174,26,22,white,true);
            // left navigation
            fill(0,40,112,654,Color.rgb(2,12,22),18); String[] nav={"⌂","➤","♫","▦","⚙"}; String[] labels={"Главная","Навигация","Музыка","Приложения","Настройки"};
            for(int k=0;k<nav.length;k++){float y=58+k*116; if(k==0) fill(0,y,110,y+96,Color.rgb(0,48,86),12); text(nav[k],38,y+42,34,k==0?blue:white,true); text(labels[k],12,y+70,15,k==0?white:muted,false);}
            // hero car panel
            fill(124,52,820,424,panel,18); stroke(124,52,820,424,Color.rgb(35,74,105),18,1.5f);
            text("LADA",150,88,23,white,true); text("GRANTA  •  2017",150,111,15,muted,true);
            if(car!=null){p.setAlpha(255); Rect src=new Rect(0,0,car.getWidth(),car.getHeight()); RectF dst=new RectF(140,122,806,414); c.drawBitmap(car,src,dst,p);}
            fill(143,350,360,400,Color.argb(215,0,8,17),10); text("LADA GRANTA",158,376,20,white,true); text("БЕЛАЯ • 2017",158,393,12,blue,true);
            // clock/weather
            fill(836,52,1014,238,panel,16); text(new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()),858,119,48,white,true); text(new SimpleDateFormat("EEEE, d MMMM",new Locale("ru")).format(new Date()),858,147,15,muted,false); text("☀  Погода",858,188,18,blue,true); text("Откройте приложение",858,214,13,muted,false);
            // media
            fill(1028,52,1262,424,panel,16); text("МУЗЫКА",1050,84,16,blue,true); fill(1050,102,1116,168,Color.rgb(8,56,88),10); text("♫",1070,148,36,white,true); text("Музыкальный плеер",1130,125,15,white,true); text("Нажмите для запуска",1130,150,12,muted,false); fill(1050,194,1238,198,Color.rgb(42,62,80),2); fill(1050,194,1118,198,blue,2); text("|◀",1070,249,23,white,true); fill(1140,214,1190,264,Color.rgb(0,87,148),25); text("▶",1157,246,22,white,true); text("▶|",1202,249,23,white,true); text("Bluetooth / USB / радио",1050,303,14,muted,false); text("Выберите источник звука",1050,330,13,muted,false);
            // vehicle info panel
            fill(836,250,1014,424,Color.rgb(3,13,23),14); text("АВТОМОБИЛЬ",853,279,13,blue,true); text("LADA Granta",853,310,17,white,true); text("2017 год",853,335,14,muted,false); text("Показания не подключены",853,370,12,muted,false); text("OBD-II: опционально",853,393,12,muted,false);
            // app tiles
            String[] names={"Навигация","Музыка","Радио","Bluetooth","YouTube","Настройки"}; String[] subs={"Карты","Плеер","FM / приложение","Подключение","Видео","Система"}; String[] icons={"➤","♫","▣","ᛒ","▶","⚙"};
            for(int k=0;k<6;k++){float x=124+k*190; fill(x,442,x+174,610,panel,14); stroke(x,442,x+174,610,Color.rgb(36,66,91),14,1.5f); fill(x+53,460,x+121,528,new int[]{Color.rgb(0,110,190),Color.rgb(220,44,77),Color.rgb(80,55,150),Color.rgb(0,120,220),Color.rgb(220,40,40),Color.rgb(38,67,91)}[k],16); text(icons[k],x+70,506,29,white,true); text(names[k],x+16,555,17,white,true); text(subs[k],x+16,582,13,muted,false); }
            // bottom dock
            fill(0,628,1280,720,Color.rgb(0,5,10),0); p.setShader(new LinearGradient(0,628,1280,720,blue,Color.rgb(0,15,28),Shader.TileMode.CLAMP)); p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2);c.drawLine(0,630,1280,630,p);p.setStyle(Paint.Style.FILL);p.setShader(null);
            text("▦",130,680,30,white,true); text("➤",280,680,28,white,true); text("☎",430,680,28,white,true); text("♫",570,680,30,white,true); text("LADA",740,680,25,white,true); text("ᛒ",930,680,28,white,true); text("⚙",1080,680,28,white,true); text("☼",1200,680,28,white,true);
            c.restore(); if(System.currentTimeMillis()-last>1000){last=System.currentTimeMillis(); postInvalidateDelayed(1000);} }
        public boolean onTouchEvent(android.view.MotionEvent e){ if(e.getAction()!=MotionEvent.ACTION_UP)return true; float x=e.getX()/sx,y=e.getY()/sy; if(y>442&&y<615){int k=(int)((x-124)/190); switch(k){case 0:launch("maps");break;case 1:launch("music");break;case 2:launch("radio");break;case 3:launch("bluetooth");break;case 4:launch("youtube");break;case 5:launch("settings");break;}return true;} if(x<112){if(y<160)return true;if(y<275)launch("maps");else if(y<390)launch("music");else if(y<510)launch("apps");else launch("settings");return true;} if(y>628){if(x<220)launch("apps");else if(x<370)launch("maps");else if(x<500)launch("phone");else if(x<660)launch("music");else if(x>1040)launch("settings");else if(x>860)launch("bluetooth");return true;} if(x>1020&&y<420)launch("music");return true; }
    }
}
