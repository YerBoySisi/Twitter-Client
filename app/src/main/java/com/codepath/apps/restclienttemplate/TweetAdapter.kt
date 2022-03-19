package com.codepath.apps.restclienttemplate

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TweetAdapter(private val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetAdapter.ViewHolder, position: Int) {
        // get data model based on position
        val tweet: Tweet = tweets[position]

        // set item views based on views and data model
        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvRelativeTime.text = getRelativeTimeAgo(tweet.createdAt)
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)

    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage: ImageView = itemView.findViewById(R.id.profile_pic)
        val tvUsername: TextView = itemView.findViewById(R.id.username)
        val tvTweetBody: TextView = itemView.findViewById(R.id.tweet_body)
        val tvRelativeTime: TextView = itemView.findViewById(R.id.timestamp)
    }

    fun getRelativeTimeAgo(rawJsonDate: String?): String? {
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val sf = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
        sf.isLenient = true
        var relativeDate = ""
        try {
            val dateMillis: Long = sf.parse(rawJsonDate).time
            relativeDate = DateUtils.getRelativeTimeSpanString(
                dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
            ).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return relativeDate
    }

}