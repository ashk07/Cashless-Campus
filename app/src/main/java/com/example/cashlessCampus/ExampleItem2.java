package com.example.cashlessCampus;
import android.graphics.Bitmap;

public class ExampleItem2 {
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String mText5;

    public ExampleItem2(String text1, String text2, String text3, String text4) {
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3 = text3;
        this.mText4 = text4;
    }

    public ExampleItem2(String text1, String text2, String text3, String text4, String text5) {
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3 = text3;
        this.mText4 = text4;
        this.mText5 = text5;
    }

    public String getText1() {
        return this.mText1;
    }
    public String getText2() {
        return this.mText2;
    }
    public String getText3() {
        return this.mText3;
    }
    public String getText4() {
        return this.mText4;
    }
    public String getText5() {
        return this.mText5;
    }


}