package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

private const val TAG = "ComposeActivity"

class ComposeActivity : AppCompatActivity() {

    private lateinit var tweetField: EditText
    private lateinit var tweetBtn: Button

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        tweetField = findViewById(R.id.field_tweet)
        tweetBtn = findViewById(R.id.btn_tweet)

        client = TwitterApplication.getRestClient(this)

        tweetBtn.setOnClickListener {

            // grab content of tweet text field
            val tweetContent = tweetField.text.toString()

            // check if tweet is empty
            if(tweetContent.isEmpty()) {
                Toast.makeText(this, "Your Tweet cannot be empty", Toast.LENGTH_SHORT).show()
            }

            // check if tweet is within character count
            if(tweetContent.length > 140) {
                Toast.makeText(this, "Your Tweet should not exceed 140 characters",
                    Toast.LENGTH_SHORT).show()
            } else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet: $tweetContent")
                        Toast.makeText(baseContext, "Your Tweet was successfully posted",
                            Toast.LENGTH_SHORT).show()

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.i(TAG, "Failed to publish Tweet", throwable)
                    }

                })
            }

        }

    }
}