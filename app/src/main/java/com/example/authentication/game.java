package com.example.authentication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;

public class game extends AppCompatActivity {

    private ListView wordLV;
    private LinkedList<String> theWordStrings = new LinkedList<String>();
    private ArrayAdapter<String> aa;
    private EditText wordET;
    private Scanner scanner;
    private String newWord;
    public int highscore;
    private int letterCount;
    private boolean bool;
    private boolean pass;
    private boolean WLC;
    private boolean WDC;
    private boolean WLCC;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        this.wordET = this.findViewById(R.id.wordET);
        this.wordLV = (ListView)this.findViewById(R.id.wordListView);
        aa = new ArrayAdapter<String>(this, R.layout.another_row, theWordStrings);
        this.wordLV.setAdapter(aa);
        aa.clear();

    }

    public boolean wordDictionaryChecker(String newWord)
    {
        scanner = new Scanner(this.getBaseContext().getResources().openRawResource(R.raw.allwords));
        int lineNum = 0;
        bool = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNum++;
            if(line.equalsIgnoreCase(newWord))
            {
                bool =  true;
                break;
            }
        }
        return bool;
    }

    public boolean wordListChecker(String newWord)
    {
        if (theWordStrings.contains(newWord))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean wordLetterCountChecker(String newWord)
    {
        pass = true;
        if (theWordStrings.isEmpty())
        {
            System.out.println("SORRY LIST IS EMPTY");
        }
        else
        {
            String latestWord = theWordStrings.getLast();

            System.out.println("*****" + latestWord);
            letterCount = 0;
            for (int i = 0; i < latestWord.length(); i++)
            {
                int LWchar = (int)latestWord.charAt(i);
                int NWchar = (int)newWord.charAt(i);
                if (LWchar != NWchar)
                {
                    letterCount++;
                }
            }
            if (letterCount > 1)
            {
                pass = false;
                System.out.println("******" + letterCount);
            }

        }

        return pass;
    }








    public void onSubmitbuttonPressed(View v)
    {
        newWord = this.wordET.getText().toString();
        System.out.println(newWord + "");
        WLC = wordListChecker(newWord);
        WDC = wordDictionaryChecker(newWord);
        WLCC = wordLetterCountChecker(newWord);

        if (WLC == false || WDC == false || WLCC == false)
        {
            System.out.println("****WLC:" + WLC);
            System.out.println("****WDC:" + WDC);
            System.out.println("****WLCCC:" + WLCC);

            highscore = theWordStrings.size();

            Intent i = new Intent(this, GameOver.class);
            i.putExtra("highscore",this.highscore);
            this.startActivity(i);

        }
        else
        {
            aa.add(newWord);
        }


    }


    public void onResetbuttonPressed(View v)
    {
        aa.clear();
    }
}