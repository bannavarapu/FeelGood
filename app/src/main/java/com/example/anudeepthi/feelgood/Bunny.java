package com.example.anudeepthi.feelgood;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Bunny extends AppCompatActivity {

    ImageView b11_1,b11_2,b11_3;
    TextView tv_timeleft, tv_score,tv;
    Button button;
    Random rand;
    int score=0, fps=1000, left=5, tempifleft=0;

    int which=0;
    int whichsave=0;
    MyCount counter;
    boolean flag;

    AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunny);
        flag=true;
        rand= new Random();
        button =(Button)findViewById(R.id.button);
        tv=(TextView)findViewById(R.id.textView3);
        tv_score=(TextView)findViewById(R.id.textView);
        tv_timeleft=(TextView)findViewById(R.id.textView2);

        b11_1=(ImageView)findViewById(R.id.bunny11_1);
        b11_2=(ImageView)findViewById(R.id.bunny11_2);
        b11_3=(ImageView)findViewById(R.id.bunny11_3);

        b11_1.setVisibility(View.INVISIBLE);
        b11_2.setVisibility(View.INVISIBLE);
        b11_3.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                left=5;
                tv_timeleft.setText("Lives left "+left );
                score=0;
                tv_score.setText("SCORE "+score);
                counter = new MyCount(60000,1000);
                counter.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        theGameActions();
                    }
                },1000);
                button.setEnabled(false);
            }
        });

        b11_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempifleft=1;
                b11_1.setImageResource(R.drawable.crying_bunny);
                score++;
                tv_score.setText("SCORE "+score);
                b11_1.setEnabled(false);

            }
        });

        b11_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempifleft=1;
                b11_2.setImageResource(R.drawable.crying_bunny);
                score++;
                tv_score.setText("SCORE "+score);
                b11_2.setEnabled(false);

            }
        });

        b11_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempifleft=1;
                b11_3.setImageResource(R.drawable.crying_bunny);
                score++;
                tv_score.setText("SCORE "+score);
                b11_3.setEnabled(false);

            }
        });

    }

    private  void theGameActions()
    {
        if(score<10)
        {
            fps=850;
        }
        else if(score>=10 && score<20)
        {
            fps=800;
        }
        else if(score>=20 && score<30)
        {
            fps=750;
        }
        else if(score>=30 && score<40)
        {
            fps=700;
        }
        else if(score>=40 && score<50)
        {
            fps=650;
        }
        else
        {
            fps=500;
        }

        animationDrawable=(AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.animation);

        do{
            which= rand.nextInt(3)+1;
        }while(whichsave==which);
        whichsave=which;
        if(which==1)
        {
            b11_1.setImageDrawable(animationDrawable);
            b11_1.setVisibility(View.VISIBLE);
            b11_1.setEnabled(true);
        }else if(which==2)
        {
            b11_2.setImageDrawable(animationDrawable);
            b11_2.setVisibility(View.VISIBLE);
            b11_2.setEnabled(true);
        }else if(which==3)
        {
            b11_3.setImageDrawable(animationDrawable);
            b11_3.setVisibility(View.VISIBLE);
            b11_3.setEnabled(true);
        }
        animationDrawable.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b11_1.setVisibility(View.INVISIBLE);
                b11_2.setVisibility(View.INVISIBLE);
                b11_3.setVisibility(View.INVISIBLE);

                b11_1.setEnabled(false);
                b11_2.setEnabled(false);
                b11_3.setEnabled(false);

                if(tempifleft==0)
                {
                    left=left-1;
                    tv_timeleft.setText("Lives Left "+left);
                }else if(tempifleft==1)
                {
                    tempifleft=0;
                }

                if(left==0)
                {
                    Toast.makeText(Bunny.this,"Game Over, you scored "+score+" points",Toast.LENGTH_LONG).show();
                    flag=true;
                    counter.cancel();
                    button.setEnabled(true);
                }
                else if(left>0 && flag==true)
                {
                    theGameActions();
                }
                else if(flag==false)
                {

                    Toast.makeText(Bunny.this,"Game Over, you scored "+score+" points",Toast.LENGTH_LONG).show();
                    flag=true;
                    button.setEnabled(true);
                }

            }
        },fps);

    }
    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv.setText("Time up");
            flag=false;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv.setText("Time Left: "  + millisUntilFinished/1000);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_dashboard){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}