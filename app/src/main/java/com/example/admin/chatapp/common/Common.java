package com.example.admin.chatapp.common;

import com.example.admin.chatapp.ModelClasses.ChatMessage;
import com.example.admin.chatapp.ModelClasses.FriendsModel;
import com.example.admin.chatapp.ModelClasses.RequestModel;
import com.example.admin.chatapp.ModelClasses.modelUser;

import java.util.HashMap;

public class Common {
    public static HashMap<String, ChatMessage> MsgHashMap = new HashMap<>();
    public static HashMap<String, modelUser> ChatHashMap = new HashMap<>();
    public static HashMap<String, modelUser> UserHashMap = new HashMap<>();
    public static HashMap<String, FriendsModel> FriendHashMap = new HashMap<>();
    public static HashMap<String, RequestModel> RequestHashMap = new HashMap<>();
}
