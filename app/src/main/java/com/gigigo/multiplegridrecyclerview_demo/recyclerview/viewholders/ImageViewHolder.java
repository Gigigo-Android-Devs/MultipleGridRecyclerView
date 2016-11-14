package com.gigigo.multiplegridrecyclerview_demo.recyclerview.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview_demo.MainActivity;
import com.gigigo.multiplegridrecyclerview_demo.R;
import com.gigigo.multiplegridrecyclerview_demo.recyclerview.ImageWidget;
import com.gigigo.ui.imageloader.ImageLoader;

public class ImageViewHolder extends BaseViewHolder<ImageWidget> {

  private ImageLoader imageLoader;
  private ImageView imageView;

  public ImageViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.content_item_image_element);

    this.imageLoader = ((MainActivity)parent.getContext()).imageLoader;
    imageView = (ImageView) this.itemView.findViewById(R.id.image_view);

    bindListeners();
  }

  public void bindListeners() {
    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View view) {
        Toast.makeText(view.getContext(),
            "Long clicked from viewHolder - position: " + getLayoutPosition(), Toast.LENGTH_SHORT)
            .show();
        return false;
      }
    });
  }

  @Override public void bindTo(ImageWidget item, int position) {
    imageLoader.load(item.getImageUrl(), imageView, R.drawable.placeholder);
  }
}
