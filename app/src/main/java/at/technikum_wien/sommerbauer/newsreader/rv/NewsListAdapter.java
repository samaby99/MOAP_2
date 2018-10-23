package at.technikum_wien.sommerbauer.newsreader.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import at.technikum_wien.sommerbauer.newsreader.R;
import at.technikum_wien.sommerbauer.newsreader.data.NewsItem;
import at.technikum_wien.sommerbauer.newsreader.data.download.ImageLoaderTask;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ItemViewHolder> {
  private static final int TYPE_TOP = 0;
  private static final int TYPE_OTHERS = 1;

  public interface ListItemClickListener {
    void onListItemClick(NewsItem clickedItem);
  }

  private List<NewsItem> mItems;
  private ListItemClickListener listItemClickListener;

  class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    DateFormat mDateFormat;
    private TextView mNewsItemTitleTextView;
    private TextView mNewsItemAuthorTextView;
    private TextView mNewsItemPublicationDateTextView;
    private ImageView mNewsItemImageView;
    private ProgressBar mNewsItemProgressBar;

    ItemViewHolder(View itemView) {
      super(itemView);
      mDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
      mNewsItemTitleTextView = itemView.findViewById(R.id.tv_news_item_title);
      mNewsItemAuthorTextView = itemView.findViewById(R.id.tv_news_item_author);
      mNewsItemPublicationDateTextView = itemView.findViewById(R.id.tv_news_item_publication_date);
      mNewsItemImageView = itemView.findViewById(R.id.iv_news_item_icon);
      mNewsItemProgressBar =  itemView.findViewById(R.id.pb_news_item_icon);
      mNewsItemTitleTextView.setOnClickListener(this);
    }

    void bind(int index) {
      mNewsItemTitleTextView.setText(mItems.get(index).getTitle());
      mNewsItemAuthorTextView.setText(mItems.get(index).getAuthor());
      mNewsItemPublicationDateTextView.setText(mDateFormat.format(mItems.get(index).getPublicationDate()));
      new ImageLoaderTask(mNewsItemImageView, mNewsItemProgressBar, mItems.get(index).getImageUrl()).execute();
    }

    @Override
    public void onClick(View v) {
      int clickedItemIndex = getAdapterPosition();
      listItemClickListener.onListItemClick(mItems.get(clickedItemIndex));
    }
  }

  public NewsListAdapter(ListItemClickListener clickListener) {
    listItemClickListener = clickListener;
  }

  @Override
  @NonNull
  public ItemViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    int layoutId;
    if (viewType == TYPE_TOP)
      layoutId = R.layout.news_list_item_top;
    else
      layoutId = R.layout.news_list_item;
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(layoutId, parent, false);
    return new ItemViewHolder(view);
  }

  @Override
  public int getItemCount() {
    return (mItems == null) ? 0 : mItems.size();
  }

  @Override
  public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
    holder.bind(position);
  }

  public void swapItems(List<NewsItem> items) {
    mItems = items;
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    return (position == 0) ? TYPE_TOP : TYPE_OTHERS;
  }
}
