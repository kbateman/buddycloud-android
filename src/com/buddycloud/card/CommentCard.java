package com.buddycloud.card;

import java.text.ParseException;
import java.util.Date;

import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buddycloud.MainActivity;
import com.buddycloud.R;
import com.buddycloud.utils.AvatarUtils;
import com.buddycloud.utils.ImageHelper;
import com.buddycloud.utils.TextUtils;
import com.buddycloud.utils.TimeUtils;

public class CommentCard extends AbstractCard {
	
	private final String published;
	private Spanned anchoredContent;
	private MainActivity activity;
	private String replyAuthor;
	
	public CommentCard(String replyAuthor, String content, String published, 
			MainActivity activity) {
		this.replyAuthor = replyAuthor;
		this.published = published;
		this.activity = activity;
		this.anchoredContent = TextUtils.anchor(content);
	}

	@Override
	public View getContentView(int position, View convertView,
			ViewGroup viewGroup) {
		
		boolean reuse = convertView != null && convertView.getTag() != null; 
		CardViewHolder holder = null;
		
		if (!reuse) {
			LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
			convertView = inflater.inflate(R.layout.comment_entry, viewGroup, false);
			holder = fillHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (CardViewHolder) convertView.getTag();
		}
		
		String avatarURL = AvatarUtils.avatarURL(viewGroup.getContext(), replyAuthor);
		ImageView avatarView = holder.getView(R.id.bcProfilePic);
		ImageHelper.picasso(viewGroup.getContext()).load(avatarURL)
				.placeholder(R.drawable.personal_50px)
				.error(R.drawable.personal_50px)
				.transform(ImageHelper.createRoundTransformation(
						viewGroup.getContext(), 16, false, -1))
				.into(avatarView);
		avatarView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.showChannelFragment(replyAuthor);
			}
		});
		
		TextView contentView = holder.getView(R.id.bcPostContent);
		contentView.setMovementMethod(LinkMovementMethod.getInstance());
		contentView.setText(anchoredContent);
		
		try {
			long publishedTime = TimeUtils.fromISOToDate(published).getTime();
			TextView publishedView = holder.getView(R.id.bcPostDate);
			publishedView.setText(
					DateUtils.getRelativeTimeSpanString(publishedTime, 
							new Date().getTime(), DateUtils.MINUTE_IN_MILLIS));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return convertView;
	}

	private static CardViewHolder fillHolder(View view) {
		return CardViewHolder.create(view, R.id.bcProfilePic, 
				R.id.bcPostContent, R.id.bcPostDate);
	}
}
