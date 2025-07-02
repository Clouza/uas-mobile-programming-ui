package com.example.uiclient.model;

import com.google.gson.annotations.SerializedName;

public class NewsItem {
     @SerializedName("id")
     private String id;
     @SerializedName("messageId")
     private String messageId;
     @SerializedName("channelId")
     private String channelId;
     @SerializedName("guildId")
     private String guildId;
     @SerializedName("author")
     private String author;
     @SerializedName("content")
     private String content;
     @SerializedName("timestamp")
     private String timestamp;

     public String getId() {
         return id;
     }

     public String getMessageId() {
         return messageId;
     }

     public String getChannelId() {
         return channelId;
     }

     public String getGuildId() {
         return guildId;
     }

     public String getAuthor() {
         return author;
     }

     public String getContent() {
         return content;
     }

     public String getTimestamp() {
         return timestamp;
     }
 }
