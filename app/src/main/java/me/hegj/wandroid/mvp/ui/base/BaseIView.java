package me.hegj.wandroid.mvp.ui.base;

import androidx.appcompat.app.AppCompatActivity;

import com.jess.arms.mvp.IView;

public interface BaseIView extends IView {
    AppCompatActivity getActivityContext();
}
