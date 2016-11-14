/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigigo.multiplegridrecyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import java.util.List;

/**
 * A {@link RecyclerView.ItemDecoration} which draws dividers (along the right & bottom)
 * for certain {@link RecyclerView.ViewHolder} types.
 */
public class GridItemDividerDecoration extends RecyclerView.ItemDecoration {

  private final int dividerSize;
  private final Paint paint;
  private final @ColorInt int blankBackgroundColor;

  private List<Class<? extends BaseViewHolder>> undecoratedClassTypes;

  public GridItemDividerDecoration(int dividerSize, @ColorInt int dividerColor,
      @ColorInt int blankBackgroundColor, List<Class<? extends BaseViewHolder>> undecoratedClassViewHolder) {
    this.dividerSize = dividerSize;
    paint = new Paint();
    paint.setColor(dividerColor);
    paint.setStyle(Paint.Style.FILL);

    this.blankBackgroundColor = blankBackgroundColor;
    this.undecoratedClassTypes = undecoratedClassViewHolder;
  }

  public GridItemDividerDecoration(@NonNull Context context, @DimenRes int dividerSizeResId,
      @ColorRes int dividerColorResId, @ColorRes int blankBackgroundColorResId, List<Class<? extends BaseViewHolder>> undecoratedClassViewHolder) {
    this(context.getResources().getDimensionPixelSize(dividerSizeResId),
        ContextCompat.getColor(context, dividerColorResId),
        ContextCompat.getColor(context, blankBackgroundColorResId), undecoratedClassViewHolder);
  }

  @Override public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    if (parent.isAnimating()) return;

    final int rightWithPadding = parent.getWidth() - parent.getPaddingRight();
    final int bottomWithPadding = parent.getHeight() - parent.getPaddingBottom();

    final int childCount = parent.getChildCount();
    final RecyclerView.LayoutManager lm = parent.getLayoutManager();

    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);

      final int childLeft = lm.getDecoratedLeft(child);
      final int childTop = lm.getDecoratedTop(child);
      final int childRight = lm.getDecoratedRight(child);
      final int childBottom = lm.getDecoratedBottom(child);

      final ViewGroup.MarginLayoutParams lp =
          (ViewGroup.MarginLayoutParams) child.getLayoutParams();

      if (!(parent.getChildViewHolder(child) instanceof CellBlankViewHolder)) {
        final int bottomOffset = childBottom - child.getBottom() - lp.bottomMargin;
        if (/*bottomOffset > 0 && */childBottom < bottomWithPadding) {
          final int left = childLeft;
          final int top = childBottom - bottomOffset;
          final int right = childRight;
          final int bottom = top + dividerSize;

          canvas.drawRect(left, top, right, bottom, paint);
        }

        final int rightOffset = childRight - child.getRight() - lp.rightMargin;
        if (/*rightOffset > 0 && */childRight < rightWithPadding) {
          final int left = childRight - rightOffset;
          final int top = childTop;
          final int right = left + dividerSize;
          final int bottom = childBottom;

          canvas.drawRect(left, top, right, bottom, paint);
        }
      } else {
        child.setBackgroundColor(this.blankBackgroundColor);
      }
    }
  }

  private boolean isUndecoratedViewHolder(Class valueClass) {
    boolean undecorated = true;
    for(int i=0; undecorated && i<undecoratedClassTypes.size(); i++) {
      if(undecoratedClassTypes.get(i) == valueClass)
       undecorated = false;
    }

    return undecorated;
  }
}
