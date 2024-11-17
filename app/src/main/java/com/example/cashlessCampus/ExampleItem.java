package com.example.cashlessCampus;
import android.graphics.Bitmap;
public class ExampleItem {
    private String mText0;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;

    public ExampleItem(String text0, String text1, String text2, String text3, String text4) {
        this.mText0 = text0;
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3 = text3;
        this.mText4 = text4;
    }

    public String getText0() {
        return this.mText0;
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


}