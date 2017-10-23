package com.gigigo.multiplegridrecyclerview.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class CustomRecyclerView extends RecyclerView {

  private float deltaX = 1;
  private float deltaY = 1;

  public CustomRecyclerView(Context context) {
    super(context);
  }

  public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public boolean fling(int velocityX, int velocityY) {

    velocityX = (int) (velocityY * deltaX);
    velocityY = (int) (velocityY * deltaY);

    return super.fling(velocityX, velocityY);
  }

  public void overrideScollingVelocityX(float deltaX) {
    if (deltaX == 0) {
      return;
    }
    this.deltaX = deltaX;
  }

  public void overrideScollingVelocityY(float deltaY) {
    if (deltaY == 0) {
      return;
    }
    this.deltaY = deltaY;
  }
}
