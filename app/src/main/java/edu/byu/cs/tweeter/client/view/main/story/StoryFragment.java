package edu.byu.cs.tweeter.client.view.main.story;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class StoryFragment extends Fragment implements StoryPresenter.View {
    private static final String LOG_TAG = "FollowingFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private StoryPresenter presenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    public static StoryFragment newInstance(User user, AuthToken authToken){
        StoryFragment fragment = new StoryFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new StoryPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    private class StoryHolder extends RecyclerView.ViewHolder{

        private final ImageView userImage;
        private final TextView userName;
        private final TextView userAlias;
        private final TextView timeStamp;
        private final TextView userStatus;

        StoryHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            userAlias = itemView.findViewById(R.id.userAlias);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            userStatus = itemView.findViewById(R.id.userStatus);
        }

        void bindStatus(Status status) {
            User author = status.getAuthor();
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(author.getImageBytes()));
            userAlias.setText(author.getAlias());
            userName.setText(author.getName());
            userStatus.setText(findAlias(status.getText()));
            userStatus.setMovementMethod(LinkMovementMethod.getInstance());
            timeStamp.setText(status.getDate().toString());

        }

        public SpannableString findAlias(String text){
            SpannableString spannableString = new SpannableString(text);
            int start = 0;

            for(String s : text.split(" ")){
                int wordLen = s.length();

                if(s.charAt(0) == '@'){
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            Toast.makeText(getActivity().getBaseContext(), "it works", Toast.LENGTH_LONG).show();
                        }
                    };
                    spannableString.setSpan(clickableSpan, start, start + wordLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                start = start + wordLen + 1;
            }

            return spannableString;
        }

    }

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements GetStoryTask.Observer{

        private final List<Status> statuses = new ArrayList<>();
        private Status lastStatus;
        private boolean hasMorePages;
        private boolean isLoading = false;

        StoryRecyclerViewAdapter(){loadMoreItems();}

        @NonNull
        @Override
        public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW){
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);
            }
            else{
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryHolder holder, int position) {
            if(!isLoading){
                holder.bindStatus(statuses.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void addItems(List<Status> newStatuses){
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        void addItem(Status status){
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void loadMoreItems(){
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(user, PAGE_SIZE, lastStatus);
            getStoryTask.execute(request);
        }

        void removeItem(Status status){
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        private void addLoadingFooter() {
            addItem(new Status("Dummy text", new User("Dummy", "User", "")));
        }

        private void removeLoadingFooter(){
            removeItem(statuses.get(statuses.size() - 1));
        }

        @Override
        public void storyRetrieved(StoryResponse storyResponse) {
            List<Status> stories = storyResponse.getStatuses();

            lastStatus = (stories.size() > 0) ? stories.get(stories.size() - 1) : null;
            hasMorePages = storyResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(stories);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener{
        private final LinearLayoutManager layoutManager;

        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager){
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if(!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages){
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }

        }
    }
}
