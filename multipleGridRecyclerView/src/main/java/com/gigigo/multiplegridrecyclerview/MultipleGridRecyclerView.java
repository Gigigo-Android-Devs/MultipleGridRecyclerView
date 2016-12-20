package com.gigigo.multiplegridrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.gigigo.baserecycleradapter.adapter.BaseRecyclerAdapter;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.multiplegridrecyclerview.decoration.GridItemDividerDecoration;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.layoutManager.SpannedGridLayoutManager;
import java.util.ArrayList;
import java.util.List;

public class MultipleGridRecyclerView extends FrameLayout {
  private final int DEFAULT_COLUMNS = 3;
  private final float DEFAULT_ASPECT_RATIO = 0.705f;
  private final @DimenRes int DEFAULT_DIVIDER_SIZE_RESOURCE = R.dimen.divider_size;
  private final @ColorInt int DEFAULT_DIVIDER_COLOR = Color.WHITE;
  private final @ColorInt int DEFAULT_BLANK_COLOR = Color.WHITE;
  private final @IdRes int DEFAULT_LOADING_VIEW_RESOURCE = R.id.loading_view_layout;
  private final @IdRes int DEFAULT_EMPTY_VIEW_RESOURCE = R.id.empty_view_layout;
  private final @IdRes int DEFAULT_ERROR_VIEW_RESOURCE = R.id.error_view_layout;

  private View view;
  private View emptyViewLayout;
  private View errorViewLayout;
  private View recyclerViewLayout;
  private View loadingViewLayout;

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView recyclerView;
  private BaseRecyclerAdapter adapter;
  private List<Class<? extends BaseViewHolder>> undecoratedClassViewHolder;
  private RecyclerView.LayoutManager layoutManager;

  private int gridColumns = DEFAULT_COLUMNS;
  private float cellAspectRatio = DEFAULT_ASPECT_RATIO;
  private int dividerSize;
  private @ColorInt int dividerColor;
  private @ColorInt int blankBackgroundColor;
  private @IdRes int loadingResourceId = DEFAULT_LOADING_VIEW_RESOURCE;
  private @IdRes int emptyResourceId = DEFAULT_EMPTY_VIEW_RESOURCE;
  private @IdRes int errorResourceId = DEFAULT_ERROR_VIEW_RESOURCE;

  private OnRefreshListener refreshListener;

  public MultipleGridRecyclerView(Context context) {
    super(context);
    init(null, 0);
  }

  public MultipleGridRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public MultipleGridRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle) {
    loadAttributes(attrs, defStyle);

    view =
        LayoutInflater.from(getContext()).inflate(R.layout.multiple_grid_recycler_view, this, true);

    initAdapter();
    initRecyclerView();
    initRefreshLayout();
  }

  private void loadAttributes(AttributeSet attrs, int defStyle) {
    final TypedArray a =
        getContext().obtainStyledAttributes(attrs, R.styleable.MultipleGridRecyclerView, defStyle,
            0);

    try {
      gridColumns = a.getInteger(R.styleable.MultipleGridRecyclerView_columns, DEFAULT_COLUMNS);
      cellAspectRatio =
          a.getFloat(R.styleable.MultipleGridRecyclerView_aspect_ratio, DEFAULT_ASPECT_RATIO);
      dividerSize = a.getDimensionPixelSize(R.styleable.MultipleGridRecyclerView_divider_size,
          getResources().getDimensionPixelSize(DEFAULT_DIVIDER_SIZE_RESOURCE));
      dividerColor =
          a.getColor(R.styleable.MultipleGridRecyclerView_divider_color, DEFAULT_DIVIDER_COLOR);

      blankBackgroundColor =
          a.getColor(R.styleable.MultipleGridRecyclerView_blank_background_color, DEFAULT_BLANK_COLOR);

      loadingResourceId = a.getResourceId(R.styleable.MultipleGridRecyclerView_loading_view_layout,
          DEFAULT_LOADING_VIEW_RESOURCE);
      emptyResourceId = a.getResourceId(R.styleable.MultipleGridRecyclerView_empty_view_layout,
          DEFAULT_EMPTY_VIEW_RESOURCE);
      errorResourceId = a.getResourceId(R.styleable.MultipleGridRecyclerView_error_view_layout,
          DEFAULT_ERROR_VIEW_RESOURCE);
    } finally {
      a.recycle();
    }
  }

  private void initAdapter() {
    adapter = new BaseRecyclerAdapter(getContext());
  }

  private void initRecyclerView() {
    recyclerViewLayout = view.findViewById(R.id.recycler_view_layout);
    loadingViewLayout = view.findViewById(loadingResourceId);
    emptyViewLayout = view.findViewById(emptyResourceId);
    errorViewLayout = view.findViewById(errorResourceId);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

    recyclerView.setAdapter(adapter);

    initMultipleGridLayoutManager();
    initItemDecoration();

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      }

      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
            : recyclerView.getChildAt(0).getTop();
        swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
      }
    });
  }

  private void initRefreshLayout() {
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_recycler_view);
    swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        refreshListener.onRefresh();
      }
    });
  }

  public void setAdapterViewHolderFactory(BaseViewHolderFactory baseViewHolderFactory) {
    adapter = new BaseRecyclerAdapter(baseViewHolderFactory);
    recyclerView.setAdapter(adapter);
  }

  public void setAdapterDataViewHolder(Class valueClass,
      Class<? extends BaseViewHolder> viewHolder) {
    adapter.bind(valueClass, viewHolder);
  }

  public void setUndecoratedViewHolder(Class<? extends BaseViewHolder> viewHolder) {
    undecoratedClassViewHolder.add(viewHolder);
  }

  private void initMultipleGridLayoutManager() {
    configureLayoutManager(gridColumns, cellAspectRatio);

    recyclerView.setLayoutManager(layoutManager);

    setMultipleGridLayoutManager(layoutManager);
  }

  private void configureLayoutManager(final int gridColumns, float cellAspectRatio) {
    layoutManager = new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
      @Override public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
        Object element = adapter.getItem(position);

        int colSpan, rowSpan;
        if (element instanceof Cell) {
          colSpan = Math.min(((Cell) element).getColumn(), gridColumns);
          rowSpan = ((Cell) element).getRow();
        } else {
          colSpan = gridColumns;
          rowSpan = 1;
        }

        return new SpannedGridLayoutManager.SpanInfo(colSpan, rowSpan);
      }
    }, gridColumns, cellAspectRatio);
  }

  private void initItemDecoration() {
    undecoratedClassViewHolder = new ArrayList<>();
    recyclerView.addItemDecoration(new GridItemDividerDecoration(dividerSize, dividerColor, blankBackgroundColor, undecoratedClassViewHolder));
  }

  public int getGridColumns() {
    return this.gridColumns;
  }

  public void setGridColumns(int gridColumns) {
    this.gridColumns = gridColumns;
    configureLayoutManager(gridColumns, cellAspectRatio);
    setMultipleGridLayoutManager(this.layoutManager);
  }

  public float getCellAspectRatio() {
    return this.cellAspectRatio;
  }

  public void setCellAspectRatio(float cellAspectRatio) {
    this.cellAspectRatio = cellAspectRatio;
    configureLayoutManager(gridColumns, cellAspectRatio);
    setMultipleGridLayoutManager(this.layoutManager);
  }

  private void setMultipleGridLayoutManager(RecyclerView.LayoutManager layoutManager) {
    recyclerView.setLayoutManager(layoutManager);
  }

  public void setEmptyViewLayout(View emptyViewLayout) {
    detachViewFromParent(this.emptyViewLayout);
    if (emptyViewLayout != null) {
      this.emptyViewLayout = emptyViewLayout;
      setVisibilityEmptyView(false);
    }
  }

  public void setErrorViewLayout(View errorViewLayout) {
    detachViewFromParent(this.errorViewLayout);
    if (errorViewLayout != null) {
      this.errorViewLayout = errorViewLayout;
      setVisibilityErrorView(false);
    }
  }

  public void setLoadingViewLayout(View loadingViewLayout) {
    detachViewFromParent(this.loadingViewLayout);
    if (loadingViewLayout != null) {
      this.loadingViewLayout = loadingViewLayout;
      this.loadingViewLayout.setVisibility(GONE);
    }
  }

  public void setOnRefreshListener(OnRefreshListener refreshListener) {
    this.refreshListener = refreshListener;
  }

  public void setItemClickListener(BaseViewHolder.OnItemClickListener itemClickListener) {
    adapter.setItemClickListener(itemClickListener);
  }

  public void setItemLongClickListener(
      BaseViewHolder.OnItemLongClickListener itemLongClickListener) {
    adapter.setItemLongClickListener(itemLongClickListener);
  }

  public void setItemDragListener(BaseViewHolder.OnItemDragListener itemDragListener) {
    adapter.setItemDragListener(itemDragListener);
  }

  public void add(Object item) {
    adapter.add(item);
    showRecyclerView();
  }

  public void addAt(Object item, int position) {
    adapter.addAt(item, position);
    showRecyclerView();
  }

  public void addAll(List<?> data) {
    adapter.addAll(data);
    showRecyclerView();
  }

  public void addData(List<?> data) {
    adapter.append(data);
    showRecyclerView();
  }

  public boolean remove(Object item) {
    showRecyclerView();
    return adapter.remove(item);
  }

  public boolean removeAt(int position) {
    showRecyclerView();
    return adapter.removeAt(position);
  }

  public void clearData() {
    adapter.clear();
    showEmptyView();
  }

  public void showRecyclerView() {
    setVisibilityEmptyView(false);
    setVisibilityErrorView(false);
    setVisibilityRecyclerView(true);
  }

  private void setVisibilityErrorView(boolean visible) {
    errorViewLayout.setVisibility(visible ? VISIBLE : GONE);
  }

  private void setVisibilityEmptyView(boolean visible) {
    emptyViewLayout.setVisibility(visible ? VISIBLE : GONE);
  }

  public void showLoadingView(boolean isLoading) {
    swipeRefreshLayout.setRefreshing(isLoading);
    loadingViewLayout.setVisibility((isLoading) ? VISIBLE : GONE);
  }

  public void showEmptyView() {
    setVisibilityRecyclerView(false);
    setVisibilityErrorView(false);
    setVisibilityEmptyView(true);
  }

  private void setVisibilityRecyclerView(boolean visible) {
    recyclerViewLayout.setVisibility(visible ? VISIBLE : GONE);
  }

  public void showErrorView() {
    setVisibilityRecyclerView(false);
    setVisibilityEmptyView(false);
    setVisibilityErrorView(true);
  }

  public void setClipToPaddingSize(int clipToPaddingSize) {
    recyclerView.setClipToPadding(false);
    recyclerView.setPadding(0, 0, 0, clipToPaddingSize);
  }

  public void scrollToTop() {
    layoutManager.scrollToPosition(0);
  }

  public interface OnRefreshListener {
    void onRefresh();
  }
}
