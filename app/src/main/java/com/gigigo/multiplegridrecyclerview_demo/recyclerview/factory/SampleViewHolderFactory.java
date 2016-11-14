package com.gigigo.multiplegridrecyclerview_demo.recyclerview.factory;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.CellImageWidget;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.ImageWidget;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.TextWidget;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.viewholders.CellImageViewHolder;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.viewholders.ImageViewHolder;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.viewholders.TextViewHolder;
import com.gigigo.ui.imageloader.ImageLoader;

/**
 * Created by rui.alonso on 14/11/16.
 */
public class SampleViewHolderFactory extends BaseViewHolderFactory {
  private ImageLoader imageLoader;

  public SampleViewHolderFactory(Context context, ImageLoader imageLoader) {
    super(context);
    this.imageLoader = imageLoader;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == CellImageWidget.class) {
      return new CellImageViewHolder(context, parent, imageLoader);
    } else if (valueClass == ImageWidget.class) {
      return new ImageViewHolder(context, parent);
    } else if (valueClass == TextWidget.class) {
      return new TextViewHolder(context, parent);
    } else {
      return super.create(valueClass, parent);
    }
  }
}
